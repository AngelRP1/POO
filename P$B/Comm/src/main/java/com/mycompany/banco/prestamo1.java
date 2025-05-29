package com.mycompany.banco;

import com.mycompany.comm.Connection;
import com.mycompany.prestamodebito.p_debito;
import com.mycompany.prestamoefectivo.p_efectivo;

import javax.swing.*;
import java.awt.*;

public class prestamo1 extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    public static Connection banco;
    public String uri;

    public prestamo1(Connection database) {
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

        // Títulos
        JLabel titulo = new JLabel("Forma de pago del préstamo");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(480,160 , 500, 50);
        this.add(titulo);

        JLabel subtitulo = new JLabel("Seleccione su método de pago");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setBounds(560, 210, 400, 40);
        this.add(subtitulo);

        // Botón Menú Principal
        JButton btnMenu = crearBotonTexto("Menú Principal", new Color(10, 123, 196));
        btnMenu.setBounds(80, 700, 300, 70);
        btnMenu.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new menu(banco));
            frame.invalidate();
            frame.validate();
            frame.repaint();
        });
        this.add(btnMenu);

        // Botón Efectivo
        JButton btnEfectivo = crearBotonTexto("Pagar en efectivo", new Color(10, 123, 196));
        btnEfectivo.setBounds(1000, 610, 350, 70);
        btnEfectivo.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new p_efectivo(banco));
            frame.invalidate();
            frame.validate();
            frame.repaint();
        });
        this.add(btnEfectivo);

        // Botón Débito
        JButton btnDebito = crearBotonTexto("Pagar con tarjeta de débito", new Color(10, 123, 196));
        btnDebito.setBounds(1000, 700, 350, 70);
        btnDebito.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new p_debito(banco));
            frame.invalidate();
            frame.validate();
            frame.repaint();
        });
        this.add(btnDebito);
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
        JFrame frame = new JFrame("Pago préstamo");
        frame.setContentPane(new prestamo1(banco));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
