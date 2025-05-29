package com.mycompany.t_credito;

import com.mycompany.banco.menu;
import com.mycompany.comm.Connection;
import com.mycompany.t_credito.t_credito4;

import javax.swing.*;
import java.awt.*;

public class t_credito3 extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    public static Connection banco;
    public String uri;
    
    public t_credito3(Connection database) { 
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

        JLabel titulo = new JLabel("Ingresa el número de tu tarjeta de crédito");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(400, 150, 800, 40);
        this.add(titulo);

        JLabel subtitulo = new JLabel("El número de tarjeta debe de ser de 16 dígitos");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setBounds(420, 190, 700, 30);
        this.add(subtitulo);

        JPanel panelTarjeta = new JPanel();
        panelTarjeta.setLayout(null);
        panelTarjeta.setBackground(Color.WHITE);
        panelTarjeta.setBounds(480, 250, 350, 350);

        JLabel lblTitulo = new JLabel("DATOS DE LA TARJETA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setBounds(0, 20, 350, 30);
        panelTarjeta.add(lblTitulo);

        JLabel lblNum = new JLabel("<html><i>Número de tarjeta</i><br>Ingresa cuenta o tarjeta</html>", SwingConstants.CENTER);
        lblNum.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblNum.setBounds(0, 100, 350, 50);
        panelTarjeta.add(lblNum);

        JLabel lblTipo = new JLabel("<html><i>Tipo</i><br><b>Crédito</b></html>", SwingConstants.CENTER);
        lblTipo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblTipo.setBounds(0, 190, 350, 50);
        panelTarjeta.add(lblTipo);

        this.add(panelTarjeta);

        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(null);
        panelDerecho.setBackground(new Color(10, 123, 196));
        panelDerecho.setBounds(1000, 540, 320, 250);

        JLabel lblInput = new JLabel("Número de tarjeta");
        lblInput.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblInput.setForeground(Color.WHITE);
        lblInput.setBounds(20, 10, 280, 30);
        panelDerecho.add(lblInput);

        JTextField txtTarjeta = new JTextField("");
        txtTarjeta.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        txtTarjeta.setBounds(20, 40, 280, 60);
        panelDerecho.add(txtTarjeta);

        JLabel info = new JLabel("Ingresa y corrige con el teclado");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        info.setForeground(Color.WHITE);
        info.setBounds(20, 110, 280, 30);
        panelDerecho.add(info);

        JButton btnContinuar = crearBotonTexto("Continuar", new Color(10, 123, 196));
        btnContinuar.setBounds(20, 160, 280, 60);
        btnContinuar.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new t_credito4(banco));
            frame.revalidate();
        });
        panelDerecho.add(btnContinuar);

        this.add(panelDerecho);

        JButton btnCancelar = crearBotonTexto("Cancelar", new Color(234, 0, 0));
        btnCancelar.setBounds(80, 700, 300, 70);
        btnCancelar.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new menu(banco));
            frame.revalidate();
        });
        this.add(btnCancelar);
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
        JFrame frame = new JFrame("Verificar tarjeta crédito");
        frame.setContentPane(new t_credito3(banco));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
