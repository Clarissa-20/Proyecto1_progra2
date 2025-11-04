/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import java.awt.*;
import javax.swing.*;
import java.time.LocalDate;

public class MenuInicio extends JFrame {
    private SistemaJuego sistema;

    public MenuInicio(SistemaJuego sistema) {
        this.sistema = sistema;
        setTitle("Vampire Wargame - Menú Inicio");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout(20, 20));
        
        fondoPanel fp = new fondoPanel("/img/fondoMenuInicio.png");
        fp.setLayout(new BorderLayout(20, 20));
        
        JLabel titulo = new JLabel("VAMPIRE WARGAME", SwingConstants.CENTER);
        titulo.setFont(new Font("Bodoni Bd BT", Font.BOLD, 50));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        fp.add(titulo, BorderLayout.NORTH);
        
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(4, 1, 15, 15));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30, 200, 60, 200));
        panelBotones.setOpaque(false);
        
        JButton btnLogIn = crearBotones("LOG IN");
        JButton btnCrearPlayer = crearBotones("CREAR PLAYER");
        JButton btnSalir = crearBotones("SALIR");
        
        panelBotones.add(btnLogIn);
        panelBotones.add(btnCrearPlayer);
        panelBotones.add(btnSalir);
        
        fp.add(panelBotones, BorderLayout.CENTER);
        
        this.add(fp);
        
        btnLogIn.addActionListener(e -> mostrarLogin());
        btnCrearPlayer.addActionListener(e -> mostrarCrearPlayer());
        btnSalir.addActionListener(e -> System.exit(0));
    }
    
    private JButton crearBotones(String texto) {
        JButton btn = new JButton(texto);
        Color color = new Color(124, 252, 0);
        
        btn.setFont(new Font("Bodoni Bd BT", Font.BOLD, 20));
        btn.setForeground(Color.WHITE);
        btn.setBackground(Color.BLACK);
        btn.setPreferredSize(new Dimension(250, 50));
        
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 5),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        
        return btn;
    }

    private void mostrarLogin() {
        vtnLogin login = new vtnLogin(sistema);
        login.setVisible(true);
        this.dispose();
        
        /*JTextField txtNomUsuario = new JTextField(15);
        JPasswordField txtContra = new JPasswordField(15);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Usuario:"));
        panel.add(txtNomUsuario);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtContra);

        int resultado = JOptionPane.showConfirmDialog(this, panel, 
                 "Iniciar Sesión", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            String user = txtNomUsuario.getText();
            String contra = new String(txtContra.getPassword());
            manejarLogIn(user, contra);
        }*/
    }
    
    private void mostrarCrearPlayer() {
        vtnCrearPlayer crearPlayer = new vtnCrearPlayer(sistema);
        crearPlayer.setVisible(true);
        this.dispose();
        
        /*TextField txtNomUsuario = new JTextField(15);
        JPasswordField txtContra = new JPasswordField(15);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Usuario (debe ser único):"));
        panel.add(txtNomUsuario);
        panel.add(new JLabel("Contraseña (exactamente 5 caracteres):"));
        panel.add(txtContra);

        int resultado = JOptionPane.showConfirmDialog(this, panel, 
                 "Crear Nuevo Jugador", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            String user = txtNomUsuario.getText();
            String contra = new String(txtContra.getPassword());
            manejarCrearPlayer(user, contra);
        }*/
    }
}
