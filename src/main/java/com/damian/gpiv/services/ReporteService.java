package com.damian.gpiv.services;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.damian.gpiv.database.Database;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class ReporteService {

    public void generarReporteEmpresas() {
        String sql = "SELECT tipo, COUNT(*) FROM empresas GROUP BY tipo";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            while (rs.next()) {
                String tipo = rs.getString(1);
                int cantidad = rs.getInt(2);
                dataset.addValue(cantidad, "Empresas", tipo);
            }

            JFreeChart chart = ChartFactory.createBarChart(
                    "Empresas por tipo",
                    "Tipo",
                    "Cantidad",
                    dataset
            );

            ChartFrame frame = new ChartFrame("Reporte Empresas", chart);
            frame.pack();
            frame.setVisible(true);

        } catch (SQLException e) {
            System.err.println("Error al generar reporte de empresas: " + e.getMessage());
        }
    }

    // Reporte de lotes por estado (gráfico de torta)
    public void generarReporteLotes() {
        String sql = "SELECT estado, COUNT(*) FROM lotes GROUP BY estado";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            DefaultPieDataset dataset = new DefaultPieDataset();

            while (rs.next()) {
                String estado = rs.getString(1);
                int cantidad = rs.getInt(2);
                dataset.setValue(estado, cantidad);
            }

            JFreeChart chart = ChartFactory.createPieChart(
                    "Estado de los lotes",
                    dataset,
                    true, true, false
            );

            ChartFrame frame = new ChartFrame("Reporte Lotes", chart);
            frame.pack();
            frame.setVisible(true);

        } catch (SQLException e) {
            System.err.println("Error al generar reporte de lotes: " + e.getMessage());
        }
    }

    // Reporte de proyectos por estado (gráfico de barras)
    public void generarReporteProyectos() {
        String sql = "SELECT estado, COUNT(*) FROM proyectos GROUP BY estado";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            while (rs.next()) {
                String estado = rs.getString(1);
                int cantidad = rs.getInt(2);
                dataset.addValue(cantidad, "Proyectos", estado);
            }

            JFreeChart chart = ChartFactory.createBarChart(
                    "Proyectos por estado",
                    "Estado",
                    "Cantidad",
                    dataset
            );

            ChartFrame frame = new ChartFrame("Reporte Proyectos", chart);
            frame.pack();
            frame.setVisible(true);

        } catch (SQLException e) {
            System.err.println("Error al generar reporte de proyectos: " + e.getMessage());
        }
    }

}
