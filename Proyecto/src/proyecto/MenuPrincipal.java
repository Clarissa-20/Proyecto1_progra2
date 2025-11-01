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
        //initComponents();
        this.sistema = sistema;
        this.jugadorActual = jugador;

        setTitle("Vampire Wargame - Menu Principal");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        //panel central de btns
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(4, 1, 20, 20)); //4 f, 1 c

        JButton btnJugar = new JButton("JUGAR");
        JButton btnMiCuenta = new JButton("MI CUENTA");
        JButton btnReportes = new JButton("REPORTES");
        JButton btnLogOut = new JButton("LOG OUT");

        panelBotones.add(btnJugar);
        panelBotones.add(btnMiCuenta);
        panelBotones.add(btnReportes);

        //añadir componentes a la vtn
        add(new JLabel("Bienvenido, " + jugador.getUsername(), SwingConstants.CENTER), BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);
        add(btnLogOut, BorderLayout.SOUTH);

        //manejo de eventos
        //jugar
        btnJugar.addActionListener(e -> iniciarJuego());
        //mi cuenta(abre otra vtn para el manejo de la cuenta)
        btnMiCuenta.addActionListener(e -> new MiCuenta(sistema, jugadorActual).setVisible(true));
        //reportes
        btnReportes.addActionListener(e -> new Reportes(sistema, jugadorActual).setVisible(true));
        //log out
        btnLogOut.addActionListener(e -> manejarLogOut());
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
