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
    private final Font btnFont = new Font("Bodoni Bd BT", Font.BOLD, 16);
    
    public MiCuenta(SistemaJuego sistema, Player jugador){
        this.miSistema = sistema;
        this.jugadorActual = jugador;
        
        setTitle("Menu Inicio");
        setSize(800, 550);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        initComponents();
        this.setVisible(true);
    }
    
    private void initComponents(){
        fondoPanel fp = new fondoPanel("/img/fondoC.png");
        fp.setLayout(new GridLayout(4, 1, 15, 15));
        
        JLabel titulo = new JLabel("MI CUENTA", SwingConstants.CENTER);
        titulo.setFont(new Font("Bodoni Bd BT", Font.BOLD, 24));
        titulo.setForeground(colorTexto);
        
        JButton btnVerInfo = crearBoton("VER MI INFORMACION");
        JButton btnCambiarContra = crearBoton("CAMBIAR PASSWORD");
        JButton btnCerrarCuenta = crearBoton("CERRAR MI CUENTA");
        
        
        btnVerInfo.addActionListener(e -> vtnInfo());
        btnCambiarContra.addActionListener(e -> vtnCambioContra());
        btnCerrarCuenta.addActionListener(e -> vtnCerrarCuenta());
        
        fp.add(titulo);
        fp.add(btnVerInfo);
        fp.add(btnCambiarContra);
        fp.add(btnCerrarCuenta);
        
        this.setContentPane(fp);
    }
    
    private JButton crearBoton(String texto){
        JButton btn = new JButton(texto);
        btn.setFont(btnFont);
        btn.setBackground(color1);
        btn.setForeground(colorTexto);
        btn.setFocusPainted(false);
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
        verMiInfo info = new verMiInfo(jugadorActual);
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
}