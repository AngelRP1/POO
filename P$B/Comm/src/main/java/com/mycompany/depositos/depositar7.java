package com.mycompany.depositos;

import com.mycompany.banco.Inicio1;
import com.mycompany.banco.menu;
import com.mycompany.comm.Connection;

import javax.swing.*;
import java.awt.*;

public class depositar7 extends JPanel {

    private final Color color1 = new Color(51, 102, 51);
    private final Color color2 = new Color(102, 153, 102);
    public static Connection banco;
    public String uri;
    public static String numeroIdentificado;
    public static String tipo;
    public static String nombre;
    public static double valor;
    public static String motivo;

    public depositar7(Connection database, String nombre, String numeroIdentificado, String tipo, double valor, String motivo) {
        this.banco = database;
        this.tipo = tipo;
        this.numeroIdentificado = numeroIdentificado;
        this.valor = valor;
        this.nombre = nombre;
        this.motivo=motivo;
        this.uri = "mongodb://admin:1234@localhost:27017/";
        if(database==null){
            banco = new Connection(uri);
        }else{
            banco = database;
        }
        setLayout(null);
        setPreferredSize(new Dimension(1440, 900));
        setBackground(Color.WHITE); // Fondo blanco
        realizarDeposito();
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

        // Panel comprobante
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
        icon.setBounds(140, 145, 160, 160); // ajustado más abajo
        comprobante.add(icon);

        JLabel lblTitular = new JLabel("<html><i>Nombre del titular</i><br><b>" + nombre + "</b></html>", SwingConstants.CENTER);
        lblTitular.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblTitular.setBounds(0, 310, 440, 50);
        comprobante.add(lblTitular);

        JLabel numero = new JLabel(numeroIdentificado, SwingConstants.CENTER);
        numero.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        numero.setForeground(Color.GRAY);
        numero.setBounds(0, 350, 440, 30);
        comprobante.add(numero);

        JLabel lblImporte = new JLabel("<html><i>Importe a depositar</i><br><b>$" + String.format("%,.2f", valor) + "</b></html>", SwingConstants.CENTER);
        lblImporte.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblImporte.setBounds(0, 390, 440, 50);
        comprobante.add(lblImporte);
        
        JLabel lblMotivo;
        if(motivo.equals("")){
            lblMotivo = new JLabel("<html><i>Motivo de pago</i><br><b> Depósito </b></html>", SwingConstants.CENTER);
        }else{
            lblMotivo = new JLabel("<html><i>Motivo de pago</i><br><b>" + motivo + "</b></html>", SwingConstants.CENTER);
        }
        lblMotivo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblMotivo.setBounds(0, 440, 440, 50);
        comprobante.add(lblMotivo);

        JLabel aviso = new JLabel("ⓘ Conserva tu comprobante para cualquier aclaración", SwingConstants.CENTER);
        aviso.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        aviso.setOpaque(true);
        aviso.setBackground(new Color(180, 225, 240));
        aviso.setBounds(20, 510, 400, 30);
        comprobante.add(aviso);

        // Imagen de confirmación encima del panel blanco
        ImageIcon iconoConfirmar = new ImageIcon(getClass().getResource("/banco/icons/confirmar.png"));
        Image imgConfirmar = iconoConfirmar.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel iconConfirmar = new JLabel(new ImageIcon(imgConfirmar));
        iconConfirmar.setBounds(670, 140, 100, 100); // Posición centrada arriba
        this.add(iconConfirmar);
        setComponentZOrder(iconConfirmar, 0); // Lo pone en frente

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
        g.setColor(color1); // Solo barra verde superior
        g.fillRect(0, 0, getWidth(), 130);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Operación Exitosa");
        frame.setContentPane(new depositar7(banco,nombre, numeroIdentificado, tipo, valor, motivo));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    private boolean realizarDeposito() {
        // Dependiendo del tipo (cuenta, tarjeta débito o crédito), usamos diferentes métodos
        if (tipo.equalsIgnoreCase("cliente")) {
            return banco.depositarACuenta(numeroIdentificado, valor, motivo);
        } else if (tipo.equalsIgnoreCase("debito")) {
            return banco.depositarATarjetaDebito(numeroIdentificado, valor, motivo);
        } else if (tipo.equalsIgnoreCase("credito")) {
            return banco.abonarATarjetaCredito(numeroIdentificado, valor, motivo);
        }
        return false;
    }
}
