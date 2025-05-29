package com.mycompany.banco;

import com.mycompany.comm.Connection;
import com.mycompany.depositos.depositar;
import java.awt.*;
import javax.swing.*;

public class menu extends JPanel {
    
    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    public static Connection banco;
    public String uri;

    public menu(Connection database) {
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

        JLabel titulo = new JLabel("Inicio");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(660, 150, 200, 40);
        this.add(titulo);

        JLabel subtitulo = new JLabel("Selecciona una operación");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setBounds(570, 190, 350, 30);
        this.add(subtitulo);

        // BOTÓN DEPOSITAR
        JButton btnDepositar = crearBotonConImagen("Depositar", "banco/icons/depositar.png", new Color(78, 233, 214));
        btnDepositar.setBounds(120, 250, 360, 360);
        btnDepositar.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new depositar(banco));
            frame.revalidate();
        });
        this.add(btnDepositar);

        // BOTÓN PAGAR
        JButton btnPagar = crearBotonConImagen("Pagar", "banco/icons/pagar.png", new Color(88, 80, 220));
        btnPagar.setBounds(540, 250, 360, 360);
        btnPagar.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new pagar(banco));
            frame.revalidate();
        });
        this.add(btnPagar);

        // BOTÓN RETIRAR
        JButton btnRetirar = crearBotonConImagen("Retirar", "banco/icons/retirar.png", new Color(12, 123, 196));
        btnRetirar.setBounds(960, 250, 360, 360);
        btnRetirar.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new retirar(banco));
            frame.revalidate();
        });
        this.add(btnRetirar);

        // BOTONES INFERIORES
        /*
        JButton btnOportunidades = crearBotonTexto("Oportunidades", new Color(255, 153, 51));
        btnOportunidades.setBounds(120, 640, 360, 85);
        this.add(btnOportunidades);

        JButton btnTiempoAire = crearBotonTexto("Tiempo Aire", new Color(10, 56, 94));
        btnTiempoAire.setBounds(960, 640, 360, 85);
        this.add(btnTiempoAire);*/
    }

    private JButton crearBotonConImagen(String texto, String nombreImagen, Color fondo) {
        ImageIcon icon = new ImageIcon(getClass().getResource("/" + nombreImagen));
        Image img = icon.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);

        JButton boton = new JButton(texto, icon);
        boton.setHorizontalTextPosition(SwingConstants.CENTER);
        boton.setVerticalTextPosition(SwingConstants.BOTTOM);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        boton.setBackground(fondo);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        return boton;
    }

    private JButton crearBotonTexto(String texto, Color fondo) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 26));
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
        
        JFrame frame = new JFrame("Menú");
        frame.setUndecorated(true);
        frame.setContentPane(new menu(banco));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
