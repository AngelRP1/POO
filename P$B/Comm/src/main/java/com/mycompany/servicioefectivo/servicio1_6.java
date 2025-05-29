package com.mycompany.servicioefectivo;

import com.mycompany.comm.Connection;
import static com.mycompany.servicioefectivo.servicio1_5.banco;
import java.awt.*;
import javax.swing.*;
import com.mycompany.servicioefectivo.servicio1_7;

public class servicio1_6 extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    public static Connection banco;
    public String uri;
    public static String convenio;
    public static String referencia;
    public static String motivo;
    public static double valor;
    public servicio1_6(Connection database, String convenio, String referencia, String motivo, double valor) {
        this.banco = database;
        this.convenio = convenio;
        this.referencia=referencia;
        this.motivo = motivo;
        this.valor = valor;
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

        // Títulos
        JLabel titulo = new JLabel("Revisa y autoriza");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 150, 1440, 50);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        this.add(titulo);

        JLabel subtitulo = new JLabel("Verifica que los datos del pago sean correctos");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setBounds(0, 200, 1440, 40);
        subtitulo.setHorizontalAlignment(SwingConstants.CENTER);

        this.add(subtitulo);

        // Panel de datos centrado
        JPanel panelPago = new JPanel();
        panelPago.setLayout(null);
        panelPago.setBackground(Color.WHITE);
        panelPago.setBounds(510, 260, 420, 400); // centrado

        JLabel lblTitulo = new JLabel("DATOS DE TU PAGO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setBounds(0, 20, 420, 30);
        panelPago.add(lblTitulo);

        JLabel lblServicio = new JLabel("Nombre del servicio", SwingConstants.CENTER);
        lblServicio.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblServicio.setBounds(0, 60, 420, 30);
        panelPago.add(lblServicio);

        JLabel lblClave = new JLabel(motivo, SwingConstants.CENTER);
        lblClave.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblClave.setBounds(0, 85, 420, 30);
        panelPago.add(lblClave);

        JLabel lblNumero = new JLabel(convenio, SwingConstants.CENTER);
        lblNumero.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblNumero.setBounds(0, 110, 420, 30);
        panelPago.add(lblNumero);

        JLabel lblReferencia = new JLabel("Referencia", SwingConstants.CENTER);
        lblReferencia.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblReferencia.setBounds(0, 150, 420, 30);
        panelPago.add(lblReferencia);

        JLabel lblRefNumero = new JLabel(referencia, SwingConstants.CENTER);
        lblRefNumero.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblRefNumero.setBounds(0, 180, 420, 30);
        panelPago.add(lblRefNumero);

        JLabel lblImporte = new JLabel("Importe", SwingConstants.CENTER);
        lblImporte.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblImporte.setBounds(0, 220, 420, 30);
        panelPago.add(lblImporte);

        JLabel lblIngresa = new JLabel(String.format("$%,.2f", valor), SwingConstants.CENTER);
        lblIngresa.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblIngresa.setBounds(0, 245, 420, 30);
        panelPago.add(lblIngresa);

        this.add(panelPago);

        // Botón "Pagar"
        JButton btnPagar = crearBotonTexto("Pagar", new Color(10, 123, 196));
        btnPagar.setBounds(1040, 700, 300, 70);
        btnPagar.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new servicio1_8(banco, convenio, referencia, motivo, valor));
            frame.revalidate();
        });
        this.add(btnPagar);
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
        JFrame frame = new JFrame("Verificación de Pago");
        frame.setUndecorated(true);
        frame.setContentPane(new servicio1_6(banco, convenio, referencia,motivo,valor));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}