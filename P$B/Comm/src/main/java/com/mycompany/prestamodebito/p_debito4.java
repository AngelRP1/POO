package com.mycompany.prestamodebito;

import com.mycompany.comm.Connection;
import java.awt.*;
import javax.swing.*;

public class p_debito4 extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);

    public static Connection banco;
    public String uri;

    public p_debito4(Connection database) { 
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

        JLabel titulo = new JLabel("Ingresa el importe del pago");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(500, 150, 600, 40);
        this.add(titulo);

        JLabel subtitulo = new JLabel("Escribe el monto del pago correspondiente al préstamo");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setBounds(440, 190, 700, 30);
        this.add(subtitulo);

        JPanel panelPago = new JPanel();
        panelPago.setLayout(null);
        panelPago.setBackground(Color.WHITE);
        panelPago.setBounds(470, 250, 450, 480);

        JLabel lblTitulo = new JLabel("DATOS DEL PAGO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setBounds(0, 20, 450, 30);
        panelPago.add(lblTitulo);

        JLabel lblServicio = new JLabel("Nombre del préstamo", SwingConstants.CENTER);
        lblServicio.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblServicio.setBounds(0, 60, 450, 30);
        panelPago.add(lblServicio);

        JLabel lblClave = new JLabel("PRÉSTAMO PERSONAL", SwingConstants.CENTER);
        lblClave.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblClave.setBounds(0, 85, 450, 30);
        panelPago.add(lblClave);

        JLabel lblNumero = new JLabel("Núm. Cliente: 1234567", SwingConstants.CENTER);
        lblNumero.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblNumero.setBounds(0, 110, 450, 30);
        panelPago.add(lblNumero);

        JLabel lblReferencia = new JLabel("Referencia de pago", SwingConstants.CENTER);
        lblReferencia.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblReferencia.setBounds(0, 150, 450, 30);
        panelPago.add(lblReferencia);

        JLabel lblRefNumero = new JLabel("234565432455687643", SwingConstants.CENTER);
        lblRefNumero.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblRefNumero.setBounds(0, 180, 450, 30);
        panelPago.add(lblRefNumero);

        JLabel lblImporte = new JLabel("Importe a capturar", SwingConstants.CENTER);
        lblImporte.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblImporte.setBounds(0, 220, 450, 30);
        panelPago.add(lblImporte);

        JLabel lblIngresa = new JLabel("Ingrese el monto exacto", SwingConstants.CENTER);
        lblIngresa.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblIngresa.setBounds(0, 245, 450, 30);
        panelPago.add(lblIngresa);

        JLabel aviso = new JLabel("ⓘ Asegúrate de ingresar el importe correcto del pago", SwingConstants.CENTER);
        aviso.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        aviso.setOpaque(true);
        aviso.setBackground(new Color(180, 225, 240));
        aviso.setBounds(30, 420, 390, 30);
        panelPago.add(aviso);

        this.add(panelPago);

        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(null);
        panelDerecho.setBackground(new Color(10, 123, 196));
        panelDerecho.setBounds(1000, 540, 320, 250);

        JLabel lblImportePagar = new JLabel("Importe a pagar");
        lblImportePagar.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblImportePagar.setForeground(Color.WHITE);
        lblImportePagar.setBounds(20, 10, 200, 30);
        panelDerecho.add(lblImportePagar);

        JTextField txtImporte = new JTextField("$");
        txtImporte.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        txtImporte.setBounds(20, 40, 280, 60);
        panelDerecho.add(txtImporte);

        JLabel info = new JLabel("Ingresa y corrige con el teclado");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        info.setForeground(Color.WHITE);
        info.setBounds(20, 110, 280, 30);
        panelDerecho.add(info);

        JButton btnContinuar = crearBotonTexto("Continuar", new Color(10, 123, 196));
        btnContinuar.setBounds(20, 160, 280, 60);
        btnContinuar.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new p_debito5(banco));
            frame.revalidate();
        });
        panelDerecho.add(btnContinuar);

        this.add(panelDerecho);

        JButton btnRegresar = crearBotonTexto("Regresar", new Color(10, 123, 196));
        btnRegresar.setBounds(80, 700, 300, 70);
        btnRegresar.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new p_debito3(banco));
            frame.revalidate();
        });
        this.add(btnRegresar);
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
        JFrame frame = new JFrame("Pago de préstamo - Importe");
        frame.setContentPane(new p_debito4(banco));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
