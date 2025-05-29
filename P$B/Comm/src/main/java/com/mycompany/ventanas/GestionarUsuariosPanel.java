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

public class GestionarUsuariosPanel extends JPanel {
    private final Color headerColor = new Color(26, 46, 84);
    private final Color accentColor = new Color(96, 155, 210);
    private Connection banco;
    private JTable table;
    private DefaultTableModel model;
    private List<Document> subgerentes;

    public GestionarUsuariosPanel(Connection database) {
        this.banco = database;
        setLayout(null);
        setPreferredSize(new Dimension(1440, 900));
        initComponents();
    }

    private void initComponents() {
        // Título
        JLabel title = new JLabel("Gestionar Subgerentes");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setBounds(0, 150, 1440, 50);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title);

        // Tabla de subgerentes (se agregó el campo "Username")
        String[] cols = {"ID", "Username", "Nombre", "Fecha Nac.", "Teléfono", "Email"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(100, 220, 1240, 400);
        add(scroll);

        // Cargar datos
        cargarSubgerentes();

        // Botón Eliminar
        JButton btnEliminar = new JButton("Eliminar Seleccionado");
        styleButton(btnEliminar, new Color(200, 50, 50), 18);
        btnEliminar.setBounds(540, 650, 360, 50);
        btnEliminar.addActionListener(e -> eliminarSelected());
        add(btnEliminar);

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
    }

    private void cargarSubgerentes() {
        model.setRowCount(0);
        subgerentes = new ArrayList<>();
        MongoDatabase db = banco.getDatabase();
        MongoCollection<Document> col = db.getCollection("cuentas");
        col.find(eq("esSubgerente", true)).into(subgerentes);
        for (Document doc : subgerentes) {
            ObjectId id = doc.getObjectId("_id");
            String username = doc.getString("numero_cliente"); // nuevo campo
            String nombre = doc.getString("nombre");
            String fecha = doc.getString("fecha_nacimiento");
            String tel = doc.getString("telefono");
            String email = doc.getString("email");
            model.addRow(new Object[]{id.toHexString(), username, nombre, fecha, tel, email});
        }
    }

    private void eliminarSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            mostrarError("Seleccione un subgerente para eliminar");
            return;
        }
        String idHex = (String) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que desea eliminar al subgerente seleccionado?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            MongoCollection<Document> col = banco.getDatabase().getCollection("cuentas");
            col.deleteOne(eq("_id", new ObjectId(idHex)));
            mostrarExito("Subgerente eliminado exitosamente");
            cargarSubgerentes();
        } catch (Exception ex) {
            mostrarError("Error al eliminar: " + ex.getMessage());
        }
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int w = getWidth(), h = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, headerColor, w, 0, accentColor);
        g2.setPaint(gp);
        g2.fillRect(0, 0, w, h);
    }
}
