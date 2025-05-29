package com.mycompany.banco;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.mycompany.comm.Connection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.util.List;

public class SeleccionOperacionFrame extends JFrame {
    private static final int WINDOW_WIDTH = 1440;
    private static final int WINDOW_HEIGHT = 900;
    private final String numeroCliente;
    private final Color buttonColor = new Color(10, 123, 196);

    public SeleccionOperacionFrame(String numeroCliente) {
        this.numeroCliente = numeroCliente;
        initComponents();
        setUndecorated(true);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initComponents() {
        // Obtener nombre de cliente
        String nombreCliente = "";
        String uri = "mongodb://admin:1234@localhost:27017/?authSource=admin";
        Connection conn = new Connection(uri);
        MongoCollection<Document> col = conn.getDatabase().getCollection("cuentas");
        Document cuenta = col.find(Filters.eq("numero_cliente", numeroCliente)).first();
        if (cuenta != null) {
            nombreCliente = cuenta.getString("nombre");
        }
        conn.cerrarConexion();

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(26, 46, 84), getWidth(), 0, new Color(96, 155, 210));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(null);

        JLabel logo = new JLabel("P$B");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 60));
        logo.setForeground(Color.WHITE);
        logo.setBounds(40, 30, 200, 70);
        panel.add(logo);

        JLabel welcomeLabel = new JLabel("Bienvenido " + nombreCliente, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBounds(0, 150, WINDOW_WIDTH, 40);
        panel.add(welcomeLabel);

        JButton btnMostrar = new JButton("Mostrar tarjetas");
        styleButton(btnMostrar);
        btnMostrar.setBounds(WINDOW_WIDTH/2 - 200, WINDOW_HEIGHT/2 - 60, 400, 50);
        btnMostrar.addActionListener(e -> mostrarTarjetas());
        panel.add(btnMostrar);

        JButton btnCrear = new JButton("Crear tarjeta débito/crédito");
        styleButton(btnCrear);
        btnCrear.setBounds(WINDOW_WIDTH/2 - 200, WINDOW_HEIGHT/2 + 20, 400, 50);
        btnCrear.addActionListener(e -> {
            new AsignarTarjetaFrame(numeroCliente).setVisible(true);
            dispose();
        });
        panel.add(btnCrear);
        btnCrear.setBounds(WINDOW_WIDTH/2 - 200, WINDOW_HEIGHT/2 + 20, 400, 50);
        JButton exitBtn = new JButton("×");
        exitBtn.setFont(new Font("Segoe UI", Font.BOLD, 24));
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setBackground(new Color(231, 76, 60));
        exitBtn.setBounds(WINDOW_WIDTH - 60, 30, 40, 40);
        exitBtn.setFocusPainted(false);
        exitBtn.setBorder(null);
        exitBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exitBtn.addActionListener(e -> System.exit(0));
        panel.add(exitBtn);

        add(panel);
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(null);
    }

    private void mostrarTarjetas() {
        String uri = "mongodb://admin:1234@localhost:27017/?authSource=admin";
        Connection conn = new Connection(uri);
        MongoCollection<Document> col = conn.getDatabase().getCollection("cuentas");
        Document cuenta = col.find(Filters.eq("numero_cliente", numeroCliente)).first();

        if (cuenta != null) {
            List<Document> tarjetas = cuenta.getList("tarjetas", Document.class);
            if (tarjetas != null && !tarjetas.isEmpty()) {
                SwingUtilities.invokeLater(() -> {
                    String nombreCliente = cuenta.getString("nombre");
                    new CarruselTarjetasFrame(tarjetas, nombreCliente).setVisible(true);
                    dispose();
                });
            } else {
                JOptionPane.showMessageDialog(this, "No tienes tarjetas asociadas.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        conn.cerrarConexion();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SeleccionOperacionFrame("0000000001").setVisible(true));
    }
}
