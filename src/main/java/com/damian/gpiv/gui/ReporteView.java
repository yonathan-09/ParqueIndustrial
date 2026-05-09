package com.damian.gpiv.gui;

import com.damian.gpiv.services.ReporteService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ReporteView extends JFrame {
    private final ReporteService service;

    public ReporteView() {
        super("Reportes");
        this.service = new ReporteService();

        setLayout(new GridLayout(0, 1, 10, 10));

        JButton btnEmpresas = new JButton("Reporte de Empresas");
        btnEmpresas.addActionListener(this::generarReporteEmpresas);
        add(btnEmpresas);

        JButton btnLotes = new JButton("Reporte de Lotes");
        btnLotes.addActionListener(this::generarReporteLotes);
        add(btnLotes);

        JButton btnProyectos = new JButton("Reporte de Proyectos");
        btnProyectos.addActionListener(this::generarReporteProyectos);
        add(btnProyectos);

        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void generarReporteEmpresas(ActionEvent e) {
        service.generarReporteEmpresas();
    }

    private void generarReporteLotes(ActionEvent e) {
        service.generarReporteLotes();
    }

    private void generarReporteProyectos(ActionEvent e) {
        service.generarReporteProyectos();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ReporteView::new);
    }
}
