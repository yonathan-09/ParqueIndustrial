package com.damian.gpiv.gui;

import com.damian.gpiv.models.Lote;
import com.damian.gpiv.models.Empresa;
import com.damian.gpiv.services.LoteService;
import com.damian.gpiv.services.EmpresaService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class LoteView extends JFrame {
    private final LoteService loteService;
    private final EmpresaService empresaService;
    private JTextField txtSuperficie;
    private JTextArea output;
    private JComboBox<Lote> comboLotesDisponibles;   // Combo para seleccionar lote disponible
    private JComboBox<Empresa> comboEmpresas;        // Combo para seleccionar empresa

    public LoteView() {
        super("Gestión de Lotes Terresariales - GPIV");
        this.loteService = new LoteService();
        this.empresaService = new EmpresaService();

        // Dimensiones estandarizadas para mantener coherencia visual
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // COLOR VERDE CORPORATIVO ACTUALIZADO (Sincronizado con tu foto)
        Color verdeFoto = new Color(93, 203, 82);

        // ---------------------------------------------------------------------
        // PANEL IZQUIERDO: Barra lateral decorativa institucional
        // ---------------------------------------------------------------------
        JPanel panelIzquierdoVerde = new JPanel();
        panelIzquierdoVerde.setBackground(verdeFoto);
        panelIzquierdoVerde.setPreferredSize(new Dimension(150, 650));
        add(panelIzquierdoVerde, BorderLayout.WEST);

        // ---------------------------------------------------------------------
        // PANEL CENTRAL: Formulario estructurado con GridBagLayout
        // ---------------------------------------------------------------------
        JPanel panelContenido = new JPanel(new GridBagLayout());
        panelContenido.setBackground(Color.WHITE);
        panelContenido.setBorder(new EmptyBorder(25, 40, 25, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        int fila = 0;

        // Título de la sección
        JLabel lblTitulo = new JLabel("Administración de Lotes e Infraestructura");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(40, 45, 50));
        gbc.gridy = fila++;
        panelContenido.add(lblTitulo, gbc);

        // Separador superior sutil
        gbc.gridy = fila++;
        gbc.insets = new Insets(0, 0, 15, 0);
        panelContenido.add(new JSeparator(), gbc);
        gbc.insets = new Insets(5, 0, 5, 0); // Reset

        // =====================================================================
        // BLOQUE 1: REGISTRO DE NUEVO LOTE
        // =====================================================================
        JLabel lblSuperficie = new JLabel("Superficie (en metros cuadrados — m²)");
        lblSuperficie.setFont(new Font("Arial", Font.PLAIN, 16));
        lblSuperficie.setForeground(new Color(80, 80, 80));
        gbc.gridy = fila++;
        panelContenido.add(lblSuperficie, gbc);

        txtSuperficie = new JTextField();
        txtSuperficie.setFont(new Font("Arial", Font.PLAIN, 16));
        txtSuperficie.setPreferredSize(new Dimension(300, 35));
        gbc.gridy = fila++;
        panelContenido.add(txtSuperficie, gbc);

        JButton btnRegistrar = new JButton("Registrar Nuevo Lote");
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 15));
        btnRegistrar.setBackground(verdeFoto);
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setPreferredSize(new Dimension(200, 40));
        btnRegistrar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Borde negro remarcado
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistrar.addActionListener(this::registrar);

        gbc.gridy = fila++;
        gbc.insets = new Insets(8, 0, 15, 0); // Margen extra inferior antes del siguiente bloque
        panelContenido.add(btnRegistrar, gbc);
        gbc.insets = new Insets(5, 0, 5, 0); // Reset

        // Línea divisoria intermedia entre acciones
        gbc.gridy = fila++;
        panelContenido.add(new JSeparator(), gbc);

        // =====================================================================
        // BLOQUE 2: ASOCIACIÓN/ADJUDICACIÓN DE LOTES
        // =====================================================================
        JLabel lblLotesDispo = new JLabel("Seleccionar Lote Disponible");
        lblLotesDispo.setFont(new Font("Arial", Font.PLAIN, 16));
        lblLotesDispo.setForeground(new Color(80, 80, 80));
        gbc.gridy = fila++;
        panelContenido.add(lblLotesDispo, gbc);

        comboLotesDisponibles = new JComboBox<>();
        comboLotesDisponibles.setFont(new Font("Arial", Font.PLAIN, 16));
        comboLotesDisponibles.setBackground(Color.WHITE);
        comboLotesDisponibles.setPreferredSize(new Dimension(300, 35));
        cargarLotesDisponibles();
        gbc.gridy = fila++;
        panelContenido.add(comboLotesDisponibles, gbc);

        JLabel lblEmpresas = new JLabel("Seleccionar Empresa Adjudicataria");
        lblEmpresas.setFont(new Font("Arial", Font.PLAIN, 16));
        lblEmpresas.setForeground(new Color(80, 80, 80));
        gbc.gridy = fila++;
        panelContenido.add(lblEmpresas, gbc);

        comboEmpresas = new JComboBox<>();
        comboEmpresas.setFont(new Font("Arial", Font.PLAIN, 16));
        comboEmpresas.setBackground(Color.WHITE);
        comboEmpresas.setPreferredSize(new Dimension(300, 35));
        cargarEmpresas();
        gbc.gridy = fila++;
        panelContenido.add(comboEmpresas, gbc);

        // ---------------------------------------------------------------------
        // PANEL DE ACCIONES INFERIORES: Asociar y Listar con Bordes Negros
        // ---------------------------------------------------------------------
        JPanel panelBotonesAccion = new JPanel(new GridLayout(1, 2, 20, 0));
        panelBotonesAccion.setBackground(Color.WHITE);

        JButton btnAsociar = new JButton("Adjudicar Lote a Empresa");
        btnAsociar.setFont(new Font("Arial", Font.BOLD, 15));
        btnAsociar.setBackground(verdeFoto);
        btnAsociar.setForeground(Color.WHITE);
        btnAsociar.setFocusPainted(false);
        btnAsociar.setPreferredSize(new Dimension(150, 45));
        btnAsociar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Borde negro remarcado
        btnAsociar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAsociar.addActionListener(this::asociar);

        JButton btnListar = new JButton("Listar Todo el Catastro");
        btnListar.setFont(new Font("Arial", Font.BOLD, 15));
        btnListar.setBackground(new Color(240, 243, 247));
        btnListar.setForeground(new Color(40, 50, 60));
        btnListar.setFocusPainted(false);
        btnListar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Borde negro remarcado
        btnListar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnListar.addActionListener(this::listar);

        panelBotonesAccion.add(btnAsociar);
        panelBotonesAccion.add(btnListar);

        gbc.gridy = fila++;
        gbc.insets = new Insets(15, 0, 15, 0);
        panelContenido.add(panelBotonesAccion, gbc);

        // --- Área de Consola/Salida ---
        output = new JTextArea(10, 50);
        output.setFont(new Font("Monospaced", Font.PLAIN, 14));
        output.setEditable(false);
        output.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JScrollPane scroll = new JScrollPane(output);

        gbc.gridy = fila++;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        panelContenido.add(scroll, gbc);

        add(panelContenido, BorderLayout.CENTER);
        setVisible(true);
    }

    private void registrar(ActionEvent e) {
        try {
            int superficie = Integer.parseInt(txtSuperficie.getText().trim());
            Lote lote = new Lote(superficie, "disponible", 0);
            loteService.registrar(lote);
            output.append("» LOTE REGISTRADO EXITOSAMENTE:\n"
                    + "  • Superficie: " + superficie + " m² | Estado Inicial: DISPONIBLE\n"
                    + "──────────────────────────────────────────────────\n");

            txtSuperficie.setText("");
            cargarLotesDisponibles();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "La superficie ingresada debe ser exclusivamente un valor numérico entero",
                    "Error de Formato",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void asociar(ActionEvent e) {
        Lote loteSeleccionado = (Lote) comboLotesDisponibles.getSelectedItem();
        Empresa empresaSeleccionada = (Empresa) comboEmpresas.getSelectedItem();

        if (loteSeleccionado == null || empresaSeleccionada == null) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar de forma obligatoria un lote disponible y una empresa del listado.",
                    "Faltan Selecciones",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        loteService.asociarEmpresa(loteSeleccionado.getId(), empresaSeleccionada.getId());
        output.append("» ADJUDICACIÓN REGISTRADA:\n"
                + "  • Lote ID Catastral: " + loteSeleccionado.getId() + "\n"
                + "  • Empresa Adjudicataria: " + empresaSeleccionada.getNombre().toUpperCase() + "\n"
                + "──────────────────────────────────────────────────\n");

        cargarLotesDisponibles();
    }

    private void listar(ActionEvent e) {
        List<Lote> lotes = loteService.listar();
        output.setText("");
        if (lotes.isEmpty()) {
            output.setText("No existen registros catastrales de lotes en la base de datos.");
            return;
        }
        for (Lote l : lotes) {
            output.append("ID LOTE: " + l.getId() + " | Superficie: " + l.getSuperficie() + " m²\n"
                    + "  • Estado Ocupacional: " + l.getEstado().toUpperCase() + "\n");

            if (l.getEmpresaId() > 0) {
                output.append("  • ID Empresa Vinculada: " + l.getEmpresaId() + "\n");
            }
            output.append("──────────────────────────────────────────────────\n");
        }
    }

    private void cargarLotesDisponibles() {
        comboLotesDisponibles.removeAllItems();
        List<Lote> disponibles = loteService.listarDisponibles();
        for (Lote l : disponibles) {
            comboLotesDisponibles.addItem(l);
        }

        comboLotesDisponibles.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Lote lote) {
                    setText("Lote N.° " + lote.getId() + " — [" + lote.getSuperficie() + " m²]");
                }
                return this;
            }
        });
    }

    private void cargarEmpresas() {
        comboEmpresas.removeAllItems();
        List<Empresa> empresas = empresaService.listar();
        for (Empresa e : empresas) {
            comboEmpresas.addItem(e);
        }

        comboEmpresas.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Empresa empresa) {
                    setText("ID: " + empresa.getId() + " — " + empresa.getNombre());
                }
                return this;
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoteView::new);
    }
}