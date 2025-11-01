/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import javax.swing.*;
import java.awt.*;

public class vtnJuego extends JFrame{
    private Tablero tablero;
    private CasillaPanel[][] tableroVisual;
    private SistemaJuego sistema;
    
    //logica del estado del click(para saber si es origen o destino)
    private int origenFila = -1, origenColumna = -1;
    private boolean piezaSelec = false;
    
    private Player jugador1; //negro
    private Player jugador2; //blanco
    private String turnoActual; // negro o blanco
    
    private JLabel turnoInfo;
    private JButton btnRuleta;
    private JLabel piezaSeleccionada; //muestra la img eleigida al azar
    private JButton btnRetirar;
    
    private String piezaPermitida = null;
    private int girosRestantes = 1; //por defaut

    public vtnJuego(SistemaJuego sistema, Player p1, Player p2) {
        //initComponents();
        this.sistema = sistema;
        this.jugador1 = p1;
        this.jugador2 = p2;
        this.tablero = new Tablero(); //inicializa el tablero
        this.tableroVisual = new CasillaPanel[6][6];
        this.turnoActual = "Negro"; //este empieza
        
        setTitle("Vampire Wargame - Turno: "+turnoActual);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        //crear el panel del tablero visual
        JPanel panelTablero = new JPanel(new GridLayout(6, 6));
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                CasillaPanel casilla = new CasillaPanel(i, j, this);
                tableroVisual[i][j] = casilla;
                panelTablero.add(casilla);
            }
        }
        add(panelTablero, BorderLayout.CENTER);
        add(panelControl(), BorderLayout.EAST);
        
        /*crear el panle del control
        ruleta, info de turno, btn de retirar*/
        
        //inicializar el tablero con las imgs de las piezas
        actualizarTableroVisual();
    }
    
    private JPanel panelControl(){
        JPanel panelControl = new JPanel();
        panelControl.setLayout(new GridLayout(6, 1, 10, 10));
        panelControl.setBorder(BorderFactory.createTitledBorder("Control de Juego"));
        
        //info turno
        turnoInfo = new JLabel("Turno Actual: "+turnoActual, SwingConstants.CENTER);
        panelControl.add(turnoInfo);
        
        //ruleta y pieza selec
        btnRuleta = new JButton("GIRAR RULETA");
        piezaSeleccionada = new JLabel("Pieza a Mover: Ninguna", SwingConstants.CENTER);
        panelControl.add(btnRuleta);
        panelControl.add(piezaSeleccionada);
        
        //btns de ataque especial, se activan por turno, pendientes hasta implementar la ruleta
        
        //btn retirar
        btnRetirar = new JButton("RETIRAR");
        btnRetirar.setBackground(Color.RED);
        btnRetirar.setForeground(Color.WHITE);
        panelControl.add(btnRetirar);
        
        btnRuleta.addActionListener(e -> manejarRuleta());
        btnRetirar.addActionListener(e -> manejarRetirarse());
        
        return panelControl;
    }
    
    public void manejarRuleta(){
        //logica de giros extra si se pierden piezas
        int piezasPerdidas = 6 - tablero.contarPiezasVivas(turnoActual);
        
        if(piezasPerdidas >= 4){
            girosRestantes = 3;
        } else if(piezasPerdidas >= 2){
            girosRestantes = 2;
        } else{
            girosRestantes = 1;
        }
        
        if(girosRestantes > 0){
            //llama a la logica de tablero para selec la pieza al azar
            piezaPermitida = tablero.seleccionarPiezaAlAzar(turnoActual);
            girosRestantes --;
            
            piezaSeleccionada.setText("Pieza a Mover: "+piezaPermitida);
            btnRuleta.setEnabled(girosRestantes > 0);
            
            //verificar si el jugador tiene la pieza seleccionada
            if(piezaPermitida == null || !tablero.tienePiezasDeTipo(turnoActual, piezaPermitida)){
                //si la pieza seleccionada al azar no existe, se le permite girar de nuevo si tiene giros
                if(girosRestantes == 0){
                    JOptionPane.showMessageDialog(this, "La ruleta selecciono una pieza que no tienes. Pierdes turno.", "Turno Perdido", JOptionPane.INFORMATION_MESSAGE);
                    cambiarTurno();
                } else{
                    JOptionPane.showMessageDialog(this, "La ruleta selecciono una pieza que no tienes. Intenta girar de nuevo(Giros restantes: ."+girosRestantes+").", "Intenta de nuevo", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }
    
    //metodo sincronozar la matriz logica(Tablero.matriz) con GUI
    private void actualizarTableroVisual(){
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                Pieza p = tablero.getPieza(i, j); //nesecita un getter en tablero para esto
                tableroVisual[i][j].PiezaVisual(p);
                tableroVisual[i][j].desresaltar(); //limpia cualquer resultado anterior
            }
        }
        setTitle("Vampire Wargame - Turno: "+turnoActual);
    }
    
    //logica que se ejecuta cada vez que el usuario hace click en una casilla
    public void manejarClickCasilla(int fila, int columna){
        if(!piezaSelec){
            //selec origen
            Pieza p = tablero.getPieza(fila, columna);
            
            //verificar que sol se puede mover la pieza selec por la ruleta
            if(piezaPermitida == null){
                JOptionPane.showMessageDialog(this, "Primero deber girar la ruleta para seleccionar tu pieza", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            if(p != null && p.getColor().equals(turnoActual) && p.getTipo().equals(piezaPermitida)){
                //pieza valida del color del turno
                origenFila = fila;
                origenColumna = columna;
                piezaSelec = true;
                tableroVisual[fila][columna].resaltar(Color.GREEN);
                JOptionPane.showMessageDialog(null, "Pieza seleccionada: "+p.getTipo());
            } else{
                JOptionPane.showMessageDialog(this, "Selecciona una pieza valida de tu color");
            }
        } else{
            //selec el destino/ataque
            
            //desresaltar la pieza de origen
            tableroVisual[origenFila][origenColumna].desresaltar();
            
            //ejecutar la logica del movimiento de movimiento/ataque
            boolean exito = tablero.ejecutarMovimiento(origenFila, origenColumna, fila, columna);
            
            if(exito){
                //si la accion fue exitosa, se act la GUI
                actualizarTableroVisual();
                
                //revisar fin del juego y cambio de turno
                if(tablero.contarPiezasVivas(getOponteColor()) == 0){
                    manejarFinJuego();
                } else{
                    cambiarTurno();
                }
            } else{
                JOptionPane.showMessageDialog(this, "Movimiento o ataque invalida", "Error", JOptionPane.ERROR_MESSAGE);
                //si falla, se queda en el mismo truno y la pieza deja de estar seleccionada
            }
            //resetar el estado del click
            piezaSelec = false;
            origenFila = -1;
            origenColumna = -1;
        }
    }
    
    private void cambiarTurno(){
        turnoActual = turnoActual.equals("Negro") ? "Blanco" : "Negro";
        setTitle("Vampire Wargame - Turno: "+turnoActual);
        //llamar a la ruleta (si aplica)
        btnRuleta.setEnabled(true);
        piezaPermitida = null;
        piezaSeleccionada.setText("Pieza a mover: Ninguna");
    }
    
    private String getOponteColor(){
        return turnoActual.equals("Negro") ? "Blanco" : "Negro";
    }
    
    private void manejarRetirarse(){
        int confirmar = JOptionPane.showConfirmDialog(this, 
                "¿Seguro que deseas retirarse? El oponente ganara la partida.", 
                "Confirmar Retiro", JOptionPane.YES_NO_OPTION);
        
        if(confirmar == JOptionPane.YES_OPTION){
            //el jugador actual se retira y es le perdedor
            Player perdedor = turnoActual.equals("Negro") ? jugador1 : jugador2;
            Player ganador = turnoActual.equals("Negro") ? jugador2 : jugador1;
            
            //dar los puntos y guardar el log
            ganador.agregarPuntos(Tablero.puntosVictoria);
            GameLog log = new GameLog(ganador.getUsername(), perdedor.getUsername(), "VICTORIA POR RETIRO");
            sistema.guardarLog(log);
            
            JOptionPane.showMessageDialog(this, 
                    "¡"+perdedor.getUsername()+" se ha retirado! ¡"+ganador.getUsername()+" has ganados 3 puntos", "VICTORIA POR RETIRO", JOptionPane.INFORMATION_MESSAGE);
            
            new MenuPrincipal(sistema, ganador).setVisible(true);
            this.dispose();
        }
    }
    
    private void manejarFinJuego(){
        Player ganador = turnoActual.equals("Negro") ? jugador1 : jugador2;
        Player perdedor = turnoActual.equals("Blanco") ? jugador1 : jugador2;
        
        //dar puntos al ganador
        ganador.agregarPuntos(Tablero.puntosVictoria); //asumiendo que es public static final
        
        //guardar log
        GameLog log = new GameLog(ganador.getUsername(), perdedor.getUsername(), "VICTORIA POR DESTRUCCION TOTAL DE PIEZAS");
        sistema.guardarLog(log);
        
        //mostrar msj y volver al menu principal
        JOptionPane.showMessageDialog(this, "¡Juego Terminado! El ganador es: "+ganador.getUsername(), "VICTORIA", JOptionPane.INFORMATION_MESSAGE);
        
        //cierra la vtn de juego y abre la principal, pendiente reabrir el menu principal
        new MenuPrincipal(sistema, ganador).setVisible(true);
        this.dispose();
    }
}
