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
    private JTextArea output;

    public EmpresaView() {
        super("Gestión de Empresas - GPIV");
        this.service = new EmpresaService();

        // Base de la ventana moderna
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // COLOR VERDE CORPORATIVO ACTUALIZADO (Sincronizado con la foto)
        Color verdeFoto = new Color(93, 203, 82);

        // ---------------------------------------------------------------------
        // PANEL IZQUIERDO: Barra lateral decorativa institucional
        // ---------------------------------------------------------------------
        JPanel panelIzquierdoVerde = new JPanel();
        panelIzquierdoVerde.setBackground(verdeFoto);
        panelIzquierdoVerde.setPreferredSize(new Dimension(150, 650));
        add(panelIzquierdoVerde, BorderLayout.WEST);

        // ---------------------------------------------------------------------
        // PANEL CENTRAL/DERECHO: Formulario Operativo y Listado
        // ---------------------------------------------------------------------
        JPanel panelContenido = new JPanel(new GridBagLayout());
        panelContenido.setBackground(Color.WHITE);
        panelContenido.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // Título de la sección
        JLabel lblTitulo = new JLabel("Administración de Empresas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(40, 45, 50));
        gbc.gridy = 0;
        panelContenido.add(lblTitulo, gbc);

        // Separador superior
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 15, 0);
        panelContenido.add(new JSeparator(), gbc);
        gbc.insets = new Insets(6, 0, 6, 0); // Reset

        // --- Campo: Nombre ---
        JLabel lblNombre = new JLabel("Nombre de la Empresa");
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        lblNombre.setForeground(new Color(80, 80, 80));
        gbc.gridy = 2;
        panelContenido.add(lblNombre, gbc);

        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        txtNombre.setPreferredSize(new Dimension(300, 35));
        gbc.gridy = 3;
        panelContenido.add(txtNombre, gbc);

        // --- Campo: Tipo ---
        JLabel lblTipo = new JLabel("Tipo de Estado");
        lblTipo.setFont(new Font("Arial", Font.PLAIN, 16));
        lblTipo.setForeground(new Color(80, 80, 80));
        gbc.gridy = 4;
        panelContenido.add(lblTipo, gbc);

        comboTipo = new JComboBox<>(new String[]{"interesada", "radicada"});
        comboTipo.setFont(new Font("Arial", Font.PLAIN, 16));
        comboTipo.setBackground(Color.WHITE);
        comboTipo.setPreferredSize(new Dimension(300, 35));
        gbc.gridy = 5;
        panelContenido.add(comboTipo, gbc);

        // --- Campo: Email ---
        JLabel lblEmail = new JLabel("Correo Electrónico de Contacto");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 16));
        lblEmail.setForeground(new Color(80, 80, 80));
        gbc.gridy = 6;
        panelContenido.add(lblEmail, gbc);

        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 16));
        txtEmail.setPreferredSize(new Dimension(300, 35));
        gbc.gridy = 7;
        panelContenido.add(txtEmail, gbc);

        // ---------------------------------------------------------------------
        // PANEL DE ACCIONES: Botones en paralelo con Bordes Negros Remarcados
        // ---------------------------------------------------------------------
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 20, 0));
        panelBotones.setBackground(Color.WHITE);

        JButton btnRegistrar = new JButton("Registrar Empresa");
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 16));
        btnRegistrar.setBackground(verdeFoto);
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setPreferredSize(new Dimension(150, 45));
        // BORDE NEGRO REMARCADO SOLICITADO
        btnRegistrar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistrar.addActionListener(this::registrar);

        JButton btnListar = new JButton("Listar Registros");
        btnListar.setFont(new Font("Arial", Font.BOLD, 16));
        btnListar.setBackground(new Color(240, 243, 247));
        btnListar.setForeground(new Color(40, 50, 60));
        btnListar.setFocusPainted(false);
        // BORDE NEGRO REMARCADO SOLICITADO
        btnListar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btnListar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnListar.addActionListener(this::listar);

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnListar);

        gbc.gridy = 8;
        gbc.insets = new Insets(15, 0, 15, 0);
        panelContenido.add(panelBotones, gbc);

        // --- Área de Consola/Salida ---
        output = new JTextArea(10, 50);
        output.setFont(new Font("Monospaced", Font.PLAIN, 17));
        output.setEditable(false);
        output.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JScrollPane scroll = new JScrollPane(output);
        scroll.setPreferredSize(new Dimension(300, 180));

        gbc.gridy = 9;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        panelContenido.add(scroll, gbc);

        add(panelContenido, BorderLayout.CENTER);
        setVisible(true);
    }

    private void registrar(ActionEvent e) {
        String tipo = (String) comboTipo.getSelectedItem();
        String estado = "pendiente";

        if ("radicada".equalsIgnoreCase(tipo)) {
            estado = "aprobada";
        }

        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debes ingresar un nombre para la empresa",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debes ingresar un email para la empresa",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            JOptionPane.showMessageDialog(this,
                    "El email ingresado no tiene un formato válido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Empresa empresa = new Empresa(
                nombre,
                0,
                tipo,
                email,
                estado
        );

        service.registrar(empresa);
        output.append("» EMPRESA REGISTRADA: " + empresa.getNombre().toUpperCase() +
                "\n  Tipo: " + empresa.getTipo() + " | Email: " + empresa.getEmail() +
                " | Estado: " + empresa.getEstado() + "\n──────────────────────────────────────────────────\n");

        // Limpiar campos después de registrar con éxito
        txtNombre.setText("");
        txtEmail.setText("");
    }

    private void listar(ActionEvent e) {
        List<Empresa> empresas = service.listar();
        output.setText("");
        if (empresas.isEmpty()) {
            output.setText("No hay empresas registradas en el sistema actualmente.");
            return;
        }
        for (Empresa emp : empresas) {
            output.append("ID: " + emp.getId() + " | " + emp.getNombre().toUpperCase() + "\n"
                    + "  • Tipo: " + emp.getTipo() + "\n"
                    + "  • Email: " + emp.getEmail() + "\n"
                    + "  • Estado: " + emp.getEstado() + "\n"
                    + "──────────────────────────────────────────────────\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmpresaView::new);
    }
}
