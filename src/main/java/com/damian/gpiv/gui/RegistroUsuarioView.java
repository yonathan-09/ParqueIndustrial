package com.damian.gpiv.gui;

import com.damian.gpiv.models.Usuario;
import com.damian.gpiv.services.UsuarioService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RegistroUsuarioView extends JFrame {
    private final UsuarioService service;
    private JTextField txtNombre;
    private JTextField txtRol;
    private JPasswordField txtPassword;

    public RegistroUsuarioView() {
        super("Registrar Nuevo Usuario");
        this.service = new UsuarioService();

        setLayout(new FlowLayout());

        add(new JLabel("Nombre de usuario"));
        txtNombre = new JTextField(20);
        add(txtNombre);

        add(new JLabel("Rol (administrador/empresa/organismo/proveedor)"));
        txtRol = new JTextField(20);
        add(txtRol);

        add(new JLabel("Contraseña"));
        txtPassword = new JPasswordField(20);
        add(txtPassword);

        JButton btnRegistrar = new JButton("Registrar Usuario");
        btnRegistrar.addActionListener(this::registrar);
        add(btnRegistrar);

        setSize(350, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void registrar(ActionEvent e) {
        String nombre = txtNombre.getText();
        String rol = txtRol.getText();
        String password = new String(txtPassword.getPassword());

        if (nombre.isEmpty() || rol.isEmpty() || password.isEmpty()) {
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

        // Cerrar ventana de registro y abrir login
        dispose();
        SwingUtilities.invokeLater(LoginView::new);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegistroUsuarioView::new);
    }
}
