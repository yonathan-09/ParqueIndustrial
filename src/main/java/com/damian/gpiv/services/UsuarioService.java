package com.damian.gpiv.services;

import com.damian.gpiv.database.Database;
import com.damian.gpiv.models.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioService {

    public void registrar(Usuario usuario) {
        // MODIFICADO: Ahora insertamos también el campo empresa_id
        String sql = "INSERT INTO usuarios (nombre, rol, password, empresa_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getRol());
            pstmt.setString(3, usuario.getPassword());
            // MODIFICADO: Al usar setObject maneja correctamente un Integer que puede ser numérico o null
            pstmt.setObject(4, usuario.getEmpresaId());

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
                    // MODIFICADO: Recuperamos la columna empresa_id de la base de datos
                    int empIdRaw = rs.getInt("empresa_id");
                    // Si el valor en la BD es NULL, rs.wasNull() devuelve true, entonces asignamos null en Java
                    Integer empresaId = rs.wasNull() ? null : empIdRaw;

                    // MODIFICADO: Retornamos el objeto usando el constructor completo de Usuario
                    return new Usuario(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("rol"),
                            rs.getString("password"),
                            empresaId
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al autenticar usuario: " + e.getMessage());
        }

        return null;
    }
}