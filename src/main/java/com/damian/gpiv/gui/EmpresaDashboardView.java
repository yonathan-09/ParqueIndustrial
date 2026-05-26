package com.damian.gpiv.gui;

import com.damian.gpiv.models.Empresa;
import com.damian.gpiv.models.Proyecto;
import com.damian.gpiv.models.Lote;
import com.damian.gpiv.services.EmpresaService;
import com.damian.gpiv.services.ProyectoService;
import com.damian.gpiv.services.LoteService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class EmpresaDashboardView extends JFrame {
    private final EmpresaService empresaService;
    private final ProyectoService proyectoService;
    private final LoteService loteService;
    private final int empresaId; // ID de la empresa logueada
    private JTextArea output;

    public EmpresaDashboardView(int empresaId) {
        super("Panel Empresa");
        this.empresaService = new EmpresaService();
        this.proyectoService = new ProyectoService();
        this.loteService = new LoteService();
        this.empresaId = empresaId;

        setLayout(new FlowLayout());

        JButton btnRegistrarEmpresa = new JButton("Registrar Empresa Interesada");
        btnRegistrarEmpresa.addActionListener(this::registrarEmpresa);
        add(btnRegistrarEmpresa);

        JButton btnRegistrarProyecto = new JButton("Registrar Proyecto");
        btnRegistrarProyecto.addActionListener(this::registrarProyecto);
        add(btnRegistrarProyecto);

        JButton btnListarProyectos = new JButton("Mis Proyectos");
        btnListarProyectos.addActionListener(this::listarProyectos);
        add(btnListarProyectos);

        JButton btnListarLotes = new JButton("Lotes Disponibles");
        btnListarLotes.addActionListener(this::listarLotes);
        add(btnListarLotes);

        output = new JTextArea(15, 60);
        output.setEditable(false);
        add(new JScrollPane(output));

        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void registrarEmpresa(ActionEvent e) {
        String nombre = JOptionPane.showInputDialog(this, "Ingrese nombre de la empresa:");
        String email = JOptionPane.showInputDialog(this, "Ingrese email de la empresa:");

        if (nombre != null && email != null) {
            Empresa empresa = new Empresa(nombre, 0, "interesada", email, "pendiente");
            empresaService.registrar(empresa);
            output.append("Empresa registrada como interesada (pendiente de aprobación).\n");
        }
    }

    private void registrarProyecto(ActionEvent e) {
        String nombre = JOptionPane.showInputDialog(this, "Ingrese nombre del proyecto:");
        String descripcion = JOptionPane.showInputDialog(this, "Ingrese descripción del proyecto:");

        if (nombre != null && descripcion != null) {
            Proyecto proyecto = new Proyecto(0, nombre, descripcion, "pendiente", empresaId);
            proyectoService.registrar(proyecto);
            output.append("Proyecto registrado para la empresa ID " + empresaId + "\n");
        }
    }

    private void listarProyectos(ActionEvent e) {
        List<Proyecto> proyectos = proyectoService.listarPorEmpresa(empresaId);
        output.setText("");
        for (Proyecto p : proyectos) {
            output.append("Proyecto " + p.getId() + " | " + p.getNombre() +
                    " | Estado: " + p.getEstado() + "\n");
        }
    }

    private void listarLotes(ActionEvent e) {
        List<Lote> lotes = loteService.listarDisponibles();
        output.setText("");
        for (Lote l : lotes) {
            output.append("Lote " + l.getId() + " | Superficie: " + l.getSuperficie() +
                    " | Estado: " + l.getEstado() + "\n");
        }
    }

    public static void main(String[] args) {
        // Simulación: empresa con ID 1
        SwingUtilities.invokeLater(() -> new EmpresaDashboardView(1));
    }
}
