package com.mycompany.banco;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;
import com.mycompany.comm.Connection;

public class CrearCuenta extends JPanel {

    // Constantes para dimensiones y colores
    private static final int WINDOW_WIDTH = 1440;
    private static final int WINDOW_HEIGHT = 900;
    private static final int HEADER_HEIGHT = 130;
    private static final int FORM_WIDTH = 500;
    private static final int FORM_HEIGHT = 500;
    private static final int BUTTON_WIDTH = 300;
    private static final int BUTTON_HEIGHT = 70;

    // Expresiones regulares para validación
    private static final String NAME_REGEX = "[A-Za-zÁÉÍÓÚáéíóúÑñ ]{3,}";
    private static final String PHONE_REGEX = "\\d{10}";
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    private final Color buttonColor = new Color(10, 123, 196);
    private final Color formColor = Color.WHITE;

    private JTextField[] campos;

    public CrearCuenta() {
        setLayout(null);
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        initComponents();
    }

    private void initComponents() {
        createHeader();
        createTitle();
        createForm();
        createButtons();
    }

    private void createHeader() {
        JPanel header = new JPanel();
        header.setBackground(color1);
        header.setBounds(0, 0, WINDOW_WIDTH, HEADER_HEIGHT);
        header.setLayout(null);

        JLabel logo = new JLabel("P$B");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 60));
        logo.setForeground(Color.WHITE);
        logo.setBounds(40, 30, 200, 70);
        header.add(logo);

        JButton exitButton = new JButton("×");
        exitButton.setBounds(WINDOW_WIDTH - 60, 30, 40, 40);
        exitButton.setFont(new Font("Segoe UI", Font.BOLD, 24));
        exitButton.setForeground(Color.WHITE);
        exitButton.setBackground(new Color(231, 76, 60));
        exitButton.setBorder(BorderFactory.createEmptyBorder());
        exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exitButton.addActionListener(e -> System.exit(0));
        exitButton.setFocusPainted(false);
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                exitButton.setBackground(new Color(192, 57, 43));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                exitButton.setBackground(new Color(231, 76, 60));
            }
        });

        header.add(exitButton);
        add(header);
    }

    private void createTitle() {
        JLabel titulo = new JLabel("Registro de Cuenta Bancaria");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titulo.setForeground(Color.WHITE);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setBounds(0, 150, WINDOW_WIDTH, 40);
        add(titulo);
    }

    private void createForm() {
        JPanel formulario = new JPanel();
        formulario.setLayout(null);
        formulario.setBackground(formColor);
        formulario.setBounds((WINDOW_WIDTH - FORM_WIDTH) / 2, 220, FORM_WIDTH, FORM_HEIGHT);

        String[] etiquetas = {
            "Nombre completo", "Fecha de nacimiento (dd/MM/yyyy)", "Dirección",
            "Teléfono", "Correo electrónico", "Contraseña"
        };

        campos = new JTextField[etiquetas.length];
        for (int i = 0; i < etiquetas.length; i++) {
            addFormField(formulario, etiquetas[i], i);
        }
        add(formulario);
    }

    private void addFormField(JPanel formulario, String labelText, int index) {
        JLabel label = new JLabel(labelText);
        label.setBounds(30, 30 + index * 70, 260, 30);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formulario.add(label);

        if ("Contraseña".equals(labelText)) {
            JPasswordField pass = new JPasswordField();
            pass.setBounds(30, 60 + index * 70, 440, 30);
            formulario.add(pass);
            campos[index] = pass;
        } else {
            JTextField txt = new JTextField();
            txt.setBounds(30, 60 + index * 70, 440, 30);
            formulario.add(txt);
            campos[index] = txt;
        }
    }

    private void createButtons() {
        JButton btnRegistrar = new JButton("Registrar Cuenta");
        styleButton(btnRegistrar);
        btnRegistrar.setBounds(WINDOW_WIDTH - BUTTON_WIDTH - 100, WINDOW_HEIGHT - BUTTON_HEIGHT - 100, BUTTON_WIDTH, BUTTON_HEIGHT);
        btnRegistrar.addActionListener(e -> validateAndRegister());
        add(btnRegistrar);

        JButton btnMenu = new JButton("Menú Principal");
        styleButton(btnMenu);
        btnMenu.setBounds(80, WINDOW_HEIGHT - BUTTON_HEIGHT - 100, BUTTON_WIDTH, BUTTON_HEIGHT);
        btnMenu.addActionListener(e -> returnToMenu());
        add(btnMenu);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        button.setBackground(buttonColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    private void validateAndRegister() {
        String nombre = campos[0].getText().trim();
        String fechaNacimiento = campos[1].getText().trim();
        String direccion = campos[2].getText().trim();
        String telefono = campos[3].getText().trim();
        String email = campos[4].getText().trim();
        String contrasenia = new String(((JPasswordField) campos[5]).getPassword()).trim();

        if (!Pattern.matches(NAME_REGEX, nombre)) {
            showError("Nombre inválido: solo letras y espacios (mínimo 3 caracteres)");
            return;
        }
        if (!validateDate(fechaNacimiento)) return;
        if (direccion.isEmpty() || direccion.length() < 5) {
            showError("Dirección inválida: mínimo 5 caracteres."); return;
        }
        if (!Pattern.matches(PHONE_REGEX, telefono)) {
            showError("Teléfono inválido: debe tener 10 dígitos numéricos."); return;
        }
        if (!Pattern.matches(EMAIL_REGEX, email)) {
            showError("Correo electrónico inválido."); return;
        }
        if (contrasenia.length() < 6) {
            showError("Contraseña inválida: mínimo 6 caracteres."); return;
        }

        // Llamada a la base de datos
        String uri = "mongodb://admin:1234@localhost:27017/?authSource=admin";
        Connection banco = new Connection(uri);
        String numeroCliente = banco.crearCuentaYObtenerNumero(
            nombre, fechaNacimiento, telefono, email, contrasenia);
        banco.cerrarConexion();

        if (numeroCliente == null) {
            showError("No se pudo crear la cuenta. Verifica datos duplicados.");
            return;
        }

        JOptionPane.showMessageDialog(this, "Cuenta registrada con éxito. Número: " + numeroCliente, "Éxito", JOptionPane.INFORMATION_MESSAGE);
        SwingUtilities.invokeLater(() -> {
            AsignarTarjetaFrame frameAsig = new AsignarTarjetaFrame(numeroCliente);
            frameAsig.setVisible(true);

            JFrame current = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (current != null) current.dispose();
        });
    }

    private boolean validateDate(String fecha) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate.parse(fecha, fmt);
            return true;
        } catch (DateTimeParseException e) {
            showError("Fecha de nacimiento inválida. Usa formato dd/MM/yyyy.");
            return false;
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void returnToMenu() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) frame.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), 0, color2);
        g2D.setPaint(gp);
        g2D.fillRect(0, 0, getWidth(), getHeight());
    }
}
