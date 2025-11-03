/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class MenuPrincipal extends JFrame{
    private SistemaJuego sistema;
    private Player jugadorActual; //referencia al jugador logeado

    public MenuPrincipal(SistemaJuego sistema, Player jugador) {
        this.sistema = sistema;
        this.jugadorActual = jugador;

        setTitle("Vampire Wargame - Menu Principal");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        
        fondoPanel fp = new fondoPanel("/img/fondoMenuPrincipal.png");
        fp.setLayout(new BorderLayout(20, 20));
        
        //añadir componentes a la vtn
        JLabel titulo = new JLabel("¡Bienvenido, " + jugador.getUsername()+"!", SwingConstants.CENTER);
        titulo.setFont(new Font("Bodoni Bd BT", Font.BOLD, 30));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        fp.add(titulo, BorderLayout.NORTH);
        
        //panel central de btns
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(4, 1, 20, 20)); //4 f, 1 c
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 200, 60, 200));
        panelBotones.setOpaque(false);

        JButton btnJugar = crearBotones("JUGAR");
        JButton btnMiCuenta = crearBotones("MI CUENTA");
        JButton btnReportes = crearBotones("REPORTES");
        JButton btnLogOut = crearBotones("LOG OUT");

        panelBotones.add(btnJugar);
        panelBotones.add(btnMiCuenta);
        panelBotones.add(btnReportes);
        panelBotones.add(btnLogOut);
        
        fp.add(panelBotones, BorderLayout.CENTER);
        
        this.add(fp);

        //manejo de eventos
        btnJugar.addActionListener(e -> iniciarJuego());
        btnMiCuenta.addActionListener(e -> new MiCuenta(sistema, jugadorActual).setVisible(true));
        btnReportes.addActionListener(e -> new Reportes(sistema, jugadorActual).setVisible(true));
        btnLogOut.addActionListener(e -> manejarLogOut());
    }
    
    private JButton crearBotones(String texto) {
        JButton btn = new JButton(texto);
        Color color = new Color(220, 220, 220);
        
        btn.setFont(new Font("Bodoni Bd BT", Font.BOLD, 20));
        btn.setForeground(color);
        btn.setBackground(Color.BLACK);
        btn.setPreferredSize(new Dimension(250, 50));

        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 5),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        
        return btn;
    }
    
    private void iniciarJuego(){
        JOptionPane.showMessageDialog(this, "Iniciando juego. Necesitas seleccionar 2 jugadores", "Inicia Juego", JOptionPane.INFORMATION_MESSAGE);
        /*pendiente: aqui se debe de abrir la VentanaJuego pasandole dos objetos Plyer(el logeado y el rival seleccionado
        new VentanaJuego(sistema, jugadorActual, rival).setVisible(true);*/
        
        //selec el rival
        Player rival = null;
        ArrayList<Player> jugadores = sistema.obtenerRanking(); //ontener lista de jugadores activos
        
        for(Player p : jugadores){
            if(!p.getUsername().equals(jugadorActual.getUsername())){
                rival = p;
                break;
            }
        }
        
        if(rival == null){
            JOptionPane.showMessageDialog(this, "No hay suficientes jugadores activos para iniciar la partida", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //abrir la vtn del juego
        //p1 negro, p2 blanco
        vtnJuego juego = new vtnJuego(sistema, jugadorActual, rival);
        juego.setVisible(true);
        
        this.dispose();
    }
    
    private void manejarLogOut(){
        //volver al menu inicio
        new MenuInicio(sistema).setVisible(true);
        this.dispose();
    }
}
