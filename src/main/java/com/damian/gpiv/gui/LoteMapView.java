package com.damian.gpiv.gui;

import com.damian.gpiv.models.Lote;
import com.damian.gpiv.services.LoteService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class LoteMapView extends JFrame {
    private final LoteService service;
    private JPanel canvas;
    private List<LoteRect> loteRects; // lista de rectángulos con referencia al lote

    public LoteMapView() {
        super("Mapa Interactivo de Lotes");
        this.service = new LoteService();
        this.loteRects = new ArrayList<>();

        canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawLotes(g);
            }
        };
        canvas.setPreferredSize(new Dimension(600, 400));
        canvas.setBackground(Color.WHITE);

        // 🔑 Un solo listener para todo el canvas
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (LoteRect lr : loteRects) {
                    if (lr.rect.contains(e.getPoint())) {
                        showDetails(lr.lote);
                        break;
                    }
                }
            }
        });

        add(canvas);

        setSize(650, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void drawLotes(Graphics g) {
        loteRects.clear(); // limpiar lista antes de dibujar
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

            // Guardar coordenadas y lote asociado
            loteRects.add(new LoteRect(new Rectangle(x, y, 100, 60), lote));

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

    // Clase auxiliar para vincular rectángulo con lote
    private static class LoteRect {
        Rectangle rect;
        Lote lote;

        LoteRect(Rectangle rect, Lote lote) {
            this.rect = rect;
            this.lote = lote;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoteMapView::new);
    }
}

