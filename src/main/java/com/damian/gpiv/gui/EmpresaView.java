package com.damian.gpiv.gui;

import com.damian.gpiv.models.Empresa;
import com.damian.gpiv.services.EmpresaService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class EmpresaView extends JFrame {
    private final EmpresaService service;
    private JTextField txtNombre;
    private JComboBox<String> comboTipo;
    private JTextField txtEmail;
    private JTextField txtCuit;
    private JTextField txtActividadPrincipal;
    private JTextField txtActividadSecundaria;
    private JTextField txtDireccion;
    private JTextField txtReferente;
    private JTextField txtTelefono;
    private JComboBox<String> comboRubro;
    private JTextField txtDescripcionServicio;


    public EmpresaView() {
        super("Gestión de Empresas - GPIV");
        this.service = new EmpresaService();

        // Base de la ventana moderna
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        Color verdeFoto = new Color(93, 203, 82);

        // PANEL IZQUIERDO: Barra lateral decorativa
        JPanel panelIzquierdoVerde = new JPanel();
        panelIzquierdoVerde.setBackground(verdeFoto);
        panelIzquierdoVerde.setPreferredSize(new Dimension(150, 650));
        add(panelIzquierdoVerde, BorderLayout.WEST);

        // PANEL CENTRAL/DERECHO: Formulario
        JPanel panelTitulo = new JPanel();
        panelTitulo.setLayout(new BorderLayout());
        panelTitulo.setBackground(Color.WHITE);
        panelTitulo.setBorder(new EmptyBorder(15, 40, 15, 40));

        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new GridLayout(0, 2, 10, 10));
        panelContenido.setBackground(Color.WHITE);
        panelContenido.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Título de la sección
        JLabel lblTitulo = new JLabel("Administración de Empresas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(40, 45, 50));
        panelTitulo.add(lblTitulo);

        // --- Campo: Nombre ---
        JLabel lblNombre = new JLabel("Nombre de la Empresa");
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        lblNombre.setForeground(new Color(80, 80, 80));
        panelContenido.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        txtNombre.setPreferredSize(new Dimension(300, 35));
        panelContenido.add(txtNombre);

        // --- Campo: Tipo ---
        JLabel lblTipo = new JLabel("Tipo de Estado");
        lblTipo.setFont(new Font("Arial", Font.PLAIN, 16));
        lblTipo.setForeground(new Color(80, 80, 80));
        panelContenido.add(lblTipo);

        comboTipo = new JComboBox<>(new String[]{"interesada", "radicada"});
        panelContenido.add(comboTipo);

        // --- Campo: Email ---
        JLabel lblEmail = new JLabel("Correo Electrónico de Contacto");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 16));
        lblEmail.setForeground(new Color(80, 80, 80));
        panelContenido.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 16));
        txtEmail.setPreferredSize(new Dimension(300, 35));
        panelContenido.add(txtEmail);

        JLabel lblCuit = new JLabel("CUIT");
        lblCuit.setFont(new Font("Arial", Font.PLAIN, 16));
        lblCuit.setForeground(new Color(80, 80, 80));
        panelContenido.add(lblCuit);

        txtCuit = new JTextField();
        txtCuit.setFont(new Font("Arial", Font.PLAIN, 16));
        txtCuit.setPreferredSize(new Dimension(300, 35));
        panelContenido.add(txtCuit);

// --- Campo: Actividad Principal ---
        JLabel lblActPrin = new JLabel("Actividad Principal");
        lblActPrin.setFont(new Font("Arial", Font.PLAIN, 16));
        lblActPrin.setForeground(new Color(80, 80, 80));
        panelContenido.add(lblActPrin);

        txtActividadPrincipal = new JTextField();
        panelContenido.add(txtActividadPrincipal);

// --- Campo: Dirección ---
        JLabel lblDireccion = new JLabel("Dirección");
        lblDireccion.setFont(new Font("Arial", Font.PLAIN, 16));
        lblDireccion.setForeground(new Color(80, 80, 80));
        panelContenido.add(lblDireccion);

        txtDireccion = new JTextField();
        panelContenido.add(txtDireccion);

