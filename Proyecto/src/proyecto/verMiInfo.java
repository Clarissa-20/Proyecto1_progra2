/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class verMiInfo extends JFrame {
    private SistemaJuego miSistema;
    private Player jugadorActual;
    
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color COLOR_SECUNDARIO = new Color(30, 30, 30);
    private final Font FONT_LABEL = new Font("Bodoni Bd BT", Font.BOLD, 25);

    public verMiInfo(Player jugadorActual) {
        super("Información de la Cuenta - " + jugadorActual.getUsername());

        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        fondoPanel fondo = new fondoPanel("/img/fondoC.png");
        fondo.setLayout(new BorderLayout());
        fondo.setBorder(BorderFactory.createEmptyBorder(90, 90, 90, 90));

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(90, 90, 90, 90));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_TEXTO, 5), "MI INFORMACION",
            TitledBorder.LEFT, TitledBorder.TOP, FONT_LABEL.deriveFont(Font.BOLD, 25), COLOR_TEXTO));

        JLabel nombre = new JLabel("USUARIO:");
        nombre.setFont(FONT_LABEL);
        nombre.setForeground(COLOR_TEXTO);
        nombre.setPreferredSize(new Dimension(100, 30));

        JTextField txtUsername = new JTextField(jugadorActual.getUsername());
        txtUsername.setEditable(false);
        txtUsername.setFont(FONT_LABEL);
        txtUsername.setBackground(COLOR_SECUNDARIO);
        txtUsername.setForeground(COLOR_TEXTO);

        JLabel puntos = new JLabel("PUNTOS:");
        puntos.setFont(FONT_LABEL);
        puntos.setForeground(COLOR_TEXTO);
        JLabel txtPuntos = new JLabel(String.valueOf(jugadorActual.getPuntos()));
        txtPuntos.setFont(FONT_LABEL);
        txtPuntos.setForeground(COLOR_TEXTO);
        txtPuntos.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
        puntos.setPreferredSize(new Dimension(100, 30));

        JLabel registro = new JLabel("FECHA DE INGRESO:");
        registro.setFont(FONT_LABEL);
        registro.setForeground(COLOR_TEXTO);
        JLabel txtRegistro = new JLabel(jugadorActual.getFechaIngreso().toString());
        txtRegistro.setFont(FONT_LABEL);
        txtRegistro.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
        txtRegistro.setForeground(Color.WHITE);
        registro.setPreferredSize(new Dimension(100, 30));

        panel.add(nombre);
        panel.add(txtUsername);
        panel.add(puntos);
        panel.add(txtPuntos);
        panel.add(registro);
        panel.add(txtRegistro);

        fondo.add(panel, BorderLayout.CENTER);
        this.setContentPane(fondo);
    }
}
