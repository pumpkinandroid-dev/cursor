package com.grade.dao;

import com.grade.entity.Grade;
import com.grade.web.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GradeDao {
    public List<Grade> findAll() throws SQLException {
        String sql = "SELECT id, student_id, subject, score, semester, exam_type, created_at, updated_at FROM grades ORDER BY id";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Grade> list = new ArrayList<>();
            while (rs.next()) list.add(mapRow(rs));
            return list;
        }
    }

    public Optional<Grade> findById(long id) throws SQLException {
        String sql = "SELECT id, student_id, subject, score, semester, exam_type, created_at, updated_at FROM grades WHERE id=?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }
        }
    }

    public List<Grade> search(String keyword) throws SQLException {
        String like = "%" + keyword + "%";
        String sql = "SELECT id, student_id, subject, score, semester, exam_type, created_at, updated_at FROM grades WHERE student_id LIKE ? OR subject LIKE ? OR semester LIKE ? ORDER BY id";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                List<Grade> list = new ArrayList<>();
                while (rs.next()) list.add(mapRow(rs));
                return list;
            }
        }
    }

    public Grade insert(Grade g) throws SQLException {
        String sql = "INSERT INTO grades(student_id, subject, score, semester, exam_type, created_at, updated_at) VALUES(?,?,?,?,?,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, g.getStudentId());
            ps.setString(2, g.getSubject());
            ps.setDouble(3, g.getScore());
            ps.setString(4, g.getSemester());
            ps.setString(5, g.getExamType());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) g.setId(keys.getLong(1));
            }
            return g;
        }
    }

    public Grade update(long id, Grade g) throws SQLException {
        String sql = "UPDATE grades SET student_id=?, subject=?, score=?, semester=?, exam_type=?, updated_at=CURRENT_TIMESTAMP WHERE id=?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, g.getStudentId());
            ps.setString(2, g.getSubject());
            ps.setDouble(3, g.getScore());
            ps.setString(4, g.getSemester());
            ps.setString(5, g.getExamType());
            ps.setLong(6, id);
            ps.executeUpdate();
            g.setId(id);
            return g;
        }
    }

    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM grades WHERE id=?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM grades";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getLong(1) : 0L;
        }
    }

    private Grade mapRow(ResultSet rs) throws SQLException {
        Grade g = new Grade();
        g.setId(rs.getLong("id"));
        g.setStudentId(rs.getString("student_id"));
        g.setSubject(rs.getString("subject"));
        g.setScore(rs.getDouble("score"));
        g.setSemester(rs.getString("semester"));
        g.setExamType(rs.getString("exam_type"));
        g.setCreatedAt(rs.getString("created_at"));
        g.setUpdatedAt(rs.getString("updated_at"));
        return g;
    }
}

