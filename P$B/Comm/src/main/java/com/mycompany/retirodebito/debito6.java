package com.mycompany.retirodebito;

import com.mycompany.comm.Connection;
import javax.swing.*;
import java.awt.*;

public class debito6 extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    public static Connection banco;
    public String uri;
    public static String numeroIdentificado;
    public static double monto;

    public debito6(Connection database, String numeroIdentificado, double monto) { 
        this.banco = database;
        this.numeroIdentificado = numeroIdentificado;
        this.monto = monto;
        this.uri = "mongodb://admin:1234@localhost:27017/";
        if(database==null){
            banco = new Connection(uri);
        }else{
            banco = database;
        }
        setLayout(null);
        setPreferredSize(new Dimension(1440, 900));
        initComponents();
        Timer timer = new Timer(4000, e -> {
            JFrame nuevaVentana = (JFrame) SwingUtilities.getWindowAncestor(this);;
            nuevaVentana.setContentPane(new debito7(banco, numeroIdentificado, monto));
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

        // Imagen centrada y grande
        ImageIcon icon = new ImageIcon(getClass().getResource("/banco/icons/cuida.png"));
        Image resized = icon.getImage().getScaledInstance(240, 240, Image.SCALE_SMOOTH);
        JLabel imagen = new JLabel(new ImageIcon(resized));
        imagen.setBounds(600, 200, 240, 240); // centrado visual
        this.add(imagen);

        // Título grande
        JLabel titulo = new JLabel("Cuida que no te observen", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 470, 1440, 50);
        this.add(titulo);

        // Subtítulo grande
        JLabel subtitulo = new JLabel(
            "No aceptes ayuda de desconocidos. Si tienes dudas, llama a un asistente de P$B.",
            SwingConstants.CENTER
        );
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setBounds(0, 520, 1440, 40);
        this.add(subtitulo);
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
        JFrame frame = new JFrame("Seguridad");
        frame.setContentPane(new debito6(banco, numeroIdentificado, monto));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
