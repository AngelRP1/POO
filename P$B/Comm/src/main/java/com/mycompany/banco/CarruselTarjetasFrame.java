package com.mycompany.banco;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import org.bson.Document;
import com.mongodb.client.model.Filters;
import java.util.TimerTask;

public class CarruselTarjetasFrame extends JFrame {
    {
        setUndecorated(true);
    }

    private static final Color PRIMARY_COLOR = new Color(26, 46, 84);
    private static final Color SECONDARY_COLOR = new Color(96, 155, 210);

    public CarruselTarjetasFrame(List<Document> tarjetasBD, String nombreCliente) {
        setTitle("Carrusel de Tarjetas");
        setSize(1440, 900);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // DEBUG: Verificar contenido recibido
        System.out.println("TARJETAS RECIBIDAS:");
        for (Document doc : tarjetasBD) {
            System.out.println(doc.toJson());
        }

        // Panel de fondo con degradado
        JPanel fondo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_COLOR, getWidth(), 0, SECONDARY_COLOR);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        fondo.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        fondo.setOpaque(true);

        // Panel superior con botón de salida
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        panelSuperior.setOpaque(false);

        JButton exitButton = new JButton("CERRAR CUENTA ×");
        exitButton.setPreferredSize(new Dimension(225, 35));
        exitButton.setFont(new Font("Segoe UI", Font.BOLD, 22));
        exitButton.setForeground(Color.WHITE);
        exitButton.setBackground(new Color(231, 76, 60));
        exitButton.setBorder(BorderFactory.createLineBorder(new Color(200, 50, 50), 1));
        exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exitButton.setFocusPainted(false);
        exitButton.setOpaque(true);
        exitButton.setBorderPainted(false);
        // Efecto hover
        exitButton.addMouseListener(new ButtonHoverEffect(exitButton, new Color(192, 57, 43)));
        // Acción de cierre
        exitButton.addActionListener(e -> System.exit(0));

        panelSuperior.add(exitButton);
        fondo.add(panelSuperior, BorderLayout.NORTH);

        // Contenedor de tarjetas animadas
        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.X_AXIS));
        contenedor.setOpaque(false);

        List<TarjetaAnimada> tarjetas = new ArrayList<>();
        for (Document tarjetaDoc : tarjetasBD) {
            String nombre = nombreCliente;
            String numero = tarjetaDoc.getString("numero");
            String tipo = tarjetaDoc.getString("tipo");
            String vencimiento = tarjetaDoc.getString("fecha_vencimiento");
            tarjetas.add(new TarjetaAnimada(nombre, numero, tipo, vencimiento));
        }

        for (TarjetaAnimada tarjeta : tarjetas) {
            JPanel wrapperTarjeta = new JPanel(new BorderLayout());
            wrapperTarjeta.setOpaque(false);
            wrapperTarjeta.setBorder(BorderFactory.createEmptyBorder(100, 40, 100, 40));
            wrapperTarjeta.add(tarjeta, BorderLayout.CENTER);
            contenedor.add(wrapperTarjeta);
        }

        // Centrar verticalmente el contenedor de tarjetas
        JPanel contenedorAlto = new JPanel(new GridBagLayout());
        contenedorAlto.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridy = 0;
        gbc.insets = new Insets(60, 0, 60, 0);
        contenedorAlto.add(contenedor, gbc);

        // Scroll horizontal y vertical personalizado
        JScrollPane scroll = new JScrollPane(contenedorAlto,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setPreferredSize(new Dimension(1280, 600));
        scroll.getVerticalScrollBar().setUnitIncrement(60);
        scroll.getHorizontalScrollBar().setUnitIncrement(120);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);

        customizeScrollBar(scroll.getHorizontalScrollBar());
        customizeScrollBar(scroll.getVerticalScrollBar());

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(Box.createVerticalGlue(), BorderLayout.NORTH);
        wrapper.add(scroll, BorderLayout.CENTER);
        wrapper.add(Box.createVerticalGlue(), BorderLayout.SOUTH);

        fondo.add(wrapper, BorderLayout.CENTER);
        add(fondo);
    }

    private void customizeScrollBar(JScrollBar bar) {
        bar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(52, 152, 219);
                this.trackColor = new Color(26, 46, 84);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                Dimension zeroDim = new Dimension(0, 0);
                button.setPreferredSize(zeroDim);
                button.setMinimumSize(zeroDim);
                button.setMaximumSize(zeroDim);
                return button;
            }
        });
    }
}

