package com.grade.web;

import com.grade.dao.StudentDao;
import com.grade.entity.Student;
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

public class StudentServlet extends HttpServlet {
    private final StudentDao dao = new StudentDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        try {
            if (path == null || "/".equals(path)) {
                List<Student> list = dao.findAll();
                JsonUtil.write(resp, list);
                return;
            }
            String[] parts = path.split("/");
            if (parts.length == 2 && !parts[1].isEmpty()) {
                long id = Long.parseLong(parts[1]);
                Optional<Student> s = dao.findById(id);
                if (s.isPresent()) JsonUtil.write(resp, s.get());
                else resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
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
                List<Student> list = dao.search(keyword == null ? "" : keyword);
                JsonUtil.write(resp, list);
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
            Student s = JsonUtil.read(req, Student.class);
            if (s.getStudentId() == null || s.getName() == null || s.getClassName() == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JsonUtil.write(resp, error("缺少必填字段"));
                return;
            }
            if (dao.existsByStudentId(s.getStudentId())) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JsonUtil.write(resp, error("学号已存在"));
                return;
            }
            Student saved = dao.insert(s);
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
            Student s = JsonUtil.read(req, Student.class);
            Student updated = dao.update(id, s);
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

