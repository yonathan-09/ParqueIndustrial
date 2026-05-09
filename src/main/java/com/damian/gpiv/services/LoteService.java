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

    // Registrar lote
    public void registrar(Lote lote) {
        String sql = "INSERT INTO lotes (superficie, estado, empresa_id) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, lote.getSuperficie());
            pstmt.setString(2, lote.getEstado());
            if (lote.getEmpresaId() != 0) {
                pstmt.setInt(3, lote.getEmpresaId());
            } else {
                pstmt.setNull(3, java.sql.Types.INTEGER);
            }

            pstmt.executeUpdate();
            notifier.notify("Nuevo Lote", "Se registró un lote de " + lote.getSuperficie() + " m2");

        } catch (SQLException e) {
            System.err.println("Error al registrar lote: " + e.getMessage());
        }
    }

    // Actualizar estado de lote
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

    // Listar lotes
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

}
