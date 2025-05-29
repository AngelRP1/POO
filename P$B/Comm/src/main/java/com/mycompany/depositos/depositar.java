package com.mycompany.depositos;

import com.mycompany.banco.menu;
import com.mycompany.comm.Connection;
import com.mongodb.client.MongoDatabase;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JOptionPane;
public class depositar extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    public static Connection banco;
    public String uri;

    public depositar(Connection database) {
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
        // Header
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
        JLabel titulo = new JLabel("Ingresa la cuenta o tarjeta");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 150, 1440, 50);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(titulo);

        JLabel subtitulo = new JLabel("Usa el teclado para digitar los números");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setBounds(0, 200, 1440, 30);
        subtitulo.setHorizontalAlignment(SwingConstants.CENTER);

        this.add(subtitulo);

        // Panel blanco
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        panel.setBounds(495, 250, 450, 480);

        JLabel lblTitulo = new JLabel("DATOS DEL DEPÓSITO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setBounds(0, 30, 450, 30);
        panel.add(lblTitulo);

        JLabel lblCuenta = new JLabel("Cuenta o tarjeta", SwingConstants.CENTER);
        lblCuenta.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblCuenta.setBounds(0, 100, 450, 30);
        panel.add(lblCuenta);

        JLabel txtCuenta = new JLabel("Ingresa cuenta o tarjeta", SwingConstants.CENTER);
        txtCuenta.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        txtCuenta.setForeground(Color.GRAY);
        txtCuenta.setBounds(0, 130, 450, 30);
        panel.add(txtCuenta);
        
        JLabel lblDinamico = new JLabel("", SwingConstants.CENTER);
        lblDinamico.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblDinamico.setForeground(Color.BLACK);
        lblDinamico.setBounds(0, 70, 450, 30);  // Posición encima de lblCuenta
        panel.add(lblDinamico);
        this.add(panel);

        // Panel derecho
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(null);
        panelDerecho.setBackground(new Color(10, 123, 196));
        panelDerecho.setBounds(1020, 540, 320, 250);

        JLabel lblCampo = new JLabel("Número de cuenta o tarjeta");
        lblCampo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblCampo.setForeground(Color.WHITE);
        lblCampo.setBounds(20, 10, 280, 30);
        panelDerecho.add(lblCampo);

        JTextField txtNumero = new JTextField();
        txtNumero.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        txtNumero.setBounds(20, 40, 280, 60);
        panelDerecho.add(txtNumero);

        JLabel info = new JLabel("Ingresa y corrige con el teclado");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        info.setForeground(Color.WHITE);
        info.setBounds(20, 110, 280, 30);
        panelDerecho.add(info);

        JButton btnContinuar = crearBotonTexto("Continuar", new Color(10, 123, 196));
        btnContinuar.setBounds(20, 160, 280, 60);
        btnContinuar.addActionListener(e -> {
            String numero = txtNumero.getText().trim();

            if (numero.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese un número", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Método de validación (debe existir en tu clase Connection)
            Object[] validacion = banco.validarNumero(numero);
            MongoDatabase db = (MongoDatabase) validacion[0];
            String nombre = (String) validacion[1];
            String numeroIdentificado = (String) validacion[2];
            String tipo = (String) validacion[3];

            if (db == null) {
                JOptionPane.showMessageDialog(this, "Número no válido. Debe ser:\n" +
                        "- Número de cliente\n" +
                        "- Tarjeta de débito/crédito", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new depositar2(banco, nombre, numeroIdentificado, tipo));
            frame.revalidate();
        });
        panelDerecho.add(btnContinuar);

        this.add(panelDerecho);

        // Botón menú principal
        JButton btnMenu = crearBotonTexto("Menú Principal", new Color(10, 123, 196));
        btnMenu.setBounds(80, 700, 300, 70);
        btnMenu.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new menu(banco));
            frame.revalidate();
        });
        this.add(btnMenu);
        
        txtNumero.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    actualizarLabel();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    actualizarLabel();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    // No necesario para JTextField estándar
                }

                private void actualizarLabel() {
                    String texto = txtNumero.getText();
                    lblDinamico.setText(texto);  // Actualiza el JLabel
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
        JFrame frame = new JFrame("Depósito");
        frame.setContentPane(new depositar(banco));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
