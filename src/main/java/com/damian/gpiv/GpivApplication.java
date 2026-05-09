package com.damian.gpiv;

import com.damian.gpiv.database.Database;
import com.damian.gpiv.gui.LoginView;
import com.damian.gpiv.gui.RegistroUsuarioView;
import com.damian.gpiv.models.Usuario;
import com.damian.gpiv.services.UsuarioService;

public class GpivApplication {
    public static void main(String[] args) {
        // Inicializar base de datos
        Database.initDB();

        // Crear usuario administrador por defecto si no existe
        UsuarioService service = new UsuarioService();
        Usuario admin = service.autenticar("admin", "admin");
        if (admin == null) {
            Usuario usuarioAdmin = new Usuario("admin", "administrador", "admin");
            service.registrar(usuarioAdmin);
            System.out.println("Usuario administrador creado: admin / admin");
        }

        // Lanzar primero la ventana de registro
        javax.swing.SwingUtilities.invokeLater(() -> {
            RegistroUsuarioView registerView = new RegistroUsuarioView();

            // Cuando se cierre la ventana de registro, abrir login
            registerView.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    new LoginView();
                }
            });
        });
    }
}
