/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import java.awt.*;
import javax.swing.*;

public class vtnCrearPlayer extends JFrame{
    private SistemaJuego sistema;
    
    public vtnCrearPlayer(SistemaJuego sistema){
        this.sistema = sistema;
        setTitle("Vampire Wargame - CrearPlayer");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout(20, 20));
        
        fondoPanel fp = new fondoPanel("/img/fondoMenuInicio.png");
        fp.setLayout(new BorderLayout(20, 20));
        
        JLabel titulo = new JLabel("CREAR PLAYER", SwingConstants.CENTER);
        titulo.setFont(new Font("Bodoni Bd BT", Font.BOLD, 50));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        fp.add(titulo, BorderLayout.NORTH);
        
        
        JTextField txtNomUsuario = new JTextField(15);
        JPasswordField txtContra = new JPasswordField(15);
        
        txtNomUsuario.setBackground(Color.BLACK);
        txtNomUsuario.setForeground(Color.WHITE);
        txtContra.setBackground(Color.BLACK);
        txtContra.setForeground(Color.WHITE);
        
        JPanel panel = new JPanel(new GridLayout(6, 1, 5, 5));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        
        panel.add(Etiqueta("Usuario(debe ser unico):"));
        panel.add(txtNomUsuario);
        panel.add(Etiqueta("Contraseña(exactamente 5 caracteres):"));
        panel.add(txtContra);
        fp.add(panel, BorderLayout.CENTER);
        
        JButton btnConfirmar = btnEstilo("CREAR");
        JButton btnVolver = btnEstilo("VOLVER");
        btnConfirmar.addActionListener(e -> manejarCrearPlayer(txtNomUsuario.getText(), new String(txtContra.getPassword())));
        btnVolver.addActionListener(e -> volverMenuInicio());
        
        JPanel panelbtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
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
