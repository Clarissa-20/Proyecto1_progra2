/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import java.awt.*;
import javax.swing.*;

public class vtnLogin extends JFrame{
    private SistemaJuego sistema;
    
    public vtnLogin(SistemaJuego sistema){
        this.sistema = sistema;
        setTitle("Vampire Wargame - Iniciar Sesion");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout(20, 20));
        
        fondoPanel fp = new fondoPanel("/img/fondoMenuInicio.png");
        fp.setLayout(new BorderLayout(20, 20));
        
        JLabel titulo = new JLabel("INICIAR SESION", SwingConstants.CENTER);
        titulo.setFont(new Font("Bodoni Bd BT", Font.BOLD, 50));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        fp.add(titulo, BorderLayout.NORTH);
        
        
        JTextField txtNomUsuario = new JTextField(20);
        JPasswordField txtContra = new JPasswordField(20);
        
        txtNomUsuario.setBackground(Color.BLACK);
        txtNomUsuario.setForeground(Color.WHITE);
        txtNomUsuario.setFont(new Font("Bodoni Bd BT", Font.BOLD, 20));
        txtContra.setBackground(Color.BLACK);
        txtContra.setForeground(Color.WHITE);
        txtContra.setFont(new Font("Bodoni Bd BT", Font.BOLD, 20));
        
        JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        
        panel.add(Etiqueta("Usuario:"));
        panel.add(txtNomUsuario);
        panel.add(Etiqueta("Contraseña:"));
        panel.add(txtContra);
        fp.add(panel, BorderLayout.CENTER);
        
        JButton btnConfirmar = btnEstilo("ENTRAR");
        JButton btnVolver = btnEstilo("VOLVER");
        btnConfirmar.addActionListener(e -> manejarLogIn(txtNomUsuario.getText(), new String(txtContra.getPassword())));
        btnVolver.addActionListener(e -> volverMenuInicio());
        
        JPanel panelbtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panelbtn.setOpaque(false);
        panelbtn.add(btnConfirmar);
        panelbtn.add(btnVolver);
        fp.add(panelbtn, BorderLayout.SOUTH);
        this.add(fp);
    }
    
    private JLabel Etiqueta(String texto){
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(new Color(124, 252, 0));
        etiqueta.setFont(new Font("Bodoni Bd BT", Font.BOLD, 30));
        return etiqueta;
    }
    
    private JButton btnEstilo(String texto){
        JButton btn = new JButton(texto);
        btn.setBackground(Color.BLACK);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Bodoni Bd BT", Font.BOLD, 20));
        btn.setPreferredSize(new Dimension(250, 50));
        btn.setBorder(BorderFactory.createLineBorder(new Color(124, 252, 0) ,5));
        return btn;
    }
    
    private void volverMenuInicio(){
        new MenuInicio(sistema).setVisible(true);
        this.dispose();
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
}
