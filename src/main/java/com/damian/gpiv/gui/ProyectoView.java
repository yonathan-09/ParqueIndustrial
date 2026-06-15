package com.damian.gpiv.gui;

import com.damian.gpiv.models.Empresa;
import com.damian.gpiv.models.Proyecto;
import com.damian.gpiv.services.EmpresaService;
import com.damian.gpiv.services.ProyectoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProyectoView extends JFrame {
    private final ProyectoService service;
    private final int solicitudId;
    private final String rolUsuario;
    private JTextField txtNombre;
    private JTextField txtDesc;
    private JComboBox<Empresa> comboEmpresas;
    private List<File> archivosAdjuntos = new ArrayList<>();
    private EmpresaService empresaService = new EmpresaService();
    private JList<Proyecto> listaProyectos;
    private DefaultListModel<Proyecto> modeloProyectos = new DefaultListModel<>();
    private JTextField txtSuperficieLote;

    public ProyectoView(int solicitudId, String rolUsuario) {
        super("Gestión de Proyectos - GPIV");
        this.service = new ProyectoService();
        this.solicitudId = solicitudId;
        this.rolUsuario = rolUsuario;

        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        Color verdeFoto = new Color(93, 203, 82);

        JPanel panelIzquierdoVerde = new JPanel();
        panelIzquierdoVerde.setBackground(verdeFoto);
        panelIzquierdoVerde.setPreferredSize(new Dimension(150, 650));
        add(panelIzquierdoVerde, BorderLayout.WEST);

        JPanel panelContenido = new JPanel(new GridBagLayout());
        panelContenido.setBackground(Color.WHITE);
        panelContenido.setBorder(new EmptyBorder(25, 40, 25, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        int fila = 0;

        JLabel lblTitulo = new JLabel("Administración de Proyectos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(40, 45, 50));
        gbc.gridy = fila++;
        panelContenido.add(lblTitulo, gbc);

        gbc.gridy = fila++;
        gbc.insets = new Insets(0, 0, 15, 0);
        panelContenido.add(new JSeparator(), gbc);
        gbc.insets = new Insets(5, 0, 5, 0); // Reset

        JLabel lblNombre = new JLabel("Nombre del Proyecto");
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        lblNombre.setForeground(new Color(80, 80, 80));
        gbc.gridy = fila++;
        panelContenido.add(lblNombre, gbc);

        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        txtNombre.setPreferredSize(new Dimension(300, 35));
        gbc.gridy = fila++;
        panelContenido.add(txtNombre, gbc);

        JLabel lblDesc = new JLabel("Descripción Breve");
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 16));
        lblDesc.setForeground(new Color(80, 80, 80));
        gbc.gridy = fila++;
        panelContenido.add(lblDesc, gbc);

        txtDesc = new JTextField();
        txtDesc.setFont(new Font("Arial", Font.PLAIN, 16));
        txtDesc.setPreferredSize(new Dimension(300, 35));
        gbc.gridy = fila++;
        panelContenido.add(txtDesc, gbc);

        JLabel lblSuperficie = new JLabel("Superficie de lote requerida (m²)");
        lblSuperficie.setFont(new Font("Arial", Font.PLAIN, 16));

        gbc.gridy = fila++;
        panelContenido.add(lblSuperficie, gbc);

        txtSuperficieLote = new JTextField();
        txtSuperficieLote.setFont(new Font("Arial", Font.PLAIN, 16));

        gbc.gridy = fila++;
        panelContenido.add(txtSuperficieLote, gbc);

        if ("administrador".equalsIgnoreCase(rolUsuario)) {
            JLabel lblEmpresa = new JLabel("Empresa Asociada");
            lblEmpresa.setFont(new Font("Arial", Font.PLAIN, 16));
            lblEmpresa.setForeground(new Color(80, 80, 80));
            gbc.gridy = fila++;
            panelContenido.add(lblEmpresa, gbc);

            comboEmpresas = new JComboBox<>();
            comboEmpresas.setFont(new Font("Arial", Font.PLAIN, 16));
            comboEmpresas.setPreferredSize(new Dimension(300, 35));

            List<Empresa> empresas = empresaService.listar();
            for (Empresa emp : empresas) {
                comboEmpresas.addItem(emp);
            }

            gbc.gridy = fila++;
            panelContenido.add(comboEmpresas, gbc);
        }

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 20, 0));
        panelBotones.setBackground(Color.WHITE);

        JButton btnRegistrar = new JButton("Registrar Solicitud");
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 16));
        btnRegistrar.setBackground(verdeFoto);
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setPreferredSize(new Dimension(150, 45));
        btnRegistrar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistrar.addActionListener(this::registrar);



        panelBotones.add(btnRegistrar);


        gbc.gridy = fila++;
        gbc.insets = new Insets(15, 0, 15, 0);
        panelContenido.add(panelBotones, gbc);


        JScrollPane scroll = new JScrollPane(listaProyectos);

        gbc.gridy = fila++;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        panelContenido.add(scroll, gbc);


        add(panelContenido, BorderLayout.CENTER);
        setVisible(true);
    }

    public ProyectoView() {
        this(0, "administrador");
    }


    private void registrar(ActionEvent e) {
        int idEmpresaAsociada = solicitudId;

        if ("administrador".equalsIgnoreCase(rolUsuario)) {
            Empresa empresaSeleccionada = (Empresa) comboEmpresas.getSelectedItem();
            if (empresaSeleccionada == null) {
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar una empresa válida",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            idEmpresaAsociada = empresaSeleccionada.getId();
        }

        double superficieLote;

        try {

            superficieLote =
                    Double.parseDouble(txtSuperficieLote.getText().trim());

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese una superficie válida",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }
        System.out.println("Solicitud ID: " + solicitudId);
        System.out.println("Empresa ID: " + idEmpresaAsociada);

        Proyecto proyecto;

        if ("empresa".equalsIgnoreCase(rolUsuario)) {

            proyecto = new Proyecto(
                    0,
                    txtNombre.getText().trim(),
                    txtDesc.getText().trim(),
                    "pendiente",
                    null,          // empresa_id
                    solicitudId,   // solicitud_id
                    superficieLote
            );

        } else {

            proyecto = new Proyecto(
                    0,
                    txtNombre.getText().trim(),
                    txtDesc.getText().trim(),
                    "pendiente",
                    idEmpresaAsociada,
                    null,
                    superficieLote
            );
        }

        service.registrarConArchivos(proyecto, archivosAdjuntos);

        dispose();
        SwingUtilities.invokeLater(LoginView::new);
    }


    private void listar(ActionEvent e) {
        modeloProyectos.clear();
        List<Proyecto> proyectos;

        if ("empresa".equalsIgnoreCase(rolUsuario)) {
            proyectos = service.listarPorEmpresa(solicitudId);
        } else {
            proyectos = service.listar();
        }

        if (proyectos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No se encontraron proyectos guardados en el sistema.",
                    "Aviso",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (Proyecto p : proyectos) {
            modeloProyectos.addElement(p);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProyectoView(0, "administrador"));
    }
}

