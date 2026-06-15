package com.damian.gpiv;

import com.damian.gpiv.database.Database;
import com.damian.gpiv.gui.LoginView;
import com.damian.gpiv.models.Usuario;
import com.damian.gpiv.services.UsuarioService;

public class GpivApplication {
    public static void main(String[] args) {
        Database.initDB();

        UsuarioService service = new UsuarioService();
        Usuario admin = service.autenticar("admin", "admin");
        if (admin == null) {
            Usuario usuarioAdmin = new Usuario("admin", "administrador", "admin");
            service.registrar(usuarioAdmin);
            System.out.println("Usuario administrador creado: admin / admin");
        }

        javax.swing.SwingUtilities.invokeLater(LoginView::new);
    }
}
