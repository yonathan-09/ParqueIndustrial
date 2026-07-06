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
import com.damian.gpiv.models.Empresa;
import com.damian.gpiv.models.Proyecto;

public class ProyectoService {

    private final NotificationService notifier;

    public ProyectoService() {
        this.notifier = new NotificationService();
    }

    public void registrar(Proyecto proyecto) {
        String sql = "INSERT INTO proyectos (nombre, descripcion, superficie_lote, estado, empresa_id, solicitud_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, proyecto.getNombre());
            pstmt.setString(2, proyecto.getDescripcion());
            pstmt.setDouble(3, proyecto.getSuperficieLote());
            pstmt.setString(4, proyecto.getEstado());
            if (proyecto.getEmpresaId() != null) {
                pstmt.setInt(5, proyecto.getEmpresaId());
            } else {
                pstmt.setNull(5, java.sql.Types.INTEGER);
            }

            if (proyecto.getSolicitudId() != null) {
                pstmt.setInt(6, proyecto.getSolicitudId());
            } else {
                pstmt.setNull(6, java.sql.Types.INTEGER);
            }

            pstmt.executeUpdate();
            notifier.notify("Nuevo Proyecto", "Se registró el proyecto: " + proyecto.getNombre());

        } catch (SQLException e) {
            System.err.println("Error al registrar proyecto: " + e.getMessage());
        }
    }

    public void registrarConArchivos(Proyecto proyecto, List<File> archivos) {
        registrar(proyecto);

        String sql = "INSERT INTO proyecto_archivos (proyecto_id, ruta_archivo) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (File archivo : archivos) {
                pstmt.setInt(1, proyecto.getId()); // el ID generado del proyecto
                pstmt.setString(2, archivo.getAbsolutePath());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
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

    public Proyecto buscarPorId(int id) {
        String sql = "SELECT * FROM proyectos WHERE id=?";

        try(Connection conn = Database.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                Integer empresaId = null;
                int valorEmpresa = rs.getInt("empresa_id");

                if(!rs.wasNull()) {
                    empresaId = valorEmpresa;
                }

                return new Proyecto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("estado"),
                        empresaId,
                        rs.getInt("solicitud_id"),
                        rs.getDouble("superficie_lote")
                );
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // NUEVO MÉTODO: Busca el proyecto individual de la empresa para cumplir con la privacidad
    public Proyecto buscarPorEmpresaId(int empresaId) {
        String sql = "SELECT * FROM proyectos WHERE empresa_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, empresaId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Proyecto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("estado"),
                        rs.getInt("empresa_id"),
                        rs.getInt("solicitud_id"),
                        rs.getDouble("superficie_lote")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar proyecto por empresaId: " + e.getMessage());
        }

        return null;
    }

    public List<File> obtenerArchivosAdjuntos(int proyectoId) {
        List<File> archivos = new ArrayList<>();
        String sql = "SELECT ruta_archivo FROM proyecto_archivos WHERE proyecto_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, proyectoId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String ruta = rs.getString("ruta_archivo");
                archivos.add(new File(ruta));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener archivos adjuntos: " + e.getMessage());
        }

        return archivos;
    }

    public List<Proyecto> listar() {
        List<Proyecto> proyectos = new ArrayList<>();
        String sql = "SELECT * FROM proyectos";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            EmpresaService empresaService = new EmpresaService();

            while (rs.next()) {
                Integer empresaId = null;
                int valorEmpresa = rs.getInt("empresa_id");

                if (!rs.wasNull()) {
                    empresaId = valorEmpresa;
                }

                Proyecto proyecto = new Proyecto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("estado"),
                        empresaId,
                        rs.getInt("solicitud_id"),
                        rs.getDouble("superficie_lote")
                );

                if (empresaId != null) {
                    Empresa empresa = empresaService.buscarPorId(empresaId);
                    proyecto.setEmpresa(empresa);
                }

                proyectos.add(proyecto);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar proyectos: " + e.getMessage());
        }

        return proyectos;
    }

    public boolean existeProyectoParaSolicitud(int solicitudId) {
        String sql = "SELECT COUNT(*) FROM proyectos WHERE solicitud_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, solicitudId);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Proyecto> listarPorEmpresa(int empresaId) {
        List<Proyecto> proyectos = new ArrayList<>();
        String sql = "SELECT * FROM proyectos WHERE empresa_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, empresaId);
            ResultSet rs = pstmt.executeQuery();

            EmpresaService empresaService = new EmpresaService();

            while (rs.next()) {
                Proyecto proyecto = new Proyecto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("estado"),
                        rs.getInt("empresa_id"),
                        rs.getInt("solicitud_id"),
                        rs.getDouble("superficie_lote")
                );

                Empresa empresa = empresaService.buscarPorId(rs.getInt("empresa_id"));
                proyecto.setEmpresa(empresa);

                proyectos.add(proyecto);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar proyectos: " + e.getMessage());
        }

        return proyectos;
    }
}