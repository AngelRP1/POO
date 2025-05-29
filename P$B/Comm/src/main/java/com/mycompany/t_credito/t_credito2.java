package com.mycompany.t_credito;

import com.mycompany.comm.Connection;
import javax.swing.*;
import java.awt.*;

public class t_credito2 extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    public static Connection banco;
    public String uri;
    
    public t_credito2(Connection database) { 
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
        // Encabezado
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

        // Título principal
        JLabel titulo = new JLabel("Inserta tu NIP", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 180, 1440, 50);
        this.add(titulo);

        // Subtítulo
        JLabel subtitulo = new JLabel("Digita los cuatro números de tu NIP", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setBounds(0, 230, 1440, 40);
        this.add(subtitulo);

        // Imagen
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/banco/icons/digitar.png"));
        Image resizedImage = originalIcon.getImage().getScaledInstance(360, 360, Image.SCALE_SMOOTH);
        JLabel imagen = new JLabel(new ImageIcon(resizedImage));
        imagen.setBounds(540, 300, 360, 360);
        this.add(imagen);

        // Botón continuar
        JButton btnContinuar = new JButton("Continuar");
        btnContinuar.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        btnContinuar.setBackground(new Color(10, 123, 196));
        btnContinuar.setForeground(Color.WHITE);
        btnContinuar.setFocusPainted(false);
        btnContinuar.setBounds(1060, 700, 300, 70);
        btnContinuar.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new t_credito3(banco));
            frame.revalidate();
        });
        this.add(btnContinuar);
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
        JFrame frame = new JFrame("Insertar NIP");
        frame.setContentPane(new t_credito2(banco));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
