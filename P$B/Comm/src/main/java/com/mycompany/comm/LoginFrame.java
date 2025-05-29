package com.mycompany.comm;

import com.fazecast.jSerialComm.*;
import com.mongodb.MongoException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;

public class LoginFrame extends JFrame {
    private static final String PORT_NAME = "COM7";
    private static final int BAUD_RATE = 9600;
    private static final int TIMEOUT_MS = 2000;
    private static final int POLLING_INTERVAL = 50;

    private JPanel cardPanel;
    private JPasswordField nipField;
    private Timer animationTimer;
    private Timer serialTimer;
    private int angle = 0;
    private String cardholderName = "Joshua André Alvarado Tovar";
    private String currentCardUID = "123123123123";

    private final Color PRIMARY_COLOR = new Color(30, 30, 60);
    private final Color SECONDARY_COLOR = new Color(60, 90, 150);
    private final Color ACCENT_COLOR = new Color(52, 152, 219);
    private SerialPort serialPort;
            String uri = "mongodb://admin:1234@localhost:27017/";
    private Connection mongoDB = new Connection(uri);

    public LoginFrame() {
        initializeUI();
        setupSerialPort();
    }

    private void initializeUI() {
        setTitle("Sistema Bancario - Acceso RFID");
        setSize(1440, 900);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setShape(new RoundRectangle2D.Double(0, 0, 1440, 900, 30, 30));
        getContentPane().setBackground(PRIMARY_COLOR);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(PRIMARY_COLOR);

        // Título
        JLabel titulo = new JLabel("ACCESO RFID");
        titulo.setBounds(0, 50, 1440, 50);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titulo.setForeground(Color.WHITE);
        mainPanel.add(titulo);

        // Campo NIP
        JLabel nipLabel = new JLabel("Introduzca su NIP: (4 dígitos)");
        nipLabel.setBounds(570, 650, 300, 20);
        nipLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        nipLabel.setForeground(Color.LIGHT_GRAY);
        mainPanel.add(nipLabel);

        nipField = new JPasswordField();
        nipField.setBounds(570, 675, 300, 40);
        nipField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        nipField.setForeground(Color.BLACK);
        nipField.setBackground(new Color(255, 255, 255, 220));
        nipField.setHorizontalAlignment(JTextField.CENTER);
        nipField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR, 2),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)));

        nipField.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a)
                    throws javax.swing.text.BadLocationException {
                if (str == null) return;
                if ((getLength() + str.length()) <= 4 && str.matches("\\d+")) {
                    super.insertString(offs, str, a);
                }
            }
        });

        nipField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateLabel(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateLabel(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateLabel(); }

            private void updateLabel() {
                nipLabel.setVisible(nipField.getPassword().length == 0);
            }
        });
        mainPanel.add(nipField);

        // Botón Ingresar
        JButton ingresarBtn = new JButton("INGRESAR");
        ingresarBtn.setBounds(570, 725, 300, 45);
        ingresarBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        ingresarBtn.setForeground(Color.WHITE);
        ingresarBtn.setBackground(ACCENT_COLOR);
        ingresarBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        ingresarBtn.setBorder(BorderFactory.createEmptyBorder());
        ingresarBtn.addMouseListener(new ButtonHoverEffect(ingresarBtn, ACCENT_COLOR));
        ingresarBtn.addActionListener(e -> verificarAcceso());
        ingresarBtn.setOpaque(true);
        ingresarBtn.setBorderPainted(false);
        ingresarBtn.setFocusPainted(false);
        mainPanel.add(ingresarBtn);

        // Botón Salir
        JButton exitButton = new JButton("×");
        exitButton.setBounds(1395, 10, 35, 35);
        exitButton.setFont(new Font("Segoe UI", Font.BOLD, 22));
        exitButton.setForeground(Color.WHITE);
        exitButton.setBackground(new Color(231, 76, 60));
        exitButton.setBorder(BorderFactory.createLineBorder(new Color(200, 50, 50), 1));
        exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exitButton.addActionListener(e -> System.exit(0));
        exitButton.addMouseListener(new ButtonHoverEffect(exitButton, new Color(192, 57, 43)));
        exitButton.setOpaque(true);
        exitButton.setBorderPainted(false);
        exitButton.setFocusPainted(false);
        mainPanel.add(exitButton);

        // Panel de tarjeta animada
        cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int floatOffset = (int) (Math.sin(Math.toRadians(angle)) * 10);
                int centerX = (getWidth() - 280) / 2;
                int centerY = (getHeight() - 160) / 2 + floatOffset;
                g2d.translate(centerX, centerY);
                drawCard(g2d);
                g2d.dispose();
            }

            private void drawCard(Graphics2D g2d) {
                GradientPaint gradient = new GradientPaint(0, 0, SECONDARY_COLOR, 0, 120, PRIMARY_COLOR);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, 280, 160, 20, 20);

                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, 280, 160, 20, 20);

                g2d.setColor(new Color(50, 50, 50));
                g2d.fillRect(0, 70, 280, 30);

                if (!currentCardUID.isEmpty()) {
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Courier New", Font.BOLD, 16));
                    String formattedUID = formatUID(currentCardUID);
                    g2d.drawString(formattedUID, 30, 120);
                    g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    g2d.drawString("BANCO DIGITAL", 160, 40);
                }

                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
                if (!cardholderName.isEmpty()) {
                    String nombreMostrar = cardholderName.length() > 18 ?
                            cardholderName.substring(0, 15) + "..." : cardholderName;
                    g2d.drawString(nombreMostrar, 30, 90);
                } else {
                    g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    g2d.drawString("BANCO DIGITAL", 160, 40);
                }
            }
        };

        cardPanel.setOpaque(false);
        cardPanel.setBounds((1440 - 380) / 2, 180, 380, 380);
        mainPanel.add(cardPanel);

        add(mainPanel);
        animationTimer = new Timer(40, e -> {
            angle = (angle + 5) % 360;
            cardPanel.repaint();
        });
        animationTimer.start();
    }

    private void setupSerialPort() {
        serialPort = SerialPort.getCommPort(PORT_NAME);
        serialPort.setBaudRate(BAUD_RATE);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, TIMEOUT_MS, 0);

        if (!serialPort.openPort()) {
            JOptionPane.showMessageDialog(this,
                    "Error al abrir puerto " + PORT_NAME,
                    "Error de Comunicación",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        serialTimer = new Timer(POLLING_INTERVAL, e -> checkForCard());
        serialTimer.start();
    }

    private void checkForCard() {
        try {
            if (serialPort.bytesAvailable() > 0) {
                byte[] buffer = new byte[serialPort.bytesAvailable()];
                int numRead = serialPort.readBytes(buffer, buffer.length);
                String newUID = new String(buffer, 0, numRead).trim();
  
                if (!newUID.isEmpty() && !newUID.equals(currentCardUID)) {
                    cardholderName = obtenerNombreTitular(currentCardUID);
                    if (cardholderName.equals("No se encontró")) {
                        currentCardUID = "";
                    } else {
                        currentCardUID = newUID;
                    }
                    SwingUtilities.invokeLater(() -> cardPanel.repaint());
                }
            }
        } catch (Exception ex) {
            System.err.println("Error lectura serial: " + ex.getMessage());
        }
    }

    private void verificarAcceso() {
        String nip = new String(nipField.getPassword());/*
        if (mongoDB.verificarNIP(currentCardUID, nip)) {
            showStyledMessage("Acceso concedido", ACCENT_COLOR);
        } else {
            showStyledMessage("Acceso denegado", new Color(231, 76, 60));
            nipField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(231, 76, 60), 2),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)));
        }*/
    }

    private String formatUID(String uid) {
        String cleanUID = uid.replaceAll("\\s", "");
        return cleanUID.replaceAll("(.{4})", "$1 ").trim();
    }

    private String obtenerNombreTitular(String uid) {/*
        return mongoDB.verificarNombre(uid);*/
        return "jeje";
    }

    private void showStyledMessage(String mensaje, Color background) {
        JOptionPane optionPane = new JOptionPane(mensaje, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog(this, "Mensaje");
        dialog.setModal(false);
        dialog.getContentPane().setBackground(background);
        dialog.setVisible(true);

        new Timer(3000, e -> {
            dialog.dispose();
            ((Timer) e.getSource()).stop();
        }).start();
    }

    @Override
    public void dispose() {
        if (animationTimer != null) animationTimer.stop();
        if (serialTimer != null) serialTimer.stop();
        if (serialPort != null && serialPort.isOpen()) serialPort.closePort();
        super.dispose();
    }

    static class ButtonHoverEffect extends MouseAdapter {
        private final JButton button;
        private final Color original;

        public ButtonHoverEffect(JButton button, Color originalColor) {
            this.button = button;
            this.original = originalColor;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            button.setBackground(original.darker());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            button.setBackground(original);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LoginFrame().setVisible(true);
        });
    }
}
