package com.mycompany.ventanas;

import javax.swing.*;
import java.awt.*;
import com.mycompany.comm.Connection;
import com.mycompany.depositos.depositar5;
import com.mycompany.banco.Traspaso;

public class SeleccionarUsuario extends JFrame {
    private static final int WIDTH = 1440;
    private static final int HEIGHT = 900;
    private final Color buttonColor = new Color(10, 123, 196);
    private Connection database;

    public SeleccionarUsuario(Connection database) {
        this.database = database != null ? database : new Connection("mongodb://admin:1234@localhost:27017/");
        setUndecorated(true);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0,
                        new Color(26, 46, 84), getWidth(), 0,
                        new Color(96, 155, 210));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(null);

        // Logo y título
        JLabel logo = new JLabel("P$B");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 60));
        logo.setForeground(Color.WHITE);
        logo.setBounds(40, 30, 200, 70);
        panel.add(logo);

        JLabel titulo = new JLabel("Gestión de Usuarios", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 150, WIDTH, 40);
        panel.add(titulo);

        // Configuración de botones en dos columnas
        int buttonWidth = 300;
        int buttonHeight = 70;
        int topY = 300;
        int spacingY = 20;
        
        int totalLeftHeight = 4 * buttonHeight + 3 * spacingY;
        int totalRightHeight = 3 * buttonHeight + 2 * spacingY;
        int rightTopY = topY + (totalLeftHeight - totalRightHeight) / 2;

        int leftX = WIDTH / 4 - buttonWidth / 2;
        int rightX = 3 * WIDTH / 4 - buttonWidth / 2;
        
        // Columna Izquierda: Clientes
        JButton btnCrearCliente = new JButton("Crear Cliente"); styleButton(btnCrearCliente);
        btnCrearCliente.setBounds(leftX, topY, buttonWidth, buttonHeight);
        btnCrearCliente.addActionListener(e -> {
            JFrame frame = new JFrame("Crear Cliente");
            frame.setContentPane(new CreatePanel(database));
            frame.setSize(WIDTH, HEIGHT);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
            this.dispose();
        });
        panel.add(btnCrearCliente);

        JButton btnEditarCliente = new JButton("Editar Cliente"); styleButton(btnEditarCliente);
        btnEditarCliente.setBounds(leftX, topY + (buttonHeight + spacingY), buttonWidth, buttonHeight);
        btnEditarCliente.addActionListener(e -> {
            JFrame frame = new JFrame("Editar Cliente");
            frame.setContentPane(new EditPanel(database));
            frame.setSize(WIDTH, HEIGHT);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
            this.dispose();
        });
        panel.add(btnEditarCliente);

        JButton btnEliminarCliente = new JButton("Eliminar Cliente"); styleButton(btnEliminarCliente);
        btnEliminarCliente.setBounds(leftX, topY + 2 * (buttonHeight + spacingY), buttonWidth, buttonHeight);
        btnEliminarCliente.addActionListener(e -> {
            JFrame frame = new JFrame("Eliminar Cliente");
            frame.setContentPane(new GestionarClientes(database));
            frame.setSize(WIDTH, HEIGHT);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
            this.dispose();
        });
        panel.add(btnEliminarCliente);

        JButton btnTransferencias = new JButton("Transferencias"); styleButton(btnTransferencias);
        btnTransferencias.setBounds(leftX, topY + 3 * (buttonHeight + spacingY), buttonWidth, buttonHeight);
        btnTransferencias.addActionListener(e -> {
            TransferenciaPanel tp = new TransferenciaPanel(database);
            JFrame frame = new JFrame("Transferencias");
            frame.setContentPane(tp);
            frame.setSize(WIDTH, HEIGHT);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
            this.dispose();
        });
        panel.add(btnTransferencias);
        
        JButton btnAcceder = new JButton("Depósitos y Retiros");
        styleButton(btnAcceder);
        btnAcceder.setBounds(leftX, topY + 4 * (buttonHeight + spacingY), buttonWidth, buttonHeight);
        btnAcceder.addActionListener(e -> {
            OperacionFrame frame = new OperacionFrame(database);
            frame.setVisible(true);
            this.dispose();
        });
        panel.add(btnAcceder);

        // Columna Derecha: Subgerentes
        JButton btnCrearSubgerente = new JButton("Crear Subgerente"); styleButton(btnCrearSubgerente);
        btnCrearSubgerente.setBounds(rightX, rightTopY, buttonWidth, buttonHeight);
        btnCrearSubgerente.addActionListener(e -> {
            JFrame frame = new JFrame("Crear Subgerente");
            frame.setContentPane(new CreateUsuarioPanel(database));
            frame.setSize(WIDTH, HEIGHT);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
            this.dispose();
        });
        panel.add(btnCrearSubgerente);

        JButton btnEditarSubgerente = new JButton("Editar Subgerente"); styleButton(btnEditarSubgerente);
        btnEditarSubgerente.setBounds(rightX, rightTopY + (buttonHeight + spacingY), buttonWidth, buttonHeight);
        btnEditarSubgerente.addActionListener(e -> {
            JFrame frame = new JFrame("Editar Subgerente");
            frame.setContentPane(new EditUsuarioPanel(database));
            frame.setSize(WIDTH, HEIGHT);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
            this.dispose();
        });
        panel.add(btnEditarSubgerente);

        JButton btnEliminarSubgerente = new JButton("Eliminar Subgerente"); styleButton(btnEliminarSubgerente);
        btnEliminarSubgerente.setBounds(rightX, rightTopY + 2 * (buttonHeight + spacingY), buttonWidth, buttonHeight);
        btnEliminarSubgerente.addActionListener(e -> {
            JFrame frame = new JFrame("Eliminar Subgerente");
            frame.setContentPane(new GestionarUsuariosPanel(database));
            frame.setSize(WIDTH, HEIGHT);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
            this.dispose();
        });
        panel.add(btnEliminarSubgerente);

        // Botón salir
        JButton exitBtn = new JButton("×");
        exitBtn.setFont(new Font("Segoe UI", Font.BOLD, 24));
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setBackground(new Color(231, 76, 60));
        exitBtn.setBounds(WIDTH - 60, 30, 40, 40);
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
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        String uri = "mongodb://admin:1234@localhost:27017/";
        Connection db = new Connection(uri);
        SwingUtilities.invokeLater(() -> new SeleccionarUsuario(db).setVisible(true));
    }
}
