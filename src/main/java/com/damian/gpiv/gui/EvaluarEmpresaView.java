package com.damian.gpiv.gui;

import com.damian.gpiv.models.SolicitudRadicacion;
import com.damian.gpiv.services.ProyectoService;
import com.damian.gpiv.services.SolicitudRadicacionService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class EvaluarEmpresaView extends JFrame {
    private final SolicitudRadicacionService service;
    private JTextArea output;
    private JTextField txtEmpresaId;

    public EvaluarEmpresaView() {
        super("Evaluar Empresas Interesadas");
        this.service = new SolicitudRadicacionService();

        // Dimensiones optimizadas para la interfaz web con letra grande
        setSize(950, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // COLOR VERDE CORPORATIVO
        Color verdeFoto = new Color(93, 203, 82);

        // ---------------------------------------------------------------------
        // 1. BARRA SUPERIOR (Estilo Navbar Web)
        // ---------------------------------------------------------------------
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(verdeFoto);
        navbar.setPreferredSize(new Dimension(850, 75));
        navbar.setBorder(new EmptyBorder(0, 35, 0, 35));

        JLabel lblTitulo = new JLabel("Panel de Control: Evaluar Empresas Interesadas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        navbar.add(lblTitulo, BorderLayout.WEST);

        add(navbar, BorderLayout.NORTH);

        // ---------------------------------------------------------------------
        // 2. PANEL CENTRAL (Contenido Visual)
        // ---------------------------------------------------------------------
        JPanel panelContenido = new JPanel(new GridBagLayout());
        panelContenido.setBackground(Color.WHITE);
        panelContenido.setBorder(new EmptyBorder(25, 35, 25, 35));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        int fila = 0;

        // Fila de Entrada para el ID
        JPanel panelInput = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelInput.setOpaque(false);

        JLabel lblEmpresaId = new JLabel("ID de Empresa");
        lblEmpresaId.setFont(new Font("Arial", Font.BOLD, 16));
        lblEmpresaId.setForeground(new Color(60, 65, 70));
        panelInput.add(lblEmpresaId);

        txtEmpresaId = new JTextField();
        txtEmpresaId.setFont(new Font("Arial", Font.PLAIN, 16));
        txtEmpresaId.setPreferredSize(new Dimension(150, 38));
        panelInput.add(txtEmpresaId);

        gbc.gridy = fila++;
        panelContenido.add(panelInput, gbc);

        // Fila de Botones (Redondeados y medianos con contorno negro)
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panelAcciones.setOpaque(false);

        BotonRedondeado btnAprobar = new BotonRedondeado("Aprobar");
        configurarBoton(btnAprobar, new Color(46, 139, 87));
        btnAprobar.addActionListener(e -> evaluarEmpresa("aprobada"));
        panelAcciones.add(btnAprobar);

        BotonRedondeado btnRechazar = new BotonRedondeado("Rechazar");
        configurarBoton(btnRechazar, new Color(220, 53, 69));
        btnRechazar.addActionListener(e -> evaluarEmpresa("rechazada"));
        panelAcciones.add(btnRechazar);

        BotonRedondeado btnListar = new BotonRedondeado("Listar Empresas");
        configurarBoton(btnListar, new Color(240, 243, 247));
        btnListar.setForeground(new Color(50, 55, 60));
        btnListar.addActionListener(this::listar);
        panelAcciones.add(btnListar);

        BotonRedondeado btnVerDetalle = new BotonRedondeado("Ver Detalle");
        configurarBoton(btnVerDetalle, new Color(52, 152, 219));
        btnVerDetalle.addActionListener(e -> verDetalleEmpresa());
        panelAcciones.add(btnVerDetalle);

        gbc.gridy = fila++;
        gbc.insets = new Insets(10, 0, 15, 0);
        panelContenido.add(panelAcciones, gbc);
        gbc.insets = new Insets(6, 0, 6, 0); // Reset

        // Área de salida de texto
        output = new JTextArea(10, 50);
        output.setFont(new Font("Monospaced", Font.PLAIN, 15));
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

    private void configurarBoton(JButton boton, Color colorFondo) {
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setBackground(colorFondo);
        if (boton.getForeground() != new Color(50, 55, 60)) {
            boton.setForeground(Color.WHITE);
        }
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(180, 48));
    }

    private void verDetalleEmpresa() {

        try {

            int solicitudId =
                    Integer.parseInt(txtEmpresaId.getText());

            SolicitudRadicacion solicitud =
                    service.buscarPorId(solicitudId);

            if (solicitud == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "No existe la solicitud"
                );

                return;
            }

            new DetalleEmpresaView(solicitud);

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese un ID válido"
            );
        }
    }

    private void evaluarEmpresa(String nuevoEstado) {

        try {

            int solicitudId = Integer.parseInt(txtEmpresaId.getText());

            if (nuevoEstado.equals("aprobada")) {

                ProyectoService proyectoService = new ProyectoService();

                if (!proyectoService.existeProyectoParaSolicitud(solicitudId)) {

                    JOptionPane.showMessageDialog(
                            this,
                            "No se puede aprobar la solicitud.\nLa empresa todavía no presentó un proyecto."
                    );

                    return;
                }

                service.aprobarSolicitud(solicitudId);

            } else {

                service.rechazarSolicitud(solicitudId);
            }

            JOptionPane.showMessageDialog(this,
                    "Solicitud actualizada correctamente.");

            txtEmpresaId.setText("");

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(this,
                    "Debe ingresar un ID válido.");
        }
    }

    private void listar(ActionEvent e) {

        List<SolicitudRadicacion> solicitudes =
                service.listarPendientes();

        output.setText("");

        for (SolicitudRadicacion s : solicitudes) {

            output.append(
                    "ID: " + s.getId()
                            + " | Empresa: " + s.getNombre()
                            + " | CUIT: " + s.getCuit()
                            + " | Estado: " + s.getEstado()
                            + "\n"
            );
        }
    }

    /**
     * Componente custom para botones con puntas redondeadas y borde negro remarcado.
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

            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radioArco, radioArco));

            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2f));
            g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, radioArco, radioArco));

            g2.dispose();
            super.paintComponent(g);
        }
    }
}
