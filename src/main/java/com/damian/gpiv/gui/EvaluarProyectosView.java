package com.damian.gpiv.gui;

import com.damian.gpiv.models.Proyecto;
import com.damian.gpiv.services.ProyectoService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class EvaluarProyectosView extends JFrame {
    private final ProyectoService service;
    private JTextArea output;
    private JTextField txtProyectoId;

    public EvaluarProyectosView() {
        super("Evaluar Proyectos");
        this.service = new ProyectoService();

        setLayout(new FlowLayout());

        // Campo para ID de proyecto
        add(new JLabel("ID de Proyecto"));
        txtProyectoId = new JTextField(10);
        add(txtProyectoId);

        // Botones para aprobar o rechazar
        JButton btnAprobar = new JButton("Aprobar");
        btnAprobar.addActionListener(e -> evaluarProyecto("aprobado"));
        add(btnAprobar);

        JButton btnRechazar = new JButton("Rechazar");
        btnRechazar.addActionListener(e -> evaluarProyecto("rechazado"));
        add(btnRechazar);

        // Botón para listar proyectos
        JButton btnListar = new JButton("Listar Proyectos");
        btnListar.addActionListener(this::listar);
        add(btnListar);

        // Área de salida
        output = new JTextArea(10, 50);
        output.setEditable(false);
        add(new JScrollPane(output));

        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void evaluarProyecto(String nuevoEstado) {
        try {
            int proyectoId = Integer.parseInt(txtProyectoId.getText());
            service.actualizarEstado(proyectoId, nuevoEstado);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "El ID debe ser un número",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
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
        SwingUtilities.invokeLater(EvaluarProyectosView::new);
    }
}
