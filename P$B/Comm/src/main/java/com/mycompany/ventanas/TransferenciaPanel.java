package com.mycompany.ventanas;

import com.mycompany.banco.menu;
import com.mycompany.comm.Connection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.mycompany.banco.Traspaso;
import com.mycompany.ventanas.SeleccionarUsuario;
import com.mycompany.ventanas.SeleccionarClientes;

public class TransferenciaPanel extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    private Connection banco;

    public TransferenciaPanel(Connection database) {
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
        JLabel titulo = new JLabel("Transferencias Bancarias");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 150, 1440, 50);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(titulo);

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        panel.setBounds(420, 220, 600, 500);

        // Selector de tipo de transferencia
        JLabel lblTipo = new JLabel("Tipo de Transferencia:");
        lblTipo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblTipo.setBounds(50, 30, 200, 30);
        panel.add(lblTipo);

        JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"Cuenta a Cuenta", "Tarjeta a Cuenta", "Cuenta a Tarjeta", "Tarjeta a Tarjeta"});
        cmbTipo.setBounds(260, 30, 290, 30);
        cmbTipo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(cmbTipo);

        // Campos de origen
        JLabel lblOrigen = new JLabel("Origen:");
        lblOrigen.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblOrigen.setBounds(50, 80, 200, 30);
        panel.add(lblOrigen);

        JTextField txtOrigen = new JTextField();
        txtOrigen.setBounds(260, 80, 290, 30);
        txtOrigen.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(txtOrigen);

        // Campos de destino
        JLabel lblDestino = new JLabel("Destino:");
        lblDestino.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblDestino.setBounds(50, 130, 200, 30);
        panel.add(lblDestino);

        JTextField txtDestino = new JTextField();
        txtDestino.setBounds(260, 130, 290, 30);
        txtDestino.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(txtDestino);

        // Monto
        JLabel lblMonto = new JLabel("Monto:");
        lblMonto.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblMonto.setBounds(50, 180, 200, 30);
        panel.add(lblMonto);

        JTextField txtMonto = new JTextField();
        txtMonto.setBounds(260, 180, 290, 30);
        txtMonto.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(txtMonto);

        // Concepto
        JLabel lblConcepto = new JLabel("Concepto:");
        lblConcepto.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblConcepto.setBounds(50, 230, 200, 30);
        panel.add(lblConcepto);

        JTextField txtConcepto = new JTextField();
        txtConcepto.setBounds(260, 230, 290, 30);
        txtConcepto.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(txtConcepto);

        // Botón de transferir
        JButton btnTransferir = crearBoton("Realizar Transferencia", new Color(50, 150, 50), 20);
        btnTransferir.setBounds(150, 300, 300, 50);
        btnTransferir.addActionListener(e -> realizarTransferencia(
                cmbTipo.getSelectedIndex(),
                txtOrigen.getText().trim(),
                txtDestino.getText().trim(),
                txtMonto.getText().trim(),
                txtConcepto.getText().trim()
        ));
        panel.add(btnTransferir);

        // Botón de verificar
        JButton btnVerificar = crearBoton("Verificar Datos", color1, 16);
        btnVerificar.setBounds(150, 360, 300, 40);
        btnVerificar.addActionListener(e -> verificarDatos(
                cmbTipo.getSelectedIndex(),
                txtOrigen.getText().trim(),
                txtDestino.getText().trim()
        ));
        panel.add(btnVerificar);

        this.add(panel);

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

    private void realizarTransferencia(int tipo, String origen, String destino, String montoStr, String concepto) {
        try {
            double monto = Double.parseDouble(montoStr);
            if (monto <= 0) {
                mostrarError("El monto debe ser mayor que cero");
                return;
            }

            boolean exito = false;
            String mensaje = "";

            switch (tipo) {
                case 0: // Cuenta a Cuenta
                    exito = transferirEntreCuentas(origen, destino, monto, concepto);
                    mensaje = exito ? "Transferencia entre cuentas exitosa" : "Error en transferencia entre cuentas";
                    break;
                case 1: // Tarjeta a Cuenta
                    exito = transferirDesdeTarjeta(origen, destino, monto, concepto);
                    mensaje = exito ? "Transferencia desde tarjeta exitosa" : "Error en transferencia desde tarjeta";
                    break;
                case 2: // Cuenta a Tarjeta
                    exito = transferirATarjeta(origen, destino, monto, concepto);
                    mensaje = exito ? "Transferencia a tarjeta exitosa" : "Error en transferencia a tarjeta";
                    break;
                case 3: // Tarjeta a Tarjeta
                    exito = transferirEntreTarjetas(origen, destino, monto, concepto);
                    mensaje = exito ? "Transferencia entre tarjetas exitosa" : "Error en transferencia entre tarjetas";
                    break;
            }

            if (exito) {
                mostrarExito(mensaje);
                registrarAccion(origen, destino, monto, concepto, tipo);
            } else {
                mostrarError(mensaje + ". Verifique los fondos y los datos.");
            }

        } catch (NumberFormatException e) {
            mostrarError("El monto debe ser un número válido");
        }
    }

    private boolean transferirEntreCuentas(String cuentaOrigen, String cuentaDestino, double monto, String concepto) {
        // Verificar que ambas cuentas existen
        if (!banco.validarNumeroCuenta(cuentaOrigen) || !banco.validarNumeroCuenta(cuentaDestino)) {
            return false;
        }

        // Realizar retiro y depósito
        boolean retiroExitoso = banco.retirarDeCuenta(cuentaOrigen, monto, "Transferencia a cuenta " + cuentaDestino);
        boolean depositoExitoso = banco.depositarACuenta(cuentaDestino, monto, "Transferencia desde cuenta " + cuentaOrigen);

        return retiroExitoso && depositoExitoso;
    }

    private boolean transferirDesdeTarjeta(String numeroTarjeta, String cuentaDestino, double monto, String concepto) {
        // Verificar que la tarjeta existe y es de débito
        Document infoTarjeta = banco.obtenerInformacionCompletaTarjeta(numeroTarjeta);
        if (infoTarjeta == null || !"débito".equalsIgnoreCase(infoTarjeta.getString("tipo"))) {
            return false;
        }

        // Verificar que la cuenta destino existe
        if (!banco.validarNumeroCuenta(cuentaDestino)) {
            return false;
        }

        // Realizar retiro y depósito
        boolean retiroExitoso = banco.retirarDeTarjeta(numeroTarjeta, monto, "Transferencia a cuenta " + cuentaDestino);
        boolean depositoExitoso = banco.depositarACuenta(cuentaDestino, monto, "Transferencia desde tarjeta " + numeroTarjeta);

        return retiroExitoso && depositoExitoso;
    }

    private boolean transferirATarjeta(String cuentaOrigen, String numeroTarjeta, double monto, String concepto) {
        // Verificar que la cuenta origen existe y tiene fondos
        if (!banco.validarNumeroCuenta(cuentaOrigen)) {
            return false;
        }

        // Verificar que la tarjeta existe
        Document infoTarjeta = banco.obtenerInformacionCompletaTarjeta(numeroTarjeta);
        if (infoTarjeta == null) {
            return false;
        }

        // Realizar retiro y depósito según tipo de tarjeta
        boolean retiroExitoso = banco.retirarDeCuenta(cuentaOrigen, monto, "Transferencia a tarjeta " + numeroTarjeta);
        boolean depositoExitoso;

        if ("débito".equalsIgnoreCase(infoTarjeta.getString("tipo"))) {
            depositoExitoso = banco.depositarATarjetaDebito(numeroTarjeta, monto, "Transferencia desde cuenta " + cuentaOrigen);
        } else {
            depositoExitoso = banco.abonarATarjetaCredito(numeroTarjeta, monto, "Transferencia desde cuenta " + cuentaOrigen);
        }

        return retiroExitoso && depositoExitoso;
    }

    private boolean transferirEntreTarjetas(String tarjetaOrigen, String tarjetaDestino, double monto, String concepto) {
        // Verificar que la tarjeta origen es de débito y tiene fondos
        Document infoTarjetaOrigen = banco.obtenerInformacionCompletaTarjeta(tarjetaOrigen);
        if (infoTarjetaOrigen == null || !"débito".equalsIgnoreCase(infoTarjetaOrigen.getString("tipo"))) {
            return false;
        }

        // Verificar que la tarjeta destino existe
        Document infoTarjetaDestino = banco.obtenerInformacionCompletaTarjeta(tarjetaDestino);
        if (infoTarjetaDestino == null) {
            return false;
        }

        // Realizar retiro y depósito según tipo de tarjeta destino
        boolean retiroExitoso = banco.retirarDeTarjeta(tarjetaOrigen, monto, "Transferencia a tarjeta " + tarjetaDestino);
        boolean depositoExitoso;

        if ("débito".equalsIgnoreCase(infoTarjetaDestino.getString("tipo"))) {
            depositoExitoso = banco.depositarATarjetaDebito(tarjetaDestino, monto, "Transferencia desde tarjeta " + tarjetaOrigen);
        } else {
            depositoExitoso = banco.abonarATarjetaCredito(tarjetaDestino, monto, "Transferencia desde tarjeta " + tarjetaOrigen);
        }

        return retiroExitoso && depositoExitoso;
    }

    private void verificarDatos(int tipo, String origen, String destino) {
        String mensaje = "";
        boolean datosValidos = false;

        switch (tipo) {
            case 0: // Cuenta a Cuenta
                datosValidos = banco.validarNumeroCuenta(origen) && banco.validarNumeroCuenta(destino);
                mensaje = datosValidos ? "Cuentas válidas" : "Una o ambas cuentas no son válidas";
                break;
            case 1: // Tarjeta a Cuenta
                datosValidos = banco.validarNumeroTarjeta(origen) && banco.validarNumeroCuenta(destino);
                mensaje = datosValidos ? "Tarjeta y cuenta válidas" : "Tarjeta o cuenta no válidas";
                break;
            case 2: // Cuenta a Tarjeta
                datosValidos = banco.validarNumeroCuenta(origen) && banco.validarNumeroTarjeta(destino);
                mensaje = datosValidos ? "Cuenta y tarjeta válidas" : "Cuenta o tarjeta no válidas";
                break;
            case 3: // Tarjeta a Tarjeta
                datosValidos = banco.validarNumeroTarjeta(origen) && banco.validarNumeroTarjeta(destino);
                mensaje = datosValidos ? "Tarjetas válidas" : "Una o ambas tarjetas no son válidas";
                break;
        }

        if (datosValidos) {
            mostrarInformacion(mensaje);
        } else {
            mostrarError(mensaje);
        }
    }

    private void registrarAccion(String origen, String destino, double monto, String concepto, int tipo) {
        MongoCollection<Document> acciones = banco.getDatabase().getCollection("acciones");

        String tipoTransferencia = "";
        switch (tipo) {
            case 0: tipoTransferencia = "cuenta_a_cuenta"; break;
            case 1: tipoTransferencia = "tarjeta_a_cuenta"; break;
            case 2: tipoTransferencia = "cuenta_a_tarjeta"; break;
            case 3: tipoTransferencia = "tarjeta_a_tarjeta"; break;
        }

        Document accion = new Document()
                .append("tipo", "transferencia")
                .append("subtipo", tipoTransferencia)
                .append("fecha", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .append("origen", origen)
                .append("destino", destino)
                .append("monto", monto)
                .append("concepto", concepto)
                .append("estado", "completada");

        acciones.insertOne(accion);
    }

    // Métodos auxiliares de UI
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

    private void mostrarInformacion(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
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