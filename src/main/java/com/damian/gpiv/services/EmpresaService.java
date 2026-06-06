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

    // Registrar empresa con todos los campos
    public void registrar(Empresa empresa) {
        String sql = "INSERT INTO empresas " +
                "(nombre, tipo, email, estado, cuit, actividad_principal, direccion, referente, telefono, rubro, descripcion_servicio) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

            pstmt.setString(5, empresa.getCuit());
            pstmt.setString(6, empresa.getActividadPrincipal());
            pstmt.setString(7, empresa.getDireccion());
            pstmt.setString(8, empresa.getReferente());
            pstmt.setString(9, empresa.getTelefono());
            pstmt.setString(10, empresa.getRubro());
            pstmt.setString(11, empresa.getDescripcionServicio());

            pstmt.executeUpdate();
            System.out.println("Empresa registrada correctamente con tipo: " + empresa.getTipo());

        } catch (SQLException e) {
            System.err.println("Error al registrar empresa: " + e.getMessage());
        }
    }

    public boolean existeEmpresa(int empresaId) {
        String sql = "SELECT COUNT(*) FROM empresas WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, empresaId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar empresa: " + e.getMessage());
        }
        return false;
    }

    // Buscar empresa por ID
    public Empresa buscarPorId(int empresaId) {
        String sql = "SELECT * FROM empresas WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, empresaId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Empresa(
                        rs.getString("nombre"),
                        rs.getInt("id"),
                        rs.getString("tipo"),
                        rs.getString("email"),
                        rs.getString("estado")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar empresa: " + e.getMessage());
        }
        return null; // Si no se encuentra
    }


    // Listar empresas con todos los campos
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
                        rs.getString("estado"),
                        rs.getString("cuit"),
                        rs.getString("actividad_principal"),
                        rs.getString("direccion"),
                        rs.getString("referente"),
                        rs.getString("telefono"),
                        rs.getString("rubro"),
                        rs.getString("descripcion_servicio")
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
