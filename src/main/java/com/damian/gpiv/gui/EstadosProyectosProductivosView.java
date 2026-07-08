package com.damian.gpiv.gui;

import com.damian.gpiv.database.Database;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EstadosProyectosProductivosView extends JFrame {

    public EstadosProyectosProductivosView() {
        super("Estados de Proyectos Productivos - Panel de Control");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Encabezado estético con el verde institucional del parque
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(93, 203, 82));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel("Control de Avances - Proyectos Productivos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo);
        add(panelSuperior, BorderLayout.NORTH);

        // Definimos las columnas de la tabla
        String[] columnas = {"ID Empresa", "Empresa (Razón Social)", "Proyecto Productivo", "Porcentaje de Avance"};

        // Creamos el modelo de tabla haciendo que las celdas no sean editables directamente por teclado
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = new JTable(modeloTabla);
        tabla.setFont(new Font("Arial", Font.PLAIN, 14));
        tabla.setRowHeight(25);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // Consulta SQL con un LEFT JOIN para traer el proyecto de la empresa radicada
        String sql = "SELECT e.id AS emp_id, e.nombre AS emp_nombre, p.nombre AS proy_nombre, p.porcentaje_avance " +
                "FROM empresas e " +
                "LEFT JOIN proyectos p ON e.id = p.empresa_id " +
                "WHERE e.tipo = 'radicada'";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("emp_id");
                String empresa = rs.getString("emp_nombre");
                String proyecto = rs.getString("proy_nombre");

                // Si la empresa es radicada pero aún no se le cargó un proyecto en el sistema
                if (proyecto == null) {
                    proyecto = "Sin Proyecto Registrado";
                }

                int avance = rs.getInt("porcentaje_avance");
                String avanceStr = avance + "%";

                // Agregamos la fila a la tabla
                modeloTabla.addRow(new Object[]{id, empresa, proyecto, avanceStr});
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los estados productivos desde la base de datos:\n" + e.getMessage(),
                    "Error de Carga",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }
}