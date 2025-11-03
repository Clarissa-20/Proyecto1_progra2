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
        titulo.setFont(new Font("Bodoni Bd BT", Font.BOLD, 40));
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
        JTextField txtNomUsuario = new JTextField(15);
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
        }
    }
    
    private void mostrarCrearPlayer() {
        JTextField txtNomUsuario = new JTextField(15);
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
        }
    }
    
    private void manejarLogIn(String user, String contra) {
        Player playerLogeado = sistema.logIn(user, contra);
        
        if (playerLogeado != null) {
            JOptionPane.showMessageDialog(this, "¡Bienvenido, " + user + "!", "Login Exitoso", JOptionPane.INFORMATION_MESSAGE);
            MenuPrincipal menuPrincipal = new MenuPrincipal(sistema, playerLogeado);
            menuPrincipal.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos o no existe.", "Error de Login", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void manejarCrearPlayer(String user, String contra) {
        if (contra.length() != 5) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener exactamente 5 caracteres.", "Error de registro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean exito = sistema.crearPlayer(user, contra);
        
        if (exito) {
            JOptionPane.showMessageDialog(this, "Cuenta creada con éxito.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
            Player nuevoPlayer = sistema.logIn(user, contra);
            if (nuevoPlayer != null) {
                MenuPrincipal menuPrincipal = new MenuPrincipal(sistema, nuevoPlayer);
                menuPrincipal.setVisible(true);
                this.dispose();
            }
            
        } else {
            JOptionPane.showMessageDialog(this, "Error al crear cuenta. Verifica que el usuario no exista.", "Error de registro", JOptionPane.ERROR_MESSAGE);
        }
    }

}
