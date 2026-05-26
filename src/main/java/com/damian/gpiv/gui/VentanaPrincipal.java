package com.damian.gpiv.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class VentanaPrincipal extends JFrame {

    private final String rol;

    public VentanaPrincipal(String rol) {
        super("GPIV - Gestión del Parque Industrial de Viedma");
        this.rol = rol;

        setLayout(new GridLayout(0, 1, 10, 10)); // botones en columna
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Botones según rol
        switch (rol.toLowerCase()) {
            case "administrador":
                addButton("Gestionar Empresas", this::openEmpresas);
                addButton("Evaluar Empresas", this::openEvaluarEmpresas);
                addButton("Gestionar Proyectos", this::openProyectos);
                addButton("Evaluar Proyectos", this::openEvaluarProyectos);
                addButton("Gestionar Lotes", this::openLotes);
                addButton("Ver Reportes", this::openReportes);
                addButton("Mapa Interactivo de Lotes", this::openLoteMap);
                addButton("Registrar Nuevo Usuario", this::openUserRegister);
                break;

            case "empresa":
                addButton("Gestionar Empresas", this::openEmpresas);
                addButton("Gestionar Proyectos", this::openProyectos);
                addButton("Gestionar Lotes", this::openLotes);
                break;

            case "organismo":
                addButton("Ver Proyectos", this::openProyectos);
                break;

            case "proveedor":
                addButton("Ver Lotes Disponibles", this::openLotes);
                break;

            default:
                JOptionPane.showMessageDialog(this,
                        "Rol desconocido: " + rol,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
        }
        /*if (rol.equals("administrador")) {
            addButton("Gestionar Empresas", this::openEmpresas);
            addButton("Evaluar Empresas", this::openEvaluarEmpresas);
            addButton("Gestionar Proyectos", this::openProyectos);
            addButton("Evaluar Proyectos", this::openEvaluarProyectos);
            addButton("Gestionar Lotes", this::openLotes);
            addButton("Ver Reportes", this::openReportes);
            addButton("Mapa Interactivo de Lotes", this::openLoteMap);
            addButton("Registrar Nuevo Usuario", this::openUserRegister);

        } else if (rol.equals("empresa")) {
            addButton("Registrar Proyectos", this::openProyectos);

        } else if (rol.equals("organismo")) {
            addButton("Ver Reportes", this::openReportes);

        } else if (rol.equals("proveedor")) {
            addButton("Gestionar Lotes", this::openLotes);
        }

        setVisible(true);*/
    }

    private void addButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.addActionListener((ActionEvent e) -> action.run());
        add(button);
    }

    // Métodos que abren las vistas
    private void openEmpresas() {
        SwingUtilities.invokeLater(EmpresaView::new);
    }

    private void openEvaluarEmpresas() {SwingUtilities.invokeLater(EvaluarEmpresaView::new);}

    private void openProyectos() {
        SwingUtilities.invokeLater(ProyectoView::new);
    }

    private void openEvaluarProyectos() {SwingUtilities.invokeLater(EvaluarProyectosView::new);}

    private void openLotes() {
        SwingUtilities.invokeLater(LoteView::new);
    }

    private void openReportes() {
        SwingUtilities.invokeLater(ReporteView::new);
    }

    private void openLoteMap() {
        SwingUtilities.invokeLater(LoteMapView::new);
    }

    private void openUserRegister() {
        SwingUtilities.invokeLater(RegistroUsuarioView::new);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal("administrador"));
    }

}
