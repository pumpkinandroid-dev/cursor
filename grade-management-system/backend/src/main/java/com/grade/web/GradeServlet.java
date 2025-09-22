package com.grade.web;

import com.grade.dao.GradeDao;
import com.grade.entity.Grade;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GradeServlet extends HttpServlet {
    private final GradeDao dao = new GradeDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        try {
            if (path == null || "/".equals(path)) {
                List<Grade> list = dao.findAll();
                JsonUtil.write(resp, list);
                return;
            }
            if ("/count".equals(path)) {
                Map<String, Object> m = new HashMap<>();
                m.put("totalCount", dao.count());
                JsonUtil.write(resp, m);
                return;
            }
            if (path.startsWith("/search")) {
                String keyword = req.getParameter("keyword");
                List<Grade> list = dao.search(keyword == null ? "" : keyword);
                JsonUtil.write(resp, list);
                return;
            }
            String[] parts = path.split("/");
            if (parts.length == 2 && !parts[1].isEmpty()) {
                long id = Long.parseLong(parts[1]);
                Optional<Grade> g = dao.findById(id);
                if (g.isPresent()) JsonUtil.write(resp, g.get());
                else resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonUtil.write(resp, error(e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Grade g = JsonUtil.read(req, Grade.class);
            if (g.getStudentId() == null || g.getSubject() == null || g.getScore() == null || g.getSemester() == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JsonUtil.write(resp, error("缺少必填字段"));
                return;
            }
            Grade saved = dao.insert(g);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            JsonUtil.write(resp, saved);
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonUtil.write(resp, error(e.getMessage()));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null || path.split("/").length < 2) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonUtil.write(resp, error("缺少ID"));
            return;
        }
        try {
            long id = Long.parseLong(path.split("/")[1]);
            Grade g = JsonUtil.read(req, Grade.class);
            Grade updated = dao.update(id, g);
            JsonUtil.write(resp, updated);
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonUtil.write(resp, error(e.getMessage()));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null || path.split("/").length < 2) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonUtil.write(resp, error("缺少ID"));
            return;
        }
        try {
            long id = Long.parseLong(path.split("/")[1]);
            dao.delete(id);
            JsonUtil.write(resp, message("删除成功"));
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonUtil.write(resp, error(e.getMessage()));
        }
    }

    private Map<String, String> error(String msg) {
        Map<String, String> m = new HashMap<>();
        m.put("error", msg);
        return m;
    }

    private Map<String, String> message(String msg) {
        Map<String, String> m = new HashMap<>();
        m.put("message", msg);
        return m;
    }
}

