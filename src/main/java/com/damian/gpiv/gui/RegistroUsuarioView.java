package com.damian.gpiv.gui;

import com.damian.gpiv.models.Usuario;
import com.damian.gpiv.services.UsuarioService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RegistroUsuarioView extends JFrame {
    private final UsuarioService service;
    private JTextField txtNombre;
    private JComboBox<String> comboRol;
    private JPasswordField txtPassword;

    public RegistroUsuarioView() {
        super("Registrar Nuevo Usuario - GPIV");
        this.service = new UsuarioService();

        setLayout(new BorderLayout());

        Color verdeFoto = new Color(93, 203, 82);

        JPanel panelIzquierdoVerde = new JPanel();
        panelIzquierdoVerde.setBackground(verdeFoto);
        panelIzquierdoVerde.setPreferredSize(new Dimension(150, 500));

        JPanel panelDerechoFormulario = new JPanel(new GridBagLayout());
        panelDerechoFormulario.setBackground(Color.WHITE);
        panelDerechoFormulario.setBorder(new EmptyBorder(20, 50, 20, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 4, 0);
        gbc.gridx = 0;

        JLabel lblHeader = new JLabel("Crear Cuenta");
        lblHeader.setFont(new Font("Arial", Font.BOLD, 26));
        lblHeader.setForeground(new Color(50, 50, 50));
        gbc.gridy = 0;
        panelDerechoFormulario.add(lblHeader, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 15, 0);
        panelDerechoFormulario.add(new JSeparator(), gbc);
        gbc.insets = new Insets(8, 0, 4, 0);

        JLabel lblNombre = new JLabel("Nombre de usuario");
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 18));
        lblNombre.setForeground(new Color(80, 80, 80));
        gbc.gridy = 2;
        panelDerechoFormulario.add(lblNombre, gbc);

        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 18));
        txtNombre.setPreferredSize(new Dimension(300, 38));
        gbc.gridy = 3;
        panelDerechoFormulario.add(txtNombre, gbc);

        JLabel lblRol = new JLabel("Rol");
        lblRol.setFont(new Font("Arial", Font.PLAIN, 18));
        lblRol.setForeground(new Color(80, 80, 80));
        gbc.gridy = 4;
        panelDerechoFormulario.add(lblRol, gbc);

        comboRol = new JComboBox<>(new String[]{"administrador", "empresa", "organismo", "proveedor"});
        comboRol.setFont(new Font("Arial", Font.PLAIN, 18));
        comboRol.setBackground(Color.WHITE);
        comboRol.setPreferredSize(new Dimension(300, 38));
        gbc.gridy = 5;
        panelDerechoFormulario.add(comboRol, gbc);

        JLabel lblPassword = new JLabel("Contraseña");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 18));
        lblPassword.setForeground(new Color(80, 80, 80));
        gbc.gridy = 6;
        panelDerechoFormulario.add(lblPassword, gbc);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 18));
        txtPassword.setPreferredSize(new Dimension(300, 38));
        gbc.gridy = 7;
        panelDerechoFormulario.add(txtPassword, gbc);

        JButton btnRegistrar = new JButton("Registrar Usuario");
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 18));
        btnRegistrar.setBackground(verdeFoto);
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setPreferredSize(new Dimension(300, 42));
        btnRegistrar.addActionListener(this::registrar);
        gbc.gridy = 8;
        gbc.insets = new Insets(20, 0, 5, 0);
        panelDerechoFormulario.add(btnRegistrar, gbc);

        JButton btnVolver = new JButton("Cancelar");
        btnVolver.setFont(new Font("Arial", Font.PLAIN, 14));
        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setForeground(Color.GRAY);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(LoginView::new);
        });
        gbc.gridy = 9;
        gbc.insets = new Insets(5, 0, 0, 0);
        panelDerechoFormulario.add(btnVolver, gbc);

        add(panelIzquierdoVerde, BorderLayout.WEST);
        add(panelDerechoFormulario, BorderLayout.CENTER);

        setSize(750, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (Frame.getFrames().length <= 1) {
                    SwingUtilities.invokeLater(LoginView::new);
                }
            }
        });

        setVisible(true);
    }

    private void registrar(ActionEvent e) {
        String nombre = txtNombre.getText();
        String rol = (String) comboRol.getSelectedItem();
        String password = new String(txtPassword.getPassword());

        if (nombre.isEmpty() || rol == null || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Todos los campos son obligatorios",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario usuario = new Usuario(nombre, rol, password);
        service.registrar(usuario);

        JOptionPane.showMessageDialog(this,
                "Usuario " + nombre + " registrado con rol " + rol,
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);

        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegistroUsuarioView::new);
    }
}