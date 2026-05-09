package com.damian.gpiv.gui;

import com.damian.gpiv.models.Lote;
import com.damian.gpiv.services.LoteService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class LoteView extends JFrame {
    private final LoteService service;
    private JTextField txtSuperficie;
    private JTextField txtEmpresa;
    private JTextArea output;

    public LoteView() {
        super("Lotes");
        this.service = new LoteService();

        setLayout(new FlowLayout());

        add(new JLabel("Superficie"));
        txtSuperficie = new JTextField(20);
        add(txtSuperficie);

        add(new JLabel("Empresa ID (opcional)"));
        txtEmpresa = new JTextField(20);
        add(txtEmpresa);

        JButton btnRegistrar = new JButton("Registrar Lote");
        btnRegistrar.addActionListener(this::registrar);
        add(btnRegistrar);

        JButton btnListar = new JButton("Listar Lotes");
        btnListar.addActionListener(this::listar);
        add(btnListar);

        output = new JTextArea(10, 50);
        output.setEditable(false);
        add(new JScrollPane(output));

        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        try {
            int superficie = Integer.parseInt(txtSuperficie.getText());
            Lote lote = new Lote(superficie, "disponible", empresaId);
            service.registrar(lote);
            output.append("Lote registrado: " + lote.getSuperficie() + " m2\n");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "La superficie debe ser un número",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listar(ActionEvent e) {
        List<Lote> lotes = service.listar();
        output.setText("");
        for (Lote l : lotes) {
            output.append("Lote " + l.getId() + " | Superficie: " + l.getSuperficie() +
                    " | Estado: " + l.getEstado() +
                    " | Empresa ID: " + l.getEmpresaId() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoteView::new);
    }
}
