package com.mycompany.retirodebito;

import com.mycompany.comm.Connection;
import javax.swing.*;
import java.awt.*;

public class debito7 extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    public static Connection banco;
    public String uri;
    public static String numeroIdentificado;
    public static double monto;
    

    public debito7(Connection database, String numeroIdentificado, double monto) { 
        this.numeroIdentificado = numeroIdentificado;
        this.monto = monto;
        this.banco = database;
        this.uri = "mongodb://admin:1234@localhost:27017/";
        if(database==null){
            banco = new Connection(uri);
        }else{
            banco = database;
        }
        banco.retirarDeTarjetaDebito(numeroIdentificado, monto, "retiro_cajero");
        setLayout(null);
        setPreferredSize(new Dimension(1440, 900));
        initComponents();
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
        JLabel titulo = new JLabel("Preparando tu dinero", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 160, 1440, 50);
        this.add(titulo);

        JLabel subtitulo = new JLabel("Imprime tu comprobante para cualquier aclaración", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setBounds(0, 210, 1440, 30);
        this.add(subtitulo);

        // Imagen centrada
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/banco/icons/dinero.png"));
        Image resizedImage = originalIcon.getImage().getScaledInstance(280, 280, Image.SCALE_SMOOTH);
        JLabel imagen = new JLabel(new ImageIcon(resizedImage));
        imagen.setBounds(580, 280, 280, 280);
        this.add(imagen);

        // Botón "Continuar sin imprimir"
        JButton btnSinImprimir = crearBotonTexto("Continuar sin imprimir", new Color(10, 123, 196));
        btnSinImprimir.setBounds(1000, 600, 350, 70);
        btnSinImprimir.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new debito9(banco, numeroIdentificado, monto));
            frame.revalidate();
        });
        this.add(btnSinImprimir);

        // Botón "Imprimir Comprobante"
        JButton btnImprimir = crearBotonTexto("Imprimir Comprobante", new Color(10, 123, 196));
        btnImprimir.setBounds(1000, 700, 350, 70);
        btnImprimir.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new debito8(banco, numeroIdentificado, monto));
            frame.revalidate();
        });
        this.add(btnImprimir);
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

    public static void main(String[] args) {
        JFrame frame = new JFrame("Preparando Retiro");
        frame.setContentPane(new debito7(banco, numeroIdentificado, monto));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
