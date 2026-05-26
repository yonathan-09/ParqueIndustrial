package com.damian.gpiv.gui;

import com.damian.gpiv.models.Proyecto;
import com.damian.gpiv.services.ProyectoService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ProyectoView extends JFrame {
    private final ProyectoService service;
    private final int empresaId;
    private final String rolUsuario;
    private JTextField txtNombre;
    private JTextField txtDesc;
    private JTextArea output;

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

    private void registrar(ActionEvent e) {
        Proyecto proyecto = new Proyecto(
                0,
                txtNombre.getText(),
                txtDesc.getText(),
                "pendiente",
                empresaId
        );
        service.registrar(proyecto);
        output.append("Proyecto registrado: " + proyecto.getNombre() + "\n");
    }

    private void listar(ActionEvent e) {
        output.setText("");
        List<Proyecto> proyectos;

        if ("empresa".equalsIgnoreCase(rolUsuario)) {
            proyectos = service.listarPorEmpresa(empresaId);
        } else {
            proyectos = service.listar();
        }

        for (Proyecto p : proyectos) {
            output.append("Proyecto " + p.getId() +
                    " | Nombre: " + p.getNombre() +
                    " | Estado: " + p.getEstado() +
                    " | Empresa ID: " + p.getEmpresaId() + "\n");
        }
    }

    public static void main(String[] args) {
        // Ahora podés usar cualquiera de los dos constructores:
        SwingUtilities.invokeLater(() -> new ProyectoView()); // usa valores por defecto
        // o
        // SwingUtilities.invokeLater(() -> new ProyectoView(1, "empresa"));
    }
}