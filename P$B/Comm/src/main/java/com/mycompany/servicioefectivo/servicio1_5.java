package com.mycompany.servicioefectivo;
import com.mycompany.comm.Connection;
import java.awt.*;
import javax.swing.*;

public class servicio1_5 extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    public static Connection banco;
    public String uri;
    public static String convenio;
    public static String referencia;
    public static String motivo;
    public static double valor;
    public servicio1_5(Connection database, String convenio, String referencia, String motivo, double valor) {
        this.banco = database;
        this.convenio = convenio;
        this.referencia=referencia;
        this.motivo = motivo;
        this.valor = valor;
        setLayout(null);
        setPreferredSize(new Dimension(1440, 900));
        initComponents();
        // Temporizador para cambiar de ventana después de 4 segundos
        Timer timer = new Timer(4000, e -> {
            JFrame nuevaVentana = (JFrame) SwingUtilities.getWindowAncestor(this);;
            nuevaVentana.setContentPane(new servicio1_6(banco, convenio, referencia, motivo, valor));
            nuevaVentana.revalidate();
        });
        timer.setRepeats(false);
        timer.start();
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
        JLabel titulo = new JLabel("Ingresa los billetes");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 150, 1440, 50);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        this.add(titulo);

        // Subtítulo
        JLabel subtitulo = new JLabel("Asegúrate de que no tengan grapas o clips");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setBounds(0, 200, 1440, 40);
        subtitulo.setHorizontalAlignment(SwingConstants.CENTER);

        this.add(subtitulo);

        // Imagen alargada (más alta que ancha)
        ImageIcon icono = new ImageIcon(getClass().getResource("/banco/icons/efectivo.png"));
        Image img = icono.getImage().getScaledInstance(240, 380, Image.SCALE_SMOOTH); // ← más larga
        JLabel imagen = new JLabel(new ImageIcon(img));
        imagen.setBounds(600, 280, 240, 380); // ajustado para centrar mejor
        this.add(imagen);
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

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ingreso de efectivo");
        frame.setUndecorated(true);
        frame.setContentPane(new servicio1_5(banco, convenio, referencia, motivo, valor));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}