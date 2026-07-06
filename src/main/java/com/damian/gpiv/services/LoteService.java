package com.damian.gpiv.services;

import com.damian.gpiv.database.Database;
import com.damian.gpiv.models.Lote;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoteService {

    private final NotificationService notifier;

    public LoteService() {
        this.notifier = new NotificationService();
    }

    public void registrar(Lote lote) {
        String sql = "INSERT INTO lotes (superficie, estado, empresa_id) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, lote.getSuperficie());

            if (lote.getEmpresaId() != 0) {
                pstmt.setString(2, "ocupado");
                pstmt.setInt(3, lote.getEmpresaId());
            } else {
                pstmt.setString(2, "disponible");
                pstmt.setNull(3, java.sql.Types.INTEGER);
            }

            pstmt.executeUpdate();
            notifier.notify("Nuevo Lote", "Se registró un lote de " + lote.getSuperficie() + " m2");

        } catch (SQLException e) {
            if (e.getMessage().contains("FOREIGN KEY constraint failed")) {
                System.err.println("La empresa indicada no existe, no se puede registrar el lote.");
                notifier.notify("Error", "La empresa indicada no existe.");
            } else {
                System.err.println("Error al registrar lote: " + e.getMessage());
            }
        }
    }


    public void actualizarEstado(int loteId, String nuevoEstado) {
        String sql = "UPDATE lotes SET estado=? WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, loteId);

            pstmt.executeUpdate();
            notifier.notify("Estado de Lote", "Lote " + loteId + " actualizado a: " + nuevoEstado);

        } catch (SQLException e) {
            System.err.println("Error al actualizar estado de lote: " + e.getMessage());
        }
    }

    public void asociarEmpresa(int loteId, int empresaId) {
        String sql = "UPDATE lotes SET empresa_id = ?, estado = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, empresaId);
            pstmt.setString(2, "ocupado");
            pstmt.setInt(3, loteId);

            pstmt.executeUpdate();
            notifier.notify("Lote ocupado", "El lote " + loteId + " fue asignado a la empresa " + empresaId);

        } catch (SQLException e) {
            if (e.getMessage().contains("FOREIGN KEY constraint failed")) {
                System.err.println("La empresa indicada no existe, no se puede asociar el lote.");
                notifier.notify("Error", "La empresa indicada no existe.");
            } else {
                System.err.println("Error al asociar lote: " + e.getMessage());
            }
        }
    }

    // NUEVO MÉTODO: Busca el lote individual asignado a la empresa para cumplir con la privacidad
    public Lote buscarPorEmpresaId(int empresaId) {
        String sql = "SELECT * FROM lotes WHERE empresa_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, empresaId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Lote(
                        rs.getInt("id"),
                        rs.getInt("superficie"),
                        rs.getString("estado"),
                        rs.getInt("empresa_id")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar lote por empresaId: " + e.getMessage());
        }

        return null;
    }

    public List<Lote> listar() {
        List<Lote> lotes = new ArrayList<>();
        String sql = "SELECT * FROM lotes";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Lote lote = new Lote(
                        rs.getInt("id"),
                        rs.getInt("superficie"),
                        rs.getString("estado"),
                        rs.getInt("empresa_id")
                );
                lotes.add(lote);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar lotes: " + e.getMessage());
        }

        return lotes;
    }

    public List<Lote> listarDisponibles() {
        List<Lote> lotes = new ArrayList<>();
        String sql = "SELECT * FROM lotes WHERE estado = 'disponible'";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Lote lote = new Lote(
                        rs.getInt("id"),
                        rs.getInt("superficie"),
                        rs.getString("estado"),
                        rs.getInt("empresa_id")
                );
                lotes.add(lote);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar lotes disponibles: " + e.getMessage());
        }

        return lotes;
    }
}