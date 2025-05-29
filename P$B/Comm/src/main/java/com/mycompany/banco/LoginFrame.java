package com.mycompany.banco;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.mycompany.comm.Connection;
import java.awt.*;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private static final Color COLOR1 = new Color(26, 46, 84);
    private static final Color COLOR2 = new Color(96, 155, 210);
    private JTextField userField;
    private JPasswordField passField;

    public LoginFrame() {
        // Configuración de la ventana
        setUndecorated(true);
        setSize(1440, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel principal con degradado
        GradientPanel gradientPanel = new GradientPanel(COLOR1, COLOR2);
        gradientPanel.setLayout(null);
        add(gradientPanel, BorderLayout.CENTER);

        // Botón de cerrar (tachita)
        JButton exitButton = createExitButton();
        gradientPanel.add(exitButton);

        // Nombre del banco (Logo)
        JLabel bankLabel = createBankLabel();
        gradientPanel.add(bankLabel);

        // Contenedor central para login
        RoundedPanel container = createLoginContainer();
        gradientPanel.add(container);

        // Hacer la ventana desplazable
        makeDraggable();

        // Mostrar al final, cuando todo esté agregado
        setVisible(true);
    }

    private JButton createExitButton() {
        JButton exitButton = new JButton("×");
        exitButton.setBounds(1395, 10, 35, 35);
        exitButton.setFont(new Font("Segoe UI", Font.BOLD, 22));
        exitButton.setForeground(Color.WHITE);
        exitButton.setBackground(new Color(231, 76, 60));
        exitButton.setBorder(BorderFactory.createEmptyBorder());
        exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exitButton.addActionListener(e -> System.exit(0));
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
        exitButton.setOpaque(true);
        exitButton.setFocusPainted(false);
        return exitButton;
    }

    private JLabel createBankLabel() {
        JLabel bankLabel = new JLabel("P$B", SwingConstants.CENTER);
        bankLabel.setFont(new Font("Segoe UI", Font.BOLD, 60));
        bankLabel.setForeground(Color.WHITE);
        bankLabel.setBounds(0, 150, 1440, 70);
        return bankLabel;
    }

    private RoundedPanel createLoginContainer() {
        RoundedPanel container = new RoundedPanel(40, new Color(255, 255, 255, 230));
        container.setBounds(470, 275, 500, 250);
        container.setLayout(null);

        JPanel loginPanel = new JPanel(null);
        loginPanel.setOpaque(false);
        loginPanel.setBounds(0, 0, 500, 250);

        addFormFields(loginPanel, "Ingresar", e -> doLogin());
        container.add(loginPanel);

        return container;
    }

    private void addFormFields(JPanel panel, String btnText, ActionListener action) {
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        JLabel userLabel = new JLabel("Usuario:");
        userLabel.setFont(labelFont);
        userLabel.setBounds(50, 40, 100, 25);
        panel.add(userLabel);

        userField = new JTextField();
        userField.setFont(fieldFont);
        userField.setBounds(160, 40, 290, 30);
        userField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        panel.add(userField);

        JLabel passLabel = new JLabel("Contraseña:");
        passLabel.setFont(labelFont);
        passLabel.setBounds(50, 100, 100, 25);
        panel.add(passLabel);

        passField = new JPasswordField();
        passField.setFont(fieldFont);
        passField.setBounds(160, 100, 290, 30);
        passField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        panel.add(passField);

        JButton actionBtn = new JButton(btnText);
        actionBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        actionBtn.setBounds(160, 160, 290, 40);
        actionBtn.setForeground(Color.WHITE);
        actionBtn.setFocusPainted(false);
        actionBtn.setBackground(COLOR1);
        actionBtn.setBorder(new EmptyBorder(10, 20, 10, 20));
        actionBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                actionBtn.setBackground(COLOR1.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                actionBtn.setBackground(COLOR1);
            }
        });
        actionBtn.addActionListener(action);
        panel.add(actionBtn);
    }

    private void doLogin() {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor ingrese usuario y contraseña",
                    "Error de autenticación",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 1) Conectar y validar credenciales
        String uri = "mongodb://admin:1234@localhost:27017/?authSource=admin";
        Connection conn = new Connection(uri);
        MongoCollection<Document> col = conn.getDatabase().getCollection("cuentas");
        Document usuarioDoc = col.find(Filters.and(
                Filters.eq("numero_cliente", username),
                Filters.eq("contrasena", password)
        )).first();
        conn.cerrarConexion();


        // 2) Extraer numeroCliente
        String numeroCliente = usuarioDoc.getString("numero_cliente");
        conn.cerrarConexion();

        // 3) Abrir Selección de operación
        SwingUtilities.invokeLater(() -> {
            Traspaso selOp = new Traspaso(numeroCliente);
            selOp.setVisible(true);
            this.dispose(); // cierra LoginFrame
        });
    }


    private void makeDraggable() {
        MouseAdapter ma = new MouseAdapter() {
            private Point initialClick;
            @Override
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                int thisX = getLocation().x;
                int thisY = getLocation().y;
                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;
                setLocation(thisX + xMoved, thisY + yMoved);
            }
        };
        addMouseListener(ma);
        addMouseMotionListener(ma);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }

    // Panel con degradado
    static class GradientPanel extends JPanel {
        private final Color start;
        private final Color end;

        GradientPanel(Color start, Color end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(0, 0, start, getWidth(), 0, end);
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    // Panel redondeado
    static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color backgroundColor;

        RoundedPanel(int radius, Color bgColor) {
            this.radius = radius;
            this.backgroundColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
