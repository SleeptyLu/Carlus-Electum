package org.carluselectum.ecommerce.data;

import java.sql.*;
import org.carluselectum.ecommerce.model.auth.User;

public class UserRepository {
    public void save(User user) {
        String sql = "INSERT INTO users (email, password, role) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao registar utilizador: " + e.getMessage());
        }
    }

    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getString("email"), rs.getString("password"), rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao procurar utilizador: " + e.getMessage());
        }
        return null;
    }
}