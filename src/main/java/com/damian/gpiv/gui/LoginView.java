package com.damian.gpiv.gui;

import com.damian.gpiv.models.Usuario;
import com.damian.gpiv.services.UsuarioService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URI;

public class LoginView extends JFrame {
    private final UsuarioService service;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;

    public LoginView() {
        super("Login GPIV");
        this.service = new UsuarioService();

        setLayout(new GridLayout(1, 2));

        Color verdeFoto = new Color(93, 203, 82);

        JPanel panelIzquierdo = new JPanel(new GridBagLayout());
        panelIzquierdo.setBackground(verdeFoto);
        panelIzquierdo.setBorder(new EmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbcIzquierdo = new GridBagConstraints();
        gbcIzquierdo.fill = GridBagConstraints.HORIZONTAL;
        gbcIzquierdo.gridx = 0;
        gbcIzquierdo.weightx = 1.0;

        JLabel lblTitulo = new JLabel("<html><center>PARQUE<br>INDUSTRIAL<br>VIEDMA</center></html>") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                setForeground(Color.BLACK);

                g2.translate(2, 2);
                super.paintComponent(g2);

                g2.translate(-2, -2);
                setForeground(Color.WHITE);
                super.paintComponent(g2);

                g2.dispose();
            }
        };
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 45));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbcIzquierdo.gridy = 0;
        gbcIzquierdo.insets = new Insets(0, 0, 30, 0);
        panelIzquierdo.add(lblTitulo, gbcIzquierdo);

        JSeparator sepMedio = new JSeparator();
        sepMedio.setForeground(Color.BLACK);
        gbcIzquierdo.gridy = 1;
        gbcIzquierdo.insets = new Insets(0, 10, 20, 10);
        panelIzquierdo.add(sepMedio, gbcIzquierdo);

        JLabel lblTituloContacto = new JLabel("Administración ENREPAVI");
        lblTituloContacto.setFont(new Font("Arial", Font.BOLD, 24));
        lblTituloContacto.setForeground(Color.BLACK);
        lblTituloContacto.setHorizontalAlignment(SwingConstants.CENTER);
        gbcIzquierdo.gridy = 2;
        gbcIzquierdo.insets = new Insets(0, 0, 15, 0);
        panelIzquierdo.add(lblTituloContacto, gbcIzquierdo);

        String[] datosContacto = {
                "-Dirección: Ruta Provincial N.° 1 Km 6.5, Viedma",
                "-Teléfono: +54 9 2920 213078",
                "️-Email: enrepavi@gmail.com",
                "-Horario: Lunes a Viernes — 08:00 a 14:00 hs."
        };

        int filaActual = 3;
        for (String dato : datosContacto) {
            JLabel lblDato = new JLabel(dato);
            lblDato.setFont(new Font("Arial", Font.PLAIN, 16));
            lblDato.setForeground(Color.BLACK);
            lblDato.setHorizontalAlignment(SwingConstants.LEFT);
            gbcIzquierdo.gridy = filaActual++;
            gbcIzquierdo.insets = new Insets(4, 15, 2, 15);
            panelIzquierdo.add(lblDato, gbcIzquierdo);

            JSeparator sublinea = new JSeparator();
            sublinea.setForeground(new Color(0, 0, 0, 60));
            gbcIzquierdo.gridy = filaActual++;
            gbcIzquierdo.insets = new Insets(0, 15, 8, 15);
            panelIzquierdo.add(sublinea, gbcIzquierdo);
        }

        JLabel lblTituloUbicacion = new JLabel("Ubicación");
        lblTituloUbicacion.setFont(new Font("Arial", Font.BOLD, 24));
        lblTituloUbicacion.setForeground(Color.BLACK);
        lblTituloUbicacion.setHorizontalAlignment(SwingConstants.CENTER);
        gbcIzquierdo.gridy = filaActual++;
        gbcIzquierdo.insets = new Insets(15, 0, 15, 0);
        panelIzquierdo.add(lblTituloUbicacion, gbcIzquierdo);

        JButton btnMaps = new JButton(" Ver en Google Maps");
        btnMaps.setFont(new Font("Arial", Font.BOLD, 15));
        btnMaps.setBackground(Color.WHITE);
        btnMaps.setForeground(Color.BLACK);
        btnMaps.setFocusPainted(false);
        btnMaps.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMaps.setPreferredSize(new Dimension(200, 40));

        btnMaps.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btnMaps.addActionListener(e -> abrirGoogleMaps());

        JPanel panelContenedorMaps = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelContenedorMaps.setOpaque(false);
        panelContenedorMaps.add(btnMaps);

        gbcIzquierdo.gridy = filaActual++;
        gbcIzquierdo.insets = new Insets(0, 0, 0, 0);
        panelIzquierdo.add(panelContenedorMaps, gbcIzquierdo);


        JPanel panelDerecho = new JPanel(new GridBagLayout());
        panelDerecho.setBackground(Color.WHITE);
        panelDerecho.setBorder(new EmptyBorder(40, 50, 40, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new java.awt.Insets(10, 0, 10, 0);
        gbc.gridx = 0;

        JLabel lblLogin = new JLabel("Iniciar Sesión");
        lblLogin.setFont(new Font("Arial", Font.BOLD, 30));
        lblLogin.setForeground(new Color(50, 50, 50));
        gbc.gridy = 0;
        panelDerecho.add(lblLogin, gbc);

        gbc.gridy = 1;
        gbc.insets = new java.awt.Insets(0, 0, 25, 0);
        panelDerecho.add(new JSeparator(), gbc);
        gbc.insets = new java.awt.Insets(10, 0, 10, 0);

        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 18));
        lblUsuario.setForeground(new Color(100, 100, 100));
        gbc.gridy = 2;
        panelDerecho.add(lblUsuario, gbc);

        txtUsuario = new JTextField();
        txtUsuario.setPreferredSize(new Dimension(200, 38));
        txtUsuario.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 3;
        panelDerecho.add(txtUsuario, gbc);

        JLabel lblPassword = new JLabel("Contraseña");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 18));
        lblPassword.setForeground(new Color(100, 100, 100));
        gbc.gridy = 4;
        panelDerecho.add(lblPassword, gbc);

        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(200, 38));
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 5;
        panelDerecho.add(txtPassword, gbc);

        JButton btnIngresar = new JButton("Ingresar");
        btnIngresar.setBackground(verdeFoto);
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setPreferredSize(new Dimension(200, 42));
        btnIngresar.setFont(new Font("Arial", Font.BOLD, 18));
        btnIngresar.addActionListener(this::login);
        gbc.gridy = 6;
        gbc.insets = new java.awt.Insets(30, 0, 15, 0);
        panelDerecho.add(btnIngresar, gbc);

        JPanel panelBotonesSecundarios = new JPanel(new GridLayout(1, 2, 15, 0));
        panelBotonesSecundarios.setBackground(Color.WHITE);

        JButton btnSolicitarRadicacion = new JButton("Solicitar Radicación");
        btnSolicitarRadicacion.setFont(new Font("Arial", Font.PLAIN, 16));
        btnSolicitarRadicacion.addActionListener(e -> {
            new EmpresaView();
            dispose();
        });

        panelBotonesSecundarios.add(btnSolicitarRadicacion);

        gbc.gridy = 7;
        gbc.insets = new java.awt.Insets(10, 0, 0, 0);
        panelDerecho.add(panelBotonesSecundarios, gbc);

        add(panelIzquierdo);
        add(panelDerecho);

        setSize(950, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private void login(ActionEvent e) {
        String nombre = txtUsuario.getText();
        String password = new String(txtPassword.getPassword());

        Usuario user = service.autenticar(nombre, password);

        if (user != null) {
            JOptionPane.showMessageDialog(this,
                    "Bienvenido " + user.getNombre() + " - Rol: " + user.getRol(),
                    "Login exitoso",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();

            // MODIFICADO: Ahora pasamos el objeto 'user' completo cumpliendo la arquitectura en capas
            SwingUtilities.invokeLater(() -> {
                VentanaPrincipal main = new VentanaPrincipal(user);
                main.setVisible(true);
            });

        } else {
            JOptionPane.showMessageDialog(this,
                    "Usuario o contraseña incorrectos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirFormulario() {
        try {
            Desktop.getDesktop().browse(new URI(
                    "https://docs.google.com/forms/d/e/1FAIpQLSc36TWzWPMdNUuBeeMMGEkBVaCDdwOjJn7Y3yhcGz-zXusCng/viewform"
            ));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo abrir el formulario",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirGoogleMaps() {
        try {
            Desktop.getDesktop().browse(new URI(
                    "https://www.google.com/maps/place/PARQUE+INDUSTRIAL+DE+VIEDMA/@-40.8368019,-62.9648095,17z/data=!3m1!4b1!4m6!3m5!1s0x95f6993b34350e3d:0x55ff7e59f421eee4!8m2!3d-40.836806!4d-62.9622346!16s%2Fg%2F11j3tb5c5y?hl=es&entry=ttu&g_ep=EgoyMDI2MDYwMS4wIKXMDSoASAFQAw%3D%3D"
            ));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo abrir el mapa de ubicación",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginView::new);
    }
}