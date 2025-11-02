/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import javax.swing.*;
import java.awt.*;

public class vtnJuego extends JFrame {

    private Tablero tablero;
    private CasillaPanel[][] tableroVisual;
    private SistemaJuego sistema;
    private String accionEspecial = null;

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

        setTitle("Vampire Wargame - Turno: " + turnoActual);
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

    private JPanel panelControl() {
        JPanel panelControl = new JPanel();
        panelControl.setLayout(new GridLayout(6, 1, 10, 10));
        panelControl.setBorder(BorderFactory.createTitledBorder("Control de Juego"));

        //info turno
        turnoInfo = new JLabel("Turno Actual: " + turnoActual, SwingConstants.CENTER);
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

    public void manejarRuleta() {
        //logica de giros extra si se pierden piezas
        int piezasPerdidas = 6 - tablero.contarPiezasVivas(turnoActual);

        if (piezasPerdidas >= 4) {
            girosRestantes = 3;
        } else if (piezasPerdidas >= 2) {
            girosRestantes = 2;
        } else {
            girosRestantes = 1;
        }

        if (girosRestantes > 0) {
            //llama a la logica de tablero para selec la pieza al azar
            piezaPermitida = tablero.seleccionarPiezaAlAzar(turnoActual);
            girosRestantes--;

            piezaSeleccionada.setText("Pieza a Mover: " + piezaPermitida);
            btnRuleta.setEnabled(girosRestantes > 0);

            //verificar si el jugador tiene la pieza seleccionada
            if (piezaPermitida == null || !tablero.tienePiezasDeTipo(turnoActual, piezaPermitida)) {
                //si la pieza seleccionada al azar no existe, se le permite girar de nuevo si tiene giros
                if (girosRestantes == 0) {
                    JOptionPane.showMessageDialog(this, "La ruleta selecciono una pieza que no tienes. Pierdes turno.", "Turno Perdido", JOptionPane.INFORMATION_MESSAGE);
                    cambiarTurno();
                } else {
                    JOptionPane.showMessageDialog(this, "La ruleta selecciono una pieza que no tienes. Intenta girar de nuevo(Giros restantes: ." + girosRestantes + ").", "Intenta de nuevo", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    //metodo sincronozar la matriz logica(Tablero.matriz) con GUI
    private void actualizarTableroVisual() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                Pieza p = tablero.getPieza(i, j); //nesecita un getter en tablero para esto
                tableroVisual[i][j].PiezaVisual(p);
                tableroVisual[i][j].desresaltar(); //limpia cualquer resultado anterior
            }
        }
        setTitle("Vampire Wargame - Turno: " + turnoActual);
    }

    //logica que se ejecuta cada vez que el usuario hace click en una casilla
    public void manejarClickCasilla(int fila, int columna) {
        Pieza piezaClickeada = tablero.getPieza(fila, columna);

        if (!piezaSelec) {
            //1 selec origen

            //validacion de la ruleta
            if (piezaPermitida == null) {
                JOptionPane.showMessageDialog(this, "Primero debes girar la ruleta para selecionar tu pieza", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //validar que la pieza sea del color correcto y del tipo permitido por la ruleta
            if (piezaClickeada != null && piezaClickeada.getColor().equals(turnoActual) && piezaClickeada.getTipo().equals(piezaPermitida)) {
                if (piezaClickeada.getTipo().equals("Muerte")) {
                    String[] opcM = {"Mover/Atacar", "Conjurar Zombie"};
                    int seleccion = JOptionPane.showOptionDialog(this,
                            "¿Que accion deseas realizar con la Muerte?",
                            "Opcion de Muerte",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, opcM, opcM[0]);

                    if (seleccion == 1) { //se seleciona conjurar el zombie
                        this.accionEspecial = "conjurarZombie";
                        origenFila = fila;
                        origenColumna = columna;
                        piezaSelec = true;
                        tableroVisual[fila][columna].resaltar(Color.ORANGE);
                        JOptionPane.showMessageDialog(this, "Selecciona una casilla vacia para conjurar el Zombie");
                        return; //terminamos el primer click, esperando destino
                    }
                }

                //flujo normal de seleccion de origen
                origenFila = fila;
                origenColumna = columna;
                piezaSelec = true;
                tableroVisual[fila][columna].resaltar(Color.GREEN);
                JOptionPane.showMessageDialog(null, "Piezaseleccionada:" + piezaClickeada.getTipo() + ". Selecciona la casilla destino");
            } else {
                //error de selecion
                JOptionPane.showMessageDialog(this, "Selecciona una pieza valida de tu color y seleccionada por la ruleta (" + piezaPermitida + ")", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else{
            //2 seleccionar destino/ataque
            
            //desrsaltar la pieza de origen
            tableroVisual[origenFila][origenColumna].desresaltar();
            
            Pieza atacante = tablero.getPieza(origenFila, origenColumna);
            Pieza defensor = tablero.getPieza(fila, columna);
            boolean exito = false;
            String mensaje = null;
            
            //manejo de conjurar el zombie, segundo click
            if(this.accionEspecial != null && this.accionEspecial.equals("conjurarZombie")){
                //verifica que este vacio y sea adyacente
                if(defensor == null && tablero.calcularDistancia(origenFila, origenColumna, fila, columna) == 1){
                    tablero.ConjurarZombie(origenFila, origenColumna, fila, columna);
                    mensaje = "¡Zombie conjurado!";
                    exito = true;
                } else{
                    mensaje = "Solo puedes conjurar un Zombie en una casilla vacia adyacente";
                } 
                this.accionEspecial = null; //resetera el estado especial
            } else if(defensor == null){
                //movimiento simple - destino vacio
                exito = tablero.ejecutarMovimiento(origenFila, origenColumna, fila, columna);
                if(exito){
                    mensaje = "Movimiento exitoso";
                } else{
                    mensaje = "Movimiento invalido, distancia o direccion incorrecta";
                }
            } else if(defensor.getColor().equals(atacante.getColor())){
                //ocupada por pieza propia
                mensaje = " Casilla ocupada por pieza propia, selecciona otra casila";
            } else{
                //ataque a rivalk, se puede elejir el ataque normal o especial
                String tipoAtacante = atacante.getTipo();
                String[] opcAtaque = {"Ataque Normal"};
                
                //configurar las opc especiales
                if(tipoAtacante.equals("Vampiro") && tablero.calcularDistancia(origenFila, origenColumna, fila, columna) == 1){
                    opcAtaque = new String[]{"Ataque Normal", "Chupar Sangre"};
                } else if(tipoAtacante.equals("Muerte")){
                    opcAtaque = new String[]{"Ataque Normal", "Lanza", "Conjurar Zombie"};
                }
                
                String seleccionAtaque = (String) JOptionPane.showInputDialog(this, 
                        "Selecciona el tipo de ataque:",
                        "Ataque Especial",
                        JOptionPane.PLAIN_MESSAGE, null, opcAtaque, opcAtaque[0]);
                
                if(seleccionAtaque != null){
                    String ataqueTipo = "NORMAL";
                    if(seleccionAtaque.contains("Chupar Sangre")) ataqueTipo = "chuparSangre";
                    else if(seleccionAtaque.contains("Lanza")) ataqueTipo = "Lanza";
                    else if(seleccionAtaque.contains("Zombie")) ataqueTipo = "ataqueZombie";
                    
                    String resultadoAtaque = tablero.ejecutarAtaque(origenFila, origenColumna, fila, columna, ataqueTipo);
                    
                    if(!resultadoAtaque.startsWith("ERROR")){
                        exito = true;
                        mensaje = resultadoAtaque;
                    } else{
                        mensaje = resultadoAtaque;
                    }
                } else{
                    mensaje = "Seleccion de ataque cancelada";
                }
            }
            
            //manejo del mensaje y fin del turno
            
            //mostrar msj de la accion, exit - error
            if(mensaje != null){
                if(mensaje.startsWith("ERROR")){
                    JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
                } else{
                    JOptionPane.showMessageDialog(this, mensaje, "Resultado", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            if(exito){
                actualizarTableroVisual();
                if(tablero.contarPiezasVivas(getOponenteColor()) == 0){
                    manejarFinJuego();
                } else{
                    cambiarTurno();
                }
            }
            
            //resetear el estado del click para el isguiente turno o intento
            piezaSelec = false;
            origenFila = -1;
            origenColumna = -1;
        }
    }

    private void cambiarTurno() {
        turnoActual = turnoActual.equals("Negro") ? "Blanco" : "Negro";
        setTitle("Vampire Wargame - Turno: " + turnoActual);
        //llamar a la ruleta (si aplica)
        btnRuleta.setEnabled(true);
        piezaPermitida = null;
        piezaSeleccionada.setText("Pieza a mover: Ninguna");
    }

    private String getOponenteColor() {
        return turnoActual.equals("Negro") ? "Blanco" : "Negro";
    }

    private void manejarRetirarse() {
        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas retirarse? El oponente ganara la partida.",
                "Confirmar Retiro", JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            //el jugador actual se retira y es le perdedor
            Player perdedor = turnoActual.equals("Negro") ? jugador1 : jugador2;
            Player ganador = turnoActual.equals("Negro") ? jugador2 : jugador1;

            //dar los puntos y guardar el log
            ganador.agregarPuntos(Tablero.puntosVictoria);
            GameLog log = new GameLog(ganador.getUsername(), perdedor.getUsername(), "VICTORIA POR RETIRO");
            sistema.guardarLog(log);

            JOptionPane.showMessageDialog(this,
                    "¡" + perdedor.getUsername() + " se ha retirado! ¡" + ganador.getUsername() + " has ganados 3 puntos", "VICTORIA POR RETIRO", JOptionPane.INFORMATION_MESSAGE);

            new MenuPrincipal(sistema, ganador).setVisible(true);
            this.dispose();
        }
    }

    private void manejarFinJuego() {
        Player ganador = turnoActual.equals("Negro") ? jugador1 : jugador2;
        Player perdedor = turnoActual.equals("Blanco") ? jugador1 : jugador2;

        //dar puntos al ganador
        ganador.agregarPuntos(Tablero.puntosVictoria); //asumiendo que es public static final

        //guardar log
        GameLog log = new GameLog(ganador.getUsername(), perdedor.getUsername(), "VICTORIA POR DESTRUCCION TOTAL DE PIEZAS");
        sistema.guardarLog(log);

        //mostrar msj y volver al menu principal
        JOptionPane.showMessageDialog(this, "¡Juego Terminado! El ganador es: " + ganador.getUsername(), "VICTORIA", JOptionPane.INFORMATION_MESSAGE);

        //cierra la vtn de juego y abre la principal, pendiente reabrir el menu principal
        new MenuPrincipal(sistema, ganador).setVisible(true);
        this.dispose();
    }
}
