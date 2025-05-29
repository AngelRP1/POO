package com.mycompany.serviciotarjeta;

import com.mycompany.banco.Inicio1;
import com.mycompany.banco.menu;
import com.mycompany.comm.Connection;

import javax.swing.*;
import java.awt.*;

public class servicio2_6 extends JPanel {

    private final Color color1 = new Color(51, 102, 51);
    public static Connection banco;
    public String uri;
    
    public servicio2_6(Connection database) { 
        this.banco = database;
        this.uri = "mongodb://admin:1234@localhost:27017/";
        if(database==null){
            banco = new Connection(uri);
        }else{
            banco = database;
        }
        setLayout(null);
        setPreferredSize(new Dimension(1440, 900));
        setBackground(Color.WHITE);
        initComponents();
    }

    private void initComponents() {
        // Barra superior
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

        // Panel central
        JPanel comprobante = new JPanel();
        comprobante.setLayout(null);
        comprobante.setBackground(Color.WHITE);
        comprobante.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        comprobante.setBounds(500, 180, 440, 570);
        this.add(comprobante);

        JLabel titulo = new JLabel("OPERACIÓN EXITOSA", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setBounds(0, 90, 440, 30);
        comprobante.add(titulo);

        JLabel subtitulo = new JLabel("RETIRA TU COMPROBANTE", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitulo.setForeground(new Color(45, 71, 86));
        subtitulo.setBounds(0, 120, 440, 20);
        comprobante.add(subtitulo);

        ImageIcon icono = new ImageIcon(getClass().getResource("/banco/icons/comprobante.png"));
        Image img = icono.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH);
        JLabel icon = new JLabel(new ImageIcon(img));
        icon.setBounds(140, 145, 160, 160);
        comprobante.add(icon);

        JLabel lblServicio = new JLabel("<html><i>Nombre del servicio</i><br><b>AGUA</b></html>", SwingConstants.CENTER);
        lblServicio.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblServicio.setBounds(0, 310, 440, 50);
        comprobante.add(lblServicio);

        JLabel numeroServicio = new JLabel("2345786", SwingConstants.CENTER);
        numeroServicio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        numeroServicio.setForeground(Color.GRAY);
        numeroServicio.setBounds(0, 350, 440, 30);
        comprobante.add(numeroServicio);

        JLabel lblReferencia = new JLabel("<html><i>Referencia</i></html>", SwingConstants.CENTER);
        lblReferencia.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblReferencia.setBounds(0, 380, 440, 30);
        comprobante.add(lblReferencia);

        JLabel referencia = new JLabel("4567890654323456", SwingConstants.CENTER);
        referencia.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        referencia.setForeground(Color.GRAY);
        referencia.setBounds(0, 405, 440, 30);
        comprobante.add(referencia);

        JLabel lblImporte = new JLabel("<html><i>Importe</i><br><b>$1,000</b></html>", SwingConstants.CENTER);
        lblImporte.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblImporte.setBounds(0, 435, 440, 50);
        comprobante.add(lblImporte);

        JLabel aviso = new JLabel("ⓘ (SIMBOLO) Conserva tu comprobante para cualquier aclaración", SwingConstants.CENTER);
        aviso.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        aviso.setOpaque(true);
        aviso.setBackground(new Color(180, 225, 240));
        aviso.setBounds(20, 510, 400, 30);
        comprobante.add(aviso);

        // Imagen de confirmación
        ImageIcon iconoConfirmar = new ImageIcon(getClass().getResource("/banco/icons/confirmar.png"));
        Image imgConfirmar = iconoConfirmar.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel iconConfirmar = new JLabel(new ImageIcon(imgConfirmar));
        iconConfirmar.setBounds(670, 130, 100, 100);
        this.add(iconConfirmar);
        setComponentZOrder(iconConfirmar, 0);

        // Botón Menú Principal
        JButton btnMenu = crearBotonTexto("Menú Principal", new Color(10, 123, 196));
        btnMenu.setBounds(80, 700, 300, 70);
        btnMenu.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new menu(banco));
            frame.revalidate();
        });
        this.add(btnMenu);

        // Botón Salir
        JButton btnSalir = crearBotonTexto("Salir", new Color(234, 50, 35));
        btnSalir.setBounds(1060, 700, 300, 70);
        btnSalir.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new Inicio1(banco));
            frame.revalidate();
        });
        this.add(btnSalir);
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
        g.setColor(color1);
        g.fillRect(0, 0, getWidth(), 130);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Operación Exitosa");
        frame.setContentPane(new servicio2_6(banco));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
