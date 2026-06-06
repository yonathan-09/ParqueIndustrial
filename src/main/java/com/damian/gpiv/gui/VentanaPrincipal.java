package com.damian.gpiv.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class VentanaPrincipal extends JFrame {

    private final String rol;

    public VentanaPrincipal(String rol) {
        super("GPIV - Gestión del Parque Industrial de Viedma");
        this.rol = rol;

        // Base de la ventana principal
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // COLOR VERDE CORPORATIVO ACTUALIZADO (Sincronizado con el de la foto)
        Color verdeFoto = new Color(93, 203, 82);

        // ---------------------------------------------------------------------
        // 1. BARRA SUPERIOR (Estilo Navbar de Página Web)
        // ---------------------------------------------------------------------
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(verdeFoto); // Cambiado al verde brillante
        navbar.setPreferredSize(new Dimension(1100, 70));
        navbar.setBorder(new EmptyBorder(0, 40, 0, 40));

        // Título/Logo de la Web a la izquierda
        JLabel lblLogo = new JLabel("GPIV   |   PARQUE INDUSTRIAL");
        lblLogo.setFont(new Font("Arial", Font.BOLD, 20));
        lblLogo.setForeground(Color.WHITE);
        navbar.add(lblLogo, BorderLayout.WEST);

        // Perfil y Salida a la derecha
        JPanel panelPerfil = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        panelPerfil.setOpaque(false);

        JLabel lblSesion = new JLabel("Rol: " + rol.toUpperCase());
        lblSesion.setFont(new Font("Arial", Font.BOLD, 14));
        lblSesion.setForeground(Color.WHITE); // Cambiado a blanco para mejor contraste sobre el verde claro
        panelPerfil.add(lblSesion);

        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(LoginView::new);
        });
        panelPerfil.add(btnLogout);
        navbar.add(panelPerfil, BorderLayout.EAST);

        // ---------------------------------------------------------------------
        // 2. PANEL CENTRAL (Cuerpo de la Página Web)
        // ---------------------------------------------------------------------
        JPanel cuerpoWeb = new JPanel(new GridBagLayout());
        cuerpoWeb.setBackground(Color.WHITE); // Fondo blanco limpio de web
        cuerpoWeb.setBorder(new EmptyBorder(40, 60, 40, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(12, 0, 12, 0); // Espaciado entre los rectángulos
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST; // Clava absolutamente todo a la izquierda

        // Título de bienvenida de la página
        JLabel lblBienvenida = new JLabel("Panel de Gestión Operativa");
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 28));
        lblBienvenida.setForeground(new Color(40, 45, 50));
        cuerpoWeb.add(lblBienvenida, gbc);

        gbc.gridy++;
        JLabel lblSubtitulo = new JLabel("Seleccione una de las siguientes opciones operativas disponibles para su perfil:");
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 16));
        lblSubtitulo.setForeground(Color.GRAY);
        gbc.insets = new Insets(0, 0, 30, 0); // Espacio grande antes de los botones
        cuerpoWeb.add(lblSubtitulo, gbc);

        // Reajuste de margen para el listado de botones horizontales
        gbc.insets = new Insets(10, 0, 10, 0);

        // Inyección de botones según rol
        switch (rol.toLowerCase()) {
            case "administrador":
                addWebButton(cuerpoWeb, gbc, "Gestionar Empresas", this::openEmpresas);
                addWebButton(cuerpoWeb, gbc, "Evaluar Empresas", this::openEvaluarEmpresas);
                addWebButton(cuerpoWeb, gbc, "Gestionar Proyectos", this::openProyectos);
                addWebButton(cuerpoWeb, gbc, "Evaluar Proyectos", this::openEvaluarProyectos);
                addWebButton(cuerpoWeb, gbc, "Gestionar Lotes", this::openLotes);
                addWebButton(cuerpoWeb, gbc, "Ver Reportes", this::openReportes);
                addWebButton(cuerpoWeb, gbc, "Mapa Interactivo de Lotes", this::openLoteMap);
                break;

            case "empresa":
                addWebButton(cuerpoWeb, gbc, "Ver Proyectos", this::openProyectosConsulta);
                addWebButton(cuerpoWeb, gbc, "Mapa Interactivo de Lotes", this::openLoteMap);
                break;

            case "organismo":
                addWebButton(cuerpoWeb, gbc, "Ver Proyectos", this::openProyectosConsulta);
                break;

            case "proveedor":
                addWebButton(cuerpoWeb, gbc, "Ver Proyectos", this::openProyectosConsulta);
                addWebButton(cuerpoWeb, gbc, "Ver Reportes", this::openReportes);
                addWebButton(cuerpoWeb, gbc, "Mapa Interactivo de Lotes", this::openLoteMap);
                break;

            default:
                JOptionPane.showMessageDialog(this,
                        "Rol desconocido: " + rol,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
        }

        // Agregar secciones al Frame principal
        add(navbar, BorderLayout.NORTH);

        // Un JScrollPane envuelve el cuerpo por si los botones superan el alto de pantallas chicas
        JScrollPane scrollPane = new JScrollPane(cuerpoWeb);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    /**
     * Construye un botón rectangular horizontal gigante con bordes negros remarcados.
     */
    private void addWebButton(JPanel panel, GridBagConstraints gbc, String text, Runnable action) {
        gbc.gridy++;

        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18)); // Letra grande requerida
        button.setBackground(new Color(250, 251, 253));   // Fondo off-white limpio
        button.setForeground(new Color(40, 50, 60));      // Texto oscuro elegante
        button.setFocusPainted(false);

        // Dimensión de rectángulo horizontal gigante
        button.setPreferredSize(new Dimension(550, 65));
        button.setHorizontalAlignment(SwingConstants.LEFT);

        // COLOR VERDE CORPORATIVO (Para la pestaña lateral del botón)
        Color verdeFoto = new Color(93, 203, 82);

        // Diseño del borde: Línea verde de diseño a la izquierda + CONTORNO NEGRO REMARCADO
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 6, 0, 0, verdeFoto), // Pestaña verde a la izquierda
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLACK, 2), // CONTORNO NEGRO DE 2px PARA RESALTAR
                        BorderFactory.createEmptyBorder(0, 20, 0, 0)    // Desplazamiento del texto interno
                )
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto Hover básico al pasar el mouse (cambia sutilmente el fondo)
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(240, 243, 247));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(250, 251, 253));
            }
        });

        button.addActionListener((ActionEvent e) -> action.run());
        panel.add(button, gbc);
    }

    // Métodos lógicos de apertura
    private void openEmpresas() { SwingUtilities.invokeLater(EmpresaView::new); }
    private void openEvaluarEmpresas() { SwingUtilities.invokeLater(EvaluarEmpresaView::new); }
    private void openProyectos() { SwingUtilities.invokeLater(ProyectoView::new); }
    private void openProyectosConsulta() { SwingUtilities.invokeLater(() -> new ProyectoConsultaView(rol)); }
    private void openEvaluarProyectos() { SwingUtilities.invokeLater(EvaluarProyectosView::new); }
    private void openLotes() { SwingUtilities.invokeLater(LoteView::new); }
    private void openReportes() { SwingUtilities.invokeLater(ReporteView::new); }
    private void openLoteMap() { SwingUtilities.invokeLater(LoteMapView::new); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal("administrador"));
    }
}

