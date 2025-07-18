package com.mycompany.banco;

import javax.swing.*;
import java.awt.*;

public class insertartarjeta extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);

    public insertartarjeta() {
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
        JLabel titulo = new JLabel("Inserta tu tarjeta", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 180, 1440, 50);
        this.add(titulo);

        JLabel subtitulo = new JLabel("Solo se aceptan tarjetas de débito P$B", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setBounds(0, 230, 1440, 30);
        this.add(subtitulo);

        // Imagen centrada, más grande y larga
        ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/banco/icons/ingtarjeta.png"));
        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(300, 350, Image.SCALE_SMOOTH);
        ImageIcon iconoFinal = new ImageIcon(imagenEscalada);

        JLabel tarjeta = new JLabel(iconoFinal);
        tarjeta.setBounds(570, 280, 300, 350); // centrado y alargado
        this.add(tarjeta);
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
        JFrame frame = new JFrame("Insertar Tarjeta");
        frame.setContentPane(new insertartarjeta());
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
