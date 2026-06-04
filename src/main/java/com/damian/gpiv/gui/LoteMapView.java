package com.damian.gpiv.gui;

import com.damian.gpiv.models.Lote;
import com.damian.gpiv.services.LoteService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

public class LoteMapView extends JFrame {
    private final LoteService service;
    private JPanel canvas;
    private List<LoteRect> loteRects;

    public LoteMapView() {
        super("Mapa Interactivo de Lotes - GPIV");
        this.service = new LoteService();
        this.loteRects = new ArrayList<>();

        // MEDIDAS COMPACTAS Y ESTILIZADAS
        setSize(780, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        Color verdeFoto = new Color(93, 203, 82);

        // 1. BARRA SUPERIOR (Estilo Navbar Fino)
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(verdeFoto);
        navbar.setPreferredSize(new Dimension(780, 60));
        navbar.setBorder(new EmptyBorder(0, 25, 0, 25));

        JLabel lblTitulo = new JLabel("Distribución Espacial del Parque Industrial");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        navbar.add(lblTitulo, BorderLayout.WEST);

        add(navbar, BorderLayout.NORTH);

        // 2. LIENZO DE DIBUJO (Canvas interactivo)
        canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawLotes(g);
            }
        };
        canvas.setBackground(Color.WHITE);

        // Listener exacto para las colisiones del clic
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

        // Contenedor para darle un margen limpio al canvas en los bordes
        JPanel panelContenedor = new JPanel(new BorderLayout());
        panelContenedor.setBorder(new EmptyBorder(20, 25, 20, 25));
        panelContenedor.setBackground(Color.WHITE);
        panelContenedor.add(canvas, BorderLayout.CENTER);

        add(panelContenedor, BorderLayout.CENTER);
        setVisible(true);
    }

    private void drawLotes(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        loteRects.clear();
        List<Lote> lotes = service.listar();

        int x = 40, y = 30; // Ajuste de márgenes iniciales en el lienzo

        for (Lote lote : lotes) {
            Color colorFondo;
            // CAMBIO VISUAL: Colores fuertes y puros para máxima visualización
            switch (lote.getEstado().toLowerCase()) {
                case "disponible" -> colorFondo = new Color(46, 184, 46);   // Verde fuerte vivo
                case "ocupado"    -> colorFondo = new Color(230, 36, 36);   // Rojo fuerte vivo
                default           -> colorFondo = new Color(242, 193, 46);  // Amarillo fuerte vivo
            }

            // Dibujar el fondo del terreno redondeado suave
            g2.setColor(colorFondo);
            g2.fill(new RoundRectangle2D.Float(x, y, 110, 65, 8, 8));

            // Contorno negro remarcado de 2px (Identidad visual del sistema)
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2f));
            g2.draw(new RoundRectangle2D.Float(x, y, 110, 65, 8, 8));

            // Indicador de ID de Lote centrado con texto Blanco para contrastar con los colores fuertes
            g2.setFont(new Font("Arial", Font.BOLD, 13));
            g2.setColor(Color.WHITE);
            g2.drawString("LOTE " + lote.getId(), x + 30, y + 38);

            // Guardamos el rectángulo con las medidas exactas actuales para el clic
            loteRects.add(new LoteRect(new Rectangle(x, y, 110, 65), lote));

            // Tu lógica exacta de salto de fila para la grilla
            x += 135;
            if (x > 600) {
                x = 40;
                y += 90;
            }
        }
    }

    private void showDetails(Lote lote) {
        String detalle = "📍 Información del Terreno" +
                "\n────────────────────" +
                "\n• Lote Nro: " + lote.getId() +
                "\n• Superficie total: " + lote.getSuperficie() + " m²" +
                "\n• Estado de ocupación: " + lote.getEstado().toUpperCase() +
                "\n• ID Empresa Asignada: " + (lote.getEmpresaId() == 0 ? "Ninguna" : lote.getEmpresaId());

        JOptionPane.showMessageDialog(this, detalle, "Ficha Técnica del Lote", JOptionPane.INFORMATION_MESSAGE);
    }

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