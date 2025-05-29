package com.mycompany.serviciotarjeta;

import com.mycompany.comm.Connection;
import java.awt.*;
import javax.swing.*;

public class servicio2_3 extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    public static Connection banco;
    public String uri;
    
    public servicio2_3(Connection database) { 
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

        JLabel titulo = new JLabel("Ingresa el motivo de pago");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 150, 1440, 50);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        this.add(titulo);

        JLabel subtitulo = new JLabel("Ingresa un motivo de pago para tu servicio");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setBounds(0, 200, 1440, 40);
        subtitulo.setHorizontalAlignment(SwingConstants.CENTER);

        this.add(subtitulo);

        JPanel panelMotivo = new JPanel();
        panelMotivo.setBackground(new Color(10, 123, 196));
        panelMotivo.setBounds(370, 300, 700, 200);
        panelMotivo.setLayout(null);

        JLabel labelMotivo = new JLabel("Motivo de Pago");
        labelMotivo.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        labelMotivo.setForeground(Color.WHITE);
        labelMotivo.setBounds(30, 10, 300, 30);
        panelMotivo.add(labelMotivo);

        JTextField txtMotivo = new JTextField();
        txtMotivo.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        txtMotivo.setBounds(30, 40, 500, 70);
        panelMotivo.add(txtMotivo);

        JButton btnLimpiar = new JButton("limpiar");
        btnLimpiar.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        btnLimpiar.setBackground(new Color(234, 50, 35));
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setBounds(550, 40, 100, 70);
        btnLimpiar.addActionListener(e -> txtMotivo.setText(""));
        panelMotivo.add(btnLimpiar);

        this.add(panelMotivo);

        JButton btnRegresar = crearBotonTexto("Regresar", new Color(10, 123, 196));
        btnRegresar.setBounds(80, 700, 300, 70);
        btnRegresar.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new servicio2_2(banco));
            frame.revalidate();
        });
        this.add(btnRegresar);

        JButton btnContinuar = crearBotonTexto("Continuar", new Color(10, 123, 196));
        btnContinuar.setBounds(1040, 700, 300, 70);
        btnContinuar.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new servicio2_4(banco));
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
        JFrame frame = new JFrame("Motivo de Pago");
        frame.setContentPane(new servicio2_3(banco));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
