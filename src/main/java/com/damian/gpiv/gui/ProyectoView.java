package com.damian.gpiv.gui;

import com.damian.gpiv.models.Proyecto;
import com.damian.gpiv.services.ProyectoService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ProyectoView extends JFrame {
    private final ProyectoService service;
    private JTextField txtNombre;
    private JTextField txtDesc;
    private JTextField txtEmpresa;
    private JTextArea output;

    public ProyectoView() {
        super("Proyectos");
        this.service = new ProyectoService();

        setLayout(new FlowLayout());

        add(new JLabel("Nombre"));
        txtNombre = new JTextField(20);
        add(txtNombre);

        add(new JLabel("Descripción"));
        txtDesc = new JTextField(20);
        add(txtDesc);

        add(new JLabel("Empresa ID"));
        txtEmpresa = new JTextField(20);
        add(txtEmpresa);

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

    private void registrar(ActionEvent e) {
        Integer empresaId = null;
        if (!txtEmpresa.getText().isEmpty()) {
            try {
                empresaId = Integer.parseInt(txtEmpresa.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "El ID de empresa debe ser un número",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Proyecto proyecto = new Proyecto(
                txtNombre.getText(),
                txtDesc.getText(),
                "en evaluación", // estado inicial por defecto
                empresaId
        );

        service.registrar(proyecto);
        output.append("Proyecto registrado: " + proyecto.getNombre() + "\n");
    }

    private void listar(ActionEvent e) {
        List<Proyecto> proyectos = service.listar();
        output.setText("");
        for (Proyecto p : proyectos) {
            output.append("Proyecto " + p.getId() +
                    " | Nombre: " + p.getNombre() +
                    " | Estado: " + p.getEstado() +
                    " | Empresa ID: " + p.getEmpresaId() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProyectoView::new);
    }
}
