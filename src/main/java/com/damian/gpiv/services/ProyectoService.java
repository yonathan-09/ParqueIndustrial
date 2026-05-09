package com.damian.gpiv.services;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.damian.gpiv.database.Database;
import com.damian.gpiv.models.Proyecto;

public class ProyectoService {

    private final NotificationService notifier;

    public ProyectoService() {
        this.notifier = new NotificationService();
    }

    // Registrar proyecto
    public void registrar(Proyecto proyecto) {
        String sql = "INSERT INTO proyectos (nombre, descripcion, estado, empresa_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, proyecto.getNombre());
            pstmt.setString(2, proyecto.getDescripcion());
            pstmt.setString(3, proyecto.getEstado());
            if (proyecto.getEmpresaId() != null) {
                pstmt.setInt(4, proyecto.getEmpresaId());
            } else {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            }

            pstmt.executeUpdate();
            notifier.notify("Nuevo Proyecto", "Se registró el proyecto: " + proyecto.getNombre());

        } catch (SQLException e) {
            System.err.println("Error al registrar proyecto: " + e.getMessage());
        }
    }

    // Actualizar estado de proyecto
    public void actualizarEstado(int proyectoId, String nuevoEstado) {
        String sql = "UPDATE proyectos SET estado=? WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, proyectoId);

            pstmt.executeUpdate();
            notifier.notify("Estado de Proyecto", "Proyecto " + proyectoId + " actualizado a: " + nuevoEstado);

        } catch (SQLException e) {
            System.err.println("Error al actualizar estado de proyecto: " + e.getMessage());
        }
    }

    // Listar proyectos
    public List<Proyecto> listar() {
        List<Proyecto> proyectos = new ArrayList<>();
        String sql = "SELECT * FROM proyectos";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Proyecto proyecto = new Proyecto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("estado"),
                        rs.getInt("empresa_id")
                );
                proyectos.add(proyecto);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar proyectos: " + e.getMessage());
        }

        return proyectos;
    }

}
