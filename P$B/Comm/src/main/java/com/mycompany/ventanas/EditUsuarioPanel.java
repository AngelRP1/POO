package com.mycompany.ventanas;

import com.mycompany.comm.Connection;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import static com.mongodb.client.model.Filters.eq;

public class EditUsuarioPanel extends JPanel {
    private final Color color1 = new Color(26, 46, 84);
    private final Color color2 = new Color(96, 155, 210);
    private Connection banco;
    private final Color accentColor = new Color(96, 155, 210);
    private List<Document> subgerentes;
    private JComboBox<String> cmbUsuarios;
    private JTextField txtUsername,txtNombre, txtFecha, txtTelefono, txtEmail;
    private JPasswordField txtContrasena;

    public EditUsuarioPanel(Connection database) {
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
        JLabel titulo = new JLabel("Editar Subgerente");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 150, 1440, 50);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(titulo);

        // Panel de formulario
        JPanel formPanel = new JPanel(null);
        formPanel.setBackground(Color.WHITE);
        formPanel.setBounds(270, 220, 900, 500);

        // Lista de subgerentes
        JLabel lblSelect = crearEtiqueta("Seleccione Subgerente:", 50, 20, 300, 30);
        formPanel.add(lblSelect);

        // Cargar subgerentes desde BD
        MongoCollection<Document> col = banco.getDatabase().getCollection("cuentas");
        subgerentes = col.find(eq("esSubgerente", true)).into(new ArrayList<>());
        String[] nombres = subgerentes.stream().map(d -> d.getString("nombre")).toArray(String[]::new);
        cmbUsuarios = new JComboBox<>(nombres);
        cmbUsuarios.setBounds(360, 20, 490, 30);
        cmbUsuarios.addActionListener(e -> cargarDatosSeleccionado());
        formPanel.add(cmbUsuarios);

        // Campo Username (Número de cliente)
        JLabel lblUsername = crearEtiqueta("Username (no editable):", 50, 70, 300, 30);
        txtUsername = crearCampoTexto(360, 70, 490, 30);
        txtUsername.setEditable(false); // Solo lectura
        formPanel.add(lblUsername);
        formPanel.add(txtUsername);

        // Campos de edición
        JLabel lblNombre = crearEtiqueta("Nombre completo:", 50, 120, 300, 30);
        txtNombre = crearCampoTexto(360, 120, 490, 30);
        formPanel.add(lblNombre);
        formPanel.add(txtNombre);

        JLabel lblFecha = crearEtiqueta("Fecha nacimiento (DD/MM/AAAA):", 50, 170, 300, 30);
        txtFecha = crearCampoTexto(360, 170, 490, 30);
        formPanel.add(lblFecha);
        formPanel.add(txtFecha);

        JLabel lblTelefono = crearEtiqueta("Teléfono:", 50, 220, 300, 30);
        txtTelefono = crearCampoTexto(360, 220, 490, 30);
        formPanel.add(lblTelefono);
        formPanel.add(txtTelefono);

        JLabel lblEmail = crearEtiqueta("Email:", 50, 270, 300, 30);
        txtEmail = crearCampoTexto(360, 270, 490, 30);
        formPanel.add(lblEmail);
        formPanel.add(txtEmail);

        JLabel lblContrasena = crearEtiqueta("Contraseña:", 50, 320, 300, 30);
        txtContrasena = new JPasswordField();
        txtContrasena.setBounds(360, 320, 490, 30);
        txtContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(lblContrasena);
        formPanel.add(txtContrasena);

        // Botón actualizar
        JButton btnActualizar = crearBoton("Guardar Cambios", new Color(50, 150, 50), 20);
        btnActualizar.setBounds(360, 380, 490, 50);
        btnActualizar.addActionListener(e -> actualizarSubgerente());
        formPanel.add(btnActualizar);

        add(formPanel);

        // Cargar datos iniciales
        if (!subgerentes.isEmpty()) cargarDatosSeleccionado();
        
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
    }

    private void cargarDatosSeleccionado() {
        int idx = cmbUsuarios.getSelectedIndex();
        if (idx < 0) return;
        Document doc = subgerentes.get(idx);
        txtUsername.setText(doc.getString("numero_cliente"));
        txtNombre.setText(doc.getString("nombre"));
        txtFecha.setText(doc.getString("fecha_nacimiento"));
        txtTelefono.setText(doc.getString("telefono"));
        txtEmail.setText(doc.getString("email"));
        txtContrasena.setText(doc.getString("contrasena"));
    }
    
    private void styleButton(JButton btn, Color bg, int fontSize) {
        btn.setFont(new Font("Segoe UI", Font.PLAIN, fontSize));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private void actualizarSubgerente() {
        int idx = cmbUsuarios.getSelectedIndex();
        if (idx < 0) return;
        Document original = subgerentes.get(idx);
        ObjectId id = original.getObjectId("_id");

        String user = txtUsername.getText().trim();
        String nombre = txtNombre.getText().trim();
        String fecha = txtFecha.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String email = txtEmail.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        if (user.isEmpty() || nombre.isEmpty() || fecha.isEmpty() || telefono.isEmpty() || email.isEmpty() || contrasena.isEmpty()) {
            mostrarError("Todos los campos son obligatorios");
            return;
        }

        try {
            MongoCollection<Document> col = banco.getDatabase().getCollection("cuentas");
            Document updated = new Document("$set", new Document()
                .append("numero_cliente", user)
                .append("nombre", nombre)
                .append("fecha_nacimiento", fecha)
                .append("telefono", telefono)
                .append("email", email)
                .append("contraseña", contrasena)
            );
            col.updateOne(eq("_id", id), updated);
            mostrarExito("Subgerente actualizado exitosamente");
            // Actualizar lista local
            original.put("numero_cliente", user);
            original.put("nombre", nombre);
            original.put("fecha_nacimiento", fecha);
            original.put("telefono", telefono);
            original.put("email", email);
            original.put("contraseña", contrasena);
        } catch (Exception ex) {
            mostrarError("Error al actualizar: " + ex.getMessage());
        }
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

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarExito(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Éxito", JOptionPane.INFORMATION_MESSAGE);
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