// Clase de muestra: efecto hover para botones
class ButtonHoverEffect extends java.awt.event.MouseAdapter {
    private final AbstractButton button;
    private final Color hoverBackground;
    private final Color originalBackground;

    public ButtonHoverEffect(AbstractButton button, Color hoverBackground) {
        this.button = button;
        this.hoverBackground = hoverBackground;
        this.originalBackground = button.getBackground();
    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
        button.setBackground(hoverBackground);
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
        button.setBackground(originalBackground);
    }
}

// TarjetaAnimada ajustada para usar java.util.Timer explícitamente
class TarjetaAnimada extends JPanel {
    private final String nombre;
    private final String numero;
    private final String tipo;
    private final String fecha_vencimiento;
    private int angle = 0;
    private boolean hovered = false;

    public TarjetaAnimada(String nombre, String numero, String tipo, String fecha_vencimiento) {
        this.nombre = nombre;
        this.numero = numero;
        this.tipo = tipo;
        this.fecha_vencimiento = fecha_vencimiento;

        setPreferredSize(new Dimension(360, 260));
        setMinimumSize(new Dimension(360, 260));
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                angle = (angle + 5) % 360;
                repaint();
            }
        }, 0, 40);

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                hovered = true;
                repaint();
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                hovered = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int padding = 10;
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int floatOffset = (int) (Math.sin(Math.toRadians(angle)) * 6);
        g2d.translate(0, floatOffset + padding);

        int width = getWidth();
        int height = getHeight() - (padding * 2);

        // Fondo de la tarjeta con gradiente
        GradientPaint gp = new GradientPaint(0, 0, new Color(60, 90, 150), 0, getHeight(), new Color(30, 30, 60));
        g2d.setPaint(gp);
        g2d.fillRoundRect(0, 0, width, height, 25, 25);

        // Establecer el color para el borde de la tarjeta
        g2d.setColor(hovered ? new Color(255, 255, 255, 220) : new Color(255, 255, 255, 90));
        g2d.setStroke(new BasicStroke(hovered ? 3f : 1.5f));
        g2d.drawRoundRect(0, 0, width, height, 25, 25);

        // Banda negra en la tarjeta
        g2d.setColor(new Color(50, 50, 50));
        g2d.fillRect(0, 90, width, 40);

        // Definir textos a mostrar, con manejo de valores nulos
        String tipoTexto = (tipo != null && !tipo.isEmpty()) ? tipo.toUpperCase() : "DESCONOCIDO";
        String banco = "P$B";
        String nombreTexto = (nombre != null && !nombre.isEmpty()) ? nombre : "Nombre no disponible";
        String numeroTexto = (numero != null && !numero.isEmpty()) ? numero : "Número no disponible";
        String fechaTexto = (fecha_vencimiento != null && !fecha_vencimiento.isEmpty()) ? fecha_vencimiento : "Fecha no disponible";

        // Dibujar el tipo de tarjeta
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
        g2d.drawString(tipoTexto, 20, 35);

        // Dibujar el banco (P$B)
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 24));
        FontMetrics metrics = g2d.getFontMetrics();
        int bancoWidth = metrics.stringWidth(banco);
        g2d.drawString(banco, width - bancoWidth - 20, 35);

        // Dibujar el nombre
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 22));
        g2d.drawString(nombreTexto, 30, 120);

        // Dibujar el número de tarjeta
        g2d.setFont(new Font("Courier New", Font.BOLD, 22));
        g2d.drawString(numeroTexto, 30, 160);

        // Dibujar la fecha de vencimiento
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        int textoAncho = g2d.getFontMetrics().stringWidth(fechaTexto);
        g2d.drawString(fechaTexto, width - textoAncho - 20, height - 20);

        g2d.dispose();
    }

}
