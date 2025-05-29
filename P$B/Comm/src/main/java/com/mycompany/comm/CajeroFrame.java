package com.mycompany.comm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class CajeroFrame extends JFrame {
    private final Color PRIMARY_COLOR = new Color(35, 35, 70);
    private final Color SECONDARY_COLOR = new Color(75, 105, 175);
    private final Color ACCENT_COLOR = new Color(92, 172, 229);
    private JPanel mainContentPanel;

    public CajeroFrame() {
        configurarVentana();
        agregarEncabezado();
        agregarCuerpoPrincipal();
    }

    private void configurarVentana() {
        setTitle("Cajero Automático Digital");
        setSize(800, 650);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setShape(new RoundRectangle2D.Double(0, 0, 800, 650, 40, 40));
        getContentPane().setBackground(PRIMARY_COLOR);
        setLayout(new BorderLayout());
    }

    private void agregarEncabezado() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(PRIMARY_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel titulo = new JLabel("BANCO DIGITAL", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);
        topPanel.add(titulo, BorderLayout.CENTER);

        JButton cerrarBtn = crearBotonRedondo("×", new Color(231, 76, 60), 40);
        cerrarBtn.addActionListener(e -> System.exit(0));
        topPanel.add(cerrarBtn, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
    }

    private void agregarCuerpoPrincipal() {
        JPanel panelCentral = new JPanel(new BorderLayout(20, 20));
        panelCentral.setBackground(PRIMARY_COLOR);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

        // Botones de operaciones a la izquierda
        panelCentral.add(crearPanelBotones("OPERACIONES", new String[]{"RECARGA", "PAGO", "TRANSFERENCIA", "OTROS"}), BorderLayout.WEST);

        // Panel central con tarjeta
        mainContentPanel = crearPanelContenido();
        panelCentral.add(mainContentPanel, BorderLayout.CENTER);

        // Botones de servicios a la derecha
        panelCentral.add(crearPanelBotones("SERVICIOS", new String[]{"RETIRO", "CONSULTA", "DEPÓSITO", "HISTORIAL"}), BorderLayout.EAST);

        add(panelCentral, BorderLayout.CENTER);
    }

    private JPanel crearPanelBotones(String titulo, String[] opciones) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        labelTitulo.setForeground(ACCENT_COLOR);
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(labelTitulo);
        panel.add(Box.createVerticalStrut(20));

        for (String opcion : opciones) {
            JButton btn = crearBotonRedondeado(opcion);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.addActionListener(e -> mostrarPanelOpcion(opcion));
            panel.add(btn);
            panel.add(Box.createVerticalStrut(10));
        }

        return panel;
    }

    private JPanel crearPanelContenido() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel bienvenida = new JLabel("Bienvenido", SwingConstants.CENTER);
        bienvenida.setFont(new Font("Segoe UI", Font.BOLD, 30));
        bienvenida.setForeground(Color.WHITE);

        JPanel tarjetaPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint grad = new GradientPaint(0, 0, SECONDARY_COLOR, getWidth(), getHeight(), PRIMARY_COLOR);
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                g2.setColor(new Color(255, 215, 0));
                g2.fillRoundRect(20, 30, 40, 30, 10, 10);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Courier New", Font.BOLD, 16));
                g2.drawString("••••  ••••  ••••  3456", 80, 55);
            }
        };

        tarjetaPanel.setPreferredSize(new Dimension(400, 100));
        tarjetaPanel.setOpaque(false);
        tarjetaPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JPanel tarjetaWrapper = new JPanel();
        tarjetaWrapper.setOpaque(false);
        tarjetaWrapper.setLayout(new GridBagLayout());
        tarjetaWrapper.add(tarjetaPanel);

        panel.add(bienvenida, BorderLayout.NORTH);
        panel.add(tarjetaWrapper, BorderLayout.CENTER);

        return panel;
    }

   private JButton crearBotonRedondeado(String texto) {
    JButton btn = new JButton(texto);
    
    btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
    btn.setForeground(Color.WHITE);
    btn.setBackground(SECONDARY_COLOR);
    btn.setFocusPainted(false);
    btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    btn.setContentAreaFilled(false);
    btn.setOpaque(false);

    Dimension fixedSize = new Dimension(180, 50);
    btn.setPreferredSize(fixedSize);
    btn.setMinimumSize(fixedSize);
    btn.setMaximumSize(fixedSize);

    btn.addMouseListener(new ButtonHoverEffect());

    // Personalización redondeada
    btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            AbstractButton b = (AbstractButton) c;

            // Fondo redondeado con hover
            g2.setColor(b.getModel().isRollover() ? ACCENT_COLOR : SECONDARY_COLOR);
            g2.fillRoundRect(0, 0, b.getWidth(), b.getHeight(), 20, 20);

            // Texto centrado
            FontMetrics fm = g2.getFontMetrics();
            int stringWidth = fm.stringWidth(b.getText());
            int stringHeight = fm.getAscent();
            g2.setColor(Color.WHITE);
            g2.setFont(b.getFont());
            g2.drawString(b.getText(), (b.getWidth() - stringWidth) / 2, (b.getHeight() + stringHeight) / 2 - 4);

            g2.dispose();
        }
    });

    return btn;
}



    private JButton crearBotonRedondo(String texto, Color color, int size) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 22));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(size, size));
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        return btn;
    }

    private void mostrarPanelOpcion(String opcion) {
        mainContentPanel.removeAll();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        JLabel label = new JLabel(opcion);
        label.setFont(new Font("Segoe UI", Font.BOLD, 28));
        label.setForeground(ACCENT_COLOR);

        panel.add(label);
        mainContentPanel.add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    class ButtonHoverEffect extends MouseAdapter {
        @Override
        public void mouseEntered(MouseEvent e) {
            ((JButton) e.getSource()).setBackground(ACCENT_COLOR);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            ((JButton) e.getSource()).setBackground(SECONDARY_COLOR);
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CajeroFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CajeroFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CajeroFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CajeroFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CajeroFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
