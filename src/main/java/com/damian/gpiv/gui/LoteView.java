package com.damian.gpiv.gui;

import com.damian.gpiv.models.Lote;
import com.damian.gpiv.models.Empresa;
import com.damian.gpiv.services.LoteService;
import com.damian.gpiv.services.EmpresaService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class LoteView extends JFrame {
    private final LoteService loteService;
    private final EmpresaService empresaService;
    private JTextField txtSuperficie;
    private JTextArea output;
    private JComboBox<Lote> comboLotesDisponibles;   // Combo para seleccionar lote disponible
    private JComboBox<Empresa> comboEmpresas;        // Combo para seleccionar empresa

    public LoteView() {
        super("Lotes");
        this.loteService = new LoteService();
        this.empresaService = new EmpresaService();

        setLayout(new FlowLayout());

        add(new JLabel("Superficie"));
        txtSuperficie = new JTextField(20);
        add(txtSuperficie);

        JButton btnRegistrar = new JButton("Registrar Lote");
        btnRegistrar.addActionListener(this::registrar);
        add(btnRegistrar);

        // Combo para lotes disponibles
        add(new JLabel("Seleccionar Lote Disponible"));
        comboLotesDisponibles = new JComboBox<>();
        cargarLotesDisponibles();
        add(comboLotesDisponibles);

        // Combo para empresas
        add(new JLabel("Seleccionar Empresa"));
        comboEmpresas = new JComboBox<>();
        cargarEmpresas();
        add(comboEmpresas);

        JButton btnAsociar = new JButton("Asociar Lote a Empresa");
        btnAsociar.addActionListener(this::asociar);
        add(btnAsociar);

        JButton btnListar = new JButton("Listar Lotes");
        btnListar.addActionListener(this::listar);
        add(btnListar);

        output = new JTextArea(10, 50);
        output.setEditable(false);
        add(new JScrollPane(output));

        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void registrar(ActionEvent e) {
        try {
            int superficie = Integer.parseInt(txtSuperficie.getText());
            // Nuevo lote siempre inicia como disponible
            Lote lote = new Lote(superficie, "disponible", 0);
            loteService.registrar(lote);
            output.append("Lote registrado: superficie " + superficie + " | Estado: disponible\n");

            cargarLotesDisponibles(); // refrescar combo
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "La superficie debe ser un número",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void asociar(ActionEvent e) {
        Lote loteSeleccionado = (Lote) comboLotesDisponibles.getSelectedItem();
        Empresa empresaSeleccionada = (Empresa) comboEmpresas.getSelectedItem();

        if (loteSeleccionado == null || empresaSeleccionada == null) {
            JOptionPane.showMessageDialog(this,
                    "Debes seleccionar un lote disponible y una empresa",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        loteService.asociarEmpresa(loteSeleccionado.getId(), empresaSeleccionada.getId());
        output.append("Lote " + loteSeleccionado.getId() +
                " asociado a empresa " + empresaSeleccionada.getNombre() + "\n");

        cargarLotesDisponibles(); // refrescar combo
    }

    private void listar(ActionEvent e) {
        List<Lote> lotes = loteService.listar();
        output.setText("");
        for (Lote l : lotes) {
            output.append("Lote " + l.getId()
                    + " | Superficie: " + l.getSuperficie() + " m2"
                    + " | Estado: " + l.getEstado());

            if (l.getEmpresaId() > 0) {
                output.append(" | Empresa ID: " + l.getEmpresaId());
            }

            output.append("\n");
        }
    }

    private void cargarLotesDisponibles() {
        comboLotesDisponibles.removeAllItems();
        List<Lote> disponibles = loteService.listarDisponibles();
        for (Lote l : disponibles) {
            comboLotesDisponibles.addItem(l);
        }

        // Renderer para mostrar "Lote X - Superficie Y m2"
        comboLotesDisponibles.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Lote lote) {
                    setText("Lote " + lote.getId() + " - Superficie " + lote.getSuperficie() + " m2");
                }
                return this;
            }
        });
    }

    private void cargarEmpresas() {
        comboEmpresas.removeAllItems();
        List<Empresa> empresas = empresaService.listar(); // método que devuelve todas las empresas
        for (Empresa e : empresas) {
            comboEmpresas.addItem(e);
        }

        // Renderer para mostrar "Empresa X - Nombre"
        comboEmpresas.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Empresa empresa) {
                    setText("Empresa " + empresa.getId() + " - " + empresa.getNombre());
                }
                return this;
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoteView::new);
    }
}