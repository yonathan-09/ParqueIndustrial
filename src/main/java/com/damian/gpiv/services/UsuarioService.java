package com.damian.gpiv.services;
import com.damian.gpiv.database.Database;
import com.damian.gpiv.models.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioService {

    public void registrar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, rol, password) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getRol());
            pstmt.setString(3, usuario.getPassword());

            pstmt.executeUpdate();
            System.out.println("Usuario registrado correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
        }
    }

    public Usuario autenticar(String nombre, String password) {
        String sql = "SELECT * FROM usuarios WHERE nombre=? AND password=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("rol"),
                            rs.getString("password")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al autenticar usuario: " + e.getMessage());
        }

        return null;
    }

}
