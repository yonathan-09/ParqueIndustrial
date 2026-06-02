package com.damian.gpiv.gui;

import com.damian.gpiv.models.Proyecto;
import com.damian.gpiv.services.ProyectoService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ProyectoConsultaView extends JFrame {
    private final ProyectoService service;
    private final String rolUsuario;
    private JTextArea output;

    // Constructor principal
    public ProyectoConsultaView(String rolUsuario) {
        super("Consulta de Proyectos");
        this.service = new ProyectoService();
        this.rolUsuario = rolUsuario;

        setLayout(new FlowLayout());

        JButton btnListar = new JButton("Listar Proyectos Disponibles");
        btnListar.addActionListener(this::listar);
        add(btnListar);

        output = new JTextArea(10, 50);
        output.setEditable(false);
        add(new JScrollPane(output));

        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void listar(ActionEvent e) {
        output.setText("");
        List<Proyecto> proyectos = service.listar();

        for (Proyecto p : proyectos) {
            // Solo mostrar proyectos en estado "pendiente" o "aprobado"
            if ("pendiente".equalsIgnoreCase(p.getEstado()) || "aprobado".equalsIgnoreCase(p.getEstado())) {
                output.append("Proyecto " + p.getId() +
                        " | Nombre: " + p.getNombre() +
                        " | Estado: " + p.getEstado() +
                        " | Empresa ID: " + p.getEmpresaId() + "\n");
            }
        }
    }

    public static void main(String[] args) {
        // Simulación: organismo
        SwingUtilities.invokeLater(() -> new ProyectoConsultaView("organismo"));

        // Simulación: proveedor
        // SwingUtilities.invokeLater(() -> new ProyectoConsultaView("proveedor"));
    }
}
