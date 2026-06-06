package com.damian.gpiv.gui;

import com.damian.gpiv.models.Empresa;
import com.damian.gpiv.models.Proyecto;
import com.damian.gpiv.models.Lote;
import com.damian.gpiv.services.EmpresaService;
import com.damian.gpiv.services.ProyectoService;
import com.damian.gpiv.services.LoteService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class EmpresaDashboardView extends JFrame {
    private final EmpresaService empresaService;
    private final ProyectoService proyectoService;
    private final LoteService loteService;
    private final int empresaId;
    private JTextArea output;

    public EmpresaDashboardView(int empresaId) {
        super("Panel Empresa - GPIV");
        this.empresaService = new EmpresaService();
        this.proyectoService = new ProyectoService();
        this.loteService = new LoteService();
        this.empresaId = empresaId;

        // FORMATO COMPACTO: Tamaño horizontal fino y equilibrado
        setSize(780, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        Color verdeFoto = new Color(93, 203, 82);

        // 1. BARRA SUPERIOR (Estilo Navbar Web Fino)
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(verdeFoto);
        navbar.setPreferredSize(new Dimension(780, 60)); // Alto reducido para que sea sutil
        navbar.setBorder(new EmptyBorder(0, 25, 0, 25));

        JLabel lblTitulo = new JLabel("Panel de Autogestión (Empresa ID: " + empresaId + ")");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18)); // Fuente elegante, no gigante
        lblTitulo.setForeground(Color.WHITE);
        navbar.add(lblTitulo, BorderLayout.WEST);

        add(navbar, BorderLayout.NORTH);

        // 2. PANEL CENTRAL (Cuerpo blanco estilizado)
        JPanel panelContenido = new JPanel(new GridBagLayout());
        panelContenido.setBackground(Color.WHITE);
        panelContenido.setBorder(new EmptyBorder(15, 25, 15, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // --- Fila Superior: Botones de Acción Inline ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        panelBotones.setOpaque(false);

        BotonRedondeado btnRegistrarEmpresa = new BotonRedondeado("Registrar Interesada");
        configurarBoton(btnRegistrarEmpresa, new Color(245, 247, 250));
        btnRegistrarEmpresa.addActionListener(this::registrarEmpresa);
        panelBotones.add(btnRegistrarEmpresa);

        BotonRedondeado btnRegistrarProyecto = new BotonRedondeado("Registrar Proyecto");
        configurarBoton(btnRegistrarProyecto, new Color(245, 247, 250));
        btnRegistrarProyecto.addActionListener(this::registrarProyecto);
        panelBotones.add(btnRegistrarProyecto);

        BotonRedondeado btnListarProyectos = new BotonRedondeado("Mis Proyectos");
        configurarBoton(btnListarProyectos, verdeFoto);
        btnListarProyectos.setForeground(Color.WHITE);
        btnListarProyectos.addActionListener(this::listarProyectos);
        panelBotones.add(btnListarProyectos);

        BotonRedondeado btnListarLotes = new BotonRedondeado("Lotes Disponibles");
        configurarBoton(btnListarLotes, verdeFoto);
        btnListarLotes.setForeground(Color.WHITE);
        btnListarLotes.addActionListener(this::listarLotes);
        panelBotones.add(btnListarLotes);

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 15, 0);
        panelContenido.add(panelBotones, gbc);

        // --- Fila Inferior: Consola de Texto Equilibrada ---
        output = new JTextArea(10, 50);
        output.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Letra estándar limpia para el log
        output.setEditable(false);
        output.setBorder(BorderFactory.createLineBorder(new Color(225, 228, 232), 1));

        JScrollPane scroll = new JScrollPane(output);

        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        panelContenido.add(scroll, gbc);

        add(panelContenido, BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * Configura los botones con medidas compactas y fuentes balanceadas.
     */
    private void configurarBoton(JButton boton, Color colorFondo) {
        boton.setFont(new Font("Arial", Font.BOLD, 12)); // Texto compacto y prolijo
        boton.setBackground(colorFondo);
        if (boton.getForeground() != Color.WHITE) {
            boton.setForeground(new Color(60, 65, 70));
        }
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(160, 36)); // Tamaño intermedio perfecto
    }

    private void registrarEmpresa(ActionEvent e) {
        // 1. Crear los componentes de texto para el formulario único
        JTextField txtNombre = new JTextField(15);
        JTextField txtEmail = new JTextField(15);
        JTextField txtCuit = new JTextField(15);
        JTextField txtActividadPrincipal = new JTextField(15);
        JTextField txtDireccion = new JTextField(15);
        JTextField txtReferente = new JTextField(15);
        JTextField txtTelefono = new JTextField(15);
        JComboBox<String> comboRubro = new JComboBox<>(new String[]{"Servicios", "Bienes", "Bienes y Servicios"});
        JTextField txtDescripcionServicio = new JTextField(15);


        // 2. Armar un panel contenedor para agrupar los campos verticalmente
        JPanel panelFormulario = new JPanel(new GridLayout(0, 1, 5, 5));

        panelFormulario.add(new JLabel("Nombre de la empresa:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Email de la empresa:"));
        panelFormulario.add(txtEmail);
        panelFormulario.add(new JLabel("CUIT:"));
        panelFormulario.add(txtCuit);
        panelFormulario.add(new JLabel("Actividad Principal:"));
        panelFormulario.add(txtActividadPrincipal);
        panelFormulario.add(new JLabel("Dirección:"));
        panelFormulario.add(txtDireccion);
        panelFormulario.add(new JLabel("Persona Referente:"));
        panelFormulario.add(txtReferente);
        panelFormulario.add(new JLabel("Teléfono:"));
        panelFormulario.add(txtTelefono);
        panelFormulario.add(new JLabel("Rubro:"));
        panelFormulario.add(comboRubro);
        panelFormulario.add(new JLabel("Descripción del Servicio/Bien:"));
        panelFormulario.add(txtDescripcionServicio);

        // 3. Lanzar el JOptionPane mostrando el panel completo con botones de Aceptar/Cancelar
        int result = JOptionPane.showConfirmDialog(this, panelFormulario,
                "Registrar Empresa Interesada", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // 4. Si el usuario presionó 'Aceptar' (OK), procesamos los datos ingresados
        if (result == JOptionPane.OK_OPTION) {
            String nombre = txtNombre.getText().trim();
            String email = txtEmail.getText().trim();

            // Validación simple para asegurarnos de que no envíe datos vacíos
            if (txtNombre.getText().trim().isEmpty() ||
                    txtEmail.getText().trim().isEmpty() ||
                    txtCuit.getText().trim().isEmpty() ||
                    txtActividadPrincipal.getText().trim().isEmpty() ||
                    txtReferente.getText().trim().isEmpty() ||
                    txtTelefono.getText().trim().isEmpty() ||
                    txtDescripcionServicio.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Debes completar todos los campos obligatorios",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            else {
                Empresa empresa = new Empresa(
                        txtNombre.getText().trim(),
                        0,
                        "interesada",
                        txtEmail.getText().trim(),
                        "pendiente",
                        txtCuit.getText().trim(),
                        txtActividadPrincipal.getText().trim(),
                        txtDireccion.getText().trim(),
                        txtReferente.getText().trim(),
                        txtTelefono.getText().trim(),
                        (String) comboRubro.getSelectedItem(),
                        txtDescripcionServicio.getText().trim()
                );

                empresaService.registrar(empresa);
                output.append("» [" + java.time.LocalTime.now().toString().substring(0, 5) + "] Empresa registrada como interesada (pendiente de aprobación).\n");
            }
        }
    }

    private void registrarProyecto(ActionEvent e) {
        // 1. Crear los componentes de texto para el formulario único
        JTextField txtNombre = new JTextField(15);
        JTextField txtDescripcion = new JTextField(15);

        // 2. Armar el panel contenedor para agrupar los campos de forma vertical
        JPanel panelFormulario = new JPanel(new GridLayout(0, 1, 5, 5));

        panelFormulario.add(new JLabel("Nombre del proyecto:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Descripción del proyecto:"));
        panelFormulario.add(txtDescripcion);

        // 3. Lanzar el JOptionPane con el panel completo
        int result = JOptionPane.showConfirmDialog(this, panelFormulario,
                "Registrar Nuevo Proyecto", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // 4. Si el usuario presionó 'Aceptar', procesamos las entradas
        if (result == JOptionPane.OK_OPTION) {
            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();

            // Validación para asegurar que no se envíen campos vacíos
            if (!nombre.isEmpty() && !descripcion.isEmpty()) {
                Proyecto proyecto = new Proyecto(0, nombre, descripcion, "pendiente", empresaId);
                proyectoService.registrar(proyecto);
                output.append("» [" + java.time.LocalTime.now().toString().substring(0, 5) + "] Proyecto registrado para la empresa ID " + empresaId + "\n");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Debe completar ambos campos para registrar el proyecto.",
                        "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void listarProyectos(ActionEvent e) {
        List<Proyecto> proyectos = proyectoService.listarPorEmpresa(empresaId);
        output.setText("");
        if (proyectos.isEmpty()) {
            output.setText("No se registran proyectos asociados a su empresa.");
            return;
        }
        output.append("=== MIS PROYECTOS REGISTRADOS ===\n");
        for (Proyecto p : proyectos) {
            output.append("• ID: " + p.getId() + " | " + p.getNombre() + " | Estado: " + p.getEstado() + "\n");
        }
    }

    private void listarLotes(ActionEvent e) {
        List<Lote> lotes = loteService.listarDisponibles();
        output.setText("");
        if (lotes.isEmpty()) {
            output.setText("No hay lotes disponibles en este momento.");
            return;
        }
        output.append("=== LOTES DISPONIBLES EN PARQUE INDUSTRIAL ===\n");
        for (Lote l : lotes) {
            output.append("• Lote ID: " + l.getId() + " | Superficie: " + l.getSuperficie() + "m² | Estado: " + l.getEstado() + "\n");
        }
    }

    /**
     * Componente personalizado de botón con puntas redondeadas finas y contorno negro de 2px.
     */
    private static class BotonRedondeado extends JButton {
        private final int radioArco = 12; // Curvatura más fina y estética

        public BotonRedondeado(String texto) {
            super(texto);
            setContentAreaFilled(false);
            setBorderPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Fondo del botón
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radioArco, radioArco));

            // Contorno negro remarcado fino
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2f));
            g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, radioArco, radioArco));

            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmpresaDashboardView(1));
    }
}
