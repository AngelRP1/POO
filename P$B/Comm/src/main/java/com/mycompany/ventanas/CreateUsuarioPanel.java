package com.mycompany.ventanas;

import com.mycompany.comm.Connection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import static com.mongodb.client.model.Filters.eq;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class CreateUsuarioPanel extends JPanel {
    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    private final Color accentColor = new Color(96, 155, 210);
    private Connection banco;

    public CreateUsuarioPanel(Connection database) {
        this.banco = database;
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
        add(header);

        // Título
        JLabel titulo = new JLabel("Crear Subgerente");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 150, 1440, 50);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(titulo);

        // Formulario de Subgerente
        JPanel formPanel = new JPanel(null);
        formPanel.setBackground(Color.WHITE);
        formPanel.setBounds(270, 220, 900, 500);

        // Username (numero_cliente)
        JLabel lblUsername = crearEtiqueta("Username:", 50, 20, 300, 30);
        JTextField txtUsername = crearCampoTexto(360, 20, 490, 30);
        formPanel.add(lblUsername);
        formPanel.add(txtUsername);

        JLabel lblNombre = crearEtiqueta("Nombre completo:", 50, 70, 300, 30);
        JTextField txtNombre = crearCampoTexto(360, 70, 490, 30);

        JLabel lblFecha = crearEtiqueta("Fecha nacimiento (DD/MM/AAAA):", 50, 120, 300, 30);
        JTextField txtFecha = crearCampoTexto(360, 120, 490, 30);

        JLabel lblTelefono = crearEtiqueta("Teléfono:", 50, 170, 300, 30);
        JTextField txtTelefono = crearCampoTexto(360, 170, 490, 30);

        JLabel lblEmail = crearEtiqueta("Email:", 50, 220, 300, 30);
        JTextField txtEmail = crearCampoTexto(360, 220, 490, 30);

        JLabel lblContrasena = crearEtiqueta("Contraseña:", 50, 270, 300, 30);
        JPasswordField txtContrasena = new JPasswordField();
        txtContrasena.setBounds(360, 270, 490, 30);
        txtContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JButton btnCrear = crearBoton("Crear Subgerente", new Color(50, 150, 50), 20);
        btnCrear.setBounds(360, 350, 490, 50);
        btnCrear.addActionListener(e -> {
            String username    = txtUsername.getText().trim();
            String nombre      = txtNombre.getText().trim();
            String fecha       = txtFecha.getText().trim();
            String telefono    = txtTelefono.getText().trim();
            String email       = txtEmail.getText().trim();
            String contrasena  = new String(txtContrasena.getPassword());

            if (username.isEmpty() || nombre.isEmpty() || fecha.isEmpty() || telefono.isEmpty()
                || email.isEmpty() || contrasena.isEmpty()) {
                mostrarError("Todos los campos son obligatorios");
                return;
            }

            try {
                MongoCollection<Document> col = banco.getDatabase().getCollection("cuentas");
                Document doc = new Document()
                    .append("numero_cliente", username)
                    .append("nombre", nombre)
                    .append("fecha_nacimiento", fecha)
                    .append("telefono", telefono)
                    .append("email", email)
                    .append("contrasena", contrasena)
                    .append("rango", "Subgerente")
                    .append("esSubgerente", true);
                col.insertOne(doc);
                mostrarExito("Subgerente creado exitosamente");
                limpiarCampos(txtUsername, txtNombre, txtFecha, txtTelefono, txtEmail, txtContrasena);
            } catch (Exception ex) {
                mostrarError("Error al crear el subgerente: " + ex.getMessage());
            }
        });
        
        // Botón Menú Principal
        JButton btnMenu = new JButton("Menú Principal");
        styleButton(btnMenu, accentColor, 20);
        btnMenu.setBounds(80, 790, 300, 70);
        btnMenu.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.dispose();
            SeleccionarUsuario su = new SeleccionarUsuario(banco);
            su.setVisible(true);
        });
        add(btnMenu);

        formPanel.add(lblNombre);
        formPanel.add(txtNombre);
        formPanel.add(lblFecha);
        formPanel.add(txtFecha);
        formPanel.add(lblTelefono);
        formPanel.add(txtTelefono);
        formPanel.add(lblEmail);
        formPanel.add(txtEmail);
        formPanel.add(lblContrasena);
        formPanel.add(txtContrasena);
        formPanel.add(btnCrear);
        add(formPanel);
        
    }

    private JLabel crearEtiqueta(String texto, int x, int y, int w, int h) {
        JLabel label = new JLabel(texto);
        label.setBounds(x, y, w, h);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return label;
    }

    private JTextField crearCampoTexto(int x, int y, int w, int h) {
        JTextField campo = new JTextField();
        campo.setBounds(x, y, w, h);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return campo;
    }

    private JButton crearBoton(String texto, Color color, int tamFuente) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, tamFuente));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return btn;
    }
    
    private void styleButton(JButton btn, Color bg, int fontSize) {
        btn.setFont(new Font("Segoe UI", Font.PLAIN, fontSize));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarExito(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void limpiarCampos(JComponent... comps) {
        for (JComponent c : comps) {
            if (c instanceof JTextField) ((JTextField) c).setText("");
            else if (c instanceof JPasswordField) ((JPasswordField) c).setText("");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, color1, w, 0, color2);
        g2.setPaint(gp);
        g2.fillRect(0, 0, w, h);
    }
}