// --- Campo: Persona Referente ---
        JLabel lblReferente = new JLabel("Persona Referente");
        lblReferente.setFont(new Font("Arial", Font.PLAIN, 16));
        lblReferente.setForeground(new Color(80, 80, 80));
        panelContenido.add(lblReferente);

        txtReferente = new JTextField();
        panelContenido.add(txtReferente);

// --- Campo: Teléfono ---
        JLabel lblTelefono = new JLabel("Teléfono");
        lblTelefono.setFont(new Font("Arial", Font.PLAIN, 16));
        lblTelefono.setForeground(new Color(80, 80, 80));
        panelContenido.add(lblTelefono);

        txtTelefono = new JTextField();
        panelContenido.add(txtTelefono);

// --- Campo: Rubro ---
        JLabel lblRubro = new JLabel("Rubro");
        lblRubro.setFont(new Font("Arial", Font.PLAIN, 16));
        lblRubro.setForeground(new Color(80, 80, 80));
        panelContenido.add(lblRubro);

        comboRubro = new JComboBox<>(new String[]{"Servicios", "Bienes", "Bienes y Servicios"});
        panelContenido.add(comboRubro);

// --- Campo: Descripción del servicio/bien ---
        JLabel lblDescServ = new JLabel("Descripción del Servicio/Bien");
        lblDescServ.setFont(new Font("Arial", Font.PLAIN, 16));
        lblDescServ.setForeground(new Color(80, 80, 80));
        panelContenido.add(lblDescServ);

        txtDescripcionServicio = new JTextField();
        panelContenido.add(txtDescripcionServicio);


        // ---------------------------------------------------------------------
        // PANEL DE ACCIONES: Botones en paralelo con Bordes Negros Remarcados
        // ---------------------------------------------------------------------
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(Color.WHITE);

        JButton btnRegistrar = new JButton("Siguiente");
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 16));
        btnRegistrar.setBackground(verdeFoto);
        btnRegistrar.setForeground(Color.BLACK);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setPreferredSize(new Dimension(150, 45));
        // BORDE NEGRO REMARCADO SOLICITADO
        btnRegistrar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistrar.addActionListener(this::registrar);

        panelBotones.add(btnRegistrar);
        panelContenido.add(panelBotones);


        JPanel panelAcciones = new JPanel(new BorderLayout());
        panelAcciones.add(panelBotones, BorderLayout.NORTH);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.add(panelTitulo);
        panelPrincipal.add(panelContenido);
        panelPrincipal.add(panelAcciones);

        JScrollPane scrollPanel = new JScrollPane(panelPrincipal);
        scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPanel.getVerticalScrollBar().setUnitIncrement(20);

        add(scrollPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void registrar(ActionEvent e) {

        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        String cuit = txtCuit.getText().trim();
        String actividadPrincipal = txtActividadPrincipal.getText().trim();
        String referente = txtReferente.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String rubro = (String) comboRubro.getSelectedItem();
        String descripcionServicio = txtDescripcionServicio.getText().trim();

        if (nombre.isEmpty() || email.isEmpty() || cuit.isEmpty()
                || actividadPrincipal.isEmpty()
                || referente.isEmpty()
                || telefono.isEmpty()
                || descripcionServicio.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debes completar todos los campos obligatorios",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

            JOptionPane.showMessageDialog(
                    this,
                    "El email ingresado no tiene un formato válido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int solicitudId = service.registrarSolicitud(
                nombre,
                email,
                cuit,
                actividadPrincipal,
                txtDireccion.getText().trim(),
                referente,
                telefono,
                rubro,
                descripcionServicio
        );

        JOptionPane.showMessageDialog(
                this,
                "Solicitud de radicación enviada correctamente.\nQuedará pendiente de aprobación por un administrador.",
                "Solicitud enviada",
                JOptionPane.INFORMATION_MESSAGE
        );



        txtNombre.setText("");
        txtEmail.setText("");
        txtCuit.setText("");
        txtActividadPrincipal.setText("");
        txtDireccion.setText("");
        txtReferente.setText("");
        txtTelefono.setText("");
        txtDescripcionServicio.setText("");

        dispose();

        SwingUtilities.invokeLater(
                () -> new ProyectoView(solicitudId, "empresa")
        );
    }




    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmpresaView::new);
    }
}
