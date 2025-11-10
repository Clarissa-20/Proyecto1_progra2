/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */package proyecto;

import java.awt.*;
import javax.swing.*;

public class verMiInfo extends JFrame {

    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color COLOR_SECUNDARIO = new Color(30, 30, 30);
    private final Font FONT_LABEL = new Font("Bodoni Bd BT", Font.PLAIN, 16);

    public verMiInfo(Player jugadorActual) {
        super("Información de la Cuenta - " + jugadorActual.getUsername());

        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        fondoPanel fondo = new fondoPanel("/img/fondoC.png");
        fondo.setLayout(new BorderLayout());
        fondo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel contentPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        contentPanel.setOpaque(false);

        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setFont(FONT_LABEL);
        lblUser.setForeground(COLOR_TEXTO);
        JTextField txtUsername = new JTextField(jugadorActual.getUsername());
        txtUsername.setEditable(false);
        txtUsername.setFont(FONT_LABEL);
        txtUsername.setBackground(COLOR_SECUNDARIO);
        txtUsername.setForeground(COLOR_TEXTO);

        JLabel lblPts = new JLabel("Puntos:");
        lblPts.setFont(FONT_LABEL);
        lblPts.setForeground(COLOR_TEXTO);
        JLabel lblPuntos = new JLabel(String.valueOf(jugadorActual.getPuntos()));
        lblPuntos.setFont(FONT_LABEL);
        lblPuntos.setForeground(Color.YELLOW);

        JLabel lblReg = new JLabel("Fecha de Registro:");
        lblReg.setFont(FONT_LABEL);
        lblReg.setForeground(COLOR_TEXTO);
        JLabel lblRegistro = new JLabel(jugadorActual.getFechaIngreso().toString());
        lblRegistro.setFont(FONT_LABEL);
        lblRegistro.setForeground(Color.LIGHT_GRAY);

        contentPanel.add(lblUser);
        contentPanel.add(txtUsername);
        contentPanel.add(lblPts);
        contentPanel.add(lblPuntos);
        contentPanel.add(lblReg);
        contentPanel.add(lblRegistro);

        fondo.add(contentPanel, BorderLayout.CENTER);
        this.setContentPane(fondo);
    }
}
