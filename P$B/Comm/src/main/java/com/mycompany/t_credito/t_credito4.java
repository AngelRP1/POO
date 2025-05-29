package com.mycompany.t_credito;

import com.mycompany.banco.menu;
import com.mycompany.comm.Connection;
import com.mycompany.t_credito.t_credito3;
import com.mycompany.t_credito.t_credito5;

import javax.swing.*;
import java.awt.*;

public class t_credito4 extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    public static Connection banco;
    public String uri;
    
    public t_credito4(Connection database) { 
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

        JLabel titulo = new JLabel("Revisa tus datos");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(490, 160, 500, 40);
        this.add(titulo);

        JLabel subtitulo = new JLabel("Revisa que los datos de la tarjeta ingresada sean correctos");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setBounds(420, 200, 700, 30);
        this.add(subtitulo);

        JPanel panelTarjeta = new JPanel();
        panelTarjeta.setLayout(null);
        panelTarjeta.setBackground(Color.WHITE);
        panelTarjeta.setBounds(500, 260, 400, 400);

        JLabel lblTitulo = new JLabel("DATOS DE TU TARJETA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setBounds(0, 20, 400, 30);
        panelTarjeta.add(lblTitulo);

        JLabel lblNombre = new JLabel("<html><i>Nombre del titular</i><br><b>Antonio Manzano</b><br>2332546798765476</html>", SwingConstants.CENTER);
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblNombre.setBounds(0, 70, 400, 60);
        panelTarjeta.add(lblNombre);

        JLabel lblTipo = new JLabel("<html><i>Tipo</i><br><b>Crédito</b></html>", SwingConstants.CENTER);
        lblTipo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblTipo.setBounds(0, 150, 400, 50);
        panelTarjeta.add(lblTipo);

        JLabel lblImporte = new JLabel("<html><i>Importe del adeudo</i><br><b>$3,456.57</b></html>", SwingConstants.CENTER);
        lblImporte.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblImporte.setBounds(0, 220, 400, 50);
        panelTarjeta.add(lblImporte);

        JLabel lblInfo = new JLabel("<html><center>Asegúrate de que la cantidad<br>a pagar sea la correcta</center></html>", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfo.setBounds(100, 290, 200, 40);
        panelTarjeta.add(lblInfo);

        this.add(panelTarjeta);

        JButton btnCancelar = crearBotonTexto("Cancelar", new Color(234, 0, 0));
        btnCancelar.setBounds(80, 700, 300, 70);
        btnCancelar.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new menu(banco));
            frame.revalidate();
        });
        this.add(btnCancelar);

        JButton btnEditar = crearBotonTexto("Editar importe", new Color(10, 123, 196));
        btnEditar.setBounds(1000, 610, 350, 70);
        btnEditar.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new t_credito3(banco));
            frame.revalidate();
        });
        this.add(btnEditar);

        JButton btnContinuar = crearBotonTexto("Continuar", new Color(10, 123, 196));
        btnContinuar.setBounds(1000, 700, 350, 70);
        btnContinuar.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new t_credito5(banco));
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
        JFrame frame = new JFrame("Revisión de tarjeta");
        frame.setContentPane(new t_credito4(banco));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
