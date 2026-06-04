package com.damian.gpiv.gui;

import com.damian.gpiv.models.Proyecto;
import com.damian.gpiv.services.ProyectoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class ProyectoConsultaView extends JFrame {
    private final ProyectoService service;
    private final String rolUsuario;
    private JTextArea output;

    // Constructor principal
    public ProyectoConsultaView(String rolUsuario) {
        super("Consulta de Proyectos - GPIV");
        this.service = new ProyectoService();
        this.rolUsuario = rolUsuario;

        // Dimensiones estandarizadas y compactas
        setSize(850, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // COLOR VERDE CORPORATIVO
        Color verdeFoto = new Color(93, 203, 82);

        // ---------------------------------------------------------------------
        // 1. BARRA SUPERIOR (Estilo Web con la Franja Verde)
        // ---------------------------------------------------------------------
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(verdeFoto);
        navbar.setPreferredSize(new Dimension(850, 75));
        navbar.setBorder(new EmptyBorder(0, 35, 0, 35));

        JLabel lblTitulo = new JLabel("Catálogo General de Proyectos Radicados");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        navbar.add(lblTitulo, BorderLayout.WEST);

        add(navbar, BorderLayout.NORTH);

        // ---------------------------------------------------------------------
        // 2. PANEL CENTRAL (Cuerpo limpio de Solo Lectura con Letras Grandes)
        // ---------------------------------------------------------------------
        JPanel panelContenido = new JPanel(new GridBagLayout());
        panelContenido.setBackground(Color.WHITE);
        panelContenido.setBorder(new EmptyBorder(25, 35, 25, 35));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 15, 0);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        int fila = 0;

        // Botón Redondeado Mediano con Borde Negro de 2px para listar
        BotonRedondeado btnListar = new BotonRedondeado(" Listar Proyectos Disponibles");
        btnListar.setFont(new Font("Arial", Font.BOLD, 16));
        btnListar.setBackground(new Color(240, 243, 247));
        btnListar.setForeground(new Color(50, 55, 60));
        btnListar.setPreferredSize(new Dimension(280, 48));
        btnListar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnListar.addActionListener(this::listar);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelBoton.setOpaque(false);
        panelBoton.add(btnListar);

        gbc.gridy = fila++;
        panelContenido.add(panelBoton, gbc);

        // Área de salida de datos (Grande y legible)
        output = new JTextArea(12, 50);
        output.setFont(new Font("Monospaced", Font.PLAIN, 15)); // Letra grande para lectura clara
        output.setEditable(false);
        output.setBorder(BorderFactory.createLineBorder(new Color(210, 215, 220), 1));

        JScrollPane scroll = new JScrollPane(output);

        gbc.gridy = fila++;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        panelContenido.add(scroll, gbc);

        add(panelContenido, BorderLayout.CENTER);
        setVisible(true);
    }

    private void listar(ActionEvent e) {
        output.setText("");
        List<Proyecto> proyectos = service.listar();

        boolean hayProyectos = false;
        output.append("======================== PROYECTOS DISPONIBLES ========================\n");

        for (Proyecto p : proyectos) {
            // Tu lógica original de filtrado por estado sin alterar absolutamente nada
            if ("pendiente".equalsIgnoreCase(p.getEstado()) || "aprobado".equalsIgnoreCase(p.getEstado())) {
                hayProyectos = true;
                output.append(" ID PROYECTO: " + p.getId() + " | " + p.getNombre().toUpperCase() + "\n"
                        + "   • Estado Operativo: " + p.getEstado().toUpperCase() + "\n"
                        + "   • ID Empresa Asociada: " + p.getEmpresaId() + "\n"
                        + " ─────────────────────────────────────────────────────────────────────────\n");
            }
        }

        if (!hayProyectos) {
            output.append("No se registran proyectos pendientes o aprobados en el sistema actualmente.\n");
        }
    }

    /**
     * Componente personalizado de botón con puntas redondeadas medianas y borde negro grueso de 2px.
     */
    private static class BotonRedondeado extends JButton {
        private final int radioArco = 18;

        public BotonRedondeado(String texto) {
            super(texto);
            setContentAreaFilled(false);
            setBorderPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Fondo del botón
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radioArco, radioArco));

            // Contorno negro de 2px sólido remarcado para resaltar
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2f));
            g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, radioArco, radioArco));

            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        // Simulación: organismo
        SwingUtilities.invokeLater(() -> new ProyectoConsultaView("organismo"));
    }
}
