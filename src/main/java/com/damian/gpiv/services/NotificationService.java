package com.damian.gpiv.services;

import javax.swing.JOptionPane;

public class NotificationService {

    public void notify(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(
                null,
                mensaje,
                titulo,
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
