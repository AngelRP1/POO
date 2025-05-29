package com.mycompany.comm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class Crear extends JFrame {
    private final Color PRIMARY_COLOR = new Color(30, 30, 60);
    private final Color SECONDARY_COLOR = new Color(60, 90, 150);
    private final Color ACCENT_COLOR = new Color(52, 152, 219);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private final Color WARNING_COLOR = new Color(241, 196, 15);

    // Tamaños base para referencia
    private final int BASE_WIDTH = 1400;
    private final int BASE_HEIGHT = 1200;

    public Crear() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Sistema Bancario - Creación de Tarjetas");
        setSize(BASE_WIDTH, BASE_HEIGHT);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setShape(new RoundRectangle2D.Double(0, 0, BASE_WIDTH, BASE_HEIGHT, 30, 30));
        getContentPane().setBackground(PRIMARY_COLOR);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(PRIMARY_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel superior con título y botón de salida
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        JLabel titulo = new JLabel("CREACIÓN DE TARJETAS", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, scaleFont(30)));
        titulo.setForeground(Color.WHITE);
        topPanel.add(titulo, BorderLayout.CENTER);
        
        JButton exitButton = createExitButton();
        topPanel.add(exitButton, BorderLayout.EAST);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Panel central con las opciones de tarjetas
        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Tarjeta de Débito
        JPanel debitCardPanel = createCardOptionPanel(
                "TARJETA DE DÉBITO", 
                "Acceso directo a fondos en cuenta", 
                ACCENT_COLOR, 
                "DÉBITO");
        debitCardPanel.add(createCardButton("CREAR", ACCENT_COLOR, e -> showCardCreationForm("Débito")));
        centerPanel.add(debitCardPanel);

        // Tarjeta de Crédito
        JPanel creditCardPanel = createCardOptionPanel(
                "TARJETA DE CRÉDITO", 
                "Línea de crédito con límite aprobado", 
                new Color(155, 89, 182), 
                "CRÉDITO");
        creditCardPanel.add(createCardButton("CREAR", new Color(155, 89, 182), e -> showCardCreationForm("Crédito")));
        centerPanel.add(creditCardPanel);

        // Tarjeta de Nómina
        JPanel payrollCardPanel = createCardOptionPanel(
                "TARJETA DE NÓMINA", 
                "Vinculada a cuenta de nómina empresarial", 
                new Color(230, 126, 34), 
                "NÓMINA");
        payrollCardPanel.add(createCardButton("CREAR", new Color(230, 126, 34), e -> showCardCreationForm("Nómina")));
        centerPanel.add(payrollCardPanel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Panel inferior con botón de regreso
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JButton backButton = createBackButton();
        bottomPanel.add(backButton);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Listener para redimensionamiento
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
            }
        });
    }

    private JButton createExitButton() {
        JButton exitButton = new JButton("×");
        exitButton.setPreferredSize(new Dimension(scaleX(35), scaleY(35)));
        exitButton.setFont(new Font("Segoe UI", Font.BOLD, scaleFont(22)));
        exitButton.setForeground(Color.WHITE);
        exitButton.setBackground(new Color(231, 76, 60));
        exitButton.setBorder(BorderFactory.createLineBorder(new Color(200, 50, 50), 1));
        exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exitButton.addActionListener(e -> System.exit(0));
        exitButton.addMouseListener(new ButtonHoverEffect(exitButton, new Color(192, 57, 43)));
        return exitButton;
    }

    private JButton createBackButton() {
        JButton backButton = new JButton("REGRESAR");
        backButton.setPreferredSize(new Dimension(scaleX(200), scaleY(45)));
        backButton.setFont(new Font("Segoe UI", Font.BOLD, scaleFont(16)));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(149, 165, 166));
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.setBorder(BorderFactory.createEmptyBorder());
        backButton.addMouseListener(new ButtonHoverEffect(backButton, new Color(127, 140, 141)));
        backButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        return backButton;
    }

    private JPanel createCardOptionPanel(String title, String description, Color color, String iconText) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(40, 40, 80));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        // Icono de texto
        JLabel iconLabel = new JLabel(iconText);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, scaleFont(24)));
        iconLabel.setForeground(color);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(iconLabel);

        // Título
        JLabel titleLabel = new JLabel(title);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, scaleFont(18)));
        titleLabel.setForeground(color);
        panel.add(titleLabel);

        // Descripción
        JTextArea descArea = new JTextArea(description);
        descArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, scaleFont(12)));
        descArea.setForeground(Color.LIGHT_GRAY);
        descArea.setBackground(new Color(40, 40, 80));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setMaximumSize(new Dimension(scaleX(180), scaleY(60)));
        descArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        panel.add(descArea);

        // Botón
        panel.add(Box.createVerticalGlue());
        panel.add(createCardButton("CREAR", color, e -> {}));
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JButton createCardButton(String text, Color color, ActionListener action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(scaleX(140), scaleY(40)));
        button.setMinimumSize(new Dimension(scaleX(140), scaleY(40)));
        button.setMaximumSize(new Dimension(scaleX(140), scaleY(40)));
        button.setFont(new Font("Segoe UI", Font.BOLD, scaleFont(14)));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.addMouseListener(new ButtonHoverEffect(button, color));
        button.addActionListener(action);
        return button;
    }

    private void showCardCreationForm(String cardType) {
        JDialog dialog = new JDialog(this, "Crear Tarjeta de " + cardType, true);
        dialog.setSize(scaleX(500), scaleY(600));
        dialog.setLocationRelativeTo(this);
        dialog.setUndecorated(true);
        dialog.setShape(new RoundRectangle2D.Double(0, 0, scaleX(500), scaleY(600), 30, 30));
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel title = new JLabel("CREAR TARJETA DE " + cardType.toUpperCase());
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Segoe UI", Font.BOLD, scaleFont(20)));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        panel.add(title);

        // Campos del formulario
        panel.add(createFormField("Nombre del Titular:"));
        panel.add(Box.createVerticalStrut(scaleY(15)));
        panel.add(createFormField("Número de Cuenta:"));
        panel.add(Box.createVerticalStrut(scaleY(15)));
        panel.add(createFormField("Fecha de Nacimiento:"));
        panel.add(Box.createVerticalStrut(scaleY(15)));
        panel.add(createFormField("RFC:"));
        
        if (cardType.equals("Crédito")) {
            panel.add(Box.createVerticalStrut(scaleY(15)));
            panel.add(createFormField("Ingreso Mensual:"));
        }

        // Checkbox para tarjeta física
        JCheckBox physicalCardCheck = new JCheckBox("Incluir tarjeta física (+$150)");
        physicalCardCheck.setAlignmentX(Component.CENTER_ALIGNMENT);
        physicalCardCheck.setFont(new Font("Segoe UI", Font.PLAIN, scaleFont(14)));
        physicalCardCheck.setForeground(Color.WHITE);
        physicalCardCheck.setBackground(PRIMARY_COLOR);
        physicalCardCheck.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(physicalCardCheck);

        // Botón de crear
        JButton createButton = new JButton("CREAR TARJETA");
        createButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createButton.setPreferredSize(new Dimension(scaleX(260), scaleY(45)));
        createButton.setFont(new Font("Segoe UI", Font.BOLD, scaleFont(16)));
        createButton.setForeground(Color.WHITE);
        createButton.setBackground(SUCCESS_COLOR);
        createButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        createButton.setBorder(BorderFactory.createEmptyBorder());
        createButton.addMouseListener(new ButtonHoverEffect(createButton, SUCCESS_COLOR));
        createButton.addActionListener(e -> {
            showStyledMessage("Tarjeta de " + cardType + " creada exitosamente", SUCCESS_COLOR);
            dialog.dispose();
        });
        panel.add(createButton);

        // Botón de cancelar
        JButton cancelButton = new JButton("CANCELAR");
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancelButton.setPreferredSize(new Dimension(scaleX(260), scaleY(45)));
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, scaleFont(16)));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBackground(WARNING_COLOR);
        cancelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelButton.setBorder(BorderFactory.createEmptyBorder());
        cancelButton.addMouseListener(new ButtonHoverEffect(cancelButton, WARNING_COLOR));
        cancelButton.addActionListener(e -> dialog.dispose());
        cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panel.add(cancelButton);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private JPanel createFormField(String labelText) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(scaleX(380), scaleY(60)));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, scaleFont(14)));
        label.setForeground(Color.LIGHT_GRAY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);

        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(scaleX(380), scaleY(35)));
        field.setFont(new Font("Segoe UI", Font.PLAIN, scaleFont(14)));
        field.setBackground(new Color(60, 60, 90));
        field.setForeground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        panel.add(field);

        return panel;
    }

    private void showStyledMessage(String mensaje, Color background) {
        JOptionPane optionPane = new JOptionPane(mensaje, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog(this, "Mensaje");
        dialog.setModal(false);
        dialog.getContentPane().setBackground(background);
        dialog.setVisible(true);

        new Timer(3000, e -> {
            dialog.dispose();
            ((Timer)e.getSource()).stop();
        }).start();
    }

    // Métodos para escalado proporcional
    private int scaleX(int value) {
        return (int) (value * ((double) getWidth() / BASE_WIDTH));
    }

    private int scaleY(int value) {
        return (int) (value * ((double) getHeight() / BASE_HEIGHT));
    }

    private int scaleFont(int value) {
        return (int) (value * Math.min(
            (double) getWidth() / BASE_WIDTH,
            (double) getHeight() / BASE_HEIGHT
        ));
    }

    // Clase para efecto hover en botones
    static class ButtonHoverEffect extends MouseAdapter {
        private final JButton button;
        private final Color original;

        public ButtonHoverEffect(JButton button, Color originalColor) {
            this.button = button;
            this.original = originalColor;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            button.setBackground(original.darker());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            button.setBackground(original);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Crear frame = new Crear();
            frame.setVisible(true);
        });
    }
}