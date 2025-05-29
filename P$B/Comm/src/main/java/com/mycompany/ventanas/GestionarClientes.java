package com.mycompany.ventanas;

import com.mycompany.banco.menu;
import com.mycompany.comm.Connection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mycompany.banco.Traspaso;
import com.mycompany.ventanas.SeleccionarUsuario;
import com.mycompany.ventanas.SeleccionarClientes;

import org.bson.Document;
import org.bson.conversions.Bson;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GestionarClientes extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    private Connection banco;

    public GestionarClientes(Connection database) {
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
        JLabel titulo = new JLabel("Panel de Eliminación");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 150, 1440, 50);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(titulo);

        // Panel principal con tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(50, 220, 1340, 550);
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        // Panel para eliminar clientes
        JPanel eliminarClientePanel = crearPanelEliminacionCliente();
        tabbedPane.addTab("Eliminar Clientes", eliminarClientePanel);

        // Panel para eliminar tarjetas
        JPanel eliminarTarjetaPanel = crearPanelEliminacionTarjeta();
        tabbedPane.addTab("Eliminar Tarjetas", eliminarTarjetaPanel);

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

    private JPanel crearPanelEliminacionCliente() {
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

        // Panel de confirmación de eliminación
        JPanel confirmacionPanel = new JPanel(new BorderLayout());
        confirmacionPanel.setBackground(Color.WHITE);
        confirmacionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblConfirmacion = new JLabel("¿Está seguro de eliminar este cliente?");
        lblConfirmacion.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblConfirmacion.setHorizontalAlignment(SwingConstants.CENTER);
        confirmacionPanel.add(lblConfirmacion, BorderLayout.CENTER);

        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        botonesPanel.setBackground(Color.WHITE);

        JButton btnEliminar = crearBoton("Confirmar Eliminación", new Color(200, 50, 50), 16);
        btnEliminar.addActionListener(e -> {
            int filaSeleccionada = tabla.getSelectedRow();
            if (filaSeleccionada >= 0) {
                String numeroCliente = model.getValueAt(filaSeleccionada, 0).toString();
                boolean eliminado = eliminarCliente(numeroCliente);
                if (eliminado) {
                    mostrarExito("Cliente eliminado exitosamente");
                    cargarClientes(model, txtBuscar.getText().trim());
                } else {
                    mostrarError("Error al eliminar el cliente");
                }
            }
        });

        JButton btnCancelar = crearBoton("Cancelar", color1, 16);
        btnCancelar.addActionListener(e -> confirmacionPanel.setVisible(false));

        botonesPanel.add(btnEliminar);
        botonesPanel.add(btnCancelar);
        confirmacionPanel.add(botonesPanel, BorderLayout.SOUTH);
        confirmacionPanel.setVisible(false);

        // Listener para selección de cliente
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                confirmacionPanel.setVisible(true);
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);

        // Panel contenedor para tabla y panel de confirmación
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(scroll, BorderLayout.CENTER);
        contentPanel.add(confirmacionPanel, BorderLayout.SOUTH);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelEliminacionTarjeta() {
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

        // Panel de confirmación de eliminación
        JPanel confirmacionPanel = new JPanel(new BorderLayout());
        confirmacionPanel.setBackground(Color.WHITE);
        confirmacionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblConfirmacion = new JLabel("¿Está seguro de eliminar esta tarjeta?");
        lblConfirmacion.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblConfirmacion.setHorizontalAlignment(SwingConstants.CENTER);
        confirmacionPanel.add(lblConfirmacion, BorderLayout.CENTER);

        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        botonesPanel.setBackground(Color.WHITE);

        JButton btnEliminar = crearBoton("Confirmar Eliminación", new Color(200, 50, 50), 16);
        btnEliminar.addActionListener(e -> {
            int filaSeleccionada = tabla.getSelectedRow();
            if (filaSeleccionada >= 0) {
                String numeroTarjeta = model.getValueAt(filaSeleccionada, 0).toString();
                String numeroCliente = model.getValueAt(filaSeleccionada, 2).toString();
                boolean eliminado = eliminarTarjeta(numeroCliente, numeroTarjeta);
                if (eliminado) {
                    mostrarExito("Tarjeta eliminada exitosamente");
                    cargarTarjetas(model, txtBuscar.getText().trim(), cmbFiltro.getSelectedIndex());
                } else {
                    mostrarError("Error al eliminar la tarjeta");
                }
            }
        });

        JButton btnCancelar = crearBoton("Cancelar", color1, 16);
        btnCancelar.addActionListener(e -> confirmacionPanel.setVisible(false));

        botonesPanel.add(btnEliminar);
        botonesPanel.add(btnCancelar);
        confirmacionPanel.add(botonesPanel, BorderLayout.SOUTH);
        confirmacionPanel.setVisible(false);

        // Listener para selección de tarjeta
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                confirmacionPanel.setVisible(true);
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);

        // Panel contenedor para tabla y panel de confirmación
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(scroll, BorderLayout.CENTER);
        contentPanel.add(confirmacionPanel, BorderLayout.SOUTH);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private void cargarClientes(DefaultTableModel model, String terminoBusqueda) {
        model.setRowCount(0);
        MongoCollection<Document> cuentas = banco.getDatabase().getCollection("cuentas");

        Bson filtroBase = Filters.and(
                Filters.or(
                        Filters.exists("rango", false),
                        Filters.not(Filters.in("rango", "admin", "subgerente"))
                ));

        Bson filtro = terminoBusqueda.isEmpty() ? filtroBase : Filters.and(
                filtroBase,
                Filters.or(
                        Filters.regex("numero_cliente", terminoBusqueda, "i"),
                        Filters.regex("nombre", terminoBusqueda, "i"),
                        Filters.regex("telefono", terminoBusqueda, "i"),
                        Filters.regex("email", terminoBusqueda, "i")
                )
        );

        for (Document cuenta : cuentas.find(filtro)) {
            String rango = cuenta.getString("rango");
            if (esClienteValido(rango)) {
                model.addRow(new Object[]{
                        cuenta.getString("numero_cliente"),
                        cuenta.getString("nombre"),
                        cuenta.getString("telefono"),
                        cuenta.getString("email")
                });
            }
        }
    }

    private void cargarTarjetas(DefaultTableModel model, String terminoBusqueda, int tipoFiltro) {
        model.setRowCount(0);
        MongoCollection<Document> cuentas = banco.getDatabase().getCollection("cuentas");

        Bson filtroBase = Filters.and(
                Filters.or(
                        Filters.exists("rango", false),
                        Filters.not(Filters.in("rango", "admin", "subgerente"))
                )
        );

        Bson filtroCuentas = filtroBase;

        if (!terminoBusqueda.isEmpty()) {
            switch (tipoFiltro) {
                case 0:
                    filtroCuentas = Filters.and(filtroBase, Filters.regex("tarjetas.numero", terminoBusqueda, "i"));
                    break;
                case 1:
                    filtroCuentas = Filters.and(filtroBase, Filters.regex("numero_cliente", terminoBusqueda, "i"));
                    break;
                case 2:
                    filtroCuentas = Filters.and(filtroBase, Filters.regex("tarjetas.tipo", terminoBusqueda, "i"));
                    break;
            }
        }

        for (Document cuenta : cuentas.find(filtroCuentas)) {
            String rango = cuenta.getString("rango");
            if (esClienteValido(rango)) {
                List<Document> tarjetas = cuenta.getList("tarjetas", Document.class, new ArrayList<>());
                for (Document tarjeta : tarjetas) {
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

    private boolean eliminarCliente(String numeroCliente) {
        MongoCollection<Document> cuentas = banco.getDatabase().getCollection("cuentas");
        try {
            cuentas.deleteOne(Filters.eq("numero_cliente", numeroCliente));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean eliminarTarjeta(String numeroCliente, String numeroTarjeta) {
        MongoCollection<Document> cuentas = banco.getDatabase().getCollection("cuentas");
        try {
            Bson actualizacion = Updates.pull("tarjetas", Filters.eq("numero", numeroTarjeta));
            cuentas.updateOne(Filters.eq("numero_cliente", numeroCliente), actualizacion);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean esClienteValido(String rango) {
        return rango == null || (!rango.equalsIgnoreCase("admin") && !rango.equalsIgnoreCase("subgerente"));
    }

    private boolean terminoCoincide(String terminoBusqueda, int tipoFiltro, String... valores) {
        if (terminoBusqueda.isEmpty()) return true;
        String valor = valores[tipoFiltro];
        return valor != null && valor.toLowerCase().contains(terminoBusqueda.toLowerCase());
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