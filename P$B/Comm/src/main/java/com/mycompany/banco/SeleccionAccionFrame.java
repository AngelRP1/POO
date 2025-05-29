package com.mycompany.banco;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SeleccionAccionFrame extends JPanel {
    
    // Constantes para dimensiones y colores
    private static final int WINDOW_WIDTH = 1440;
    private static final int WINDOW_HEIGHT = 900;
    private static final int HEADER_HEIGHT = 130;
    private static final int BUTTON_WIDTH = 300;
    private static final int BUTTON_HEIGHT = 70;

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    private final Color buttonColor = new Color(10, 123, 196);

    public SeleccionAccionFrame() {
        setLayout(null);
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        initComponents();
    }

    private void initComponents() {
        createHeader();
        createTitle();
        createButtons();
    }

    private void createHeader() {
        JPanel header = new JPanel();
        header.setBackground(color1);
        header.setBounds(0, 0, WINDOW_WIDTH, HEADER_HEIGHT);
        header.setLayout(null);

        JLabel logo = new JLabel("P$B");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 60));
        logo.setForeground(Color.WHITE);
        logo.setBounds(40, 30, 200, 70);
        header.add(logo);

        // Botón de cierre personalizado
        JButton exitButton = new JButton("×");
        exitButton.setBounds(WINDOW_WIDTH - 60, 30, 40, 40);
        exitButton.setFont(new Font("Segoe UI", Font.BOLD, 24));
        exitButton.setForeground(Color.WHITE);
        exitButton.setBackground(new Color(231, 76, 60));
        exitButton.setBorder(BorderFactory.createEmptyBorder());
        exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exitButton.addActionListener(e -> System.exit(0));
        exitButton.setFocusPainted(false);

        // Efecto hover
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                exitButton.setBackground(new Color(192, 57, 43));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                exitButton.setBackground(new Color(231, 76, 60));
            }
        });

        header.add(exitButton);
        add(header);
    }

    private void createTitle() {
        JLabel titulo = new JLabel("Bienvenido a P$B");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 160, 1440, 40);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        add(titulo);
    }

    private void createButtons() {
        createLoginButton();
        createRegisterButton();
    }

    private void createLoginButton() {
        JButton btnLogin = new JButton("Iniciar Sesión");
        styleButton(btnLogin);
        btnLogin.setBounds(WINDOW_WIDTH / 2 - BUTTON_WIDTH / 2, WINDOW_HEIGHT / 2 - BUTTON_HEIGHT / 2 - 80, BUTTON_WIDTH, BUTTON_HEIGHT);
        btnLogin.addActionListener(e -> openLoginFrame());
        add(btnLogin);
    }

    private void createRegisterButton() {
        JButton btnRegister = new JButton("Registrarse");
        styleButton(btnRegister);
        btnRegister.setBounds(WINDOW_WIDTH / 2 - BUTTON_WIDTH / 2, WINDOW_HEIGHT / 2 - BUTTON_HEIGHT / 2 + 80, BUTTON_WIDTH, BUTTON_HEIGHT);
        btnRegister.addActionListener(e -> openRegisterFrame());
        add(btnRegister);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        button.setBackground(buttonColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    private void openLoginFrame() {
        new LoginFrame().setVisible(true);
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.dispose();
    }

    private void openRegisterFrame() {
        JFrame crearCuentaFrame = new JFrame("Registro de Cuenta");
        crearCuentaFrame.setUndecorated(true);
        crearCuentaFrame.setContentPane(new CrearCuenta());
        crearCuentaFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        crearCuentaFrame.setResizable(false);
        crearCuentaFrame.setLocationRelativeTo(null);
        crearCuentaFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JFrame current = (JFrame) SwingUtilities.getWindowAncestor(this);
        current.dispose();

        crearCuentaFrame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), 0, color2);
        g2D.setPaint(gp);
        g2D.fillRect(0, 0, getWidth(), getHeight());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Seleccion de Accion");
            frame.setUndecorated(true);
            frame.setContentPane(new SeleccionAccionFrame());
            frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            MouseAdapter ma = new MouseAdapter() {
                private Point initialClick;

                @Override
                public void mousePressed(MouseEvent e) {
                    initialClick = e.getPoint();
                    frame.getComponentAt(initialClick);
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    int thisX = frame.getLocation().x;
                    int thisY = frame.getLocation().y;
                    int xMoved = e.getX() - initialClick.x;
                    int yMoved = e.getY() - initialClick.y;
                    frame.setLocation(thisX + xMoved, thisY + yMoved);
                }
            };

            frame.addMouseListener(ma);
            frame.addMouseMotionListener(ma);
        });
    }
}
