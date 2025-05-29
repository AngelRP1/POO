package com.mycompany.retirodebito;

import com.fazecast.jSerialComm.SerialPort;
import com.mycompany.banco.menu;
import com.mycompany.comm.Connection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import org.bson.Document;
import com.mongodb.client.model.Filters;
import com.mycompany.depositos.depositar5;

public class debito extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    private static final String PORT_NAME = "COM3";
    private static final int BAUD_RATE = 9600;
    private static final int TIMEOUT_MS = 2000;
    private static final int POLLING_INTERVAL = 50;

    private TarjetaAnimada tarjetaPanel;
    private JButton exitButton;
    private Timer serialTimer;
    private String currentCardUID = "";
    private SerialPort serialPort;
    private JLabel imagenTarjeta;
    public static Connection banco;
    public String uri;
    public JLabel titulo; 
    public JLabel subtitulo;

    public debito(Connection database) {
        this.banco = database;
        this.uri = "mongodb://admin:1234@localhost:27017/";
        if(database==null){
            banco = new Connection(uri);
        }else{
            banco = database;
        }
        setLayout(null);
        setPreferredSize(new Dimension(1440, 900));
        initComponents();
        setupSerialPort();
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

        // Título
        titulo = new JLabel("Inserta tu tarjeta", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 180, 1440, 50);
        this.add(titulo);

        subtitulo = new JLabel("Solo se aceptan tarjetas de débito P$B", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setBounds(0, 230, 1440, 40);
        this.add(subtitulo);

        // Imagen de tarjeta (inicialmente visible)
        ImageIcon icono = new ImageIcon(getClass().getResource("/banco/icons/ingtarjeta.png"));
        Image img = icono.getImage().getScaledInstance(240, 380, Image.SCALE_SMOOTH);
        imagenTarjeta = new JLabel(new ImageIcon(img));
        imagenTarjeta.setBounds(600, 280, 240, 380);
        this.add(imagenTarjeta);

        // Tarjeta centrada (inicialmente invisible)
        tarjetaPanel = new TarjetaAnimada("------", "---- ---- ---- ----", "Débito", "MM/AA");
        tarjetaPanel.setBounds((1440 - 360) / 2, 280, 360, 260);
        tarjetaPanel.setVisible(false);
        this.add(tarjetaPanel);
        
        
        // Botón Salir
        exitButton = crearBotonTexto("Salir", new Color(234, 0, 0));
        exitButton.setBounds(80, 700, 300, 70);
        exitButton.addActionListener(e -> regresarAlMenu());
        this.add(exitButton);
}

    private void regresarAlMenu() {
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
        }
        if (serialTimer != null) {
            serialTimer.cancel();
        }

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.setContentPane(new menu(banco));
        frame.revalidate();
    }


    private JButton crearBotonTexto(String texto, Color fondo) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        boton.setBackground(fondo);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        return boton;
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

    public void setupSerialPort() {
        
        serialPort = SerialPort.getCommPort(PORT_NAME);
        serialPort.setBaudRate(BAUD_RATE);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, TIMEOUT_MS, 0);
        if (!serialPort.openPort()) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo abrir el puerto COM", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        serialTimer = new Timer();
        serialTimer.scheduleAtFixedRate(new TimerTask() {
            @Override public void run() { checkForCard(); }
        }, 0, POLLING_INTERVAL);
    }
    private void checkForCard() {
        try {
            if (serialPort.bytesAvailable() > 0) {
                byte[] buf = new byte[serialPort.bytesAvailable()];
                int n = serialPort.readBytes(buf, buf.length);
                String uid = new String(buf, 0, n).trim();

                if (!uid.isEmpty() && !uid.equals(currentCardUID)) {
                    currentCardUID = uid;
                    String numeroTarjeta = uid;

                    SwingUtilities.invokeLater(() -> {
                        Document infoTarjeta = banco.obtenerInformacionCompletaTarjeta(numeroTarjeta);

                        if (infoTarjeta == null) {
                            mostrarAlerta("Tarjeta no registrada en el banco");
                            regresarAlMenu();
                            return;
                        }

                        String tipoTarjeta = infoTarjeta.getString("tipo_tarjeta");
                        if (!"débito".equalsIgnoreCase(tipoTarjeta)) {
                            mostrarAlerta("Solo se aceptan tarjetas de débito");
                            regresarAlMenu();
                            return;
                        }

                        // Mostrar animación de tarjeta detectada
                        imagenTarjeta.setVisible(false);
                        tarjetaPanel.setVisible(true);
                        tarjetaPanel.setNumero(uid);
                        tarjetaPanel.setNombre(infoTarjeta.getString("nombre_cliente"));
                        tarjetaPanel.setFecha(infoTarjeta.getString("fecha_vencimiento"));
                        tarjetaPanel.repaint();

                        // Esperar 4 segundos antes de continuar
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                SwingUtilities.invokeLater(() -> {
                                    // Abrir ventana debito2 con el número de tarjeta
                                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(debito.this);
                                    frame.setContentPane(new debito2(banco, numeroTarjeta));
                                    frame.revalidate();

                                    // Cerrar recursos
                                    if (serialPort != null && serialPort.isOpen()) {
                                        serialPort.closePort();
                                    }
                                    if (serialTimer != null) {
                                        serialTimer.cancel();
                                    }
                                });
                            }
                        }, 4000); // 4 segundos de espera
                    });
                }
            }
        } catch (Exception ex) {
            System.err.println("Error RFID: " + ex.getMessage());
            mostrarAlerta("Error al leer tarjeta");
            regresarAlMenu();
        }
    }

    private String formatUID(String uid) {
        return uid.replaceAll("(.{4})", "$1 ").trim();
    }
    private void mostrarAlerta(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Alerta", JOptionPane.WARNING_MESSAGE);
    }
    public static class TarjetaAnimada extends JPanel {
        private String nombre;
        private String numero;
        private String tipo;
        private String fechaVencimiento;
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

            java.util.Timer timer = new java.util.Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override public void run() { angle = (angle + 5) % 360; repaint(); }
            }, 0, 40);

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                @Override public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
            });
        }

        public void setNumero(String numero) {
            this.numero = numero;
        }
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
        public void setFecha(String fecha){
            this.fechaVencimiento = fecha;
        }
        public void setTipo(String tipo){
            this.tipo = tipo;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int floatOffset = (int) (Math.sin(Math.toRadians(angle)) * 6);
            g2d.translate(0, floatOffset + 10);

            int w = getWidth();
            int h = getHeight() - 20;
            GradientPaint gp = new GradientPaint(0, 0, new Color(60, 90, 150), 0, getHeight(), new Color(30, 30, 60));
            g2d.setPaint(gp);
            g2d.fillRoundRect(0, 0, w, h, 25, 25);

            g2d.setColor(hovered ? new Color(255,255,255,220) : new Color(255,255,255,90));
            g2d.setStroke(new BasicStroke(hovered ? 3f : 1.5f));
            g2d.drawRoundRect(0, 0, w, h, 25, 25);

            // Banda negra
            g2d.setColor(new Color(50, 50, 50));
            g2d.fillRect(0, 90, w, 40);

            // Tipo arriba izquierda
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
            g2d.drawString(tipo.toUpperCase(), 20, 35);

            // Banco arriba derecha
            String banco = "P$B";
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 24));
            FontMetrics fm = g2d.getFontMetrics();
            int bw = fm.stringWidth(banco);
            g2d.drawString(banco, w - bw - 20, 35);

            // Nombre cliente
            g2d.setFont(new Font("Sego UI", Font.BOLD, 22));
            g2d.drawString(nombre, 30, 120);

            // Número de tarjeta
            g2d.setFont(new Font("Courier New", Font.BOLD, 22));
            g2d.drawString(numero, 30, 160);

            // Fecha de vencimiento abajo derecha
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            int fw = g2d.getFontMetrics().stringWidth(fechaVencimiento);
            g2d.drawString(fechaVencimiento, w - fw - 20, h - 10);

            g2d.dispose();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Insertar Tarjeta");
        frame.setContentPane(new debito(banco));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
