package com.mycompany.depositos;

import com.mycompany.depositos.depositar;
import com.mycompany.banco.menu;
import com.mycompany.comm.Connection;
import javax.swing.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JOptionPane;

public class depositar2 extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    
    public static String numeroIdentificado;
    public static String tipo;
    public static String nombre;
    public static double valor;
    public static Connection banco;
    public String uri;
    

    public depositar2(Connection database, String nombre, String numeroIdentificado, String tipo) {
        this.banco = database;
        this.tipo = tipo;
        this.numeroIdentificado = numeroIdentificado;
        this.nombre = nombre;
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

        JLabel titulo = new JLabel("Ingresa el importe a depositar");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 150, 1440, 50);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(titulo);

        JLabel subtitulo = new JLabel("Usa el teclado para digitar el importe a depositar");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setBounds(0, 200, 1440, 30);
        subtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(subtitulo);

        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(null);
        panelDatos.setBackground(Color.WHITE);
        panelDatos.setBounds(495, 250, 450, 480);

        JLabel lblTitulo = new JLabel("DATOS DE TU DEPÓSITO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setBounds(0, 20, 450, 30);
        panelDatos.add(lblTitulo);

        JLabel lblTitular = new JLabel("<html><i>Nombre del titular</i><br><b>"+ nombre+ "</b><br><span style='color:gray'>"+ numeroIdentificado+ "</span></html>", SwingConstants.CENTER);
        lblTitular.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblTitular.setBounds(0, 80, 450, 60);
        panelDatos.add(lblTitular);

        JLabel lblImporte = new JLabel("<html><i>Importe a depositar</i><br><b>$0.00</b></html>", SwingConstants.CENTER);
        lblImporte.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblImporte.setBounds(0, 160, 450, 60);
        panelDatos.add(lblImporte);

        this.add(panelDatos);

        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(null);
        panelDerecho.setBackground(new Color(10, 123, 196));
        panelDerecho.setBounds(1020, 540, 320, 250);
        
        JTextField txtNumero = new JTextField();
        txtNumero.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        txtNumero.setBounds(20, 40, 280, 60);
        panelDerecho.add(txtNumero);
        
        JLabel lblCantidad = new JLabel("Cantidad a depositar");
        lblCantidad.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblCantidad.setForeground(Color.WHITE);
        lblCantidad.setBounds(20, 10, 280, 30);
        panelDerecho.add(lblCantidad);

        JLabel info = new JLabel("Ingresa y corrige con el teclado");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        info.setForeground(Color.WHITE);
        info.setBounds(20, 110, 280, 30);
        panelDerecho.add(info);

        JButton btnContinuar = crearBotonTexto("Continuar", new Color(10, 123, 196));
        btnContinuar.setBounds(20, 160, 280, 60);
        btnContinuar.addActionListener(e -> {
            if (valor > 0 && valor < 10001) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                frame.setContentPane(new depositar3(banco, nombre, numeroIdentificado, tipo, valor));
                frame.revalidate();
            } else if(valor > 10000){
                JOptionPane optionPane = new JOptionPane("El valor debe ser menor a $10.000, acude a Ventanilla",
                JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION);
                JDialog dialog = optionPane.createDialog(this, "Valor inválido");
                dialog.setModal(false);
                dialog.setVisible(true);

                // Cerrar automáticamente después de 2 segundos
                new Timer(2000, evt -> dialog.dispose()).start();
                }else {
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

        JButton btnVolver = crearBotonTexto("Volver", new Color(10, 123, 196));
        btnVolver.setBounds(80, 700, 300, 70);
        btnVolver.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new depositar(banco));
            frame.revalidate();
        });
        this.add(btnVolver);

        txtNumero.getDocument().addDocumentListener(new DocumentListener() {
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
                String texto = txtNumero.getText();
                // Formatear el texto como cantidad monetaria
                String cantidad;
                try {
                    valor = texto.isEmpty() ? 0.0 : Double.parseDouble(texto);
                    cantidad = String.format("$%,.2f", valor);
                } catch (NumberFormatException ex) {
                    cantidad = "$0.00 (inválido)";
                }

                // Actualizar el JLabel
                lblImporte.setText("<html><i>Importe a depositar</i><br><b>" + cantidad + "</b></html>");
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
        frame.setContentPane(new depositar2(banco,nombre, numeroIdentificado, tipo));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
