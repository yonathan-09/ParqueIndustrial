package com.damian.gpiv.services;

import com.damian.gpiv.database.Database;
import com.damian.gpiv.models.SolicitudRadicacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SolicitudRadicacionService {

    public void registrar(SolicitudRadicacion solicitud) {

        String sql = """
                INSERT INTO solicitudes_radicacion
                (
                    nombre,
                    email,
                    cuit,
                    actividad_principal,
                    direccion,
                    referente,
                    telefono,
                    rubro,
                    descripcion_servicio
                )
                VALUES (?,?,?,?,?,?,?,?,?)
                """;

        try(Connection conn = Database.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, solicitud.getNombre());
            pstmt.setString(2, solicitud.getEmail());
            pstmt.setString(3, solicitud.getCuit());
            pstmt.setString(4, solicitud.getActividadPrincipal());
            pstmt.setString(5, solicitud.getDireccion());
            pstmt.setString(6, solicitud.getReferente());
            pstmt.setString(7, solicitud.getTelefono());
            pstmt.setString(8, solicitud.getRubro());
            pstmt.setString(9, solicitud.getDescripcionServicio());

            pstmt.executeUpdate();

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public List<SolicitudRadicacion> listarPendientes() {

        List<SolicitudRadicacion> lista = new ArrayList<>();

        String sql =
                "SELECT * FROM solicitudes_radicacion WHERE estado='pendiente'";

        try(Connection conn = Database.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            while(rs.next()) {

                lista.add(
                        new SolicitudRadicacion(
                                rs.getInt("id"),
                                rs.getString("nombre"),
                                rs.getString("email"),
                                rs.getString("cuit"),
                                rs.getString("actividad_principal"),
                                rs.getString("direccion"),
                                rs.getString("referente"),
                                rs.getString("telefono"),
                                rs.getString("rubro"),
                                rs.getString("descripcion_servicio"),
                                rs.getString("estado")
                        )
                );
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // =====================================================
    // APROBAR SOLICITUD
    // =====================================================

    public void aprobarSolicitud(int solicitudId) {

        SolicitudRadicacion solicitud = buscarPorId(solicitudId);

        if(solicitud == null) {
            System.out.println("Solicitud inexistente.");
            return;
        }

        try(Connection conn = Database.getConnection()) {

            // 1) Marcar solicitud aprobada
            String updateSolicitud =
                    "UPDATE solicitudes_radicacion SET estado='aprobada' WHERE id=?";

            try(PreparedStatement pstmt =
                        conn.prepareStatement(updateSolicitud)) {

                pstmt.setInt(1, solicitudId);
                pstmt.executeUpdate();
            }

            // 2) Crear empresa radicada
            String insertEmpresa = """
            INSERT INTO empresas
            (
                nombre,
                tipo,
                email,
                estado,
                cuit,
                actividad_principal,
                direccion,
                referente,
                telefono,
                rubro,
                descripcion_servicio
            )
            VALUES (?,?,?,?,?,?,?,?,?,?,?)
            """;

            try(PreparedStatement pstmt =
                        conn.prepareStatement(insertEmpresa)) {

                pstmt.setString(1, solicitud.getNombre());
                pstmt.setString(2, "radicada");
                pstmt.setString(3, solicitud.getEmail());
                pstmt.setString(4, "aprobada");
                pstmt.setString(5, solicitud.getCuit());
                pstmt.setString(6, solicitud.getActividadPrincipal());
                pstmt.setString(7, solicitud.getDireccion());
                pstmt.setString(8, solicitud.getReferente());
                pstmt.setString(9, solicitud.getTelefono());
                pstmt.setString(10, solicitud.getRubro());
                pstmt.setString(11, solicitud.getDescripcionServicio());

                pstmt.executeUpdate();
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // RECHAZAR SOLICITUD
    // =====================================================

    public void rechazarSolicitud(int solicitudId) {

        String sql =
                "UPDATE solicitudes_radicacion SET estado='rechazada' WHERE id=?";

        try(Connection conn = Database.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, solicitudId);
            pstmt.executeUpdate();

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    public SolicitudRadicacion buscarPorId(int id) {

        String sql =
                "SELECT * FROM solicitudes_radicacion WHERE id=?";

        try(Connection conn = Database.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {

                return new SolicitudRadicacion(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("cuit"),
                        rs.getString("actividad_principal"),
                        rs.getString("direccion"),
                        rs.getString("referente"),
                        rs.getString("telefono"),
                        rs.getString("rubro"),
                        rs.getString("descripcion_servicio"),
                        rs.getString("estado")
                );
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


}