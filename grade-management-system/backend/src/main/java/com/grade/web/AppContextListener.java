package com.grade.web;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@WebListener
public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            String dbPath = sce.getServletContext().getRealPath("/WEB-INF/grade_management.db");
            DbUtil.ensureDbFile(dbPath);
            DbUtil.configure(dbPath);

            // Run init.sql from classpath
            try (InputStream is = getClass().getClassLoader().getResourceAsStream("database/init.sql")) {
                if (is != null) {
                    StringBuilder sql = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            sql.append(line).append('\n');
                        }
                    }
                    try (Connection conn = DbUtil.getConnection(); Statement st = conn.createStatement()) {
                        st.executeUpdate("PRAGMA foreign_keys = ON;");
                        for (String stmt : sql.toString().split(";\\s*\n")) {
                            String s = stmt.trim();
                            if (!s.isEmpty()) {
                                st.execute(s);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}

