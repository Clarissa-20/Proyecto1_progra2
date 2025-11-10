/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package proyecto;

import java.awt.*;
import javax.swing.*;

public class MiCuenta extends JFrame{
    private SistemaJuego miSistema;
    private Player jugadorActual;
    
    private final Color color1 = new Color(150, 0, 0);
    private final Color colorTexto = Color.WHITE;
    private final Font btnFont = new Font("Bodoni Bd BT", Font.BOLD, 20);
    
    public MiCuenta(SistemaJuego sistema, Player jugador){
        this.miSistema = sistema;
        this.jugadorActual = jugador;
        
        setTitle("Mi Cuenta");
        setSize(800, 550);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        initComponents();
        this.setVisible(true);
    }
    
    private void initComponents(){
        fondoPanel fp = new fondoPanel("/img/fondoC.png");
        fp.setLayout(new BorderLayout());
        
        JLabel titulo = new JLabel("MI CUENTA", SwingConstants.CENTER);
        titulo.setFont(new Font("Bodoni Bd BT", Font.BOLD, 24));
        titulo.setForeground(colorTexto);
        titulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(4, 1, 15, 15));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30, 200, 60, 200));
        panelBotones.setOpaque(false);
        
        JButton btnVerInfo = crearBoton("VER MI INFORMACION");
        JButton btnCambiarContra = crearBoton("CAMBIAR PASSWORD");
        JButton btnCerrarCuenta = crearBoton("CERRAR MI CUENTA");
        JButton btnVolver = crearBoton("VOLVER");
        
        panelBotones.add(btnVerInfo);
        panelBotones.add(btnCambiarContra);
        panelBotones.add(btnCerrarCuenta);
        panelBotones.add(btnVolver);
        
        btnVerInfo.addActionListener(e -> vtnInfo());
        btnCambiarContra.addActionListener(e -> vtnCambioContra());
        btnCerrarCuenta.addActionListener(e -> vtnCerrarCuenta());
        btnVolver.addActionListener(e -> vtnVolver());
        
        fp.add(titulo, BorderLayout.NORTH);
        fp.add(panelBotones, BorderLayout.CENTER);
        
        this.add(fp);
    }
    
    private JButton crearBoton(String texto){
        JButton btn = new JButton(texto);
        btn.setFont(btnFont);
        btn.setBackground(color1);
        btn.setForeground(colorTexto);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(250, 50));
        
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 5),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        return btn;
    }
    
    private JButton btnEliminar(String texto){
        JButton btn = new JButton(texto);
        btn.setFont(btnFont);
        btn.setBackground(Color.RED.darker());
        btn.setForeground(colorTexto);
        btn.setFocusPainted(false);
        return btn;
    }
    
    private void vtnInfo(){
        verMiInfo info = new verMiInfo( jugadorActual);
        info.setVisible(true);
        this.dispose();
    }
    
    private void vtnCambioContra(){
        cambiarContra contra = new cambiarContra(miSistema, jugadorActual);
        contra.setVisible(true);
        this.dispose();
    }
    
    private void vtnCerrarCuenta(){
        cerrarCuenta cerrar = new cerrarCuenta(miSistema, jugadorActual);
        cerrar.setVisible(true);
        this.dispose();
    }
    
    private void vtnVolver(){
        new MenuPrincipal(miSistema, jugadorActual).setVisible(true);
        this.dispose(); 
    }
}