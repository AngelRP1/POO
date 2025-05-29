package com.mycompany.ventanas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.mycompany.comm.Connection;
import com.mycompany.banco.Traspaso;
public class OperacionFrame extends JFrame {
    private JComboBox<String> comboOperacion;
    private JComboBox<String> comboDestino;
    private JTextField campoNumero;
    private JTextField campoMonto;
    private JTextField campoConcepto;
    private JButton botonConfirmar;
    private Connection conexion;

    public OperacionFrame(Connection conexion) {
        this.conexion = conexion;
        setTitle("Operación Bancaria");
        setSize(1440, 900);  // Adjusted size for a larger screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Header with logo
        JPanel header = new JPanel();
        header.setBackground(new Color(26, 46, 84));  // Dark blue
        header.setBounds(0, 0, 1440, 130);
        header.setLayout(null);
        JLabel logo = new JLabel("P$B");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 60));
        logo.setForeground(Color.WHITE);
        logo.setBounds(40, 30, 200, 70);
        header.add(logo);
        this.add(header);

        // Title
        JLabel titulo = new JLabel("Operación Bancaria");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titulo.setForeground(new Color(26, 46, 84));
        titulo.setBounds(0, 150, 1440, 50);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(titulo);

        // Main panel
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        panel.setBounds(420, 220, 600, 500);

        // Operation Type Selection
        JLabel lblOperacion = new JLabel("Tipo de Operación:");
        lblOperacion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblOperacion.setBounds(50, 30, 200, 30);
        panel.add(lblOperacion);

        comboOperacion = new JComboBox<>(new String[]{"Depósito", "Retiro"});
        comboOperacion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        comboOperacion.setBounds(260, 30, 290, 30);
        panel.add(comboOperacion);

        // Destination Selection
        JLabel lblDestino = new JLabel("Destino:");
        lblDestino.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblDestino.setBounds(50, 80, 200, 30);
        panel.add(lblDestino);

        comboDestino = new JComboBox<>(new String[]{"Cuenta", "Tarjeta Débito", "Tarjeta Crédito"});
        comboDestino.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        comboDestino.setBounds(260, 80, 290, 30);
        panel.add(comboDestino);

        // Number Field
        JLabel lblNumero = new JLabel("Número (cliente o tarjeta):");
        lblNumero.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblNumero.setBounds(50, 130, 200, 30);
        panel.add(lblNumero);

        campoNumero = new JTextField();
        campoNumero.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campoNumero.setBounds(260, 130, 290, 30);
        panel.add(campoNumero);

        // Amount Field
        JLabel lblMonto = new JLabel("Monto:");
        lblMonto.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblMonto.setBounds(50, 180, 200, 30);
        panel.add(lblMonto);

        campoMonto = new JTextField();
        campoMonto.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campoMonto.setBounds(260, 180, 290, 30);
        panel.add(campoMonto);

        // Concept Field
        JLabel lblConcepto = new JLabel("Concepto:");
        lblConcepto.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblConcepto.setBounds(50, 230, 200, 30);
        panel.add(lblConcepto);

        campoConcepto = new JTextField();
        campoConcepto.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campoConcepto.setBounds(260, 230, 290, 30);
        panel.add(campoConcepto);

        // Confirm Button
        botonConfirmar = new JButton("Confirmar operación");
        botonConfirmar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        botonConfirmar.setBackground(new Color(50, 150, 50));  // Greenish color for confirm button
        botonConfirmar.setForeground(Color.WHITE);
        botonConfirmar.setBounds(150, 300, 300, 50);
        botonConfirmar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String operacion = (String) comboOperacion.getSelectedItem();
                String destino = (String) comboDestino.getSelectedItem();
                String numero = campoNumero.getText().trim();
                String concepto = campoConcepto.getText().trim();
                double monto;

                try {
                    monto = Double.parseDouble(campoMonto.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Monto inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean exito = false;

                if (operacion.equals("Depósito")) {
                    if (destino.equals("Cuenta")) {
                        exito = conexion.depositarACuenta(numero, monto, concepto);
                    } else if (destino.equals("Tarjeta Débito")) {
                        exito = conexion.depositarATarjetaDebito(numero, monto, concepto);
                    } else if (destino.equals("Tarjeta Crédito")) {
                        exito = conexion.abonarATarjetaCredito(numero, monto, concepto);
                    }
                } else if (operacion.equals("Retiro")) {
                    if (destino.equals("Cuenta")) {
                        exito = conexion.retirarDeCuenta(numero, monto, concepto);
                    } else {
                        exito = conexion.retirarDeTarjeta(numero, monto, concepto);
                    }
                }

                if (exito) {
                    JOptionPane.showMessageDialog(null, "Operación realizada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo completar la operación.", "Fallo", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
                JButton btnMenu = crearBoton("Menú Principal", new Color(10, 123, 196), 24);
        btnMenu.setBounds(80, 790, 300, 70);
        btnMenu.addActionListener(e -> {
            OperacionFrame.this.dispose();  // Cierra esta ventana

            if ("ADMINISTRADOR".equalsIgnoreCase(Traspaso.rango)) {
                new SeleccionarUsuario(conexion).setVisible(true);
            } else {
                new SeleccionarClientes(conexion).setVisible(true);
            }
        });
        this.add(btnMenu);
        this.add(panel);
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
}
