package com.damian.gpiv.gui;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class PlanoLotes extends JFrame {

    private Point initialClick;
    private JScrollPane scrollPane;

    public PlanoLotes(String resourcePath) {
        super("Plano del Parque Industrial");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // maximiza la ventana
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        try {
            // Cargar el PDF desde resources
            InputStream in = getClass().getResourceAsStream(resourcePath);
            PDDocument document = PDDocument.load(in);
            PDFRenderer renderer = new PDFRenderer(document);

            // Renderizar la primera página como imagen
            BufferedImage image = renderer.renderImageWithDPI(0, 120); // DPI ajustable
            JLabel label = new JLabel(new ImageIcon(image));

            JScrollPane scrollPane = new JScrollPane(label);
            add(scrollPane, BorderLayout.CENTER);

            // Listener para arrastrar con el mouse
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    initialClick = e.getPoint();
                    setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    setCursor(Cursor.getDefaultCursor());
                }
            });

            label.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    JViewport viewport = scrollPane.getViewport();
                    Point viewPos = viewport.getViewPosition();

                    int dx = initialClick.x - e.getX();
                    int dy = initialClick.y - e.getY();

                    viewPos.translate(dx, dy);
                    label.scrollRectToVisible(new Rectangle(viewPos, viewport.getSize()));
                }
            });

            document.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al abrir el PDF: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
