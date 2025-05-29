package com.mycompany.servicioefectivo;

import com.mycompany.comm.Connection;
import com.mycompany.servicioefectivo.servicio1_3;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class servicio1_4 extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    public static Connection banco;
    public String uri;
    public static String convenio;
    public static String referencia;
    public static String motivo;
    public static double valor;
    public servicio1_4(Connection database, String convenio, String referencia, String motivo) { 
        this.banco = database;
        this.convenio = convenio;
        this.referencia=referencia;
        this.motivo = motivo;
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

        JLabel titulo = new JLabel("Ingresa el importe");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 150, 1440, 50);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        this.add(titulo);

        JLabel subtitulo = new JLabel("Usa el teclado para digitar el importe a depositar");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setBounds(0, 200, 1440, 40);
        subtitulo.setHorizontalAlignment(SwingConstants.CENTER);

        this.add(subtitulo);

        // Panel de datos centrado y más grande
        JPanel panelPago = new JPanel();
        panelPago.setLayout(null);
        panelPago.setBackground(Color.WHITE);
        panelPago.setBounds(495, 250, 450, 480);

        JLabel lblTitulo = new JLabel("DATOS DE TU PAGO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setBounds(0, 20, 450, 30);
        panelPago.add(lblTitulo);

        JLabel lblServicio = new JLabel("Nombre del servicio", SwingConstants.CENTER);
        lblServicio.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblServicio.setBounds(0, 60, 450, 30);
        panelPago.add(lblServicio);

        JLabel lblClave = new JLabel(motivo, SwingConstants.CENTER);
        lblClave.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblClave.setBounds(0, 85, 450, 30);
        panelPago.add(lblClave);

        JLabel lblNumero = new JLabel(convenio, SwingConstants.CENTER);
        lblNumero.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblNumero.setBounds(0, 110, 450, 30);
        panelPago.add(lblNumero);

        JLabel lblReferencia = new JLabel("Referencia", SwingConstants.CENTER);
        lblReferencia.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblReferencia.setBounds(0, 150, 450, 30);
        panelPago.add(lblReferencia);

        JLabel lblRefNumero = new JLabel(referencia, SwingConstants.CENTER);
        lblRefNumero.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblRefNumero.setBounds(0, 180, 450, 30);
        panelPago.add(lblRefNumero);

        JLabel lblImporte = new JLabel("Importe", SwingConstants.CENTER);
        lblImporte.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblImporte.setBounds(0, 220, 450, 30);
        panelPago.add(lblImporte);

        JLabel lblIngresa = new JLabel("$0.00", SwingConstants.CENTER);
        lblIngresa.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblIngresa.setBounds(0, 245, 450, 30);
        panelPago.add(lblIngresa);

        JLabel aviso = new JLabel("ⓘ Capture el importe exacto del recibo de pago", SwingConstants.CENTER);
        aviso.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        aviso.setOpaque(true);
        aviso.setBackground(new Color(180, 225, 240));
        aviso.setBounds(30, 420, 390, 30);
        panelPago.add(aviso);

        this.add(panelPago);

        // Panel derecho alineado con el botón de continuar
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(null);
        panelDerecho.setBackground(new Color(10, 123, 196));
        panelDerecho.setBounds(1040, 540, 320, 250);

        JLabel lblImportePagar = new JLabel("Importe a pagar");
        lblImportePagar.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblImportePagar.setForeground(Color.WHITE);
        lblImportePagar.setBounds(20, 10, 200, 30);
        panelDerecho.add(lblImportePagar);

        JTextField txtImporte = new JTextField("");
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
        btnContinuar.addActionListener(e -> {
            if (valor > 0) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                frame.setContentPane(new servicio1_5(banco, convenio, referencia, motivo, valor));
                frame.revalidate();
            } else {
                // Mostrar mensaje de advertencia con opción de cierre manual
                JOptionPane optionPane = new JOptionPane("El valor debe ser mayor a $0.00",
                        JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION);

                JDialog dialog = optionPane.createDialog(this, "Valor inválido");
                dialog.setModal(false);
                dialog.setVisible(true);

                // Cerrar automáticamente después de 2 segundos
                new Timer(2000, evt -> dialog.dispose()).start();
            }
        });
        panelDerecho.add(btnContinuar);

        this.add(panelDerecho);

        JButton btnMenu = crearBotonTexto("Menú Pagar", new Color(10, 123, 196));
        btnMenu.setBounds(80, 700, 300, 70);
        btnMenu.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new servicio1_3(banco, "", ""));
            frame.revalidate();
        });
        this.add(btnMenu);
        
        txtImporte.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarImporte();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarImporte();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // No necesario para JTextField estándar
            }

            private void actualizarImporte() {
                String texto = txtImporte.getText();
                // Formatear el texto como cantidad monetaria
                String cantidad;
                try {
                    valor = texto.isEmpty() ? 0.0 : Double.parseDouble(texto);
                    cantidad = String.format("$%,.2f", valor);
                } catch (NumberFormatException ex) {
                    cantidad = "$0.00 (inválido)";
                }

                // Actualizar el JLabel
                lblIngresa.setText("<html><b>" + cantidad + "</b></html>");
            }
        });
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
        JFrame frame = new JFrame("Menú de Operaciones");
        frame.setUndecorated(true);
        frame.setContentPane(new servicio1_4(banco, convenio, referencia, motivo));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}