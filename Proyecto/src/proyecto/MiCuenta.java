/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import java.awt.*;
import javax.swing.*;

public class MiCuenta extends JFrame{
    private SistemaJuego sistema;
    private Player jugadorActual; //el player logeado
    
    //componentes para la contraseña
    private JPasswordField txtviejaPassword, txtnuevaPassword, txtConfirmarPassword;
    private JTextField txtUsername;

    public MiCuenta(SistemaJuego sistema, Player jugador) {
        this.sistema = sistema;
        this.jugadorActual = jugador;
        
        setTitle("Mi Cuenta - "+jugador.getUsername());
        setSize(400, 450);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1)); //3 seciones: info, contra, eliminar
        
        //seccion 1: info y datos
        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informacion"));
        
        txtUsername = new JTextField(jugador.getUsername());
        txtUsername.setEditable(false);
        
        infoPanel.add(new JLabel("Usuario"));
        infoPanel.add(txtUsername);
        infoPanel.add(new JLabel("Puntos"));
        infoPanel.add(new JLabel(String.valueOf(jugador.getPuntos())));
        infoPanel.add(new JLabel("Registro"));
        infoPanel.add(new JLabel(jugador.getFechaIngreso().toString()));
        add(infoPanel);
        
        //secciona 2: cambiar contra
        JPanel contraPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        contraPanel.setBorder(BorderFactory.createTitledBorder("Cambiar Contraseña (5 caracteres)"));
        
        txtviejaPassword = new JPasswordField(5);
        txtnuevaPassword = new JPasswordField(5);
        JButton btnCambiarContra = new JButton("CAMBIAR CONTRASEÑA");
        
        contraPanel.add(new JLabel("Contraseña actual: "));
        contraPanel.add(txtviejaPassword);
        contraPanel.add(new JLabel("Nueva contraseña: "));
        contraPanel.add(txtnuevaPassword);
        contraPanel.add(new JLabel(""));
        contraPanel.add(btnCambiarContra);
        add(contraPanel);
        
        //seccion 3: eliminar cuenta
        JPanel eliminarPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        eliminarPanel.setBorder(BorderFactory.createTitledBorder("Peligro - Eliminar Cuenta"));
        
        txtConfirmarPassword = new JPasswordField(5);
        JButton btnEliminar = new JButton("ELIMINAR CUENTA");
        btnEliminar.setBackground(Color.RED);
        btnEliminar.setForeground(Color.WHITE);
        
        eliminarPanel.add(new JLabel("Confirmar contraseña"));
        eliminarPanel.add(txtConfirmarPassword);
        eliminarPanel.add(new JLabel(""));
        eliminarPanel.add(btnEliminar);
        add(eliminarPanel);
        
        //manejo de eventos
        btnCambiarContra.addActionListener(e -> manejarCambioContra());
        btnEliminar.addActionListener(e -> manejarEliminarCuenta());
    }
    
    private void manejarCambioContra(){
        String viejaPassword = new String(txtviejaPassword.getPassword());
        String nuevaPassword = new String(txtnuevaPassword.getPassword());
        
        boolean exito = sistema.cambiarPassword(jugadorActual.getUsername(), viejaPassword, nuevaPassword);
        if(exito){
            JOptionPane.showMessageDialog(this, "Contraseña cambiada con exito", "Exito", JOptionPane.INFORMATION_MESSAGE);
            txtviejaPassword.setText("");
            txtnuevaPassword.setText("");
        } else{
            JOptionPane.showMessageDialog(this, "Error al cambia la contraseña. Verifique que tenga 5 caracteres", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void manejarEliminarCuenta(){
        String confirmarPassword = new String(txtConfirmarPassword.getPassword());
        int confirmar = JOptionPane.showConfirmDialog(this, "¿Esta seguro que desea eliminar su cuenta?", "Confirmar eliminacion", JOptionPane.YES_NO_OPTION);
        
        if(confirmar == JOptionPane.YES_OPTION){
            boolean exito = sistema.eliminarCuenta(jugadorActual.getUsername(), confirmarPassword);
            if(exito){
                JOptionPane.showMessageDialog(this, "Cuenta eliminada con exito. Volviendo al Menu de Inicio", "Eliminacion", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } else{
                JOptionPane.showMessageDialog(this, "Contraseña de confirmacion incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
