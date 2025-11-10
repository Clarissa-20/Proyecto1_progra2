/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class vtnJuego extends JFrame {

    private Tablero tablero;
    private CasillaPanel[][] tableroVisual;
    private SistemaJuego sistema;
    private String accionEspecial = null;

    private int origenFila = -1, origenColumna = -1;
    private boolean piezaSelec = false;

    private Player jugador1;
    private Player jugador2; 
    private String turnoActual; 

    private JLabel turnoInfo;
    private JButton btnRuleta;
    private JLabel piezaSeleccionada; 
    private JButton btnRetirar;

    private String piezaPermitida = null;
    private int girosRestantes = 1; 

    private JLabel imgRuleta;
    private JLabel ruletaGif;
    private JPanel panelRuleta;
    private static final String[] opcRuleta = {"Vampiro", "HombreLobo", "Muerte"};
    private static final int tiempoGiro = 3000;
    
    private JPanel cementerioNegro;
    private JPanel cementerioBlanco;
    private JLabel jugadorActual;

    public vtnJuego(SistemaJuego sistema, Player p1, Player p2) {
        this.sistema = sistema;
        this.jugador1 = p1;
        this.jugador2 = p2;
        this.tablero = new Tablero();
        this.tableroVisual = new CasillaPanel[6][6];
        this.turnoActual = "Blanco"; 

        setTitle("Vampire Wargame - Juego");
        setSize(1500, 800);
        setLocationRelativeTo(null);
        
        fondoPanel fp = new fondoPanel("/img/fondoJuego.png");
        fp.setLayout(new BorderLayout());
        setContentPane(fp);

        JPanel panelTablero = new JPanel(new GridLayout(6, 6));
        panelTablero.setOpaque(false);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                CasillaPanel casilla = new CasillaPanel(i, j, this);
                tableroVisual[i][j] = casilla;
                panelTablero.add(casilla);
            }
        }
        
        panelTablero.setBorder(BorderFactory.createLineBorder(Color.BLACK,5));
        fp.add(panelControl(), BorderLayout.EAST);
        fp.add(panelJugadoresInfo(), BorderLayout.NORTH);
        
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        panelCentral.add(panelCementerios(), BorderLayout.WEST);
        panelCentral.add(panelTablero, BorderLayout.CENTER);
        fp.add(panelCentral, BorderLayout.CENTER);

        actualizarTableroVisual();
        actualizarInfoJugador();
    }

    private JPanel panelJugadoresInfo(){
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK,5));
        panel.setPreferredSize(new Dimension(50, 50));
        jugadorActual = new JLabel();
        jugadorActual.setFont(new Font("Bodoni Bd BT", Font.BOLD, 20));
        
        panel.add(jugadorActual); 
        
        return panel;
    }
    
    private JPanel panelCementerios(){
        JPanel panelPrincipal = new JPanel(new GridLayout(2, 1));
        panelPrincipal.setOpaque(false);
        panelPrincipal.setPreferredSize(new Dimension(300, 0));
        panelPrincipal.setBorder(BorderFactory.createLineBorder(Color.BLACK,5));
        
        cementerioNegro = new fondoPanel("/img/cementerio.jpeg");
        cementerioNegro.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
        cementerioNegro.setOpaque(false);
        Border bn = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Cementerio de: "+jugador1.getUsername(), 
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Bodoni Bd BT", Font.BOLD, 25), Color.BLACK);
        cementerioNegro.setBorder(bn);
        
        cementerioBlanco = new fondoPanel("/img/cementerio.jpeg");
        cementerioBlanco.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
        cementerioBlanco.setOpaque(false);
        Border bb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Cementerio de: "+jugador2.getUsername(), 
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Bodoni Bd BT", Font.BOLD, 25), Color.BLACK);
        cementerioBlanco.setBorder(bb);
        
        panelPrincipal.add(cementerioNegro);
        panelPrincipal.add(cementerioBlanco);
        
        return panelPrincipal;
    }
    
    private void actualizarInfoJugador(){
        String nombreJugador = turnoActual.equals("Blanco") ? jugador1.getUsername() : jugador2.getUsername();
        jugadorActual.setText("Turno: "+nombreJugador+" - "+turnoActual);
        jugadorActual.setFont(new Font("Bodoni Bd BT", Font.BOLD, 30));
        jugadorActual.setForeground(Color.WHITE);
        jugadorActual.setOpaque(false);
        
    }
    
    public void actualizarCementerio(){
        cementerioNegro.removeAll();
        for(Pieza p : tablero.getPiezasCapturadas("Negro")){
            cementerioNegro.add(piezaACementerio(p));
        }
        
        cementerioBlanco.removeAll();
        for(Pieza p : tablero.getPiezasCapturadas("Blanco")){
            cementerioBlanco.add(piezaACementerio(p));
        }
        
        cementerioNegro.revalidate();
        cementerioNegro.repaint();
        cementerioBlanco.revalidate();
        cementerioBlanco.repaint();
    }
    
    private JLabel piezaACementerio(Pieza p ){
        String rutaImg = "/img/"+p.getTipo()+p.getColor().toLowerCase()+".png";
        final int tamañoCementerio = 100;
        return getScaledLabel(rutaImg, tamañoCementerio, tamañoCementerio);
    }
    
    private JLabel getScaledLabel(String path, int width, int height){
        ImageIcon originalIcon = new ImageIcon(getClass().getResource(path));
        Image originalImage = originalIcon.getImage();
        
        Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(scaledImage));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
    
    private JPanel panelControl() {
        fondoPanel panelControl = new fondoPanel("/img/pergamino.jpeg");
        panelControl.setLayout(new BoxLayout(panelControl, BoxLayout.Y_AXIS));
        
        panelControl.setBorder(BorderFactory.createLineBorder(Color.BLACK,5));
        panelControl.setPreferredSize(new Dimension(300, 0));
        
        JLabel control = new JLabel("CONTROL JUEGO");
        control.setBackground(Color.BLACK);
        control.setFont(new Font("Bodoni Bd BT", Font.BOLD, 30));
        control.setForeground(Color.WHITE);
        control.setOpaque(true);
        control.setBorder(BorderFactory.createLineBorder(Color.WHITE,5));
        control.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelControl.add(control);

        final int tamañoRuleta = 280;

        //panel de la ruleta
        panelRuleta = new JPanel();
        panelRuleta.setLayout(new CardLayout());
        panelRuleta.setOpaque(false);
        panelRuleta.setOpaque(false);
        panelRuleta.setPreferredSize(new Dimension(tamañoRuleta, tamañoRuleta));

        imgRuleta = getScaledLabel("/img/ruleta.png", tamañoRuleta, tamañoRuleta);
        ImageIcon gifIcon = new ImageIcon(getClass().getResource("/img/ruletaGirando.gif"));
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
        btnRuleta.setPreferredSize(new Dimension(250, 50));
        btnRuleta.setFont(new Font("Bodoni Bd BT", Font.BOLD, 20));
        btnRuleta.setForeground(new Color(255, 204, 51));
        btnRuleta.setBackground(Color.BLACK);
        btnRuleta.setBorder(BorderFactory.createLineBorder(new Color(255, 204, 51),5));
        panelControl.add(btnRuleta);
        panelControl.add(Box.createVerticalStrut(60));

        piezaSeleccionada = new JLabel("Pieza a mover: Ninguna", SwingConstants.CENTER);
        piezaSeleccionada.setAlignmentX(Component.CENTER_ALIGNMENT);
        piezaSeleccionada.setForeground(Color.BLACK);
        piezaSeleccionada.setFont(new Font("Bodoni Bd BT", Font.BOLD, 20));
        piezaSeleccionada.setOpaque(false);
        panelControl.add(piezaSeleccionada);
        panelControl.add(Box.createVerticalStrut(40));

        btnRetirar = new JButton("RETIRAR");
        btnRetirar.setPreferredSize(new Dimension(250, 50));
        btnRetirar.setFont(new Font("Bodoni Bd BT", Font.BOLD, 20));
        btnRetirar.setBackground(new Color(153, 0, 0));
        btnRetirar.setForeground(Color.WHITE);
        btnRetirar.setBorder(BorderFactory.createLineBorder(Color.WHITE,5));
        btnRetirar.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelControl.add(btnRetirar);
        panelControl.add(Box.createVerticalStrut(90));

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
            piezaSeleccionada.setText("GIRANDO...");

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

                    piezaSeleccionada.setText("¡MUEVE: " + piezaPermitida.toUpperCase() + "!");
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

    private void actualizarTableroVisual() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                Pieza p = tablero.getPieza(i, j); 
                tableroVisual[i][j].PiezaVisual(p);
                tableroVisual[i][j].desresaltar(); 
            }
        }
    }

    public void manejarClickCasilla(int fila, int columna) {
        Pieza piezaClickeada = tablero.getPieza(fila, columna);

        if (!piezaSelec) {
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

                    if (seleccion == 1) { 
                        this.accionEspecial = "conjurarZombie";
                        origenFila = fila;
                        origenColumna = columna;
                        piezaSelec = true;
                        tableroVisual[fila][columna].resaltar(Color.ORANGE);
                        JOptionPane.showMessageDialog(this, "Selecciona una casilla vacia para conjurar el Zombie");
                        return;
                    }
                }

                origenFila = fila;
                origenColumna = columna;
                piezaSelec = true;
                tableroVisual[fila][columna].resaltar(Color.GREEN);
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona una pieza valida de tu color y seleccionada por la ruleta (" + piezaPermitida + ")", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {

            //destinos o ataques
            tableroVisual[origenFila][origenColumna].desresaltar();

            Pieza atacante = tablero.getPieza(origenFila, origenColumna);
            Pieza defensor = tablero.getPieza(fila, columna);
            boolean exito = false;
            String mensaje = null;

            if (this.accionEspecial != null && this.accionEspecial.equals("conjurarZombie")) {
                //verifica que este vacio y sea adyacente
                if (defensor == null && tablero.calcularDistancia(origenFila, origenColumna, fila, columna) == 1) {
                    tablero.ConjurarZombie(origenFila, origenColumna, fila, columna);
                    mensaje = "¡Zombie conjurado!";
                    exito = true;
                } else {
                    mensaje = "Solo puedes conjurar un Zombie en una casilla vacia adyacente";
                }
                this.accionEspecial = null; 
            } else if (defensor == null) {
                exito = tablero.ejecutarMovimiento(origenFila, origenColumna, fila, columna);
                if (exito) {
                } else {
                    mensaje = "Movimiento invalido, distancia o direccion incorrecta";
                }
            } else if (defensor.getColor().equals(atacante.getColor())) {
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
                    String ataqueTipo = "Normal";
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

            if (mensaje != null) {
                if (mensaje.startsWith("ERROR")) {
                    JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, mensaje, "Resultado", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            if (exito) {
                actualizarTableroVisual();
                actualizarCementerio();
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
        turnoActual = turnoActual.equals("Blanco") ? "Negro" : "Blanco";
        btnRuleta.setEnabled(true);
        
        final int tamañoRuleta = 280;
        imgRuleta.setIcon(getScaledLabel("/img/ruleta.png", tamañoRuleta, tamañoRuleta).getIcon());
        ((CardLayout) panelRuleta.getLayout()).show(panelRuleta, "ESTATICA");
        
        actualizarInfoJugador();
        
        piezaPermitida = null;
        piezaSeleccionada.setText("Pieza a mover: Ninguna");
    }

    private String getOponenteColor() {
        return turnoActual.equals("Blanco") ? "Negro" : "Blanco";
    }

    private void manejarRetirarse() {
        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas retirarse? El oponente ganara la partida.",
                "Confirmar Retiro", JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            //el jugador actual se retira y es le perdedor
            Player perdedor = turnoActual.equals("Blanco") ? jugador1 : jugador2;
            Player ganador = turnoActual.equals("Blanco") ? jugador2 : jugador1;
            
            //dar los puntos y guardar el log
            ganador.agregarPuntos(Tablero.puntosVictoria);
            GameLog log = new GameLog(ganador.getUsername(), perdedor.getUsername(), "VICTORIA POR RETIRO", Tablero.puntosVictoria);
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
        ganador.agregarPuntos(Tablero.puntosVictoria);

        //guardar log
        GameLog log = new GameLog(ganador.getUsername(), perdedor.getUsername(), "VICTORIA POR DESTRUCCION TOTAL DE PIEZAS", Tablero.puntosVictoria);
        sistema.guardarLog(log);

        JOptionPane.showMessageDialog(this, "¡Juego Terminado! El jugador " + ganador.getUsername() + " vencio al jugador " + perdedor.getUsername() + ". Has ganado 3 puntos", "VICTORIA", JOptionPane.INFORMATION_MESSAGE);
        System.out.println("FIN JUEGO: jugador " + ganador.getUsername() + " has ganado la partida. Perdedor: " + perdedor.getUsername());
        new MenuPrincipal(sistema, ganador).setVisible(true);
        this.dispose();
    }
}
