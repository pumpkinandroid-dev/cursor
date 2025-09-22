package com.grade.web;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
    private static String dbUrl;

    public static void configure(String path) {
        dbUrl = "jdbc:sqlite:" + path;
    }

    public static Connection getConnection() throws SQLException {
        if (dbUrl == null) {
            throw new IllegalStateException("Database not configured");
        }
        return DriverManager.getConnection(dbUrl);
    }

    public static void ensureDbFile(String path) throws SQLException {
        try {
            File f = new File(path);
            if (!f.exists()) {
                DriverManager.getConnection("jdbc:sqlite:" + path).close();
            }
        } catch (SQLException e) {
            throw e;
        }
    }
}

