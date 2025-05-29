package com.mycompany.retirodebito;

import com.mycompany.banco.menu;
import com.mycompany.comm.Connection;
import com.mycompany.retirodebito.debito3; 

import javax.swing.*;
import java.awt.*;

public class debito2 extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    public static Connection banco;
    public String uri;
    public static String numeroIdentificado;
        private JPasswordField nipField;
    private JLabel nipLabel;
    private JButton registrarBtn;

    public debito2(Connection database, String numeroIdentificado) {
        this.numeroIdentificado = numeroIdentificado;
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
        JLabel titulo = new JLabel("¿ Eres tú, "+ banco.obtenerNombrePorTarjeta(numeroIdentificado)+" ?", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 38));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 300, 1440, 50);
        this.add(titulo);

        JLabel subtitulo = new JLabel("Presiona continuar si el nombre en tu tarjeta es correcto", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setBounds(0, 360, 1440, 30);
        this.add(subtitulo);
        
        JLabel saldo = new JLabel("Tu saldo actual es " +String.format("$%,.2f", banco.obtenerSaldoTarjetaDebito(numeroIdentificado)) , SwingConstants.CENTER);
        saldo.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        saldo.setForeground(Color.WHITE);
        saldo.setBounds(0, 400, 1440, 30);
        this.add(saldo);

        // Botón Salir
        JButton btnSalir = crearBotonTexto("Salir", new Color(234, 0, 0));
        btnSalir.setBounds(80, 700, 300, 70);
        btnSalir.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new menu(banco));
            frame.revalidate();
        });
        this.add(btnSalir);

        // Botón Continuar
        JButton btnContinuar = crearBotonTexto("Continuar", new Color(10, 123, 196));
        btnContinuar.setBounds(1060, 700, 300, 70);
        btnContinuar.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new debito3(banco, numeroIdentificado));
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
        JFrame frame = new JFrame("Verificar Titular");
        frame.setContentPane(new debito2(banco, numeroIdentificado));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
