package com.mycompany.prestamoefectivo;

import com.mycompany.comm.Connection;
import java.awt.*;
import javax.swing.*;

public class p_efectivo2 extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    public static Connection banco;
    public String uri;

    public p_efectivo2(Connection database) { 
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

        JLabel titulo = new JLabel("Captura la referencia del préstamo");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(400, 160, 800, 40);
        this.add(titulo);

        JLabel subtitulo = new JLabel("Ingresa el número de referencia que aparece en tu recibo");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setBounds(350, 200, 800, 30);
        this.add(subtitulo);

        JPanel panelReferencia = new JPanel();
        panelReferencia.setBackground(new Color(10, 123, 196));
        panelReferencia.setBounds(370, 300, 700, 200);
        panelReferencia.setLayout(null);

        JLabel labelReferencia = new JLabel("Número de Referencia");
        labelReferencia.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        labelReferencia.setForeground(Color.WHITE);
        labelReferencia.setBounds(30, 10, 300, 30);
        panelReferencia.add(labelReferencia);

        JTextField txtReferencia = new JTextField();
        txtReferencia.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        txtReferencia.setBounds(30, 40, 500, 70);
        panelReferencia.add(txtReferencia);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        btnLimpiar.setBackground(new Color(234, 50, 35));
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setBounds(550, 40, 100, 70);
        btnLimpiar.addActionListener(e -> txtReferencia.setText(""));
        panelReferencia.add(btnLimpiar);

        JLabel hint = new JLabel("Ejemplo: 4536743876541234");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        hint.setForeground(Color.WHITE);
        hint.setBounds(30, 125, 600, 30);
        panelReferencia.add(hint);

        this.add(panelReferencia);

        JButton btnRegresar = crearBotonTexto("Regresar", new Color(10, 123, 196));
        btnRegresar.setBounds(80, 700, 300, 70);
        btnRegresar.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new p_efectivo(banco));
            frame.revalidate();
        });
        this.add(btnRegresar);

        JButton btnContinuar = crearBotonTexto("Continuar", new Color(10, 123, 196));
        btnContinuar.setBounds(1000, 700, 300, 70);
        btnContinuar.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new p_efectivo3(banco));
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
        JFrame frame = new JFrame("Referencia del Préstamo");
        frame.setContentPane(new p_efectivo2(banco));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
