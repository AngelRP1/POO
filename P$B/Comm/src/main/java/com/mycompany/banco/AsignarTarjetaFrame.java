package com.mycompany.banco;

import com.fazecast.jSerialComm.SerialPort;
import com.mycompany.comm.Connection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AsignarTarjetaFrame extends JFrame {
    private static final String PORT_NAME     = "COM3";
    private static final int BAUD_RATE        = 9600;
    private static final int TIMEOUT_MS       = 2000;
    private static final int POLLING_INTERVAL = 50;

    private TarjetaAnimada tarjetaPanel;
    private JPasswordField nipField;
    private JLabel nipLabel;
    private JButton registrarBtn;
    private JButton exitButton;
    private Timer serialTimer;
    private String currentCardUID = "";
    private final String numeroCliente;
    private String selectedType;
    private double creditLimit;
    private SerialPort serialPort;

    // Colores
    private final Color PRIMARY_COLOR   = new Color(26, 46, 84);
    private final Color SECONDARY_COLOR = new Color(96, 155, 210);
    private final Color ACCENT_COLOR    = new Color(52, 152, 219);
    private final Color CLOSE_COLOR     = new Color(231, 76, 60);
    private final Color CLOSE_HOVER     = new Color(192, 57, 43);

    public AsignarTarjetaFrame(String numeroCliente) {
        this.numeroCliente = numeroCliente;
        // Determinar si ya existe una tarjeta asignada
        String uri = "mongodb://admin:1234@localhost:27017/?authSource=admin";
        Connection banco = new Connection(uri);
        MongoCollection<Document> col = banco.getDatabase().getCollection("cuentas");
        Document cuenta = col.find(Filters.eq("numero_cliente", numeroCliente)).first();
        List<Document> existing = cuenta != null ? cuenta.getList("tarjetas", Document.class) : null;
        banco.cerrarConexion();

        boolean firstAssignment = existing == null || existing.isEmpty();
        if (firstAssignment) {
            selectedType = "Débito";            
        } else {
            String[] options = {"Débito", "Crédito"};
            int choice = JOptionPane.showOptionDialog(
                null,
                "¿Qué tipo de tarjeta desea agregar?",
                "Tipo de Tarjeta",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );
            if (choice < 0) choice = 0;
            selectedType = options[choice];
            if ("Crédito".equals(selectedType)) {
                String lim = JOptionPane.showInputDialog(
                    null,
                    "Ingrese límite de crédito:",
                    "Límite",
                    JOptionPane.PLAIN_MESSAGE
                );
                try { creditLimit = Double.parseDouble(lim); }
                catch (Exception e) { creditLimit = 0; }
            }
        }

        initializeUI();
        setupSerialPort();
    }

    private void initializeUI() {
        setTitle("Asignar Tarjeta - RFID");
        setSize(1440, 900);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 25, 25));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel main = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, PRIMARY_COLOR, getWidth(), 0, SECONDARY_COLOR));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // Cierre
        exitButton = new JButton("×");
        exitButton.setBounds(1395, 10, 35, 35);
        styleCloseButton(exitButton);
        exitButton.addActionListener(e -> dispose());
        main.add(exitButton);

        // Título
        JLabel title = new JLabel("ASIGNA TU TARJETA P$B", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 42));
        title.setForeground(Color.WHITE);
        title.setBounds(0, 80, 1440, 50);
        main.add(title);

        // Panel tarjeta
        tarjetaPanel = new TarjetaAnimada(
            numeroCliente,
            "---- ---- ---- ----",
            selectedType,
            "MM/yy"
        );
        tarjetaPanel.setBounds((1440 - 360) / 2, 160, 360, 260);
        main.add(tarjetaPanel);

        // NIP
        nipLabel = new JLabel("Crea un NIP de 4 dígitos");
        nipLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        nipLabel.setForeground(Color.LIGHT_GRAY);
        nipLabel.setBounds(530, 690, 380, 20);
        nipLabel.setVisible(false);
        main.add(nipLabel);

        nipField = new JPasswordField();
        nipField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        nipField.setHorizontalAlignment(JTextField.CENTER);
        nipField.setBounds(530, 715, 380, 40);
        nipField.setVisible(false);
        main.add(nipField);

        // Botón asignar
        registrarBtn = new JButton("ASIGNAR TARJETA");
        registrarBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        registrarBtn.setForeground(Color.WHITE);
        registrarBtn.setBackground(ACCENT_COLOR);
        registrarBtn.setBounds(530, 765, 380, 45);
        registrarBtn.setVisible(false);
        registrarBtn.addActionListener(e -> onAsignar());
        registrarBtn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { registrarBtn.setBackground(ACCENT_COLOR.darker()); }
            @Override public void mouseExited(MouseEvent e)  { registrarBtn.setBackground(ACCENT_COLOR); }
        });
        main.add(registrarBtn);

        add(main);
    }

    private void styleCloseButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btn.setForeground(Color.WHITE);
        btn.setBackground(CLOSE_COLOR);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(CLOSE_HOVER); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(CLOSE_COLOR); }
        });
    }

    private void onAsignar() {
        String nip = new String(nipField.getPassword());
        if (!nip.matches("\\d{4}")) {
            JOptionPane.showMessageDialog(this, "NIP inválido. Debe ser 4 dígitos.", "Error", JOptionPane.ERROR_MESSAGE);
            nipField.requestFocusInWindow();
            return;
        }

        // Fecha vencimiento +5 años
        LocalDate hoy = LocalDate.now();
        LocalDate vence = hoy.plusYears(5);
        String fechaV = vence.format(DateTimeFormatter.ofPattern("MM/yy"));

        // Documento tarjeta
        Document tarjetaDoc = new Document()
            .append("tipo", selectedType.toLowerCase())
            .append("numero", currentCardUID)
            .append("nip", nip)
            .append("fecha_vencimiento", fechaV)
            .append("limite", "Crédito".equals(selectedType) ? creditLimit : 0)
            .append("saldo_disponible", "Crédito".equals(selectedType) ? creditLimit : 0);

        // Guardar en BD
        String uri = "mongodb://admin:1234@localhost:27017/?authSource=admin";
        Connection banco = new Connection(uri);
        banco.agregarTarjeta(numeroCliente, selectedType, currentCardUID, nip, fechaV, creditLimit);
        banco.cerrarConexion();

        JOptionPane.showMessageDialog(this,
            "Tarjeta asignada con éxito\nUID: " + currentCardUID + "\nNIP: " + nip,
            "Éxito", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    private void setupSerialPort() {
        serialPort = SerialPort.getCommPort(PORT_NAME);
        serialPort.setBaudRate(BAUD_RATE);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, TIMEOUT_MS, 0);
        if (!serialPort.openPort()) {
            JOptionPane.showMessageDialog(this, "No se pudo abrir el puerto COM", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        serialTimer = new Timer();
        serialTimer.scheduleAtFixedRate(new TimerTask() { @Override public void run() { checkForCard(); } }, 0, POLLING_INTERVAL);
    }

    private void checkForCard() {
        try {
            if (serialPort.bytesAvailable() > 0) {
                byte[] buf = new byte[serialPort.bytesAvailable()];
                int n = serialPort.readBytes(buf, buf.length);
                String uid = new String(buf, 0, n).trim();
                if (!uid.isEmpty() && !uid.equals(currentCardUID)) {
                    currentCardUID = uid;
                    SwingUtilities.invokeLater(() -> {
                        tarjetaPanel.setNumero(formatUID(uid));
                        tarjetaPanel.repaint();
                        nipLabel.setVisible(true);
                        nipField.setVisible(true);
                        registrarBtn.setVisible(true);
                    });
                }
            }
        } catch (Exception ex) {
            System.err.println("Error RFID: " + ex.getMessage());
        }
    }

    private String formatUID(String uid) {
        return uid.replaceAll("(.{4})", "$1 ").trim();
    }

    @Override public void dispose() {
        if (serialTimer != null) serialTimer.cancel();
        if (serialPort != null && serialPort.isOpen()) serialPort.closePort();
        super.dispose();
    }

    // TarjetaAnimada adaptada
    static class TarjetaAnimada extends JPanel {
        private final String nombre;
        private String numero;
        private final String tipo;
        private final String fechaVencimiento;
        private int angle = 0;
        private boolean hovered = false;

        public TarjetaAnimada(String nombre, String numero, String tipo, String fechaVencimiento) {
            this.nombre = nombre;
            this.numero = numero;
            this.tipo = tipo;
            this.fechaVencimiento = fechaVencimiento;
            setPreferredSize(new Dimension(360, 260));
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            new Timer().scheduleAtFixedRate(new TimerTask() { @Override public void run() { angle = (angle + 5) % 360; repaint(); } }, 0, 40);
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                @Override public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
            });
        }
        public void setNumero(String numero) { this.numero = numero; }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int offset = (int)(Math.sin(Math.toRadians(angle))*6)+10;
            g2.translate(0, offset);
            int w = getWidth(), h = getHeight()-20;
            g2.setPaint(new GradientPaint(0,0,new Color(60,90,150),0,getHeight(),new Color(30,30,60)));
            g2.fillRoundRect(0,0,w,h,25,25);
            g2.setColor(hovered?new Color(255,255,255,220):new Color(255,255,255,90));
            g2.setStroke(new BasicStroke(hovered?3f:1.5f));
            g2.drawRoundRect(0,0,w,h,25,25);
            g2.setColor(new Color(50,50,50)); g2.fillRect(0,90,w,40);
            g2.setColor(Color.WHITE); g2.setFont(new Font("Segoe UI",Font.BOLD,18)); g2.drawString(tipo.toUpperCase(),20,35);
            String banco = "P$B"; g2.setFont(new Font("Segoe UI",Font.BOLD,24)); int bw = g2.getFontMetrics().stringWidth(banco); g2.drawString(banco, w-bw-20,35);
            g2.setFont(new Font("Segoe UI",Font.BOLD,22)); g2.drawString(nombre,30,120);
            g2.setFont(new Font("Courier New",Font.BOLD,22)); g2.drawString(numero,30,160);
            g2.setFont(new Font("Segoe UI",Font.PLAIN,16)); int fw = g2.getFontMetrics().stringWidth(fechaVencimiento);
            g2.drawString(fechaVencimiento, w-fw-20, h-10); g2.dispose();
        }
    }
}