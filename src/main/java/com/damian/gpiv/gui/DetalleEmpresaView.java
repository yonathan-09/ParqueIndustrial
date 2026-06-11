package com.damian.gpiv.gui;

import com.damian.gpiv.models.SolicitudRadicacion;

import javax.swing.*;
import java.awt.*;

public class DetalleEmpresaView extends JFrame {

    public DetalleEmpresaView(SolicitudRadicacion s) {

        setTitle("Detalle de Empresa");
        setSize(700, 500);
        setLocationRelativeTo(null);

        JTextArea area = new JTextArea();
        area.setEditable(false);

        area.setText(
                "Empresa ID: " + s.getId() + "\n\n" +
                        "Nombre: " + s.getNombre() + "\n" +
                        "Email: " + s.getEmail() + "\n" +
                        "CUIT: " + s.getCuit() + "\n" +
                        "Actividad Principal: " + s.getActividadPrincipal() + "\n" +
                        "Dirección: " + s.getDireccion() + "\n" +
                        "Referente: " + s.getReferente() + "\n" +
                        "Teléfono: " + s.getTelefono() + "\n" +
                        "Rubro: " + s.getRubro() + "\n" +
                        "Descripción Servicio: " + s.getDescripcionServicio() + "\n" +
                        "Estado: " + s.getEstado()
        );

        add(new JScrollPane(area));

        setVisible(true);
    }
}