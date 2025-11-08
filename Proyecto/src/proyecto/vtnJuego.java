/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

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

    private JLabel imgRuleta;
    private JLabel ruletaGif;
    private JPanel panelRuleta;
    private static final String[] opcRuleta = {"Vampiro", "HombreLobo", "Muerte"};
    private static final int tiempoGiro = 3000;

    public vtnJuego(SistemaJuego sistema, Player p1, Player p2) {
        this.sistema = sistema;
        this.jugador1 = p1;
        this.jugador2 = p2;
        this.tablero = new Tablero(); //inicializa el tablero
        this.tableroVisual = new CasillaPanel[6][6];
        this.turnoActual = "Negro"; //este empieza

        setTitle("Vampire Wargame - Turno: " + turnoActual);
        setSize(900, 600);
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

    private JLabel getScaledLabel(String path, int width, int height){
        ImageIcon originalIcon = new ImageIcon(getClass().getResource(path));
        Image originalImage = originalIcon.getImage();
        // Escalar la imagen a las dimensiones deseadas
        Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(scaledImage));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
    
    private JPanel panelControl() {
        JPanel panelControl = new JPanel();
        //panelControl.setLayout(new GridLayout(6, 1, 10, 10));
        panelControl.setLayout(new BoxLayout(panelControl, BoxLayout.Y_AXIS));
        panelControl.setBorder(BorderFactory.createTitledBorder("Control de Juego"));
        panelControl.setPreferredSize(new Dimension(300, 0));

        final int tamañoRuleta = 280;
        
        //info turno
        turnoInfo = new JLabel("Turno Actual: " + turnoActual, SwingConstants.CENTER);
        turnoInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelControl.add(turnoInfo);
        panelControl.add(Box.createVerticalStrut(10));

        //panel de la ruleta
        panelRuleta = new JPanel();
        panelRuleta.setLayout(new CardLayout());
        panelRuleta.setOpaque(false);
        panelRuleta.setPreferredSize(new Dimension(tamañoRuleta, tamañoRuleta));

        imgRuleta = getScaledLabel("/img/ruleta.png", tamañoRuleta, tamañoRuleta);
        //ruletaGif = getScaledLabel("/img/gifRuleta.gif", tamañoRuleta, tamañoRuleta);
        ImageIcon gifIcon = new ImageIcon(getClass().getResource("/img/gifRuleta.gif"));
        Image gifImg = gifIcon.getImage();
        Image scaledGif = gifImg.getScaledInstance(tamañoRuleta, tamañoRuleta, Image.SCALE_DEFAULT);
        ruletaGif = new JLabel(new ImageIcon(scaledGif));
        ruletaGif.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelRuleta.add(imgRuleta, "ESTATICA");
        panelRuleta.add(ruletaGif, "GIRANDO");

        ((CardLayout) panelRuleta.getLayout()).show(panelRuleta, "ESTATICA");
        panelControl.add(panelRuleta);
        panelControl.add(Box.createVerticalStrut(10));

        //ruleta y pieza selec
        btnRuleta = new JButton("GIRAR RULETA");
        btnRuleta.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelControl.add(btnRuleta);
        panelControl.add(Box.createVerticalStrut(10));

        piezaSeleccionada = new JLabel("Pieza a Mover: Ninguna", SwingConstants.CENTER);
        piezaSeleccionada.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelControl.add(piezaSeleccionada);
        panelControl.add(Box.createVerticalStrut(20));

        //btns de ataque especial, se activan por turno, pendientes hasta implementar la ruleta
        //btn retirar
        btnRetirar = new JButton("RETIRAR");
        btnRetirar.setBackground(Color.RED);
        btnRetirar.setForeground(Color.WHITE);
        btnRetirar.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelControl.add(btnRetirar);

        btnRuleta.addActionListener(e -> manejarRuleta());
        btnRetirar.addActionListener(e -> manejarRetirarse());

        return panelControl;
    }

    public void manejarRuleta() {
        //logica de giros extra si se pierden piezas
        int piezasPerdidas = 6 - tablero.contarPiezasVivas(turnoActual);
        girosRestantes = (piezasPerdidas >= 4) ? 3 : (piezasPerdidas >= 2) ? 2 : 1;

        if (girosRestantes > 0) {
            btnRuleta.setEnabled(false);
            piezaSeleccionada.setText("Girando...");

            ((CardLayout) panelRuleta.getLayout()).show(panelRuleta, "GIRANDO");

            Timer timer = new Timer(tiempoGiro, e -> {
                String tipoPermitidoRuleta = obtenerResultado();
                if (tipoPermitidoRuleta != null && tablero.tienePiezasDeTipo(turnoActual, tipoPermitidoRuleta)) {
                    piezaPermitida = tipoPermitidoRuleta;
                    girosRestantes = 0;

                    String rutaResultado = "/img/ruletaResultado" + piezaPermitida.toLowerCase().replace(" ", "_") + ".png";
                    final int tamañoRuleta = 280;
                    ImageIcon originalIcon = new ImageIcon(getClass().getResource(rutaResultado));
                    Image originalImage = originalIcon.getImage();
                    Image scaledImage = originalImage.getScaledInstance(tamañoRuleta, tamañoRuleta, Image.SCALE_SMOOTH);
                    imgRuleta.setIcon(new ImageIcon(scaledImage));
                    ((CardLayout) panelRuleta.getLayout()).show(panelRuleta, "ESTATICA");

                    piezaSeleccionada.setText("¡Mueve: " + piezaPermitida + "!");
                    JOptionPane.showMessageDialog(this, "La ruleta permite mover: " + piezaPermitida, "Turno Asignado", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    girosRestantes--;
                    imgRuleta.setIcon(new ImageIcon(getClass().getResource("/img/ruleta.png")));
                    ((CardLayout) panelRuleta.getLayout()).show(panelRuleta, "ESTATICA");

                    if (girosRestantes == 0) {
                        JOptionPane.showMessageDialog(this, "La ruleta selecciono una pieza que no tienes. Pierdes Turno", "Turno Perdido", JOptionPane.INFORMATION_MESSAGE);
                        cambiarTurno();
                    } else {
                        piezaSeleccionada.setText("Giros restantes: " + girosRestantes);
                        JOptionPane.showMessageDialog(this, "La ruleta selecciono una pieza que no tienes. Intenta girar de nuevo", "Intenta de nuevo", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                btnRuleta.setEnabled(girosRestantes > 0);

                ((Timer) e.getSource()).stop();
            });
            timer.setRepeats(false);
            timer.start();
        }
    }
    
    private String obtenerResultado(){
        Random rd = new Random();
        int indiceAleatorio = rd.nextInt(opcRuleta.length);
        return opcRuleta[indiceAleatorio];
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
                JOptionPane.showMessageDialog(null, "Pieza seleccionada: " + piezaClickeada.getTipo() + ". Selecciona la casilla destino");
            } else {
                //error de selecion
                JOptionPane.showMessageDialog(this, "Selecciona una pieza valida de tu color y seleccionada por la ruleta (" + piezaPermitida + ")", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            //2 seleccionar destino/ataque

            //desrsaltar la pieza de origen
            tableroVisual[origenFila][origenColumna].desresaltar();

            Pieza atacante = tablero.getPieza(origenFila, origenColumna);
            Pieza defensor = tablero.getPieza(fila, columna);
            boolean exito = false;
            String mensaje = null;

            //manejo de conjurar el zombie, segundo click
            if (this.accionEspecial != null && this.accionEspecial.equals("conjurarZombie")) {
                //verifica que este vacio y sea adyacente
                if (defensor == null && tablero.calcularDistancia(origenFila, origenColumna, fila, columna) == 1) {
                    tablero.ConjurarZombie(origenFila, origenColumna, fila, columna);
                    mensaje = "¡Zombie conjurado!";
                    exito = true;
                } else {
                    mensaje = "Solo puedes conjurar un Zombie en una casilla vacia adyacente";
                }
                this.accionEspecial = null; //resetera el estado especial
            } else if (defensor == null) {
                //movimiento simple - destino vacio
                exito = tablero.ejecutarMovimiento(origenFila, origenColumna, fila, columna);
                if (exito) {
                    mensaje = "Movimiento exitoso";
                } else {
                    mensaje = "Movimiento invalido, distancia o direccion incorrecta";
                }
            } else if (defensor.getColor().equals(atacante.getColor())) {
                //ocupada por pieza propia
                mensaje = " Casilla ocupada por pieza propia, selecciona otra casila";
            } else {
                //ataque a rivalk, se puede elejir el ataque normal o especial
                String tipoAtacante = atacante.getTipo();
                String[] opcAtaque = {"Ataque Normal"};

                //configurar las opc especiales
                if (tipoAtacante.equals("Vampiro") && tablero.calcularDistancia(origenFila, origenColumna, fila, columna) == 1) {
                    opcAtaque = new String[]{"Ataque Normal", "Chupar Sangre"};
                } else if (tipoAtacante.equals("Muerte")) {
                    opcAtaque = new String[]{"Ataque Normal", "Lanza", "Conjurar Zombie"};
                }

                String seleccionAtaque = (String) JOptionPane.showInputDialog(this,
                        "Selecciona el tipo de ataque:",
                        "Ataque Especial",
                        JOptionPane.PLAIN_MESSAGE, null, opcAtaque, opcAtaque[0]);

                if (seleccionAtaque != null) {
                    String ataqueTipo = "NORMAL";
                    if (seleccionAtaque.contains("Chupar Sangre")) {
                        ataqueTipo = "chuparSangre";
                    } else if (seleccionAtaque.contains("Lanza")) {
                        ataqueTipo = "Lanza";
                    } else if (seleccionAtaque.contains("Zombie")) {
                        ataqueTipo = "ataqueZombie";
                    }

                    String resultadoAtaque = tablero.ejecutarAtaque(origenFila, origenColumna, fila, columna, ataqueTipo);

                    if (!resultadoAtaque.startsWith("ERROR")) {
                        exito = true;
                        mensaje = resultadoAtaque;
                    } else {
                        mensaje = resultadoAtaque;
                    }
                } else {
                    mensaje = "Seleccion de ataque cancelada";
                }
            }

            //manejo del mensaje y fin del turno
            //mostrar msj de la accion, exit - error
            if (mensaje != null) {
                if (mensaje.startsWith("ERROR")) {
                    JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, mensaje, "Resultado", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            if (exito) {
                actualizarTableroVisual();
                if (tablero.contarPiezasVivas(getOponenteColor()) == 0) {
                    manejarFinJuego();
                } else {
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
        btnRuleta.setEnabled(true);
        
        final int tamañoRuleta = 280;
        imgRuleta.setIcon(getScaledLabel("/img/ruleta.png", tamañoRuleta, tamañoRuleta).getIcon());
        ((CardLayout) panelRuleta.getLayout()).show(panelRuleta, "ESTATICA");
        
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
                    "¡" + perdedor.getUsername() + " se ha retirado! ¡Felicidades jugador " + ganador.getUsername() + ", has ganados 3 puntos!", "VICTORIA POR RETIRO", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("RETIRO: jugador " + perdedor.getUsername() + " se ha retirado de la partida. Jugador " + ganador.getUsername() + " has ganado");

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
        JOptionPane.showMessageDialog(this, "¡Juego Terminado! El jugador " + ganador.getUsername() + " vencio al jugador " + perdedor.getUsername() + ". Has ganado 3 puntos", "VICTORIA", JOptionPane.INFORMATION_MESSAGE);
        System.out.println("FIN JUEGO: jugador " + ganador.getUsername() + " has ganado la partida. Perdedor: " + perdedor.getUsername());
        //cierra la vtn de juego y abre la principal, pendiente reabrir el menu principal
        new MenuPrincipal(sistema, ganador).setVisible(true);
        this.dispose();
    }
}
