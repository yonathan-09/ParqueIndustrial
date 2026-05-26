package com.damian.gpiv.gui;

import com.damian.gpiv.models.Empresa;
import com.damian.gpiv.services.EmpresaService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class EvaluarEmpresaView extends JFrame {
    private final EmpresaService service;
    private JTextArea output;
    private JTextField txtEmpresaId;

    public EvaluarEmpresaView() {
        super("Evaluar Empresas Interesadas");
        this.service = new EmpresaService();

        setLayout(new FlowLayout());

        add(new JLabel("ID de Empresa"));
        txtEmpresaId = new JTextField(10);
        add(txtEmpresaId);

        JButton btnAprobar = new JButton("Aprobar");
        btnAprobar.addActionListener(e -> evaluarEmpresa("aprobada"));
        add(btnAprobar);

        JButton btnRechazar = new JButton("Rechazar");
        btnRechazar.addActionListener(e -> evaluarEmpresa("rechazada"));
        add(btnRechazar);

        JButton btnListar = new JButton("Listar Empresas");
        btnListar.addActionListener(this::listar);
        add(btnListar);

        output = new JTextArea(10, 50);
        output.setEditable(false);
        add(new JScrollPane(output));

        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void evaluarEmpresa(String nuevoEstado) {
        try {
            int empresaId = Integer.parseInt(txtEmpresaId.getText());
            service.actualizarEstado(empresaId, nuevoEstado);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "El ID debe ser un número",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listar(ActionEvent e) {
        List<Empresa> empresas = service.listar();
        output.setText("");
        for (Empresa emp : empresas) {
            if ("interesada".equalsIgnoreCase(emp.getTipo())) {
                output.append("Empresa " + emp.getId() +
                        " | Nombre: " + emp.getNombre() +
                        " | Tipo: " + emp.getTipo() +
                        " | Estado: " + emp.getEstado() + "\n");
            }
        }
    }
}
