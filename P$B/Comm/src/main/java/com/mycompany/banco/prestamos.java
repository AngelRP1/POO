package com.mycompany.banco;

import com.mycompany.comm.Connection;
import javax.swing.*;
import java.awt.*;

public class prestamos extends JPanel {

    private final Color colorFondo = Color.WHITE;
    private final Color colorHeader = new Color(245, 246, 250);
    private final Color azulOscuro = new Color(0, 46, 93);
    public static Connection banco;
    public String uri;

    public prestamos(Connection database) {
        this.banco = database;
        this.uri = "mongodb://admin:1234@localhost:27017/";
        if(database==null){
            banco = new Connection(uri);
        }else{
            banco = database;
        }
        setLayout(null);
        setPreferredSize(new Dimension(1440, 900));
        setBackground(colorFondo);
        initComponents();
    }

    private void initComponents() {
        JLabel logo = new JLabel("P$B");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 60));
        logo.setForeground(azulOscuro);
        logo.setBounds(60, 30, 200, 70);
        this.add(logo);

        JLabel titulo = new JLabel("Ingresa los datos", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        titulo.setForeground(azulOscuro);
        titulo.setBounds(0, 40, 1440, 80);
        this.add(titulo);

        int startX = 310;
        int spacing = 210;

        addCategoria("C_Hipotecario.png", "Crédito", "Hipotecario", new Color(0, 182, 186), startX);
        addCategoria("C_Personal.png", "Préstamo", "Personal/Nómina", new Color(31, 37, 97), startX + spacing);
        addCategoria("C_PYME.png", "Crédito", "PYME", new Color(235, 124, 1), startX + spacing * 2);
        addCategoria("C_Auto.png", "Crédito", "Auto", new Color(0, 116, 217), startX + spacing * 3);

        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(null);
        panelDatos.setBackground(colorHeader);
        panelDatos.setBounds(0, 270, 1440, 300);

        JLabel lblNumero = new JLabel("Número de crédito o préstamo a 20 dígitos", SwingConstants.CENTER);
        lblNumero.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblNumero.setBounds(0, 30, 1440, 30);
        panelDatos.add(lblNumero);

        JTextField txtNumero = new JTextField();
        txtNumero.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtNumero.setHorizontalAlignment(SwingConstants.CENTER);
        txtNumero.setBounds(480, 70, 400, 40);
        panelDatos.add(txtNumero);

        JLabel lblImporte = new JLabel("Importe a pagar sin centavos", SwingConstants.CENTER);
        lblImporte.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblImporte.setBounds(0, 140, 1440, 30);
        panelDatos.add(lblImporte);

        JTextField txtImporte = new JTextField();
        txtImporte.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtImporte.setHorizontalAlignment(SwingConstants.CENTER);
        txtImporte.setBounds(480, 180, 400, 40);
        panelDatos.add(txtImporte);

        this.add(panelDatos);

        JButton btnMenu = crearBoton("Menú Principal", new Color(0, 112, 192));
        btnMenu.setBounds(80, 700, 300, 70);
        btnMenu.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new menu(banco));
            frame.revalidate();
        });
        this.add(btnMenu);

        JButton btnCorregir = crearBoton("Corregir", new Color(82, 217, 204));
        btnCorregir.setBounds(80, 610, 300, 70);
        this.add(btnCorregir);

        JButton btnContinuar = crearBoton("Continuar", new Color(55, 158, 76));
        btnContinuar.setBounds(1000, 610, 350, 70);
        btnContinuar.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new prestamo1(banco));
            frame.revalidate();
        });
        this.add(btnContinuar);

        JButton btnSalir = crearBoton("Salir", new Color(225, 40, 27));
        btnSalir.setBounds(1000, 700, 350, 70);
        btnSalir.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new pagar(banco));
            frame.revalidate();
        });
        this.add(btnSalir);
    }

    private void addCategoria(String icono, String linea1, String linea2, Color colorTexto, int x) {
        ImageIcon icon = new ImageIcon(getClass().getResource("/banco/icons/" + icono));
        Image scaled = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel img = new JLabel(new ImageIcon(scaled));
        img.setBounds(x, 110, 100, 100);
        this.add(img);

        JLabel texto1 = new JLabel(linea1, SwingConstants.CENTER);
        texto1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        texto1.setForeground(colorTexto);
        texto1.setBounds(x - 10, 210, 140, 20);
        this.add(texto1);

        JLabel texto2 = new JLabel("<html><center><b>" + linea2 + "</b></center></html>", SwingConstants.CENTER);
        texto2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        texto2.setBounds(x - 10, 230, 140, 40);
        this.add(texto2);
    }

    private JButton crearBoton(String texto, Color fondo) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        btn.setForeground(Color.WHITE);
        btn.setBackground(fondo);
        btn.setFocusPainted(false);
        return btn;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Préstamos");
        frame.setContentPane(new prestamos(banco));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
