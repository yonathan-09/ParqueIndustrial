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

    public void registrar(Empresa empresa) {
        String sql = "INSERT INTO empresas (nombre, id, tipo, estado, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, empresa.getNombre());
            pstmt.setInt(2, empresa.getId());
            pstmt.setString(3, empresa.getTipo());
            pstmt.setString(4, empresa.getEstado());
            pstmt.setString(5, empresa.getEmail());

            pstmt.executeUpdate();
            System.out.println("Empresa registrada correctamente.");

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
                        rs.getString("estado"),
                        rs.getString("email")
                );
                empresas.add(empresa);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar empresas: " + e.getMessage());
        }

        return empresas;
    }

}
