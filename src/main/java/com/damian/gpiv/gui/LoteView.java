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
    private JTextField txtLoteId;

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

        add(new JLabel("Lote ID para asociar"));
        txtLoteId = new JTextField(10);
        add(txtLoteId);

        JButton btnAsociar = new JButton("Asociar a Empresa");
        btnAsociar.addActionListener(this::asociar);
        add(btnAsociar);

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

        try {
            int superficie = Integer.parseInt(txtSuperficie.getText());
            Lote lote = new Lote(superficie, "disponible", empresaId != null ? empresaId : 0);
            service.registrar(lote);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "La superficie debe ser un número",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void asociar(ActionEvent e) {
        try {
            int loteId = Integer.parseInt(txtLoteId.getText());
            int empresaId = Integer.parseInt(txtEmpresa.getText());

            service.asociarEmpresa(loteId, empresaId);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Debes ingresar números válidos para Lote ID y Empresa ID",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    private void listar(ActionEvent e) {
        List<Lote> lotes = service.listar();
        output.setText("");
        for (Lote l : lotes) {
            output.append("Lote " + l.getId()
                    + " | Superficie: " + l.getSuperficie()
                    + " | Estado: " + l.getEstado());

            // Mostrar empresa solo si está asociada
            if (l.getEmpresaId() > 0) {
                output.append(" | Empresa ID: " + l.getEmpresaId());
            }

            output.append("\n");
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoteView::new);
    }
}
