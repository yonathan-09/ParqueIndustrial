package com.damian.gpiv.gui;

import com.damian.gpiv.models.Proyecto;
import com.damian.gpiv.services.EmpresaService;
import com.damian.gpiv.services.ProyectoService;

import javax.swing.*;
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
    private JTextField txtEmpresa; // solo visible para administrador
    private JTextArea output;
    private List<File> archivosAdjuntos = new ArrayList<>();

    // Constructor principal
    public ProyectoView(int empresaId, String rolUsuario) {
        super("Proyectos");
        this.service = new ProyectoService();
        this.empresaId = empresaId;
        this.rolUsuario = rolUsuario;

        setLayout(new FlowLayout());

        add(new JLabel("Nombre"));
        txtNombre = new JTextField(20);
        add(txtNombre);

        add(new JLabel("Descripción"));
        txtDesc = new JTextField(20);
        add(txtDesc);

        // Campo Empresa ID solo para administrador
        if ("administrador".equalsIgnoreCase(rolUsuario)) {
            add(new JLabel("Empresa ID"));
            txtEmpresa = new JTextField(10);
            add(txtEmpresa);

            // Botón para adjuntar archivos PDF
            JButton btnAdjuntar = new JButton("Adjuntar PDF");
            btnAdjuntar.addActionListener(this::adjuntarArchivos);
            add(btnAdjuntar);
        }

        JButton btnRegistrar = new JButton("Registrar Proyecto");
        btnRegistrar.addActionListener(this::registrar);
        add(btnRegistrar);

        JButton btnListar = new JButton("Listar Proyectos");
        btnListar.addActionListener(this::listar);
        add(btnListar);

        output = new JTextArea(10, 50);
        output.setEditable(false);
        add(new JScrollPane(output));

        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    // Constructor vacío para pruebas rápidas
    public ProyectoView() {
        this(0, "administrador");
    }

    private void adjuntarArchivos(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files", "pdf"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            archivosAdjuntos.clear();
            archivosAdjuntos.addAll(Arrays.asList(fileChooser.getSelectedFiles()));
            JOptionPane.showMessageDialog(this,
                    "Se adjuntaron " + archivosAdjuntos.size() + " archivos PDF.",
                    "Archivos Adjuntos",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void registrar(ActionEvent e) {
        int idEmpresaAsociada = empresaId;

        if ("administrador".equalsIgnoreCase(rolUsuario)) {
            try {
                idEmpresaAsociada = Integer.parseInt(txtEmpresa.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "El ID de empresa debe ser un número",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        EmpresaService empresaService = new EmpresaService();
        if (!empresaService.existeEmpresa(idEmpresaAsociada)) {
            JOptionPane.showMessageDialog(this,
                    "La empresa no existe",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Proyecto proyecto = new Proyecto(
                0,
                txtNombre.getText(),
                txtDesc.getText(),
                "pendiente",
                idEmpresaAsociada
        );

        // Registrar proyecto con archivos adjuntos
        service.registrarConArchivos(proyecto, archivosAdjuntos);

        output.append("Proyecto registrado: " + proyecto.getNombre() +
                " | Empresa ID: " + idEmpresaAsociada +
                " | Archivos adjuntos: " + archivosAdjuntos.size() + "\n");
    }

    private void listar(ActionEvent e) {
        output.setText("");
        List<Proyecto> proyectos;

        if ("empresa".equalsIgnoreCase(rolUsuario)) {
            proyectos = service.listarPorEmpresa(empresaId);
        } else {
            proyectos = service.listar(); // administrador ve todos
        }

        for (Proyecto p : proyectos) {
            output.append("Proyecto " + p.getId() +
                    " | Nombre: " + p.getNombre() +
                    " | Estado: " + p.getEstado() +
                    " | Empresa ID: " + p.getEmpresaId() + "\n");
        }
    }

    public static void main(String[] args) {
        // Simulación: administrador
        SwingUtilities.invokeLater(() -> new ProyectoView(0, "administrador"));

        // Simulación: empresa con ID 1
        // SwingUtilities.invokeLater(() -> new ProyectoView(1, "empresa"));
    }
}

