package com.damian.gpiv.gui;

import com.damian.gpiv.models.Lote;
import com.damian.gpiv.services.LoteService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LoteMapView extends JFrame {
    private final LoteService service;
    private JPanel canvas;
    private List<LoteRect> loteRects;

    public LoteMapView() {
        super("Mapa de Lotes - GPIV");
        this.service = new LoteService();
        this.loteRects = new ArrayList<>();

        // MEDIDAS COMPACTAS Y ESTILIZADAS
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        Color verdeFoto = new Color(93, 203, 82);

        // 1. BARRA SUPERIOR (Estilo Navbar Fino)
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(verdeFoto);
        navbar.setPreferredSize(new Dimension(780, 60));
        navbar.setBorder(new EmptyBorder(0, 25, 0, 25));

        JLabel lblTitulo = new JLabel("Distribución del Parque Industrial");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        navbar.add(lblTitulo, BorderLayout.WEST);

        // En el constructor, después de lblTitulo:
        JButton btnPlanoPDF = new JButton("Abrir Plano PDF");
        btnPlanoPDF.setFocusPainted(false);
        btnPlanoPDF.setBackground(Color.WHITE);
        btnPlanoPDF.setForeground(verdeFoto);
        btnPlanoPDF.setFont(new Font("Arial", Font.BOLD, 12));

        btnPlanoPDF.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> new PlanoLotes("/PlanoLotes.pdf").setVisible(true));
        });


        navbar.add(btnPlanoPDF, BorderLayout.EAST);

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
        // Definir tamaño preferido del canvas para que active el scroll
        canvas.setPreferredSize(new Dimension(1000, 800)); // ajusta según cantidad de lotes

// Envolver el canvas en un JScrollPane
        JScrollPane scrollPane = new JScrollPane(canvas,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);

// Contenedor con margen limpio alrededor del scroll
        JPanel panelContenedor = new JPanel(new BorderLayout());
        panelContenedor.setBorder(new EmptyBorder(20, 25, 20, 25));
        panelContenedor.setBackground(Color.WHITE);
        panelContenedor.add(scrollPane, BorderLayout.CENTER);

        add(panelContenedor, BorderLayout.CENTER);

        setVisible(true);
    }

    private void drawLotes(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        loteRects.clear();
        List<Lote> lotes = service.listar();

        int x = 40, y = 30;
        int lotesPorFila = 5;
        int anchoLote = 110;
        int altoLote = 65;
        int espacioX = 135;
        int espacioY = 90;

        int contadorFila = 0;

        for (Lote lote : lotes) {
            Color colorFondo;
            switch (lote.getEstado().toLowerCase()) {
                case "disponible" -> colorFondo = new Color(46, 184, 46);
                case "ocupado"    -> colorFondo = new Color(230, 36, 36);
                default           -> colorFondo = new Color(242, 193, 46);
            }

            g2.setColor(colorFondo);
            g2.fill(new RoundRectangle2D.Float(x, y, anchoLote, altoLote, 8, 8));

            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2f));
            g2.draw(new RoundRectangle2D.Float(x, y, anchoLote, altoLote, 8, 8));

            g2.setFont(new Font("Arial", Font.BOLD, 13));
            g2.setColor(Color.WHITE);
            g2.drawString("LOTE " + lote.getId(), x + 30, y + 38);

            loteRects.add(new LoteRect(new Rectangle(x, y, anchoLote, altoLote), lote));

            contadorFila++;
            if (contadorFila < lotesPorFila) {
                x += espacioX;
            } else {
                contadorFila = 0;
                x = 40;
                y += espacioY;
            }

            int filas = (int) Math.ceil((double) lotes.size() / lotesPorFila);
            int anchoTotal = 40 + lotesPorFila * espacioX;
            int altoTotal  = 30 + (filas * espacioY) + altoLote;

            canvas.setPreferredSize(new Dimension(anchoTotal, altoTotal));
            canvas.revalidate();
        }

        int filas = (int) Math.ceil((double) lotes.size() / lotesPorFila);
        canvas.setPreferredSize(new Dimension(
                lotesPorFila * espacioX,
                filas * espacioY
        ));
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

    private void abrirPDF(String resourcePath) {
        try {
            // Cargar el recurso desde el classpath
            java.net.URL url = getClass().getResource(resourcePath);
            if (url == null) {
                JOptionPane.showMessageDialog(this,
                        "No se encontró el recurso: " + resourcePath,
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Copiar el recurso a un archivo temporal
            File tempFile = File.createTempFile("plano", ".pdf");
            tempFile.deleteOnExit();

            try (java.io.InputStream in = url.openStream();
                 java.io.OutputStream out = new java.io.FileOutputStream(tempFile)) {
                in.transferTo(out);
            }

            // Abrir con el visor predeterminado
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(tempFile);
            } else {
                JOptionPane.showMessageDialog(this,
                        "La función Desktop no está soportada en este sistema.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al intentar abrir el PDF:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
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