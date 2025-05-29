package com.mycompany.retirodebito; 

import com.mycompany.banco.menu;
import com.mycompany.comm.Connection;
import com.mycompany.retirodebito.debito5;

import javax.swing.*;
import java.awt.*;

public class debito4 extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    public static Connection banco;
    public String uri;
    public static JTextField txtImporte;
    public static String numeroIdentificado;

    public debito4(Connection database, String numeroIdentificado) {
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

        JLabel titulo = new JLabel("Ingresa el importe a retirar");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 150, 1440, 50);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(titulo);

        JLabel subtitulo = new JLabel("Usa el teclado para digitar el importe a retirar");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setBounds(0, 200, 1440, 40);
        subtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(subtitulo);

        // Panel de tarjeta blanco
        JPanel panelTarjeta = new JPanel();
        panelTarjeta.setLayout(null);
        panelTarjeta.setBackground(Color.WHITE);
        panelTarjeta.setBounds(550, 250, 350, 400);

        JLabel lblTitulo = new JLabel("DATOS DE TU TARJETA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setBounds(0, 20, 350, 30);
        panelTarjeta.add(lblTitulo);

        JLabel lblNombre = new JLabel("<html><i>Nombre del titular</i><br><b>"+ banco.obtenerNombrePorTarjeta(numeroIdentificado)+"</b></html>", SwingConstants.CENTER);
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblNombre.setBounds(0, 80, 350, 50);
        panelTarjeta.add(lblNombre);

        JLabel lblTarjeta = new JLabel("<html><i>Número de tarjeta</i><br><b>"+ numeroIdentificado+"</b></html>", SwingConstants.CENTER);
        lblTarjeta.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblTarjeta.setBounds(0, 150, 350, 50);
        panelTarjeta.add(lblTarjeta);

        JLabel lblTipo = new JLabel("<html><i>Tipo</i><br><b>Débito</b></html>", SwingConstants.CENTER);
        lblTipo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblTipo.setBounds(0, 220, 350, 50);
        panelTarjeta.add(lblTipo);

        this.add(panelTarjeta);

        // Panel derecho
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(null);
        panelDerecho.setBackground(new Color(10, 123, 196));
        panelDerecho.setBounds(1020, 540, 320, 250);

        JLabel lblImporte = new JLabel("Cantidad a retirar");
        lblImporte.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblImporte.setForeground(Color.WHITE);
        lblImporte.setBounds(20, 10, 280, 30);
        panelDerecho.add(lblImporte);

        txtImporte = new JTextField("$");
        txtImporte.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        txtImporte.setBounds(20, 40, 280, 60);
        panelDerecho.add(txtImporte);

        JLabel info = new JLabel("Ingresa y corrige con el teclado");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        info.setForeground(Color.WHITE);
        info.setBounds(20, 110, 280, 30);
        panelDerecho.add(info);

        JButton btnContinuar = crearBotonTexto("Continuar", new Color(10, 123, 196));
        btnContinuar.setBounds(20, 160, 280, 60);
        btnContinuar.addActionListener(e -> validarYContinuar());
        panelDerecho.add(btnContinuar);

        this.add(panelDerecho);


        // Botón cancelar (rojo)
        JButton btnCancelar = crearBotonTexto("Cancelar", new Color(234, 0, 0));
        btnCancelar.setBounds(80, 700, 300, 70);
        btnCancelar.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new menu(banco));
            frame.revalidate();
        });
        this.add(btnCancelar);
    }

    private void validarYContinuar() {
        try {
            String importeStr = txtImporte.getText().replace("$", "").trim();
            if(importeStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese un monto válido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double monto = Double.parseDouble(importeStr);
            if(monto <= 0 || monto > 10000) {
                JOptionPane.showMessageDialog(this, "El monto debe ser mayor a $0.00", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if(monto > 10000){
                JOptionPane.showMessageDialog(this, "El monto debe ser menor a $10000, si quieres retirar una cantidad mayor ve a Ventanilla", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar saldo disponible
            double saldoDisponible = banco.obtenerSaldoTarjeta(numeroIdentificado);
            if(monto > saldoDisponible) {
                JOptionPane.showMessageDialog(this,
                        "Saldo insuficiente. Su saldo disponible es: $" + saldoDisponible,
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Continuar al siguiente paso
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new debito5(banco, numeroIdentificado, monto));
            frame.revalidate();

        } catch(NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un monto numérico válido", "Error", JOptionPane.ERROR_MESSAGE);
        }
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
        JFrame frame = new JFrame("Retiro con tarjeta");
        frame.setContentPane(new debito4(banco, numeroIdentificado));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}