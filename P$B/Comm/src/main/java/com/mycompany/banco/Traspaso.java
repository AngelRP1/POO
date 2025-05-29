package com.mycompany.banco;

import com.mycompany.comm.Connection;
import com.mycompany.ventanas.SeleccionarClientes;
import com.mycompany.ventanas.SeleccionarUsuario;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;


import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Traspaso extends JFrame {
    private final String numeroCliente;
    public static String rango;
    public static Connection database;
    public String uri;

    public Traspaso(String numeroCliente) {
        this.numeroCliente = numeroCliente;
        this.uri = "mongodb://admin:1234@localhost:27017/";
        this.database = new Connection(uri);

        MongoCollection<Document> cuentas = database
            .getDatabase()
            .getCollection("cuentas");
        Document usuarioDoc = cuentas
            .find(eq("numero_cliente", numeroCliente))
            .first();
        boolean esSub = usuarioDoc != null && usuarioDoc.getBoolean("esSubgerente", false);

        if ("admin".equalsIgnoreCase(numeroCliente)) {
            rango = "ADMINISTRADOR";
        } else if (esSub) {
            rango = "SUBGERENTE";
        } else {
            rango = "USUARIO";
        }

        setUndecorated(true);
        setSize(1440, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        CargaPanel cargaPanel = new CargaPanel();
        cargaPanel.setBounds(0, 0, 1440, 900);
        add(cargaPanel);
        setVisible(true);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                redirigirPorRol();
            }
        }, 4000);
    }

    private void redirigirPorRol() {
        SwingUtilities.invokeLater(() -> {
            dispose();
            if ("admin".equalsIgnoreCase(numeroCliente)) {
                SeleccionarUsuario su = new SeleccionarUsuario(database);
                su.setVisible(true);
            }
            else if (rango.equals("SUBGERENTE")) {
                SeleccionarClientes sc = new SeleccionarClientes(database);
                sc.setVisible(true);
            }
            else {
                SeleccionOperacionFrame so = new SeleccionOperacionFrame(numeroCliente);
                so.setVisible(true);
            }
        });
    }

    static class CargaPanel extends JPanel {
        private final int[] posiciones = new int[15];
        private final Timer animacionTimer;
        private final Color color1 = new Color(26, 46, 84);
        private final Color color2 = new Color(96, 155, 210);

        CargaPanel() {
            setLayout(null);
            for (int i = 0; i < posiciones.length; i++) {
                posiciones[i] = 1440 + i * 30;
            }

            animacionTimer = new Timer();
            animacionTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    for (int i = 0; i < posiciones.length; i++) {
                        posiciones[i] -= 10;
                        if (posiciones[i] < -20) {
                            posiciones[i] = 1440;
                        }
                    }
                    repaint();
                }
            }, 0, 50);
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

            // Dibuja el rango
            g.setFont(new Font("Segoe UI", Font.BOLD, 36));
            g.setColor(Color.WHITE);
            FontMetrics fm = g.getFontMetrics();
            int textoWidth = fm.stringWidth(rango);
            g.drawString(rango, (w - textoWidth) / 2, 350); // Centrado en X

            // Dibuja los puntos
            g.setColor(Color.WHITE);
            for (int pos : posiciones) {
                g.fillOval(pos, 420, 20, 20);
            }
        }
    }
}
