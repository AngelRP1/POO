package com.mycompany.retirodebito;

import com.mycompany.banco.Inicio1;
import com.mycompany.banco.menu;
import com.mycompany.comm.Connection;

import javax.swing.*;
import java.awt.*;

public class debito8 extends JPanel {

    private final Color color1 = new Color(51, 102, 51);
    public static Connection banco;
    public String uri;
    public static String numeroIdentificado;
    public static double monto;
    
    public debito8(Connection database, String numeroIdentificado, double monto) { 
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

        JLabel lblTitular = new JLabel("<html><i>Nombre del titular</i><br><b>"+ banco.obtenerNombrePorTarjeta(numeroIdentificado) + "</b></html>", SwingConstants.CENTER);
        lblTitular.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblTitular.setBounds(0, 310, 440, 50);
        comprobante.add(lblTitular);

        JLabel numero = new JLabel(numeroIdentificado, SwingConstants.CENTER);
        numero.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        numero.setForeground(Color.GRAY);
        numero.setBounds(0, 350, 440, 30);
        comprobante.add(numero);

        JLabel lblImporte = new JLabel("<html><i>Importe retirado</i><br><b>$"+ monto + "</b></html>", SwingConstants.CENTER);
        lblImporte.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblImporte.setBounds(0, 390, 440, 50);
        comprobante.add(lblImporte);

        JLabel aviso = new JLabel("Conserva tu comprobante para cualquier aclaración", SwingConstants.CENTER);
        aviso.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        aviso.setOpaque(true);
        aviso.setBackground(new Color(180, 225, 240));
        aviso.setBounds(20, 510, 400, 30);
        comprobante.add(aviso);

        // Imagen de confirmación
        ImageIcon iconoConfirmar = new ImageIcon(getClass().getResource("/banco/icons/confirmar.png"));
        Image imgConfirmar = iconoConfirmar.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel iconConfirmar = new JLabel(new ImageIcon(imgConfirmar));
        iconConfirmar.setBounds(670, 130, 100, 100); // Posicionada sobre el panel
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
        g.setColor(color1); // Solo barra superior
        g.fillRect(0, 0, getWidth(), 130);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Operación Exitosa");
        frame.setContentPane(new debito8(banco, numeroIdentificado, monto));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
