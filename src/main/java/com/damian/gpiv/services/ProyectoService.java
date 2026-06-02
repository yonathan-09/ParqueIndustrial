package com.damian.gpiv.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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

    // Registrar proyecto sin archivos
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

    // Registrar proyecto con archivos PDF
    public void registrarConArchivos(Proyecto proyecto, List<File> archivos) {
        String sql = "INSERT INTO proyectos (nombre, descripcion, estado, empresa_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, proyecto.getNombre());
            pstmt.setString(2, proyecto.getDescripcion());
            pstmt.setString(3, proyecto.getEstado());
            if (proyecto.getEmpresaId() != null) {
                pstmt.setInt(4, proyecto.getEmpresaId());
            } else {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            }

            pstmt.executeUpdate();

            // Obtener ID generado del proyecto
            int proyectoId = -1;
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    proyectoId = rs.getInt(1);
                }
            }

            // Guardar archivos PDF en carpeta local
            if (proyectoId > 0 && archivos != null) {
                Path carpeta = Path.of("uploads/proyectos/" + proyectoId);
                Files.createDirectories(carpeta);

                for (File archivo : archivos) {
                    Path destino = carpeta.resolve(archivo.getName());
                    try {
                        Files.copy(archivo.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);

                        // Guardar referencia en tabla proyecto_archivos
                        String sqlArchivo = "INSERT INTO proyecto_archivos (proyecto_id, ruta) VALUES (?, ?)";
                        try (PreparedStatement pstmtArchivo = conn.prepareStatement(sqlArchivo)) {
                            pstmtArchivo.setInt(1, proyectoId);
                            pstmtArchivo.setString(2, destino.toString());
                            pstmtArchivo.executeUpdate();
                        }

                    } catch (IOException ex) {
                        System.err.println("Error al copiar archivo: " + ex.getMessage());
                    }
                }
            }

            notifier.notify("Nuevo Proyecto", "Se registró el proyecto con archivos: " + proyecto.getNombre());

        } catch (SQLException | IOException e) {
            System.err.println("Error al registrar proyecto con archivos: " + e.getMessage());
        }
    }

    public void actualizarEstado(int proyectoId, String nuevoEstado) {
        String sql = "UPDATE proyectos SET estado=? WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, proyectoId);
            pstmt.executeUpdate();

            notifier.notify("Evaluación de Proyecto",
                    "Proyecto " + proyectoId + " actualizado a: " + nuevoEstado);

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

    public List<Proyecto> listarPorEmpresa(int empresaId) {
        List<Proyecto> proyectos = new ArrayList<>();
        String sql = "SELECT * FROM proyectos WHERE empresa_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, empresaId);
            ResultSet rs = pstmt.executeQuery();

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
