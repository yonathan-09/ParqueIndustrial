package com.damian.gpiv.gui;

import com.damian.gpiv.models.Empresa;
import com.damian.gpiv.models.Proyecto;
import com.damian.gpiv.services.EmpresaService;
import com.damian.gpiv.services.ProyectoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProyectoView extends JFrame {
    private final ProyectoService service;
    private final int empresaId;
    private final String rolUsuario;
    private JTextField txtNombre;
    private JTextField txtDesc;
    private JComboBox<Empresa> comboEmpresas;
    private JTextArea output;
    private List<File> archivosAdjuntos = new ArrayList<>();
    private EmpresaService empresaService = new EmpresaService();
    private JList<Proyecto> listaProyectos;
    private DefaultListModel<Proyecto> modeloProyectos = new DefaultListModel<>();

    // Constructor principal
    public ProyectoView(int empresaId, String rolUsuario) {
        super("Gestión de Proyectos - GPIV");
        this.service = new ProyectoService();
        this.empresaId = empresaId;
        this.rolUsuario = rolUsuario;

        // Base estructural limpia
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // COLOR VERDE CORPORATIVO ACTUALIZADO (RGB de la foto)
        Color verdeFoto = new Color(93, 203, 82);

        // ---------------------------------------------------------------------
        // PANEL IZQUIERDO: Barra lateral decorativa institucional
        // ---------------------------------------------------------------------
        JPanel panelIzquierdoVerde = new JPanel();
        panelIzquierdoVerde.setBackground(verdeFoto);
        panelIzquierdoVerde.setPreferredSize(new Dimension(150, 650));
        add(panelIzquierdoVerde, BorderLayout.WEST);

        // ---------------------------------------------------------------------
        // PANEL CENTRAL: Formulario con fuentes grandes y GridBagLayout
        // ---------------------------------------------------------------------
        JPanel panelContenido = new JPanel(new GridBagLayout());
        panelContenido.setBackground(Color.WHITE);
        panelContenido.setBorder(new EmptyBorder(25, 40, 25, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        int fila = 0;

        // Título de la vista
        JLabel lblTitulo = new JLabel("Administración de Proyectos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(40, 45, 50));
        gbc.gridy = fila++;
        panelContenido.add(lblTitulo, gbc);

        // Separador superior sutil
        gbc.gridy = fila++;
        gbc.insets = new Insets(0, 0, 15, 0);
        panelContenido.add(new JSeparator(), gbc);
        gbc.insets = new Insets(5, 0, 5, 0); // Reset

        // --- Campo: Nombre del Proyecto ---
        JLabel lblNombre = new JLabel("Nombre del Proyecto");
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        lblNombre.setForeground(new Color(80, 80, 80));
        gbc.gridy = fila++;
        panelContenido.add(lblNombre, gbc);

        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        txtNombre.setPreferredSize(new Dimension(300, 35));
        gbc.gridy = fila++;
        panelContenido.add(txtNombre, gbc);

        // --- Campo: Descripción ---
        JLabel lblDesc = new JLabel("Descripción Breve");
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 16));
        lblDesc.setForeground(new Color(80, 80, 80));
        gbc.gridy = fila++;
        panelContenido.add(lblDesc, gbc);

        txtDesc = new JTextField();
        txtDesc.setFont(new Font("Arial", Font.PLAIN, 16));
        txtDesc.setPreferredSize(new Dimension(300, 35));
        gbc.gridy = fila++;
        panelContenido.add(txtDesc, gbc);

        // Bloque condicional exclusivo para Administradores
        if ("administrador".equalsIgnoreCase(rolUsuario)) {
            // --- Campo: Empresa ID ---
            JLabel lblEmpresa = new JLabel("Empresa Asociada");
            lblEmpresa.setFont(new Font("Arial", Font.PLAIN, 16));
            lblEmpresa.setForeground(new Color(80, 80, 80));
            gbc.gridy = fila++;
            panelContenido.add(lblEmpresa, gbc);

            comboEmpresas = new JComboBox<>();
            comboEmpresas.setFont(new Font("Arial", Font.PLAIN, 16));
            comboEmpresas.setPreferredSize(new Dimension(300, 35));

            List<Empresa> empresas = empresaService.listar(); // asumimos que existe listar()
            for (Empresa emp : empresas) {
                comboEmpresas.addItem(emp);
            }

            gbc.gridy = fila++;
            panelContenido.add(comboEmpresas, gbc);

//            // --- Botón: Adjuntar PDF ---
//            JButton btnAdjuntar = new JButton(" Adjuntar Archivos PDF");
//            btnAdjuntar.setFont(new Font("Arial", Font.BOLD, 15));
//            btnAdjuntar.setBackground(new Color(240, 243, 247));
//            btnAdjuntar.setForeground(new Color(40, 50, 60));
//            btnAdjuntar.setFocusPainted(false);
//            btnAdjuntar.setPreferredSize(new Dimension(200, 38));
//            btnAdjuntar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Borde negro remarcado
//            btnAdjuntar.setCursor(new Cursor(Cursor.HAND_CURSOR));
//            btnAdjuntar.addActionListener(this::adjuntarArchivos);
//
//            gbc.gridy = fila++;
//            gbc.insets = new Insets(10, 0, 10, 0);
//            panelContenido.add(btnAdjuntar, gbc);
//            gbc.insets = new Insets(5, 0, 5, 0); // Reset
//
//            // --- Botón: Ver Archivos Adjuntos ---
//            JButton btnVerAdjuntos = new JButton(" Ver Archivos Adjuntos");
//            btnVerAdjuntos.setFont(new Font("Arial", Font.BOLD, 15));
//            btnVerAdjuntos.setBackground(new Color(240, 243, 247));
//            btnVerAdjuntos.setForeground(new Color(40, 50, 60));
//            btnVerAdjuntos.setFocusPainted(false);
//            btnVerAdjuntos.setPreferredSize(new Dimension(200, 38));
//            btnVerAdjuntos.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Borde negro remarcado
//            btnVerAdjuntos.setCursor(new Cursor(Cursor.HAND_CURSOR));
//            btnVerAdjuntos.addActionListener(this::abrirArchivosAdjuntos);
//
//            gbc.gridy = fila++;
//            gbc.insets = new Insets(10, 0, 10, 0);
//            panelContenido.add(btnVerAdjuntos, gbc);
//            gbc.insets = new Insets(5, 0, 5, 0); // Reset

        }

        // ---------------------------------------------------------------------
        // PANEL DE ACCIONES PRINCIPALES (Registrar y Listar)
        // ---------------------------------------------------------------------
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 20, 0));
        panelBotones.setBackground(Color.WHITE);

        JButton btnRegistrar = new JButton("Registrar Proyecto");
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 16));
        btnRegistrar.setBackground(verdeFoto);
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setPreferredSize(new Dimension(150, 45));
        btnRegistrar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Borde negro remarcado
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistrar.addActionListener(this::registrar);

        JButton btnListar = new JButton("Listar Proyectos");
        btnListar.setFont(new Font("Arial", Font.BOLD, 16));
        btnListar.setBackground(new Color(240, 243, 247));
        btnListar.setForeground(new Color(40, 50, 60));
        btnListar.setFocusPainted(false);
        btnListar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Borde negro remarcado
        btnListar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnListar.addActionListener(this::listar);

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnListar);

        gbc.gridy = fila++;
        gbc.insets = new Insets(15, 0, 15, 0);
        panelContenido.add(panelBotones, gbc);

        // --- Área de Consola/Salida ---
        listaProyectos = new JList<>(modeloProyectos);
        listaProyectos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaProyectos.setFont(new Font("Monospaced", Font.PLAIN, 14));
        listaProyectos.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JScrollPane scroll = new JScrollPane(listaProyectos);

        gbc.gridy = fila++;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        panelContenido.add(scroll, gbc);

        output = new JTextArea(10, 50);
        output.setEditable(false);
        panelContenido.add(new JScrollPane(output), gbc);

        add(panelContenido, BorderLayout.CENTER);
        setVisible(true);
    }

    // Constructor vacío para compatibilidad con la VentanaPrincipal
    public ProyectoView() {
        this(0, "administrador");
    }

