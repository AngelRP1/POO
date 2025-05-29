package com.mycompany.ventanas;

import com.mycompany.banco.menu;
import com.mycompany.comm.Connection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import com.mycompany.banco.Traspaso;
import com.mycompany.ventanas.SeleccionarUsuario;
import com.mycompany.ventanas.SeleccionarClientes;

public class CreatePanel extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    private Connection banco;

    public CreatePanel(Connection database) {
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
        JLabel titulo = new JLabel("Panel de Creación");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 150, 1440, 50);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(titulo);

        // Panel principal con tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(50, 220, 1340, 550);
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        // Panel para crear clientes
        JPanel crearClientePanel = crearFormularioCliente();
        tabbedPane.addTab("Nuevo Cliente", crearClientePanel);

        // Panel para crear tarjetas
        JPanel crearTarjetaPanel = crearFormularioTarjeta();
        tabbedPane.addTab("Nueva Tarjeta", crearTarjetaPanel);

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

    private JPanel crearFormularioCliente() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        // Campos del formulario
        JLabel lblNombre = crearEtiqueta("Nombre completo:", 50, 50, 200, 30);
        JTextField txtNombre = crearCampoTexto(260, 50, 400, 30);

        JLabel lblTelefono = crearEtiqueta("Teléfono:", 50, 100, 200, 30);
        JTextField txtTelefono = crearCampoTexto(260, 100, 400, 30);

        JLabel lblEmail = crearEtiqueta("Email:", 50, 150, 200, 30);
        JTextField txtEmail = crearCampoTexto(260, 150, 400, 30);

        JLabel lblContrasena = crearEtiqueta("Contraseña:", 50, 200, 200, 30);
        JPasswordField txtContrasena = new JPasswordField();
        txtContrasena.setBounds(260, 200, 400, 30);
        txtContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel lblRfcCurp = crearEtiqueta("RFC/CURP:", 50, 250, 200, 30);
        JTextField txtRfcCurp = crearCampoTexto(260, 250, 400, 30);

        // Botón de crear
        JButton btnCrear = crearBoton("Crear Cliente", new Color(50, 150, 50), 20);
        btnCrear.setBounds(260, 320, 400, 50);
        btnCrear.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String email = txtEmail.getText().trim();
            String contrasena = new String(txtContrasena.getPassword());
            String rfcCurp = txtRfcCurp.getText().trim();

            if (nombre.isEmpty() || telefono.isEmpty() || email.isEmpty() || contrasena.isEmpty()) {
                mostrarError("Todos los campos son obligatorios");
                return;
            }

            boolean creado = banco.crearCuenta(nombre, rfcCurp, telefono, email, contrasena);
            if (creado) {
                mostrarExito("Cliente creado exitosamente");
                limpiarCampos(txtNombre, txtTelefono, txtEmail, txtContrasena, txtRfcCurp);
            } else {
                mostrarError("Error al crear el cliente");
            }
        });

        // Agregar componentes al panel
        panel.add(lblNombre);
        panel.add(txtNombre);
        panel.add(lblTelefono);
        panel.add(txtTelefono);
        panel.add(lblEmail);
        panel.add(txtEmail);
        panel.add(lblContrasena);
        panel.add(txtContrasena);
        panel.add(lblRfcCurp);
        panel.add(txtRfcCurp);
        panel.add(btnCrear);

        return panel;
    }

    private JPanel crearFormularioTarjeta() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        // Campos del formulario
        JLabel lblCliente = crearEtiqueta("Número de cliente:", 50, 50, 200, 30);
        JTextField txtCliente = crearCampoTexto(260, 50, 400, 30);

        JLabel lblTipo = crearEtiqueta("Tipo de tarjeta:", 50, 100, 200, 30);
        JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"Débito", "Crédito"});
        cmbTipo.setBounds(260, 100, 400, 30);
        cmbTipo.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel lblNumero = crearEtiqueta("Número de tarjeta:", 50, 150, 200, 30);
        JTextField txtNumero = crearCampoTexto(260, 150, 400, 30);

        JLabel lblNip = crearEtiqueta("NIP:", 50, 200, 200, 30);
        JPasswordField txtNip = new JPasswordField();
        txtNip.setBounds(260, 200, 400, 30);
        txtNip.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel lblVencimiento = crearEtiqueta("Fecha vencimiento (MM/YY):", 50, 250, 200, 30);
        JTextField txtVencimiento = crearCampoTexto(260, 250, 400, 30);

        JLabel lblLimite = crearEtiqueta("Límite de crédito:", 50, 300, 200, 30);
        JTextField txtLimite = crearCampoTexto(260, 300, 400, 30);

        // Botón de crear
        JButton btnCrear = crearBoton("Crear Tarjeta", new Color(50, 150, 50), 20);
        btnCrear.setBounds(260, 370, 400, 50);
        btnCrear.addActionListener(e -> {
            String numeroCliente = txtCliente.getText().trim();
            String tipo = cmbTipo.getSelectedItem().toString();
            String numeroTarjeta = txtNumero.getText().trim();
            String nip = new String(txtNip.getPassword());
            String vencimiento = txtVencimiento.getText().trim();
            double limite = 0;

            try {
                limite = Double.parseDouble(txtLimite.getText().trim());
            } catch (NumberFormatException ex) {
                mostrarError("El límite debe ser un número válido");
                return;
            }

            if (numeroCliente.isEmpty() || numeroTarjeta.isEmpty() || nip.isEmpty() || vencimiento.isEmpty()) {
                mostrarError("Todos los campos son obligatorios");
                return;
            }

            boolean creado = banco.agregarTarjeta(numeroCliente, tipo, numeroTarjeta, nip, vencimiento, limite);
            if (creado) {
                mostrarExito("Tarjeta creada exitosamente");
                limpiarCampos(txtCliente, txtNumero, txtNip, txtVencimiento, txtLimite);
            } else {
                mostrarError("Error al crear la tarjeta");
            }
        });

        // Agregar componentes al panel
        panel.add(lblCliente);
        panel.add(txtCliente);
        panel.add(lblTipo);
        panel.add(cmbTipo);
        panel.add(lblNumero);
        panel.add(txtNumero);
        panel.add(lblNip);
        panel.add(txtNip);
        panel.add(lblVencimiento);
        panel.add(txtVencimiento);
        panel.add(lblLimite);
        panel.add(txtLimite);
        panel.add(btnCrear);

        return panel;
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

    private void limpiarCampos(JComponent... componentes) {
        for (JComponent componente : componentes) {
            if (componente instanceof JTextField) {
                ((JTextField) componente).setText("");
            } else if (componente instanceof JPasswordField) {
                ((JPasswordField) componente).setText("");
            }
        }
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