package com.damian.gpiv.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String DB_URL = "jdbc:sqlite:gpiv.db";

    // Conexión
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);

        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }

        return conn;
    }

    // Inicializar base de datos
    public static void initDB() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

            // Tabla de empresas
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS empresas (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL,
                    tipo TEXT CHECK(tipo IN ('radicada','interesada')),
                    email TEXT,
                    estado TEXT DEFAULT 'pendiente',
                    cuit TEXT,
                    actividad_principal TEXT,
                    direccion TEXT,
                    referente TEXT,
                    telefono TEXT,
                    rubro TEXT,
                    descripcion_servicio TEXT
                );
            """);

            // Tabla de solicitudes de radicación
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS solicitudes_radicacion (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL,
                    email TEXT,
                    cuit TEXT,
                    actividad_principal TEXT,
                    direccion TEXT,
                    referente TEXT,
                    telefono TEXT,
                    rubro TEXT,
                    descripcion_servicio TEXT,
                    estado TEXT DEFAULT 'pendiente'
                );
            """);

            // Tabla de proyectos
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS proyectos (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        nombre TEXT NOT NULL,
                        descripcion TEXT,
                        superficie_lote REAL,
                        estado TEXT DEFAULT 'en evaluación',
                        empresa_id INTEGER,
                        solicitud_id INTEGER,
                        FOREIGN KEY(empresa_id) REFERENCES empresas(id)
                    );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS proyecto_archivos (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    proyecto_id INTEGER NOT NULL,
                    ruta_archivo TEXT NOT NULL,
                    FOREIGN KEY(proyecto_id) REFERENCES proyectos(id)
                );
            """);


            // Tabla de lotes
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS lotes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    superficie INTEGER NOT NULL,
                    estado TEXT DEFAULT 'disponible',
                    empresa_id INTEGER,
                    FOREIGN KEY(empresa_id) REFERENCES empresas(id)
                )
            """);

            // Tabla de usuarios (MODIFICADA CON EMPRESA_ID)
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS usuarios (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT UNIQUE NOT NULL,
                    rol TEXT CHECK(rol IN ('administrador','empresa','organismo','proveedor')),
                    password TEXT NOT NULL,
                    empresa_id INTEGER,
                    FOREIGN KEY(empresa_id) REFERENCES empresas(id) ON DELETE SET NULL
                )
            """);

            // Tabla de notificaciones
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS notificaciones (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    titulo TEXT,
                    mensaje TEXT,
                    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    usuario_id INTEGER,
                    FOREIGN KEY(usuario_id) REFERENCES usuarios(id)
                )
            """);


            System.out.println("Base de datos inicializada correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
        }
    }

    // Método main para probar
    public static void main(String[] args) {
        initDB();
    }
}