/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class cambiarContra extends JFrame {

    private SistemaJuego sistema;
    private Player jugadorActual;
    private JPasswordField txtviejaPassword, txtnuevaPassword;

    private final Color COLOR_PRIMARIO = new Color(150, 0, 0);
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Font FONT_LABEL = new Font("Bodoni Bd BT", Font.BOLD, 20);

    public cambiarContra(SistemaJuego sistema, Player jugadorActual) {
        super("Cambiar Contraseña - " + jugadorActual.getUsername());
        this.sistema = sistema;
        this.jugadorActual = jugadorActual;

        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        fondoPanel fondo = new fondoPanel("/img/fondoC.png");
        fondo.setLayout(new BorderLayout());
        fondo.setBorder(BorderFactory.createEmptyBorder(90, 90, 90, 90));

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_TEXTO, 5), "NUEVA CONTRASEÑA (5 caracteres)",
            TitledBorder.LEFT, TitledBorder.TOP, FONT_LABEL.deriveFont(Font.BOLD, 25), COLOR_TEXTO));

        txtviejaPassword = new JPasswordField(5);
        txtnuevaPassword = new JPasswordField(5);
        JButton btnCambiarContra = new JButton("CAMBIAR CONTRASEÑA");
        
        btnCambiarContra.setBackground(COLOR_PRIMARIO);
        btnCambiarContra.setForeground(COLOR_TEXTO);
        btnCambiarContra.setFont(FONT_LABEL.deriveFont(Font.BOLD));

        panel.add(crearLabel("CONTRASEÑA ACTUAL:"));
        panel.add(txtviejaPassword);
        panel.add(crearLabel("NUEVA CONTRASEÑA:"));
        panel.add(txtnuevaPassword);
        panel.add(new JLabel(""));
        panel.add(btnCambiarContra);

        btnCambiarContra.addActionListener(e -> manejarCambioContra());
        
        JButton btnVolver = new JButton("VOLVER");
        btnVolver.setFont(FONT_LABEL);
        btnVolver.setForeground(COLOR_TEXTO);
        btnVolver.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
        btnVolver.setBackground(new Color(153, 0, 0));
        btnVolver.setPreferredSize(new Dimension(250, 50));
        btnVolver.addActionListener(e -> vtnVolver());
        
        JPanel panelbtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panelbtn.setOpaque(false);
        panelbtn.add(btnVolver);
        fondo.add(panelbtn, BorderLayout.SOUTH);

        fondo.add(panel, BorderLayout.CENTER);
        this.setContentPane(fondo);
    }
    
    private void vtnVolver(){
        new MiCuenta(sistema, jugadorActual).setVisible(true);
        this.dispose(); 
    }
    
    private JLabel crearLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_LABEL);
        label.setForeground(COLOR_TEXTO);
        return label;
    }

    private void manejarCambioContra() {
        String viejaPassword = new String(txtviejaPassword.getPassword());
        String nuevaPassword = new String(txtnuevaPassword.getPassword());

        boolean exito = sistema.cambiarPassword(jugadorActual.getUsername(), viejaPassword, nuevaPassword);

        if (exito) {
            txtviejaPassword.setText("");
            txtnuevaPassword.setText("");
            this.dispose();
        } 
    }
}
