package com.cnbeta.summary;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class StaticFileServlet extends HttpServlet {
    private final String base;

    public StaticFileServlet(String base) {
        this.base = base;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getRequestURI();
        if (path == null || path.equals("/")) path = "/index.html";
        String resourcePath = base + path;
        try (InputStream in = getClass().getResourceAsStream(resourcePath)) {
            if (in == null) {
                resp.setStatus(404);
                resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
                resp.getWriter().write("Not found");
                return;
            }
            String contentType = guessContentType(path);
            resp.setContentType(contentType);
            in.transferTo(resp.getOutputStream());
        }
    }

    private static String guessContentType(String path) {
        if (path.endsWith(".html")) return "text/html; charset=UTF-8";
        if (path.endsWith(".js")) return "application/javascript; charset=UTF-8";
        if (path.endsWith(".css")) return "text/css; charset=UTF-8";
        return "application/octet-stream";
    }
}


