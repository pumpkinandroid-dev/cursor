package com.grade.dao;

import com.grade.entity.Student;
import com.grade.web.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentDao {
    public List<Student> findAll() throws SQLException {
        String sql = "SELECT id, student_id, name, class_name, created_at, updated_at FROM students ORDER BY id";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Student> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        }
    }

    public Optional<Student> findById(long id) throws SQLException {
        String sql = "SELECT id, student_id, name, class_name, created_at, updated_at FROM students WHERE id=?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }
        }
    }

    public Optional<Student> findByStudentId(String studentId) throws SQLException {
        String sql = "SELECT id, student_id, name, class_name, created_at, updated_at FROM students WHERE student_id=?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }
        }
    }

    public boolean existsByStudentId(String studentId) throws SQLException {
        String sql = "SELECT 1 FROM students WHERE student_id=?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public Student insert(Student s) throws SQLException {
        String sql = "INSERT INTO students(student_id, name, class_name, created_at, updated_at) VALUES(?,?,?,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getStudentId());
            ps.setString(2, s.getName());
            ps.setString(3, s.getClassName());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) s.setId(keys.getLong(1));
            }
            return s;
        }
    }

    public Student update(long id, Student s) throws SQLException {
        String sql = "UPDATE students SET student_id=?, name=?, class_name=?, updated_at=CURRENT_TIMESTAMP WHERE id=?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getStudentId());
            ps.setString(2, s.getName());
            ps.setString(3, s.getClassName());
            ps.setLong(4, id);
            ps.executeUpdate();
            s.setId(id);
            return s;
        }
    }

    public void delete(long id) throws SQLException {
        String sqlGrades = "DELETE FROM grades WHERE student_id = (SELECT student_id FROM students WHERE id=?)";
        String sql = "DELETE FROM students WHERE id=?";
        try (Connection conn = DbUtil.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement p1 = conn.prepareStatement(sqlGrades);
                 PreparedStatement p2 = conn.prepareStatement(sql)) {
                p1.setLong(1, id);
                p1.executeUpdate();
                p2.setLong(1, id);
                p2.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public List<Student> search(String keyword) throws SQLException {
        String like = "%" + keyword + "%";
        String sql = "SELECT id, student_id, name, class_name, created_at, updated_at FROM students WHERE student_id LIKE ? OR name LIKE ? OR class_name LIKE ? ORDER BY id";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                List<Student> list = new ArrayList<>();
                while (rs.next()) list.add(mapRow(rs));
                return list;
            }
        }
    }

    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM students";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getLong(1) : 0L;
        }
    }

    private Student mapRow(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setId(rs.getLong("id"));
        s.setStudentId(rs.getString("student_id"));
        s.setName(rs.getString("name"));
        s.setClassName(rs.getString("class_name"));
        s.setCreatedAt(rs.getString("created_at"));
        s.setUpdatedAt(rs.getString("updated_at"));
        return s;
    }
}

