package com.mycompany.banco;

import com.mycompany.comm.Connection;
import java.awt.*;
import javax.swing.*;

public class pagar extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    public static Connection banco;
    public String uri;

    public pagar(Connection database) {
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

        JLabel titulo = new JLabel("Pagar");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(670, 150, 200, 50);
        this.add(titulo);

        JLabel subtitulo = new JLabel("Selecciona el tipo de pago");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setBounds(570, 200, 350, 30);
        this.add(subtitulo);

        // Botón SERVICIOS con imagen más ancha
        JButton btnServicios = crearBotonServicios("Servicios", "banco/icons/servicios.png", new Color(96, 80, 220));
        btnServicios.setBounds(120, 250, 360, 360);
        btnServicios.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new servicios(banco));
            frame.revalidate();
        });
        this.add(btnServicios);

        // Botón PRÉSTAMOS
        JButton btnPrestamos = crearBotonConImagen("Préstamos y créditos", "banco/icons/prestamos.png", new Color(96, 80, 220));
        btnPrestamos.setBounds(540, 250, 360, 360);
        btnPrestamos.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new prestamo1(banco));
            frame.revalidate();
        });
        this.add(btnPrestamos);

        // Botón TARJETAS
        JButton btnTarjetas = crearBotonConImagen("Tarjetas de crédito", "banco/icons/credito.png", new Color(96, 80, 220));
        btnTarjetas.setBounds(960, 250, 360, 360);
        btnTarjetas.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new credito(banco));
            frame.revalidate();
        });
        this.add(btnTarjetas);

        // Botón REGRESAR
        JButton btnRegresar = crearBotonTexto("Regresar", new Color(10, 123, 196));
        btnRegresar.setBounds(120, 640, 360, 85);
        btnRegresar.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new menu(banco));
            frame.revalidate();
        });
        this.add(btnRegresar);

        // Botones inferiores
        /*
        JButton btnImpuestos = crearBotonTexto("Impuestos", new Color(96, 80, 220));
        btnImpuestos.setBounds(540, 640, 360, 85);
        this.add(btnImpuestos);

        JButton btnEmpresas = crearBotonTexto("Empresas", new Color(96, 80, 220));
        btnEmpresas.setBounds(960, 640, 360, 85);
        this.add(btnEmpresas);*/
    }

    // Imagen por defecto (220x220)
    private JButton crearBotonConImagen(String texto, String rutaImagen, Color fondo) {
        ImageIcon icon = new ImageIcon(getClass().getResource("/" + rutaImagen));
        Image img = icon.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);

        JButton boton = new JButton(texto, icon);
        boton.setHorizontalTextPosition(SwingConstants.CENTER);
        boton.setVerticalTextPosition(SwingConstants.BOTTOM);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        boton.setBackground(fondo);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        return boton;
    }

    // Imagen más ancha solo para SERVICIOS (260x220)
    private JButton crearBotonServicios(String texto, String rutaImagen, Color fondo) {
        ImageIcon icon = new ImageIcon(getClass().getResource("/" + rutaImagen));
        Image img = icon.getImage().getScaledInstance(260, 220, Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);

        JButton boton = new JButton(texto, icon);
        boton.setHorizontalTextPosition(SwingConstants.CENTER);
        boton.setVerticalTextPosition(SwingConstants.BOTTOM);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        boton.setBackground(fondo);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        return boton;
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
        JFrame frame = new JFrame("Pagar");
        frame.setUndecorated(true);
        frame.setContentPane(new pagar(banco));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
