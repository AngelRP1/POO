package com.mycompany.ventanas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.mycompany.banco.Traspaso;
import com.mycompany.comm.Connection;
import com.mycompany.depositos.depositar5;

public class SeleccionarClientes extends JFrame {
    private static final int WIDTH = 1440;
    private static final int HEIGHT = 900;
    private final Color buttonColor = new Color(10, 123, 196);
    public static Connection database;
    public static String uri;

    public SeleccionarClientes(Connection database) {
        this.database = database != null ? database : new Connection("mongodb://admin:1234@localhost:27017/");
        initComponents();
        setUndecorated(true);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initComponents() {
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

        JLabel titulo = new JLabel("Gestión de Clientes", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 150, WIDTH, 40);
        panel.add(titulo);

        int buttonWidth = 400;
        int buttonHeight = 50;
        int spacing = 20;
        int totalHeight = (buttonHeight + spacing) * 5 - spacing;
        int startY = HEIGHT / 2 - totalHeight / 2;

        JButton btnCrear = new JButton("Crear Cliente");
        styleButton(btnCrear);
        btnCrear.setBounds(WIDTH/2 - buttonWidth/2, startY, buttonWidth, buttonHeight);
        btnCrear.addActionListener(e -> {
            JFrame frame = new JFrame("Crear Cliente");
            frame.setContentPane(new CreatePanel(database));
            frame.setSize(1440, 900);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            this.dispose();
        });
        panel.add(btnCrear);

        JButton btnEditar = new JButton("Editar Cliente");
        styleButton(btnEditar);
        btnEditar.setBounds(WIDTH/2 - buttonWidth/2, startY + (buttonHeight + spacing), buttonWidth, buttonHeight);
        btnEditar.addActionListener(e -> {
            JFrame frame = new JFrame("Editar Cliente");
            frame.setContentPane(new EditPanel(database));
            frame.setSize(1440, 900);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            this.dispose();
        });
        panel.add(btnEditar);

        JButton btnEliminar = new JButton("Eliminar Cliente");
        styleButton(btnEliminar);
        btnEliminar.setBounds(WIDTH/2 - buttonWidth/2, startY + 2 * (buttonHeight + spacing), buttonWidth, buttonHeight);
        btnEliminar.addActionListener(e -> {
            JFrame frame = new JFrame("Eliminar Cliente");
            frame.setContentPane(new GestionarClientes(database));
            frame.setSize(1440, 900);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            this.dispose();
        });
        panel.add(btnEliminar);

        JButton btnTransferencias = new JButton("Transferencias");
        styleButton(btnTransferencias);
        btnTransferencias.setBounds(WIDTH/2 - buttonWidth/2, startY + 3 * (buttonHeight + spacing), buttonWidth, buttonHeight);
        btnTransferencias.addActionListener(e -> {
            JFrame frame = new JFrame("Transferencias");
            frame.setContentPane(new TransferenciaPanel(database));
            frame.setSize(1440, 900);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            this.dispose();
        });
        panel.add(btnTransferencias);

        JButton btnAcceder = new JButton("Depósitos y Retiros");
        styleButton(btnAcceder);
        btnAcceder.setBounds(WIDTH/2 - buttonWidth/2, startY + 4 * (buttonHeight + spacing), buttonWidth, buttonHeight);
        btnAcceder.addActionListener(e -> {
            OperacionFrame frame = new OperacionFrame(database);
            frame.setVisible(true);
            this.dispose();
        });
        panel.add(btnAcceder);


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
        SwingUtilities.invokeLater(() -> new SeleccionarClientes(db).setVisible(true));
    }
}
