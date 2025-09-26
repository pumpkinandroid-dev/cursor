package com.cnbeta.summary;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ApiServlet extends HttpServlet {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String MOBILE_INDEX = "https://m.cnbeta.com.tw/wap/index.htm?page=%d";
    private static final int REQUEST_TIMEOUT_MS = (int) Duration.ofSeconds(15).toMillis();
    private static final long THROTTLE_MS = 500L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("application/json;charset=UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");

        int page = 1;
        String pageParam = req.getParameter("n");
        if (pageParam != null) {
            try { page = Math.max(1, Integer.parseInt(pageParam)); } catch (NumberFormatException ignored) {}
        }

        List<ArticleSummary> summaries = new ArrayList<>();
        try {
            String listUrl = String.format(MOBILE_INDEX, page);
            Document listDoc = Jsoup.connect(listUrl).timeout(REQUEST_TIMEOUT_MS).get();
            Element infoList = listDoc.getElementById("info_list");
            if (infoList == null) {
                writeJson(resp, new ErrorResponse("info_list not found on index page", listUrl));
                return;
            }
            Elements links = infoList.select("a[href]");
            for (Element a : links) {
                String titleText = a.text();
                String href = a.absUrl("href");
                if (href == null || href.isEmpty()) continue;

                try {
                    Document articleDoc = Jsoup.connect(href).timeout(REQUEST_TIMEOUT_MS).get();

                    String note = null;
                    Elements timeEls = articleDoc.getElementsByClass("time");
                    if (!timeEls.isEmpty()) {
                        note = timeEls.get(0).text();
                    }

                    String summary = null;
                    Element artibody = articleDoc.getElementById("artibody");
                    if (artibody != null) {
                        Element firstP = artibody.selectFirst("p");
                        if (firstP != null) {
                            summary = firstP.text();
                        }
                    }

                    summaries.add(new ArticleSummary(titleText, href, nullToEmpty(note), nullToEmpty(summary)));
                } catch (SocketTimeoutException timeout) {
                    summaries.add(new ArticleSummary(titleText, href, "", "请求超时"));
                } catch (Exception ex) {
                    summaries.add(new ArticleSummary(titleText, href, "", "解析失败"));
                }

                try { Thread.sleep(THROTTLE_MS); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
            }
        } catch (Exception e) {
            writeJson(resp, new ErrorResponse("Failed to load index page", e.getMessage()));
            return;
        }

        writeJson(resp, summaries);
    }

    private static String nullToEmpty(String v) { return v == null ? "" : v; }

    private static void writeJson(HttpServletResponse resp, Object obj) throws IOException {
        OBJECT_MAPPER.writeValue(resp.getOutputStream(), obj);
    }

    public record ArticleSummary(String title, String url, String note, String summary) {}
    public record ErrorResponse(String message, String detail) {}
}



