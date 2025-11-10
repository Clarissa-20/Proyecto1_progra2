/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class cerrarCuenta extends JFrame {

    private SistemaJuego sistema;
    private Player jugadorActual;
    private JPasswordField txtConfirmarPassword;

    private final Color COLOR_TEXTO = Color.WHITE;
    private final Font FONT_LABEL = new Font("Bodoni Bd BT", Font.BOLD, 20);

    public cerrarCuenta(SistemaJuego sistema, Player jugadorActual) {
        super("Eliminar Cuenta - " + jugadorActual.getUsername());
        this.sistema = sistema;
        this.jugadorActual = jugadorActual;

        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        fondoPanel fondo = new fondoPanel("/img/fondoC.png");
        fondo.setLayout(new BorderLayout());
        fondo.setBorder(BorderFactory.createEmptyBorder(90, 90, 90, 90));

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE, 5), "ELIMINAR CUENTA (ACCIÓN PERMANENTE)",
            TitledBorder.LEFT, TitledBorder.TOP, FONT_LABEL.deriveFont(Font.BOLD, 25), Color.WHITE));

        txtConfirmarPassword = new JPasswordField(5);
        JButton btnEliminar = new JButton("ELIMINAR CUENTA");
        btnEliminar.setBackground(Color.RED.darker());
        btnEliminar.setForeground(COLOR_TEXTO);
        btnEliminar.setFont(FONT_LABEL.deriveFont(Font.BOLD));

        panel.add(crearLabel("CONFIRMAR CONTRASEÑA:"));
        panel.add(txtConfirmarPassword);
        panel.add(new JLabel(""));
        panel.add(btnEliminar);

        btnEliminar.addActionListener(e -> manejarEliminarCuenta());
        
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

    private void manejarEliminarCuenta() {
        String confirmarPassword = new String(txtConfirmarPassword.getPassword());

        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar su cuenta?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmar == JOptionPane.YES_OPTION) {
            boolean exito = sistema.eliminarCuenta(jugadorActual.getUsername(), confirmarPassword);

            if (exito) {
                this.dispose();
            } else {
                txtConfirmarPassword.setText("");
            }
        }
    }
}
