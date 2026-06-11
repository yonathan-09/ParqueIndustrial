package com.damian.gpiv.gui;

import com.damian.gpiv.models.Proyecto;

import javax.swing.*;
import java.awt.*;

public class DetalleProyectoView extends JFrame {

    public DetalleProyectoView(Proyecto p) {

        setTitle("Detalle Proyecto");
        setSize(700, 500);
        setLocationRelativeTo(null);

        JTextArea area = new JTextArea();
        area.setEditable(false);

        area.setText(
                "Proyecto ID: " + p.getId() + "\n\n" +
                        "Nombre: " + p.getNombre() + "\n" +
                        "Descripción: " + p.getDescripcion() + "\n" +
                        "Estado: " + p.getEstado() + "\n" +
                        "Superficie solicitada: "
                        + p.getSuperficieLote() + " m²\n" +
                        "Empresa ID: "
                        + p.getEmpresaId() + "\n"

        );

        add(new JScrollPane(area));

        setVisible(true);
    }
}
