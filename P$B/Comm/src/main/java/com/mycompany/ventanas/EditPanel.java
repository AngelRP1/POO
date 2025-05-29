package com.mycompany.ventanas;

import com.mycompany.banco.menu;
import com.mycompany.comm.Connection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mycompany.banco.Traspaso;
import com.mycompany.ventanas.SeleccionarUsuario;
import com.mycompany.ventanas.SeleccionarClientes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EditPanel extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    private Connection banco;

    public EditPanel(Connection database) {
        this.banco = database;
        setLayout(null);
        setPreferredSize(new Dimension(1440, 900));
        initComponents();
    }

    private void initComponents() {
        // Header
        JPanel header = new JPanel();
        header.setBackground(color1);
        header.setBounds(0, 0, 1440, 130);
        header.setLayout(null);

        JLabel logo = new JLabel("P$B");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 60));
        logo.setForeground(Color.WHITE);
        logo.setBounds(40, 30, 200, 70);
        header.add(logo);
        this.add(header);

        // Títulos
        JLabel titulo = new JLabel("Panel de Edición");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 150, 1440, 50);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(titulo);

        // Panel principal con tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(50, 220, 1340, 550);
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        // Panel para editar clientes
        JPanel editarClientePanel = crearPanelEdicionCliente();
        tabbedPane.addTab("Editar Clientes", editarClientePanel);

        // Panel para editar tarjetas
        JPanel editarTarjetaPanel = crearPanelEdicionTarjeta();
        tabbedPane.addTab("Editar Tarjetas", editarTarjetaPanel);

        this.add(tabbedPane);

        // Botón menú principal
        JButton btnMenu = crearBoton("Menú Principal", new Color(10, 123, 196), 24);
        btnMenu.setBounds(80, 790, 300, 70);
        btnMenu.addActionListener(e -> {
            JFrame ventana = (JFrame) SwingUtilities.getWindowAncestor(this);
            ventana.dispose();

            if ("ADMINISTRADOR".equalsIgnoreCase(Traspaso.rango)) {
                new SeleccionarUsuario(banco).setVisible(true);
            } else {
                new SeleccionarClientes(banco).setVisible(true);
            }
        });
        this.add(btnMenu);
    }

    private JPanel crearPanelEdicionCliente() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Panel de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(Color.WHITE);

        JLabel lblBuscar = new JLabel("Buscar cliente:");
        lblBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTextField txtBuscar = new JTextField(25);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton btnBuscar = crearBoton("Buscar", color1, 14);
        btnBuscar.setPreferredSize(new Dimension(100, 30));

        JButton btnReset = crearBoton("Mostrar todos", color2, 14);
        btnReset.setPreferredSize(new Dimension(120, 30));

        searchPanel.add(lblBuscar);
        searchPanel.add(txtBuscar);
        searchPanel.add(btnBuscar);
        searchPanel.add(btnReset);

        // Modelo de tabla
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Número", "Nombre", "Teléfono", "Email"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = new JTable(model);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.setRowHeight(30);

        // Cargar clientes inicialmente
        cargarClientes(model, "");

        // Acción de búsqueda
        btnBuscar.addActionListener(e -> {
            String termino = txtBuscar.getText().trim();
            cargarClientes(model, termino);
        });

        // Acción de reset
        btnReset.addActionListener(e -> {
            txtBuscar.setText("");
            cargarClientes(model, "");
        });

        // Búsqueda al presionar Enter
        txtBuscar.addActionListener(e -> {
            String termino = txtBuscar.getText().trim();
            cargarClientes(model, termino);
        });

        // Panel de formulario de edición (inicialmente oculto)
        JPanel formularioPanel = new JPanel(null);
        formularioPanel.setBackground(Color.WHITE);
        formularioPanel.setVisible(false);
        formularioPanel.setPreferredSize(new Dimension(600, 350));

        JLabel lblNombre = crearEtiqueta("Nombre:", 50, 20, 200, 30);
        JTextField txtNombre = crearCampoTexto(260, 20, 400, 30);

        JLabel lblTelefono = crearEtiqueta("Teléfono:", 50, 70, 200, 30);
        JTextField txtTelefono = crearCampoTexto(260, 70, 400, 30);

        JLabel lblEmail = crearEtiqueta("Email:", 50, 120, 200, 30);
        JTextField txtEmail = crearCampoTexto(260, 120, 400, 30);

        JLabel lblContrasena = crearEtiqueta("Nueva Contraseña:", 50, 170, 200, 30);
        JPasswordField txtContrasena = new JPasswordField();
        txtContrasena.setBounds(260, 170, 400, 30);
        txtContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JButton btnGuardar = crearBoton("Guardar Cambios", new Color(50, 150, 50), 18);
        btnGuardar.setBounds(260, 230, 400, 40);
        btnGuardar.addActionListener(e -> {
            String numeroCliente = tabla.getValueAt(tabla.getSelectedRow(), 0).toString();
            String nombre = txtNombre.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String email = txtEmail.getText().trim();
            String contrasena = new String(txtContrasena.getPassword());

            if (nombre.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
                mostrarError("Nombre, teléfono y email son obligatorios");
                return;
            }

            boolean actualizado = actualizarCliente(numeroCliente, nombre, telefono, email, contrasena);
            if (actualizado) {
                mostrarExito("Cliente actualizado exitosamente");
                cargarClientes(model, txtBuscar.getText().trim());
                formularioPanel.setVisible(false);
            } else {
                mostrarError("Error al actualizar el cliente");
            }
        });

        JButton btnCancelar = crearBoton("Cancelar", new Color(200, 50, 50), 18);
        btnCancelar.setBounds(260, 280, 400, 40);
        btnCancelar.addActionListener(e -> formularioPanel.setVisible(false));

        formularioPanel.add(lblNombre);
        formularioPanel.add(txtNombre);
        formularioPanel.add(lblTelefono);
        formularioPanel.add(txtTelefono);
        formularioPanel.add(lblEmail);
        formularioPanel.add(txtEmail);
        formularioPanel.add(lblContrasena);
        formularioPanel.add(txtContrasena);
        formularioPanel.add(btnGuardar);
        formularioPanel.add(btnCancelar);

        // Listener para selección de cliente
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                String numero = tabla.getValueAt(tabla.getSelectedRow(), 0).toString();
                String nombre = tabla.getValueAt(tabla.getSelectedRow(), 1).toString();
                String telefono = tabla.getValueAt(tabla.getSelectedRow(), 2).toString();
                String email = tabla.getValueAt(tabla.getSelectedRow(), 3).toString();

                txtNombre.setText(nombre);
                txtTelefono.setText(telefono);
                txtEmail.setText(email);
                txtContrasena.setText("");

                formularioPanel.setVisible(true);
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);

        // Panel contenedor para tabla y formulario
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(scroll, BorderLayout.CENTER);
        contentPanel.add(formularioPanel, BorderLayout.SOUTH);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelEdicionTarjeta() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Panel de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(Color.WHITE);

        JLabel lblBuscar = new JLabel("Buscar tarjeta:");
        lblBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTextField txtBuscar = new JTextField(20);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JComboBox<String> cmbFiltro = new JComboBox<>(new String[]{"Número", "Cliente", "Tipo"});
        cmbFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton btnBuscar = crearBoton("Buscar", color1, 14);
        btnBuscar.setPreferredSize(new Dimension(100, 30));

        JButton btnReset = crearBoton("Mostrar todas", color2, 14);
        btnReset.setPreferredSize(new Dimension(120, 30));

        searchPanel.add(lblBuscar);
        searchPanel.add(txtBuscar);
        searchPanel.add(cmbFiltro);
        searchPanel.add(btnBuscar);
        searchPanel.add(btnReset);

        // Modelo de tabla
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Número", "Tipo", "Cliente", "Límite", "Saldo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = new JTable(model);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.setRowHeight(30);

        // Cargar tarjetas inicialmente
        cargarTarjetas(model, "", 0);

        // Acción de búsqueda
        btnBuscar.addActionListener(e -> {
            String termino = txtBuscar.getText().trim();
            int filtro = cmbFiltro.getSelectedIndex();
            cargarTarjetas(model, termino, filtro);
        });

        // Acción de reset
        btnReset.addActionListener(e -> {
            txtBuscar.setText("");
            cargarTarjetas(model, "", 0);
        });

        // Búsqueda al presionar Enter
        txtBuscar.addActionListener(e -> {
            String termino = txtBuscar.getText().trim();
            int filtro = cmbFiltro.getSelectedIndex();
            cargarTarjetas(model, termino, filtro);
        });

        // Panel de formulario de edición (inicialmente oculto)
        JPanel formularioPanel = new JPanel(null);
        formularioPanel.setBackground(Color.WHITE);
        formularioPanel.setVisible(false);
        formularioPanel.setPreferredSize(new Dimension(600, 300));

        JLabel lblNip = crearEtiqueta("Nuevo NIP:", 50, 20, 200, 30);
        JPasswordField txtNip = new JPasswordField();
        txtNip.setBounds(260, 20, 400, 30);
        txtNip.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel lblLimite = crearEtiqueta("Nuevo Límite:", 50, 70, 200, 30);
        JTextField txtLimite = crearCampoTexto(260, 70, 400, 30);

        JLabel lblVencimiento = crearEtiqueta("Nuevo Vencimiento:", 50, 120, 200, 30);
        JTextField txtVencimiento = crearCampoTexto(260, 120, 400, 30);

        JButton btnGuardar = crearBoton("Guardar Cambios", new Color(50, 150, 50), 18);
        btnGuardar.setBounds(260, 180, 400, 40);
        btnGuardar.addActionListener(e -> {
            String numeroTarjeta = tabla.getValueAt(tabla.getSelectedRow(), 0).toString();
            String numeroCliente = tabla.getValueAt(tabla.getSelectedRow(), 2).toString();
            String nip = new String(txtNip.getPassword());
            String vencimiento = txtVencimiento.getText().trim();
            double limite = 0;

            try {
                limite = Double.parseDouble(txtLimite.getText().trim());
            } catch (NumberFormatException ex) {
                mostrarError("El límite debe ser un número válido");
                return;
            }

            boolean actualizado = actualizarTarjeta(numeroCliente, numeroTarjeta, nip, vencimiento, limite);
            if (actualizado) {
                mostrarExito("Tarjeta actualizada exitosamente");
                cargarTarjetas(model, txtBuscar.getText().trim(), cmbFiltro.getSelectedIndex());
                formularioPanel.setVisible(false);
            } else {
                mostrarError("Error al actualizar la tarjeta");
            }
        });

        JButton btnCancelar = crearBoton("Cancelar", new Color(200, 50, 50), 18);
        btnCancelar.setBounds(260, 230, 400, 40);
        btnCancelar.addActionListener(e -> formularioPanel.setVisible(false));

        formularioPanel.add(lblNip);
        formularioPanel.add(txtNip);
        formularioPanel.add(lblLimite);
        formularioPanel.add(txtLimite);
        formularioPanel.add(lblVencimiento);
        formularioPanel.add(txtVencimiento);
        formularioPanel.add(btnGuardar);
        formularioPanel.add(btnCancelar);

        // Listener para selección de tarjeta
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                txtNip.setText("");
                txtLimite.setText(tabla.getValueAt(tabla.getSelectedRow(), 3).toString());
                txtVencimiento.setText("");
                formularioPanel.setVisible(true);
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);

        // Panel contenedor para tabla y formulario
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(scroll, BorderLayout.CENTER);
        contentPanel.add(formularioPanel, BorderLayout.SOUTH);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private void cargarClientes(DefaultTableModel model, String terminoBusqueda) {
        model.setRowCount(0);
        MongoCollection<Document> cuentas = banco.getDatabase().getCollection("cuentas");

        // Filtro base que excluye admins y subgerentes
        Bson filtroBase = Filters.and(
                Filters.or(
                        Filters.exists("rango", false),
                        Filters.not(Filters.in("rango", "admin", "subgerente"))
                )
        );

        Bson filtro;

        if (terminoBusqueda.isEmpty()) {
            filtro = filtroBase;
        } else {
            filtro = Filters.and(
                    filtroBase,
                    Filters.or(
                            Filters.regex("numero_cliente", terminoBusqueda, "i"),
                            Filters.regex("nombre", terminoBusqueda, "i"),
                            Filters.regex("telefono", terminoBusqueda, "i"),
                            Filters.regex("email", terminoBusqueda, "i")
                    )
            );
        }

        for (Document cuenta : cuentas.find(filtro)) {
            String rango = cuenta.getString("rango");
            if (rango == null || (!rango.equalsIgnoreCase("admin") && !rango.equalsIgnoreCase("subgerente"))) {
                model.addRow(new Object[]{
                        cuenta.getString("numero_cliente"),
                        cuenta.getString("nombre"),
                        cuenta.getString("telefono"),
                        cuenta.getString("email"),
                        rango != null ? rango : "cliente"
                });
            }
        }
    }

    private void cargarTarjetas(DefaultTableModel model, String terminoBusqueda, int tipoFiltro) {
        model.setRowCount(0);
        MongoCollection<Document> cuentas = banco.getDatabase().getCollection("cuentas");

        // Filtro base que excluye cuentas de admins y subgerentes
        Bson filtroBase = Filters.and(
                Filters.or(
                        Filters.exists("rango", false),
                        Filters.not(Filters.in("rango", "admin", "subgerente"))
                )
        );

        Bson filtroCuentas = filtroBase;

        if (!terminoBusqueda.isEmpty()) {
            switch (tipoFiltro) {
                case 0: // Número de tarjeta
                    filtroCuentas = Filters.and(
                            filtroBase,
                            Filters.regex("tarjetas.numero", terminoBusqueda, "i")
                    );
                    break;
                case 1: // Número de cliente
                    filtroCuentas = Filters.and(
                            filtroBase,
                            Filters.regex("numero_cliente", terminoBusqueda, "i")
                    );
                    break;
                case 2: // Tipo de tarjeta
                    filtroCuentas = Filters.and(
                            filtroBase,
                            Filters.regex("tarjetas.tipo", terminoBusqueda, "i")
                    );
                    break;
            }
        }

        for (Document cuenta : cuentas.find(filtroCuentas)) {
            String rango = cuenta.getString("rango");
            // Doble verificación para asegurar que no sean admins/subgerentes
            if (rango == null || (!rango.equalsIgnoreCase("admin") && !rango.equalsIgnoreCase("subgerente"))) {
                List<Document> tarjetas = cuenta.getList("tarjetas", Document.class, new ArrayList<>());
                for (Document tarjeta : tarjetas) {
                    // Filtrado adicional para coincidencias parciales
                    if (terminoBusqueda.isEmpty() ||
                            (tipoFiltro == 0 && tarjeta.getString("numero").toLowerCase().contains(terminoBusqueda.toLowerCase())) ||
                            (tipoFiltro == 2 && tarjeta.getString("tipo").toLowerCase().contains(terminoBusqueda.toLowerCase())) || (tipoFiltro == 1 && cuenta.getString("numero_cliente").toLowerCase().contains(terminoBusqueda.toLowerCase()))) {

                        model.addRow(new Object[]{
                                tarjeta.getString("numero"),
                                tarjeta.getString("tipo"),
                                cuenta.getString("numero_cliente"),
                                tarjeta.getDouble("limite"),
                                tarjeta.getDouble("saldo_disponible")
                        });
                    }
                }
            }
        }
    }

    private boolean actualizarCliente(String numeroCliente, String nombre, String telefono, String email, String contrasena) {
        MongoCollection<Document> cuentas = banco.getDatabase().getCollection("cuentas");
        Bson filtro = Filters.eq("numero_cliente", numeroCliente);

        try {
            List<Bson> actualizaciones = new ArrayList<>();
            actualizaciones.add(Updates.set("nombre", nombre));
            actualizaciones.add(Updates.set("telefono", telefono));
            actualizaciones.add(Updates.set("email", email));

            if (!contrasena.isEmpty()) {
                actualizaciones.add(Updates.set("contrasena", contrasena));
            }

            Bson actualizacion = Updates.combine(actualizaciones);
            cuentas.updateOne(filtro, actualizacion);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean actualizarTarjeta(String numeroCliente, String numeroTarjeta, String nip, String vencimiento, double limite) {
        MongoCollection<Document> cuentas = banco.getDatabase().getCollection("cuentas");
        Bson filtro = Filters.and(
                Filters.eq("numero_cliente", numeroCliente),
                Filters.eq("tarjetas.numero", numeroTarjeta)
        );

        try {
            List<Bson> actualizaciones = new ArrayList<>();

            if (!nip.isEmpty()) {
                actualizaciones.add(Updates.set("tarjetas.$.nip", nip));
            }

            if (!vencimiento.isEmpty()) {
                actualizaciones.add(Updates.set("tarjetas.$.fecha_vencimiento", vencimiento));
            }

            if (limite > 0) {
                actualizaciones.add(Updates.set("tarjetas.$.limite", limite));
            }

            if (!actualizaciones.isEmpty()) {
                Bson actualizacion = Updates.combine(actualizaciones);
                cuentas.updateOne(filtro, actualizacion);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Métodos auxiliares
    private JLabel crearEtiqueta(String texto, int x, int y, int w, int h) {
        JLabel label = new JLabel(texto);
        label.setBounds(x, y, w, h);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return label;
    }

    private JTextField crearCampoTexto(int x, int y, int w, int h) {
        JTextField campo = new JTextField();
        campo.setBounds(x, y, w, h);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return campo;
    }

    private JButton crearBoton(String texto, Color color, int tamañoFuente) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, tamañoFuente));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return boton;
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, color1, w, 0, color2);
        g2D.setPaint(gp);
        g2D.fillRect(0, 0, w, h);
    }
}