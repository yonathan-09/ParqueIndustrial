package com.damian.gpiv.gui;

import com.damian.gpiv.models.Empresa;
import com.damian.gpiv.services.EmpresaService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class EmpresaView extends JFrame {
    private final EmpresaService service;
    private JTextField txtNombre;
    private JComboBox<String> comboTipo; // ahora es un combo
    private JTextField txtEmail;
    private JTextArea output;

    public EmpresaView() {
        super("Empresas");
        this.service = new EmpresaService();

        setLayout(new FlowLayout());

        add(new JLabel("Nombre"));
        txtNombre = new JTextField(20);
        add(txtNombre);

        add(new JLabel("Tipo"));
        comboTipo = new JComboBox<>(new String[]{"interesada", "radicada"});
        add(comboTipo);

        add(new JLabel("Email"));
        txtEmail = new JTextField(30);
        add(txtEmail);

        JButton btnRegistrar = new JButton("Registrar Empresa");
        btnRegistrar.addActionListener(this::registrar);
        add(btnRegistrar);

        JButton btnListar = new JButton("Listar Empresas");
        btnListar.addActionListener(this::listar);
        add(btnListar);

        output = new JTextArea(10, 50);
        output.setEditable(false);
        add(new JScrollPane(output));

        setSize(800, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void registrar(ActionEvent e) {
        String tipo = (String) comboTipo.getSelectedItem();
        String estado = "pendiente";

        // Si se registra como radicada, el estado debe ser aprobada
        if ("radicada".equalsIgnoreCase(tipo)) {
            estado = "aprobada";
        }

        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();

        // Validaciones
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debes ingresar un nombre para la empresa",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debes ingresar un email para la empresa",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validación básica de formato de email
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            JOptionPane.showMessageDialog(this,
                    "El email ingresado no tiene un formato válido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Si pasó las validaciones, se registra normalmente
        Empresa empresa = new Empresa(
                nombre,
                0,
                tipo,
                email,
                estado
        );

        service.registrar(empresa);
        output.append("Empresa registrada: " + empresa.getNombre() +
                " | Email: " + empresa.getEmail() +
                " | Estado: " + empresa.getEstado() + "\n");
    }


    private void listar(ActionEvent e) {
        List<Empresa> empresas = service.listar();
        output.setText("");
        for (Empresa emp : empresas) {
            output.append("ID: " + emp.getId() + "\n"
                    + "Nombre: " + emp.getNombre() + "\n"
                    + "Tipo: " + emp.getTipo() + "\n"
                    + "Email: " + emp.getEmail() + "\n"
                    + "Estado: " + emp.getEstado() + "\n\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmpresaView::new);
    }
}