//    private void adjuntarArchivos(ActionEvent e) {
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setMultiSelectionEnabled(true);
//        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files", "pdf"));
//
//        int result = fileChooser.showOpenDialog(this);
//        if (result == JFileChooser.APPROVE_OPTION) {
//            archivosAdjuntos.clear();
//            archivosAdjuntos.addAll(Arrays.asList(fileChooser.getSelectedFiles()));
//            JOptionPane.showMessageDialog(this,
//                    "Se adjuntaron " + archivosAdjuntos.size() + " archivos PDF con éxito.",
//                    "Archivos Adjuntos",
//                    JOptionPane.INFORMATION_MESSAGE);
//        }
//    }
//
//    private void abrirArchivosAdjuntos(ActionEvent e) {
//        Proyecto proyectoSeleccionado = listaProyectos.getSelectedValue();
//        if (proyectoSeleccionado == null) {
//            JOptionPane.showMessageDialog(this,
//                    "Debe seleccionar un proyecto de la lista.",
//                    "Error",
//                    JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        List<File> archivos = service.obtenerArchivosAdjuntos(proyectoSeleccionado.getId());
//
//        if (archivos.isEmpty()) {
//            JOptionPane.showMessageDialog(this,
//                    "Este proyecto no tiene archivos adjuntos.",
//                    "Archivos Adjuntos",
//                    JOptionPane.INFORMATION_MESSAGE);
//            return;
//        }
//
//        for (File archivo : archivos) {
//            try {
//                Desktop.getDesktop().open(archivo);
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(this,
//                        "No se pudo abrir el archivo: " + archivo.getName(),
//                        "Error",
//                        JOptionPane.ERROR_MESSAGE);
//            }
//        }
//    }

    private void registrar(ActionEvent e) {
        int idEmpresaAsociada = empresaId;

        if ("administrador".equalsIgnoreCase(rolUsuario)) {
            Empresa empresaSeleccionada = (Empresa) comboEmpresas.getSelectedItem();
            if (empresaSeleccionada == null) {
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar una empresa válida",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            idEmpresaAsociada = empresaSeleccionada.getId();
        }

        EmpresaService empresaService = new EmpresaService();
        if (!empresaService.existeEmpresa(idEmpresaAsociada)) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró ninguna empresa registrada con el ID ingresado",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Proyecto proyecto = new Proyecto(
                0,
                txtNombre.getText().trim(),
                txtDesc.getText().trim(),
                "pendiente",
                idEmpresaAsociada
        );

        service.registrarConArchivos(proyecto, archivosAdjuntos);

        output.append("» PROYECTO REGISTRADO: " + proyecto.getNombre().toUpperCase() + "\n"
                + "  • Empresa: " + (rolUsuario.equalsIgnoreCase("administrador")
                ? ((Empresa) comboEmpresas.getSelectedItem()).getNombre()
                : idEmpresaAsociada) + "\n"
                + "  • Documentos PDF Vinculados: " + archivosAdjuntos.size() + "\n"
                + "──────────────────────────────────────────────────\n");

        // Limpiar formulario tras el éxito
        txtNombre.setText("");
        txtDesc.setText("");
        if (comboEmpresas != null) comboEmpresas.setSelectedIndex(-1); // limpiar selección
        archivosAdjuntos.clear();
    }


    private void listar(ActionEvent e) {
        modeloProyectos.clear();
        List<Proyecto> proyectos;

        if ("empresa".equalsIgnoreCase(rolUsuario)) {
            proyectos = service.listarPorEmpresa(empresaId);
        } else {
            proyectos = service.listar();
        }

        if (proyectos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No se encontraron proyectos guardados en el sistema.",
                    "Aviso",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (Proyecto p : proyectos) {
            modeloProyectos.addElement(p);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProyectoView(0, "administrador"));
    }
}

