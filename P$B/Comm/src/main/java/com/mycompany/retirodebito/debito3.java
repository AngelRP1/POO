package com.mycompany.retirodebito;

import com.mycompany.comm.Connection;
import javax.swing.*;
import java.awt.*;

public class debito3 extends JPanel {

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    public static Connection banco;
    public String uri;
    public static String numeroIdentificado;
    private JPasswordField nipField;
    private JLabel nipLabel;
    private JButton registrarBtn;

    public debito3(Connection database, String numeroIdentificado) {
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
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 42)); // más grande
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 180, 1440, 50);
        this.add(titulo);

        // Subtítulo
        JLabel subtitulo = new JLabel("Digita los cuatro números de tu NIP", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 26)); // más grande
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setBounds(0, 230, 1440, 40);
        this.add(subtitulo);

        // Imagen centrada y agrandada
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/banco/icons/digitar.png"));
        Image resizedImage = originalIcon.getImage().getScaledInstance(360, 360, Image.SCALE_SMOOTH);
        JLabel imagen = new JLabel(new ImageIcon(resizedImage));
        imagen.setBounds(540, 300, 360, 360); // centrado
        this.add(imagen);
    
    
        // Campo NIP (inicialmente oculto)
        nipLabel = new JLabel("Ingresa tu NIP de 4 dígitos");
        nipLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        nipLabel.setForeground(Color.WHITE);
        nipLabel.setBounds(530, 650, 380, 30);
        nipLabel.setVisible(false);
        this.add(nipLabel);

        nipField = new JPasswordField(4);
        nipField.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        nipField.setBounds(530, 690, 380, 50);
        nipField.setVisible(true);
        nipField.addActionListener(e -> validarNIP());
        this.add(nipField);

        // Botón de confirmación NIP
        registrarBtn = crearBotonTexto("Confirmar", new Color(0, 150, 0));
        registrarBtn.setBounds(530, 760, 380, 50);
        registrarBtn.setVisible(true);
        registrarBtn.addActionListener(e -> validarNIP());
        this.add(registrarBtn);
        
    }

    private void validarNIP() {
        String nipIngresado = new String(nipField.getPassword());
        String numeroTarjeta = numeroIdentificado;

        if (nipIngresado.length() != 4) {
            mostrarAlerta("El NIP debe tener exactamente 4 dígitos");
            return;
        }

        // Verificar NIP en la base de datos
        boolean nipCorrecto = banco.verificarNIP(numeroTarjeta, nipIngresado);

        if (nipCorrecto) {
            // NIP correcto, proceder con la operación
            JOptionPane.showMessageDialog(this, "NIP correcto. Procesando operación...",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new debito4(banco, numeroIdentificado));
            frame.revalidate();
            // Aquí iría la lógica para continuar con el retiro
        } else {
            mostrarAlerta("NIP incorrecto. Intenta nuevamente.");
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new debito(banco));
            frame.revalidate();
            nipField.setText("");
        }
    }
    
    private void mostrarAlerta(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Alerta", JOptionPane.WARNING_MESSAGE);
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
        JFrame frame = new JFrame("Insertar NIP");
        frame.setContentPane(new debito3(banco, numeroIdentificado));
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
