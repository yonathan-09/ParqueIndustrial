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
    private JTextField txtTipo;
    private JTextField txtEstado;
    private JTextField txtEmail;
    private JTextArea output;

    public EmpresaView() {
        super("Empresas");
        this.service = new EmpresaService();

        setLayout(new FlowLayout());

        add(new JLabel("Nombre"));
        txtNombre = new JTextField(20);
        add(txtNombre);

        add(new JLabel("Tipo (radicada/interesada)"));
        txtTipo = new JTextField(20);
        add(txtTipo);

        add(new JLabel("Estado"));
        txtEstado = new JTextField(20);
        add(txtEstado);

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

        setSize(650, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void registrar(ActionEvent e) {
        // id lo dejamos en 0 porque se autoincrementa en la BD
        Empresa empresa = new Empresa(
                txtNombre.getText(),
                0,
                txtTipo.getText(),
                txtEstado.getText(),
                txtEmail.getText()
        );
        service.registrar(empresa);
        output.append("Empresa registrada: " + empresa.getNombre() + "\n");
    }

    private void listar(ActionEvent e) {
        List<Empresa> empresas = service.listar();
        output.setText("");
        for (Empresa emp : empresas) {
            output.append(emp.toString() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmpresaView::new);
    }
}