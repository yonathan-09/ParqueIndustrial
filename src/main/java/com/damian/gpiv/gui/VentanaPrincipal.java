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

        setSize(1100, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Color verdeFoto = new Color(93, 203, 82);

        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(verdeFoto);
        navbar.setPreferredSize(new Dimension(1100, 70));
        navbar.setBorder(new EmptyBorder(0, 40, 0, 40));

        JLabel lblLogo = new JLabel("GPIV   |   PARQUE INDUSTRIAL");
        lblLogo.setFont(new Font("Arial", Font.BOLD, 20));
        lblLogo.setForeground(Color.WHITE);
        navbar.add(lblLogo, BorderLayout.WEST);

        JPanel panelPerfil = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        panelPerfil.setOpaque(false);

        JLabel lblSesion = new JLabel("Rol: " + rol.toUpperCase());
        lblSesion.setFont(new Font("Arial", Font.BOLD, 14));
        lblSesion.setForeground(Color.WHITE);
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

        JPanel cuerpoWeb = new JPanel(new GridBagLayout());
        cuerpoWeb.setBackground(Color.WHITE);
        cuerpoWeb.setBorder(new EmptyBorder(40, 60, 40, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(12, 0, 12, 0);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblBienvenida = new JLabel("Panel de Gestión Operativa");
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 28));
        lblBienvenida.setForeground(new Color(40, 45, 50));
        cuerpoWeb.add(lblBienvenida, gbc);

        gbc.gridy++;
        JLabel lblSubtitulo = new JLabel("Seleccione una de las siguientes opciones operativas disponibles para su perfil:");
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 16));
        lblSubtitulo.setForeground(Color.GRAY);
        gbc.insets = new Insets(0, 0, 30, 0);
        cuerpoWeb.add(lblSubtitulo, gbc);

        gbc.insets = new Insets(10, 0, 10, 0);

        switch (rol.toLowerCase()) {
            case "administrador":
                addWebButton(cuerpoWeb, gbc, "Evaluar Solicitudes", this::openEvaluarEmpresas);
                addWebButton(cuerpoWeb, gbc, "Gestionar Proyectos", this::openProyectos);
                addWebButton(cuerpoWeb, gbc, "Evaluar Proyectos", this::openEvaluarProyectos);
                addWebButton(cuerpoWeb, gbc, "Gestionar Lotes", this::openLotes);
                addWebButton(cuerpoWeb, gbc, "Ver Reportes", this::openReportes);
                addWebButton(cuerpoWeb, gbc, "Mapa de Lotes", this::openLoteMap);
                addWebButton(cuerpoWeb, gbc, "Registrar un nuevo usuario", this::openRegistroUsuario);
                break;

            case "empresa":
                addWebButton(cuerpoWeb, gbc, "Ver Proyectos", this::openProyectosConsulta);
                addWebButton(cuerpoWeb, gbc, "Mapa de Lotes", this::openLoteMap);
                break;

            case "organismo":
                addWebButton(cuerpoWeb, gbc, "Ver Proyectos", this::openProyectosConsulta);
                break;

            case "proveedor":
                addWebButton(cuerpoWeb, gbc, "Mapa de Lotes", this::openLoteMap);
                break;

            default:
                JOptionPane.showMessageDialog(this,
                        "Rol desconocido: " + rol,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
        }

        add(navbar, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(cuerpoWeb);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void addWebButton(JPanel panel, GridBagConstraints gbc, String text, Runnable action) {
        gbc.gridy++;

        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(new Color(250, 251, 253));
        button.setForeground(new Color(40, 50, 60));
        button.setFocusPainted(false);

        button.setPreferredSize(new Dimension(550, 65));
        button.setHorizontalAlignment(SwingConstants.LEFT);

        Color verdeFoto = new Color(93, 203, 82);

        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 6, 0, 0, verdeFoto),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLACK, 2),
                        BorderFactory.createEmptyBorder(0, 20, 0, 0)
                )
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

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

    private void openEmpresas() { SwingUtilities.invokeLater(EmpresaView::new); }
    private void openEvaluarEmpresas() { SwingUtilities.invokeLater(EvaluarEmpresaView::new); }
    private void openProyectos() { SwingUtilities.invokeLater(ProyectoView::new); }
    private void openProyectosConsulta() { SwingUtilities.invokeLater(() -> new ProyectoConsultaView(rol)); }
    private void openEvaluarProyectos() { SwingUtilities.invokeLater(EvaluarProyectosView::new); }
    private void openLotes() { SwingUtilities.invokeLater(LoteView::new); }
    private void openReportes() { SwingUtilities.invokeLater(ReporteView::new); }
    private void openLoteMap() { SwingUtilities.invokeLater(LoteMapView::new); }
    private void openRegistroUsuario() { SwingUtilities.invokeLater(RegistroUsuarioView::new); }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal("administrador"));
    }
}

