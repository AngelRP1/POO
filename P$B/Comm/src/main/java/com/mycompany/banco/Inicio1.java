package com.mycompany.banco;

import com.mycompany.comm.Connection;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Inicio1 extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    public static Connection banco;
    public String uri;

    public Inicio1(Connection database) {
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

        JLabel bienvenido = new JLabel("Bienvenido a");
        bienvenido.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        bienvenido.setForeground(Color.WHITE);
        bienvenido.setBounds(40, 35, 300, 50);
        header.add(bienvenido);

        JLabel logo = new JLabel("P$B");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 60));
        logo.setForeground(Color.WHITE);
        logo.setBounds(300, 25, 200, 70);
        header.add(logo);

        this.add(header);

        JLabel eslogan = new JLabel("<html>"
                + "<div style='font-size:38px; line-height:1.2;'>"
                + "<b>Declarado</b> para<br>"
                + "todos,<br>"
                + "<b>protegido</b> como<br>"
                + "<b>ninguno</b>"
                + "</div><br><div style='font-size:20px; margin-top:10px;'>"
                + "Tu clase financiera de confianza"
                + "</div></html>");
        eslogan.setFont(new Font("Segoe UI", Font.PLAIN, 38));
        eslogan.setForeground(Color.WHITE);
        eslogan.setBounds(60, 160, 600, 350);
        this.add(eslogan);

        ImageIcon tarjetaIcon = new ImageIcon(getClass().getResource("/banco/icons/tarjetas.png"));
        Image scaledImg = tarjetaIcon.getImage().getScaledInstance(512, 512, Image.SCALE_SMOOTH);
        JLabel tarjetaLabel = new JLabel(new ImageIcon(scaledImg));
        tarjetaLabel.setBounds(850, 160, 512, 512);
        this.add(tarjetaLabel);

        JButton btnIngresar = new JButton("Ingresar");
        btnIngresar.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        btnIngresar.setBounds(1000, 700, 340, 90);
        btnIngresar.setBackground(new Color(80, 220, 210));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFocusPainted(false);
        this.add(btnIngresar);

        // ✅ Cambiado para usar 'menu' en lugar de 'opciones'
        btnIngresar.addActionListener(e -> {
            JFrame opcionesFrame = new JFrame("Menú de Operaciones");
            opcionesFrame.setContentPane(new menu(banco));
            opcionesFrame.setSize(1440, 900);
            opcionesFrame.setResizable(false);
            opcionesFrame.setLocationRelativeTo(null);
            opcionesFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            opcionesFrame.setVisible(true);
            SwingUtilities.getWindowAncestor(Inicio1.this).dispose();
        });

        JPanel footer = new JPanel();
        footer.setBackground(new Color(18, 64, 99));
        footer.setBounds(0, 800, 1440, 100);
        this.add(footer);
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
        JFrame frame = new JFrame("Pantalla de Inicio");
        Inicio1 panel = new Inicio1(banco);
        frame.setContentPane(panel);
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setVisible(true);
    }
}
