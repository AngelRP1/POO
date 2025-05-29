package com.mycompany.servicioefectivo;

import com.mycompany.banco.servicios;
import com.mycompany.comm.Connection;
import java.awt.*;
import javax.swing.*;

public class servicio1 extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    public static Connection banco;
    public String uri;
    public static JTextField txtConvenio;
    public servicio1(Connection database) { 
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

        // Título
        JLabel titulo = new JLabel("Pagar servicios");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 150, 1440, 50);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        this.add(titulo);

        JLabel subtitulo = new JLabel("Ingresa los datos del convenio");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setBounds(0, 200, 1440, 40);
        subtitulo.setHorizontalAlignment(SwingConstants.CENTER);

        this.add(subtitulo);

        // Panel de entrada centrado
        JPanel panelConvenio = new JPanel();
        panelConvenio.setBackground(new Color(10, 123, 196));
        panelConvenio.setBounds(370, 300, 700, 200); // centrado
        panelConvenio.setLayout(null);

        JLabel labelConvenio = new JLabel("Número de Convenio");
        labelConvenio.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        labelConvenio.setForeground(Color.WHITE); // ← blanco
        labelConvenio.setBounds(30, 10, 300, 30);
        panelConvenio.add(labelConvenio);

        txtConvenio = new JTextField(); // ← sin texto
        txtConvenio.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        txtConvenio.setBounds(30, 40, 640, 70);
        panelConvenio.add(txtConvenio);

        JLabel hintConvenio = new JLabel("Ingresa el número de convenio del servicio");
        hintConvenio.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        hintConvenio.setForeground(Color.WHITE);
        hintConvenio.setBounds(30, 125, 600, 30);
        panelConvenio.add(hintConvenio);

        this.add(panelConvenio);

        // Botón "Menú Pagar" con misma alineación que el archivo servicios.java
        JButton btnMenu = crearBotonTexto("Menú Pagar", new Color(10, 123, 196));
        btnMenu.setBounds(80, 700, 300, 70);
        btnMenu.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new servicios(banco));
            frame.revalidate();
        });
        this.add(btnMenu);

        // Botón "Continuar"
        JButton btnContinuar = crearBotonTexto("Continuar", new Color(10, 123, 196));
        btnContinuar.setBounds(1040, 700, 300, 70);
        btnContinuar.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new servicio1_2(banco, txtConvenio.getText()));
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
        JFrame frame = new JFrame("Pagar servicios");
        frame.setUndecorated(true);
        frame.setContentPane(new servicio1(banco));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}