package com.damian.gpiv.services;

import com.damian.gpiv.database.Database;
import com.damian.gpiv.models.Empresa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

public class EmpresaService {

    public void registrar(Empresa empresa) {
        String sql = "INSERT INTO empresas " +
                "(nombre, tipo, email, estado, cuit, actividad_principal, direccion, referente, telefono, rubro, descripcion_servicio) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, empresa.getNombre());
            pstmt.setString(2, empresa.getTipo());
            pstmt.setString(3, empresa.getEmail());

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

    public int registrarSolicitud(
            String nombre,
            String email,
            String cuit,
            String actividadPrincipal,
            String direccion,
            String referente,
            String telefono,
            String rubro,
            String descripcionServicio) {

        String sql = """
        INSERT INTO solicitudes_radicacion
        (nombre,email,cuit,actividad_principal,direccion,referente,telefono,rubro,descripcion_servicio,estado)
        VALUES(?,?,?,?,?,?,?,?,?,'pendiente')
    """;

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt =
                     conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, nombre);
            pstmt.setString(2, email);
            pstmt.setString(3, cuit);
            pstmt.setString(4, actividadPrincipal);
            pstmt.setString(5, direccion);
            pstmt.setString(6, referente);
            pstmt.setString(7, telefono);
            pstmt.setString(8, rubro);
            pstmt.setString(9, descripcionServicio);

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();

            if(rs.next()) {
                return rs.getInt(1);
            }



        } catch (SQLException e) {
            System.err.println("Error al registrar solicitud: " + e.getMessage());
        }

        return -1;
    }

    public List<Empresa> listarSolicitudes() {

        List<Empresa> solicitudes = new ArrayList<>();

        String sql = "SELECT * FROM solicitudes_radicacion";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {

                Empresa empresa = new Empresa(
                        rs.getString("nombre"),
                        rs.getInt("id"),
                        "interesada",
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

                solicitudes.add(empresa);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar solicitudes: " + e.getMessage());
        }

        return solicitudes;
    }

    public void aprobarSolicitud(int solicitudId) {

        String buscar = "SELECT * FROM solicitudes_radicacion WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(buscar)) {

            pstmt.setInt(1, solicitudId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                Empresa empresa = new Empresa(
                        rs.getString("nombre"),
                        0,
                        "interesada",
                        rs.getString("email"),
                        "aprobada",
                        rs.getString("cuit"),
                        rs.getString("actividad_principal"),
                        rs.getString("direccion"),
                        rs.getString("referente"),
                        rs.getString("telefono"),
                        rs.getString("rubro"),
                        rs.getString("descripcion_servicio")
                );

                registrar(empresa);

                eliminarSolicitud(solicitudId);

                System.out.println("Solicitud aprobada.");
            }

        } catch (SQLException e) {
            System.err.println("Error al aprobar solicitud: " + e.getMessage());
        }
    }

    public void rechazarSolicitud(int solicitudId) {
        eliminarSolicitud(solicitudId);
    }

    private void eliminarSolicitud(int solicitudId) {

        String sql = "DELETE FROM solicitudes_radicacion WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, solicitudId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar solicitud: " + e.getMessage());
        }
    }

    public boolean existeEmpresa(Integer empresaId) {

        if (empresaId == null) {
            return false;
        }

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
        return null;
    }


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

    public void actualizarEstado(int empresaId, String nuevoEstado) {
        String sql;

        if ("aprobada".equalsIgnoreCase(nuevoEstado)) {
            sql = "UPDATE empresas SET estado=?, tipo='radicada' WHERE id=? AND tipo='interesada'";
        } else {
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
