package com.damian.gpiv.gui;

import com.damian.gpiv.models.Usuario;
import com.damian.gpiv.services.UsuarioService;

import javax.swing.*;
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

        setLayout(new FlowLayout());

        add(new JLabel("Usuario"));
        txtUsuario = new JTextField(20);
        add(txtUsuario);

        add(new JLabel("Contraseña"));
        txtPassword = new JPasswordField(20);
        add(txtPassword);

        JButton btnIngresar = new JButton("Ingresar");
        btnIngresar.addActionListener(this::login);
        add(btnIngresar);

        // Botón para abrir ventana de registro
        JButton btnRegistrar = new JButton("Registrar Usuario");
        btnRegistrar.addActionListener(e -> {
            new RegistroUsuarioView(); // abre la ventana de registro
            dispose(); // opcional: cerrar login si querés que quede solo la ventana de registro
        });
        add(btnRegistrar);

        // 🔑 Nuevo botón "Formulario"
        JButton btnFormulario = new JButton("Formulario");
        btnFormulario.addActionListener(e -> abrirFormulario());
        add(btnFormulario);

        setSize(350, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // cerrar toda la app si se cierra login
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

            dispose(); // cerrar ventana de login

            // Abrir ventana principal con el rol del usuario
            SwingUtilities.invokeLater(() -> {
                VentanaPrincipal main = new VentanaPrincipal(user.getRol());
                main.setVisible(true);
            });

        } else {
            JOptionPane.showMessageDialog(this,
                    "Usuario o contraseña incorrectos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para abrir el formulario en el navegador
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginView::new);
    }
}