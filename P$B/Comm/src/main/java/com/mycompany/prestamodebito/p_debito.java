package com.mycompany.prestamodebito;

import com.mycompany.banco.prestamo1;
import com.mycompany.comm.Connection;
import com.mycompany.prestamodebito.p_debito2;

import javax.swing.*;
import java.awt.*;

public class p_debito extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    public static Connection banco;
    public String uri;

    public p_debito(Connection database) { 
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

        JLabel titulo = new JLabel("Pago de préstamo");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 150, 1440, 50);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        this.add(titulo);

        JLabel subtitulo = new JLabel("Ingresa el número de crédito o préstamo");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setBounds(0, 200, 1440, 30);
        subtitulo.setHorizontalAlignment(SwingConstants.CENTER);

        this.add(subtitulo);

        JPanel panelCredito = new JPanel();
        panelCredito.setBackground(new Color(10, 123, 196));
        panelCredito.setBounds(370, 300, 700, 200);
        panelCredito.setLayout(null);

        JLabel labelCredito = new JLabel("Número de Crédito");
        labelCredito.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        labelCredito.setForeground(Color.WHITE);
        labelCredito.setBounds(30, 10, 300, 30);
        panelCredito.add(labelCredito);

        JTextField txtCredito = new JTextField();
        txtCredito.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        txtCredito.setBounds(30, 40, 640, 70);
        panelCredito.add(txtCredito);

        JLabel hintCredito = new JLabel("Ingresa el número de crédito asociado al préstamo");
        hintCredito.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        hintCredito.setForeground(Color.WHITE);
        hintCredito.setBounds(30, 125, 600, 30);
        panelCredito.add(hintCredito);

        this.add(panelCredito);

        JButton btnMenu = crearBotonTexto("Regresar", new Color(10, 123, 196));
        btnMenu.setBounds(80, 700, 300, 70);
        btnMenu.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new prestamo1(banco));
            frame.revalidate();
        });
        this.add(btnMenu);

        JButton btnContinuar = crearBotonTexto("Continuar", new Color(10, 123, 196));
        btnContinuar.setBounds(1000, 700, 300, 70);
        btnContinuar.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new p_debito2(banco));
            frame.revalidate();
        });
        this.add(btnContinuar);
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
        JFrame frame = new JFrame("Pago de préstamo");
        frame.setContentPane(new p_debito(banco));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
