package com.damian.gpiv.gui;

import com.damian.gpiv.models.Lote;
import com.damian.gpiv.services.LoteService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class LoteMapView extends JFrame {
    private final LoteService service;
    private JPanel canvas;

    public LoteMapView() {
        super("Mapa Interactivo de Lotes");
        this.service = new LoteService();

        canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawLotes(g);
            }
        };
        canvas.setPreferredSize(new Dimension(600, 400));
        canvas.setBackground(Color.WHITE);

        add(canvas);

        setSize(650, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void drawLotes(Graphics g) {
        List<Lote> lotes = service.listar();
        int x = 50, y = 50;

        for (Lote lote : lotes) {
            Color color;
            switch (lote.getEstado()) {
                case "disponible" -> color = Color.GREEN;
                case "ocupado" -> color = Color.RED;
                default -> color = Color.YELLOW;
            }

            // Dibujar rectángulo
            g.setColor(color);
            g.fillRect(x, y, 100, 60);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, 100, 60);

            // Texto dentro del rectángulo
            g.setColor(Color.WHITE);
            g.drawString("Lote " + lote.getId(), x + 30, y + 30);

            // Guardar coordenadas para detectar clic
            Rectangle rect = new Rectangle(x, y, 100, 60);
            canvas.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (rect.contains(e.getPoint())) {
                        showDetails(lote);
                    }
                }
            });

            // Posición siguiente
            x += 120;
            if (x > 500) {
                x = 50;
                y += 80;
            }
        }
    }

    private void showDetails(Lote lote) {
        String detalle = "Lote " + lote.getId() +
                "\nSuperficie: " + lote.getSuperficie() + " m2" +
                "\nEstado: " + lote.getEstado() +
                "\nEmpresa ID: " + lote.getEmpresaId();
        JOptionPane.showMessageDialog(this, detalle, "Detalle del Lote", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoteMapView::new);
    }
}
