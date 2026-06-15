package com.damian.gpiv.gui;

import com.damian.gpiv.services.ReporteService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;

public class ReporteView extends JFrame {
    private final ReporteService service;

    public ReporteView() {
        super("Panel de Reportes - GPIV");
        this.service = new ReporteService();

        setSize(950, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        Color verdeFoto = new Color(93, 203, 82);

        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(verdeFoto);
        navbar.setPreferredSize(new Dimension(950, 80));
        navbar.setBorder(new EmptyBorder(0, 40, 0, 40));

        JLabel lblTitulo = new JLabel("Centro de Estadísticas y Reportes");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setForeground(Color.WHITE);
        navbar.add(lblTitulo, BorderLayout.WEST);

        add(navbar, BorderLayout.NORTH);

        JPanel panelContenido = new JPanel(new GridBagLayout());
        panelContenido.setBackground(Color.WHITE);
        panelContenido.setBorder(new EmptyBorder(40, 40, 40, 40));

        JPanel panelGridBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        panelGridBotones.setOpaque(false);

        BotonRedondeado btnEmpresas = new BotonRedondeado("Reporte de Empresas");
        configurarBotonReporte(btnEmpresas, verdeFoto);
        btnEmpresas.addActionListener(this::generarReporteEmpresas);
        panelGridBotones.add(btnEmpresas);

        BotonRedondeado btnLotes = new BotonRedondeado("Reporte de Lotes");
        configurarBotonReporte(btnLotes, verdeFoto);
        btnLotes.addActionListener(this::generarReporteLotes);
        panelGridBotones.add(btnLotes);

        BotonRedondeado btnProyectos = new BotonRedondeado("Reporte de Proyectos");
        configurarBotonReporte(btnProyectos, verdeFoto);
        btnProyectos.addActionListener(this::generarReporteProyectos);
        panelGridBotones.add(btnProyectos);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        panelContenido.add(panelGridBotones, gbc);

        add(panelContenido, BorderLayout.CENTER);
        setVisible(true);
    }

    private void configurarBotonReporte(JButton boton, Color colorFondo) {
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setBackground(colorFondo);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.setPreferredSize(new Dimension(240, 70));
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

    private static class BotonRedondeado extends JButton {
        private final int radioArco = 20;

        public BotonRedondeado(String texto) {
            super(texto);
            setContentAreaFilled(false);
            setBorderPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radioArco, radioArco));

            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2f));
            g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, radioArco, radioArco));

            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ReporteView::new);
    }
}
