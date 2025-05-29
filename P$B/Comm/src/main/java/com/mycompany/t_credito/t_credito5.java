package com.mycompany.t_credito;

import com.mycompany.banco.Inicio1;
import com.mycompany.banco.menu;
import com.mycompany.comm.Connection;

import javax.swing.*;
import java.awt.*;

public class t_credito5 extends JPanel {

    private final Color color1 = new Color(51, 102, 51);
    public static Connection banco;
    public String uri;
    
    public t_credito5(Connection database) { 
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

        JPanel comprobante = new JPanel();
        comprobante.setLayout(null);
        comprobante.setBackground(Color.WHITE);
        comprobante.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        comprobante.setBounds(500, 180, 440, 430);
        this.add(comprobante);

        JLabel titulo = new JLabel("OPERACIÓN EXITOSA", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setBounds(0, 90, 440, 30);
        comprobante.add(titulo);

        JLabel lblTitular = new JLabel("<html><i>Nombre del titular</i><br><b>Antonio Manzano</b></html>", SwingConstants.CENTER);
        lblTitular.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblTitular.setBounds(0, 140, 440, 50);
        comprobante.add(lblTitular);

        JLabel numero = new JLabel("2332546798765476", SwingConstants.CENTER);
        numero.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        numero.setForeground(Color.GRAY);
        numero.setBounds(0, 180, 440, 30);
        comprobante.add(numero);

        JLabel lblImporte = new JLabel("<html><i>Importe pagado</i><br><b>$3,456.57</b></html>", SwingConstants.CENTER);
        lblImporte.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblImporte.setBounds(0, 230, 440, 50);
        comprobante.add(lblImporte);

        JLabel aviso = new JLabel("Conserva tu comprobante para cualquier aclaración", SwingConstants.CENTER);
        aviso.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        aviso.setOpaque(true);
        aviso.setBackground(new Color(180, 225, 240));
        aviso.setBounds(20, 360, 400, 30);
        comprobante.add(aviso);

        ImageIcon iconoConfirmar = new ImageIcon(getClass().getResource("/banco/icons/confirmar.png"));
        Image imgConfirmar = iconoConfirmar.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel iconConfirmar = new JLabel(new ImageIcon(imgConfirmar));
        iconConfirmar.setBounds(670, 120, 100, 100);
        this.add(iconConfirmar);
        setComponentZOrder(iconConfirmar, 0);

        JButton btnMenu = crearBotonTexto("Menú Principal", new Color(10, 123, 196));
        btnMenu.setBounds(80, 700, 300, 70);
        btnMenu.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new menu(banco));
            frame.revalidate();
        });
        this.add(btnMenu);

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
        frame.setContentPane(new t_credito5(banco));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}