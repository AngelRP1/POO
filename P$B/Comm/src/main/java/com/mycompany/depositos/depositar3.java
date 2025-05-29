package com.mycompany.depositos;

import com.mycompany.comm.Connection;
import com.mycompany.depositos.depositar2;
import com.mycompany.depositos.depositar4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.*;


public class depositar3 extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    public static Connection banco;
    public String uri;
    public static String numeroIdentificado;
    public static String tipo;
    public static String nombre;
    public static double valor;
    public static JTextField txtMotivo;
    private JWindow tecladoWindow;
    public depositar3(Connection database, String nombre, String numeroIdentificado, String tipo, double valor) {
        this.banco = database;
        this.tipo = tipo;
        this.numeroIdentificado = numeroIdentificado;
        this.tipo = tipo;
        this.valor = valor;
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

        // Títulos centrados
        JLabel titulo = new JLabel("Ingresa el motivo de pago", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 160, 1440, 40);
        this.add(titulo);

        JLabel subtitulo = new JLabel("Agrega un concepto al depósito", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setBounds(0, 200, 1440, 30);
        this.add(subtitulo);

        // Panel central azul con campo de texto
        JPanel panelMotivo = new JPanel();
        panelMotivo.setBounds(370, 270, 700, 100);
        panelMotivo.setLayout(null);
        panelMotivo.setBackground(new Color(10, 123, 196)); // Fondo azul

        txtMotivo = new JTextField();
        txtMotivo.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        txtMotivo.setBounds(20, 10, 540, 70);
        panelMotivo.add(txtMotivo);

        JButton btnLimpiar = new JButton("limpiar");
        btnLimpiar.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        btnLimpiar.setBackground(new Color(234, 50, 35));
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setBounds(570, 10, 100, 70);
        btnLimpiar.addActionListener(e -> txtMotivo.setText(""));
        panelMotivo.add(btnLimpiar);

        this.add(panelMotivo);

        // Botón "Regresar"
        JButton btnRegresar = crearBotonTexto("Regresar", new Color(10, 123, 196));
        btnRegresar.setBounds(80, 700, 300, 70);
        btnRegresar.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new depositar2(banco,nombre, numeroIdentificado, tipo));
            frame.revalidate();
        });
        this.add(btnRegresar);

        // Botón "Continuar"
        JButton btnContinuar = crearBotonTexto("Continuar", new Color(10, 123, 196));
        btnContinuar.setBounds(1000, 700, 300, 70);
        btnContinuar.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new depositar4(banco,nombre, numeroIdentificado, tipo, valor, txtMotivo.getText()));
            frame.revalidate();
        });
        this.add(btnContinuar);
        txtMotivo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarTeclado(txtMotivo);
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
        JFrame frame = new JFrame("Motivo de Pago");
        frame.setContentPane(new depositar3(banco,nombre, numeroIdentificado, tipo, valor));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


    private void mostrarTeclado(JTextField campoTexto) {
        JWindow tecladoWindow = new JWindow((Window) SwingUtilities.getWindowAncestor(this));

        JPanel panelTeclado = new JPanel(new GridLayout(4, 10, 8, 8));
        panelTeclado.setBackground(new Color(245, 245, 245));
        panelTeclado.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        String[] teclas = {
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
                "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P",
                "A", "S", "D", "F", "G", "H", "J", "K", "L", "←",
                "Z", "X", "C", "V", "B", "N", "M", "Espacio", "Limpiar", "Cerrar"
        };

        for (String tecla : teclas) {
            JButton boton = new JButton(tecla);
            boton.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            boton.setFocusPainted(false);
            boton.setBackground(Color.WHITE);
            boton.setForeground(Color.DARK_GRAY);
            boton.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            boton.setContentAreaFilled(false);
            boton.setOpaque(true);
            boton.setPreferredSize(new Dimension(70, 55));

            boton.addActionListener(e -> {
                String texto = campoTexto.getText();
                switch (tecla) {
                    case "←":
                        if (!texto.isEmpty()) campoTexto.setText(texto.substring(0, texto.length() - 1));
                        break;
                    case "Espacio":
                        campoTexto.setText(texto + " ");
                        break;
                    case "Limpiar":
                        campoTexto.setText("");
                        break;
                    case "Cerrar":
                        tecladoWindow.dispose();
                        break;
                    default:
                        campoTexto.setText(texto + tecla);
                }
            });

            panelTeclado.add(boton);
        }

        tecladoWindow.getContentPane().add(panelTeclado);
        tecladoWindow.pack();

        // Posicionar el teclado ligeramente a la izquierda y debajo del campo de texto
        Point campoPantalla = campoTexto.getLocationOnScreen();
        tecladoWindow.setLocation(campoPantalla.x - 70, campoPantalla.y + campoTexto.getHeight() + 20);

        // Cerrar el teclado al perder el foco
        tecladoWindow.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                tecladoWindow.dispose();
            }
        });

        tecladoWindow.setAlwaysOnTop(true);
        tecladoWindow.setVisible(true);
    }


}
