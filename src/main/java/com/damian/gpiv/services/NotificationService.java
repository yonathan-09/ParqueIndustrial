package com.damian.gpiv.services;

import javax.swing.JOptionPane;

public class NotificationService {

    public void notify(String titulo, String mensaje) {
        // Mostrar ventana emergente con el mensaje
        JOptionPane.showMessageDialog(
                null,          // componente padre (null = pantalla principal)
                mensaje,       // contenido del mensaje
                titulo,        // título de la ventana
                JOptionPane.INFORMATION_MESSAGE // tipo de mensaje
        );
    }
}
