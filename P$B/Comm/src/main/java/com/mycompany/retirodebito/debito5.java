package com.mycompany.retirodebito;

import com.mycompany.banco.menu;
import com.mycompany.comm.Connection;

import javax.swing.*;
import java.awt.*;

public class debito5 extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    public static Connection banco;
    public String uri;
    public static String numeroIdentificado;
    public static double monto;

    public debito5(Connection database, String numeroIdentificado, double monto) { 
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

        JLabel titulo = new JLabel("Revisa y autoriza");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 150, 1440, 50);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        this.add(titulo);

        JLabel subtitulo = new JLabel("Verifica que los datos del pago sean correctos");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setBounds(0, 200, 1440, 40);
        subtitulo.setHorizontalAlignment(SwingConstants.CENTER);

        this.add(subtitulo);

        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(null);
        panelDatos.setBackground(Color.WHITE);
        panelDatos.setBounds(510, 260, 420, 400);

        JLabel lblTitulo = new JLabel("DATOS DE TU RETIRO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setBounds(0, 20, 420, 30);
        panelDatos.add(lblTitulo);

        JLabel lblNombre = new JLabel("<html><i>Nombre del titular</i><br><b>"+ banco.obtenerNombrePorTarjeta(numeroIdentificado)+"</b></html>", SwingConstants.CENTER);
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblNombre.setBounds(0, 80, 420, 50);
        panelDatos.add(lblNombre);

        JLabel lblTarjeta = new JLabel("<html><i>Número de tarjeta</i><br><b>"+ numeroIdentificado+"</b></html>", SwingConstants.CENTER);
        lblTarjeta.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblTarjeta.setBounds(0, 150, 420, 50);
        panelDatos.add(lblTarjeta);

        JLabel lblImporte = new JLabel("<html><i>Importe</i><br><b>$"+monto+"</b></html>", SwingConstants.CENTER);
        lblImporte.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblImporte.setBounds(0, 220, 420, 50);
        panelDatos.add(lblImporte);

        this.add(panelDatos);

        JButton btnRetirar = crearBotonTexto("Retirar", new Color(10, 123, 196));
        btnRetirar.setBounds(1040, 700, 300, 70);
        btnRetirar.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new debito6(banco, numeroIdentificado, monto));
            frame.revalidate();
        });
        this.add(btnRetirar);

        JButton btnSalir = crearBotonTexto("Salir", new Color(234, 0, 0));
        btnSalir.setBounds(80, 700, 300, 70);
        btnSalir.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new menu(banco));
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
        Graphics2D g2D = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, color1, w, 0, color2);
        g2D.setPaint(gp);
        g2D.fillRect(0, 0, w, h);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Verificar Retiro");
        frame.setContentPane(new debito5(banco, numeroIdentificado, monto));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
