package com.damian.gpiv.services;

import com.damian.gpiv.database.Database;
import com.damian.gpiv.models.Empresa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmpresaService {

    // Registrar empresa
    public void registrar(Empresa empresa) {
        String sql = "INSERT INTO empresas (nombre, tipo, email, estado) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, empresa.getNombre());
            pstmt.setString(2, empresa.getTipo());
            pstmt.setString(3, empresa.getEmail());

            // Estado depende del tipo
            if ("radicada".equalsIgnoreCase(empresa.getTipo())) {
                pstmt.setString(4, "aprobada");
            } else {
                pstmt.setString(4, "pendiente");
            }

            pstmt.executeUpdate();
            System.out.println("Empresa registrada correctamente con tipo: " + empresa.getTipo());

        } catch (SQLException e) {
            System.err.println("Error al registrar empresa: " + e.getMessage());
        }
    }


    // Listar empresas
    public List<Empresa> listar() {
        List<Empresa> empresas = new ArrayList<>();
        String sql = "SELECT * FROM empresas";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Empresa empresa = new Empresa(
                        rs.getString("nombre"),
                        rs.getInt("id"),
                        rs.getString("tipo"),
                        rs.getString("email"),
                        rs.getString("estado")
                );
                empresas.add(empresa);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar empresas: " + e.getMessage());
        }

        return empresas;
    }

    // Actualizar estado de empresa (solo interesadas)
    public void actualizarEstado(int empresaId, String nuevoEstado) {
        String sql;

        if ("aprobada".equalsIgnoreCase(nuevoEstado)) {
            // Si se aprueba → cambiar estado y tipo
            sql = "UPDATE empresas SET estado=?, tipo='radicada' WHERE id=? AND tipo='interesada'";
        } else {
            // Si se rechaza → solo cambiar estado
            sql = "UPDATE empresas SET estado=? WHERE id=? AND tipo='interesada'";
        }

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, empresaId);
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Empresa " + empresaId + " actualizada a estado: " + nuevoEstado);
            } else {
                System.out.println("No se encontró empresa interesada con ID " + empresaId);
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar estado de empresa: " + e.getMessage());
        }
    }
}