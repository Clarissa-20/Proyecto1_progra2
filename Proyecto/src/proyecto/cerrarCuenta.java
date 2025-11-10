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
    private final Font FONT_LABEL = new Font("Bodoni Bd BT", Font.PLAIN, 16);

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
        fondo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.RED, 2), "ELIMINAR CUENTA (ACCIÓN PERMANENTE)",
            TitledBorder.LEFT, TitledBorder.TOP, FONT_LABEL.deriveFont(Font.BOLD, 18), Color.RED));

        txtConfirmarPassword = new JPasswordField(5);
        JButton btnEliminar = new JButton("ELIMINAR CUENTA");
        btnEliminar.setBackground(Color.RED.darker());
        btnEliminar.setForeground(COLOR_TEXTO);
        btnEliminar.setFont(FONT_LABEL.deriveFont(Font.BOLD));

        panel.add(crearLabel("Confirmar contraseña:"));
        panel.add(txtConfirmarPassword);
        panel.add(new JLabel(""));
        panel.add(btnEliminar);

        btnEliminar.addActionListener(e -> manejarEliminarCuenta());

        fondo.add(panel, BorderLayout.CENTER);
        this.setContentPane(fondo);
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
                "¿Está seguro que desea ELIMINAR su cuenta? Esta acción es irreversible.",
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
