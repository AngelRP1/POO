package com.mycompany.depositos;

import com.mycompany.comm.Connection;
import com.mycompany.depositos.depositar6;
import java.awt.*;
import javax.swing.*;

public class depositar5 extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    public static Connection banco;
    public String uri;

    public static String numeroIdentificado;
    public static String tipo;
    public static String nombre;
    public static double valor;
    public static String motivo;

    public depositar5(Connection database, String nombre, String numeroIdentificado, String tipo, double valor, String motivo) {
        this.banco = database;
        this.tipo = tipo;
        this.numeroIdentificado = numeroIdentificado;
        this.valor = valor;
        this.nombre = nombre;
        this.motivo = motivo;
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
        JLabel titulo = new JLabel("Revisa y autoriza");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 150, 1440, 50);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        this.add(titulo);

        JLabel subtitulo = new JLabel("Verifica que los datos del depósito sean correctos");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setBounds(0, 200, 1440, 40);
        subtitulo.setHorizontalAlignment(SwingConstants.CENTER);

        
        this.add(subtitulo);

        // Panel central
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        panel.setBounds(510, 260, 420, 400);

        JLabel lblTitulo = new JLabel("DATOS DE TU DEPÓSITO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setBounds(0, 20, 420, 30);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblTitulo);

        JLabel lblTitular = new JLabel("<html><i>Nombre del titular</i><br><b>"+nombre+"</b></html>", SwingConstants.CENTER);
        lblTitular.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblTitular.setBounds(0, 70, 420, 50);
        panel.add(lblTitular);

        JLabel lblCuenta = new JLabel(numeroIdentificado, SwingConstants.CENTER);
        lblCuenta.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        lblCuenta.setForeground(Color.GRAY);
        lblCuenta.setBounds(0, 115, 420, 30);
        panel.add(lblCuenta);

        JLabel lblImporte = new JLabel("<html><i>Importe a depositar</i><br><b>"+ String.format("$%,.2f", valor) + "</b></html>", SwingConstants.CENTER);
        lblImporte.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblImporte.setBounds(0, 170, 420, 50);
        panel.add(lblImporte);
        
        /*

        JLabel lblCambio = new JLabel("<html><i>Cambio</i><br><b>$500</b></html>", SwingConstants.CENTER);
        lblCambio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblCambio.setBounds(0, 235, 420, 50);
        panel.add(lblCambio);
        */
        this.add(panel);
        
        // Botón "Depositar"
        JButton btnDepositar = new JButton("Depositar");
        btnDepositar.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        btnDepositar.setBackground(new Color(10, 123, 196));
        btnDepositar.setForeground(Color.WHITE);
        btnDepositar.setFocusPainted(false);
        btnDepositar.setBounds(1000, 700, 300, 70);
        btnDepositar.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new depositar7(banco,nombre, numeroIdentificado, tipo, valor, motivo));
            frame.revalidate();
        });
        this.add(btnDepositar);
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
        JFrame frame = new JFrame("Verificación de Depósito");
        frame.setContentPane(new depositar5(banco,nombre, numeroIdentificado, tipo, valor, motivo));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
