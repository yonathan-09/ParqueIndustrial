package com.damian.gpiv.gui;

import com.damian.gpiv.models.Proyecto;
import com.damian.gpiv.services.ProyectoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class EvaluarProyectosView extends JFrame {
    private final ProyectoService service;
    private JTextArea output;
    private JTextField txtProyectoId;

    public EvaluarProyectosView() {
        super("Evaluación de Proyectos - GPIV");
        this.service = new ProyectoService();

        // Tamaño mediano y optimizado para una lectura cómoda
        setSize(1000, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // COLOR VERDE CORPORATIVO ACTUALIZADO (Sincronizado con la foto)
        Color verdeFoto = new Color(93, 203, 82);

        // ---------------------------------------------------------------------
        // 1. BARRA SUPERIOR (Estilo Web con la Franja Verde)
        // ---------------------------------------------------------------------
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(verdeFoto);
        navbar.setPreferredSize(new Dimension(850, 75));
        navbar.setBorder(new EmptyBorder(0, 35, 0, 35));

        JLabel lblTitulo = new JLabel("Panel de Moderación y Evaluación de Proyectos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        navbar.add(lblTitulo, BorderLayout.WEST);

        add(navbar, BorderLayout.NORTH);

        // ---------------------------------------------------------------------
        // 2. PANEL CENTRAL (Contenido con Letras Grandes y GridBagLayout)
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

        // --- Fila 1: Entrada del ID de Proyecto ---
        JPanel panelInput = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelInput.setOpaque(false);

        JLabel lblProyectoId = new JLabel("Ingrese el ID del Proyecto:");
        lblProyectoId.setFont(new Font("Arial", Font.BOLD, 16)); // Letra grande
        lblProyectoId.setForeground(new Color(60, 65, 70));
        panelInput.add(lblProyectoId);

        txtProyectoId = new JTextField();
        txtProyectoId.setFont(new Font("Arial", Font.PLAIN, 16)); // Letra grande
        txtProyectoId.setPreferredSize(new Dimension(150, 38));
        panelInput.add(txtProyectoId);

        gbc.gridy = fila++;
        panelContenido.add(panelInput, gbc);

        // --- Fila 2: Botones de Acción (Redondeados con Borde Negro) ---
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panelAcciones.setOpaque(false);

        BotonRedondeado btnAprobar = new BotonRedondeado("✔ Aprobar");
        configurarBoton(btnAprobar, new Color(46, 139, 87)); // Verde oscuro para el éxito
        btnAprobar.addActionListener(e -> evaluarProyecto("aprobado"));
        panelAcciones.add(btnAprobar);

        BotonRedondeado btnRechazar = new BotonRedondeado("✖ Rechazar");
        configurarBoton(btnRechazar, new Color(220, 53, 69)); // Rojo para rechazos
        btnRechazar.addActionListener(e -> evaluarProyecto("rechazado"));
        panelAcciones.add(btnRechazar);

        BotonRedondeado btnListar = new BotonRedondeado("📋 Listar Proyectos");
        configurarBoton(btnListar, new Color(240, 243, 247));
        btnListar.setForeground(new Color(50, 55, 60)); // Letra oscura para contraste en gris
        btnListar.addActionListener(this::listar);
        panelAcciones.add(btnListar);

        BotonRedondeado btnDetalle =
                new BotonRedondeado("Ver Proyecto");

        configurarBoton(
                btnDetalle,
                new Color(52, 152, 219)
        );

        btnDetalle.addActionListener(
                e -> verDetalleProyecto()
        );

        panelAcciones.add(btnDetalle);

        gbc.gridy = fila++;
        gbc.insets = new Insets(10, 0, 15, 0);
        panelContenido.add(panelAcciones, gbc);
        gbc.insets = new Insets(6, 0, 6, 0); // Reset

        // --- Fila 3: Consola de Salida Estilizada ---
        output = new JTextArea(10, 50);
        output.setFont(new Font("Monospaced", Font.PLAIN, 15)); // Letra grande para el log
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

    /**
     * Aplica las fuentes grandes de 16px y el tamaño mediano estandarizado a los botones.
     */
    private void configurarBoton(JButton boton, Color colorFondo) {
        boton.setFont(new Font("Arial", Font.BOLD, 16)); // Letra grande e impactante
        boton.setBackground(colorFondo);
        if (boton.getForeground() == Color.BLACK || boton.getForeground() == new Color(51,51,51)) {
            boton.setForeground(Color.WHITE);
        } else if (colorFondo.getRed() > 230 && colorFondo.getGreen() > 230) {
            boton.setForeground(Color.BLACK); // Mantener texto oscuro en fondos muy claros si fuese necesario
        } else {
            boton.setForeground(Color.WHITE);
        }
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(190, 48)); // Botones medianos estilizados
    }

    private void verDetalleProyecto() {

        try {

            int proyectoId =
                    Integer.parseInt(txtProyectoId.getText());

            Proyecto proyecto =
                    service.buscarPorId(proyectoId);

            if (proyecto == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Proyecto inexistente"
                );

                return;
            }

            new DetalleProyectoView(proyecto);

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese un ID válido"
            );
        }
    }

    private void evaluarProyecto(String nuevoEstado) {
        String idIngresado = txtProyectoId.getText().trim();
        if (idIngresado.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe colocar el ID del proyecto a evaluar.",
                    "Campo Vacío",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int proyectoId = Integer.parseInt(idIngresado);
            service.actualizarEstado(proyectoId, nuevoEstado);

            output.append("» [" + java.time.LocalTime.now().toString().substring(0, 5) + "] ESTADO ACTUALIZADO: Proyecto ID "
                    + proyectoId + " cambiado a -> " + nuevoEstado.toUpperCase() + "\n");

            txtProyectoId.setText(""); // Limpiar campo tras la acción exitosa
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "El ID de proyecto debe ser un valor numérico entero.",
                    "Error de Formato",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listar(ActionEvent e) {
        List<Proyecto> proyectos = service.listar();
        output.setText("");
        if (proyectos.isEmpty()) {
            output.setText("No existen solicitudes de proyectos en el sistema.");
            return;
        }

        output.append("======================== LISTADO GENERAL DE PROYECTOS ========================\n");
        for (Proyecto p : proyectos) {
            output.append(" ID: " + p.getId() + " | Nombre: " + p.getNombre().toUpperCase() + "\n"
                    + "   • Estado: " + p.getEstado().toUpperCase() + " | Empresa ID Asoc: " + p.getEmpresaId() + "\n"
                    + " ─────────────────────────────────────────────────────────────────────────\n");
        }
    }

    /**
     * Componente personalizado de botón con puntas redondeadas medianas y borde negro grueso de 2px.
     */
    private static class BotonRedondeado extends JButton {
        private final int radioArco = 18; // Puntas redondeadas medianas

        public BotonRedondeado(String texto) {
            super(texto);
            setContentAreaFilled(false);
            setBorderPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Pintar fondo del botón
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radioArco, radioArco));

            // Pintar contorno de 2px negro remarcado
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2f));
            g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, radioArco, radioArco));

            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EvaluarProyectosView::new);
    }
}
