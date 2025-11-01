/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import java.awt.*;
import javax.swing.*;

public class MenuInicio extends JFrame {
    private SistemaJuego sistema;
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public MenuInicio(SistemaJuego sistema) {
        //initComponents();
        this.sistema = sistema;
        setTitle("Vampire Wargame - Menú Inicio");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        //congiguracion del layout
        setLayout(new BorderLayout(10, 10));
        JPanel panelCentral = new JPanel(new GridLayout(4, 2, 10, 10)); //grid para inputs
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); //flow para botones
        
        //componentes de inputs
        txtUsername = new JTextField(15);
        txtPassword = new JPasswordField(15);
        
        panelCentral.add(new JLabel("Usuario: "));
        panelCentral.add(txtUsername);
        panelCentral.add(new JLabel("Contraseña (5 caracteres): "));
        panelCentral.add(txtPassword);
        
        //componentes de btns
        JButton btnLogIn = new JButton("LOG IN");
        JButton btnCrearPlayer = new JButton("CREAR PLAYER");
        JButton btnSalir = new JButton("SALIR");
        
        //añadir btns al panel
        panelBotones.add(btnLogIn);
        panelBotones.add(btnCrearPlayer);
        panelBotones.add(btnSalir);
        
        //añadir paneles a la vtn
        add(new JLabel("VAMPIRE WARGAME", SwingConstants.CENTER), BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        
        //manejo de eventos - integracion con logica
        btnLogIn.addActionListener(e -> manejarLogIn());
        btnCrearPlayer.addActionListener(e -> manejarCrearPlayer());
        btnSalir.addActionListener(e -> System.exit(0));
        
        //validacio de contra
        txtPassword.addActionListener(e -> manejarLogIn());
    }
    
    private void manejarLogIn(){
        String user = txtUsername.getText();
        String contra = new String(txtPassword.getPassword());
        
        Player playerLogeado = sistema.logIn(user, contra);
        
        if(playerLogeado != null){
            JOptionPane.showMessageDialog(this, "¡Bienvenido, "+user+"!", "Login Exitoso", JOptionPane.INFORMATION_MESSAGE);
            //exito al abrir menu principal
            MenuPrincipal menuPrincipal = new MenuPrincipal(sistema, playerLogeado);
            menuPrincipal.setVisible(true);
            this.dispose();
        } else{
            //mostrar la falla
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error de Login", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void manejarCrearPlayer(){
        String user = txtUsername.getText();
        String contra = new String(txtPassword.getPassword());
        
        boolean exito = sistema.crearPlayer(user, contra);
        
        if(exito){
            JOptionPane.showMessageDialog(this, "Cuenta creada con exito, Ya puedes iniciar sesion.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
        } else{
            JOptionPane.showMessageDialog(this, "Error al crear cuenta. Verifica que el usuario no existe y que la contraseña tenga 5 caracteres", "Error de registro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
