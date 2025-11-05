/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;

public class Tablero {

    /*el tablero contiene objetos
    una casilla vacia se representa como null*/
    private static final int tamaño = 6;
    private Pieza[][] matriz;
    private Random random;

    //constante para los puntos de victoria
    public static final int puntosVictoria = 3;

    public Tablero() {
        this.matriz = new Pieza[tamaño][tamaño];
        this.random = new Random();
        inicializarTablero();
    }

    public Pieza getPieza(int fila, int columna) {
        if (fila >= 0 && fila < tamaño && columna >= 0 && columna < tamaño) {
            return matriz[fila][columna];
        }
        return null; //returno null si esta fuera de limites
    }

    //colocar las piezas en sus posociones iniciales
    private void inicializarTablero() {
        //jugador conpiezas negras
        matriz[0][0] = new HombreLobo("Negro");
        matriz[0][1] = new Vampiro("Negro");
        matriz[0][2] = new Muerte("Negro");
        matriz[0][3] = new Muerte("Negro");
        matriz[0][4] = new Vampiro("Negro");
        matriz[0][5] = new HombreLobo("Negro");

        //se establece la posicion para cada jugador
        for (int c = 0; c < tamaño; c++) {
            if (matriz[0][c] != null) {
                matriz[0][c].setPosicion(0, c);
            }
        }

        //jugador conpiezas blancas
        matriz[5][0] = new HombreLobo("Blanco");
        matriz[5][1] = new Vampiro("Blanco");
        matriz[5][2] = new Muerte("Blanco");
        matriz[5][3] = new Muerte("Blanco");
        matriz[5][4] = new Vampiro("Blanco");
        matriz[5][5] = new HombreLobo("Blanco");

        for (int c = 0; c < tamaño; c++) {
            if (matriz[5][c] != null) {
                matriz[5][c].setPosicion(5, c);
            }
        }
    }

    //logica del turno
    //Pieza - String
    public String seleccionarPiezaAlAzar(String colorTurno) {
        //ArrayList<Pieza> piezasDisponibles = new ArrayList<>();
        ArrayList<String> piezasDisponibles = new ArrayList<>();

        //recorrer el tablero para encontrar todas las piezas del colorTurno
        for (int i = 0; i < tamaño; i++) {
            for (int j = 0; j < tamaño; j++) {
                Pieza p = matriz[i][j];
                if (p != null && p.getColor().equals(colorTurno) && p.getVidas() > 0) {
                    String tipo = p.getTipo(); //agg
                    if ((tipo.equals("Vampiro") || tipo.equals("HombreLobo") || tipo.equals("Muerte")) && !piezasDisponibles.contains(tipo)) {
                        piezasDisponibles.add(tipo);
                    }
                }
            }
        }
        if (piezasDisponibles.isEmpty()) {
            return null; //el jugador no tiene piezas
        }

        //seleccionar una pieza al azar, simulacion de la ruleta
        int indiceAleatorio = random.nextInt(piezasDisponibles.size());
        String piezaSeleccionada = piezasDisponibles.get(indiceAleatorio);
        //Pieza piezaSeleccionada = piezasDisponibles.get(indiceAleatorio);
        //JOptionPane.showMessageDialog(null, "Pieza seleccionada: " + piezaSeleccionada.getTipo() + " de " + colorTurno);
        return piezaSeleccionada; //.getTipo();
    }

    public boolean tienePiezasDeTipo(String color, String tipo) {
        for (int i = 0; i < tamaño; i++) {
            for (int j = 0; j < tamaño; j++) {
                Pieza p = matriz[i][j];
                if (p != null && p.getColor().equals(color) && p.getTipo().equals(tipo)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean ejecutarMovimiento(int origenFila, int origenColumna, int destFila, int destColumna) {
        Pieza piezaOrigen = matriz[origenFila][origenColumna];
        Pieza piezaDestino = matriz[destFila][destColumna];

        if (piezaOrigen == null || (origenFila == destFila && origenColumna == destColumna)) {
            return false;
        }

        if (piezaDestino != null) {
            return false;
        }

        //logica de distancia incluyendo al HL
        double distancia = calcularDistancia(origenFila, origenColumna, destFila, destColumna);
        int maxDistancia = 1;

        //HL puede moverse hasta 2 casillas vacias
        if (piezaOrigen.getTipo().equals("HombreLobo")) {
            maxDistancia = 2;
        }

        if (distancia > maxDistancia) {
            return false;
        }

        //validar la direccion, todos se mueven horizontal, vertical o diagonal
        if (!movimientoValido(origenFila, origenColumna, destFila, destColumna)) {
            return false;
        }

        //maver la pieza a la casilla vacia
        matriz[destFila][destColumna] = piezaOrigen;
        matriz[origenFila][origenColumna] = null;
        piezaOrigen.setPosicion(destFila, destColumna);
        return true;

        /*if (piezaOrigen == null) {
            JOptionPane.showMessageDialog(null, "ERROR: No hay pieza en la casilla de origen");
            return false;
        }

        //validar si el movimiento es valido segun las reglas de la pieza
        if (!piezaOrigen.esMovimientoValido(destFila, destColumna)) {
            JOptionPane.showMessageDialog(null, "ERROR: Movimiento invalido para " + piezaOrigen.getTipo());
            return false;
        }

        //casilla de destino vacia (mivimiento)
        if (piezaDestino == null) {
            //mover la pieza
            matriz[destFila][destColumna] = piezaOrigen;
            matriz[origenFila][origenColumna] = null;
            piezaOrigen.setPosicion(destFila, destColumna);
            JOptionPane.showMessageDialog(null, "Movimiento exitoso a (" + destFila + ", " + destColumna + ")");
            return true;
        }

        //casilla de destino ocupada por pieza propia
        if (piezaDestino.getColor().equals(piezaOrigen.getColor())) {
            JOptionPane.showMessageDialog(null, "ERROR: Casilla de destino ocupada por pieza propia");
            return false;
        }

        //casilla de destino ocipada por pieza enemiga(ataque normal)
        if (!piezaDestino.getColor().equals(piezaOrigen.getColor())) {
            //aqui se asume que es ataque normal, el especial debe ser manejada antes de llamar a este metodo, poner un boton

            int danio = piezaOrigen.getAtaque(); //ataque base de la pieza
            piezaDestino.recibirDanio(danio);
            JOptionPane.showMessageDialog(null, piezaOrigen.getTipo() + " ataca a " + piezaDestino.getTipo() + ". Daño: " + danio);

            //si la oieza enemiga muere
            if (piezaDestino.getVidas() <= 0) {
                JOptionPane.showMessageDialog(null, piezaDestino.getTipo() + " ha sido DESTRUIDA.");
                matriz[destFila][destColumna] = piezaOrigen; //la pieza atacante ocupa la posicion
                matriz[origenFila][origenColumna] = null; //la casilla origen queda vacia
                piezaOrigen.setPosicion(destFila, destColumna);
            }
            return true; //si la pieza rival no muere, lapieza origen regresa a su casilla
        }
        return false;*/
    }

    //contar el numero de piezas vicas que le quedan al jugador
    public int contarPiezasVivas(String color) {
        int contador = 0;
        for (int i = 0; i < tamaño; i++) {
            for (int j = 0; j < tamaño; j++) {
                Pieza p = matriz[i][j];
                //solo cuenta si existe, tiene el color correcto y tiene vidas
                if (p != null && p.getColor().equals(color) && p.getVidas() > 0) {
                    contador++;
                }
            }
        }
        return contador;
    }

    /*recursividad 1: verificar si el camino entre dos puntos esta despejado,
    solo para una casilla intermedia
    funcion clave para el ataque de lanza de la muerte*/
    public boolean esCaminoDespejado(int f1, int c1, int f2, int c2) {
        //caso base 1: si el destino es el siguiente paso(movimiento normal)
        if (Math.max(Math.abs(f2 - f1), Math.abs(c2 - c1)) == 1) {
            return true;
        }

        //caso base 2: fuera de los limites del tablero(no deberia pasar si se llama correctamente)
        if (f1 < 0 || f1 >= tamaño || c1 < 0 || c1 >= tamaño) {
            return true;
        }

        //calcular el punto intermedio(solo funciona para lineas rectas y distancias de 2)
        int fIntermedia = f1;
        int cIntermedia = c1;

        //determinar la direccions del movimiento(horizontal, vertica o diagonal)
        if (f2 > f1) {
            fIntermedia++;
        }
        if (f2 < f1) {
            fIntermedia--;
        }

        if (c2 > c1) {
            cIntermedia++;
        }
        if (c2 < c1) {
            cIntermedia--;
        }

        //verificar si la casilla intermedia esta ocupada
        if (matriz[fIntermedia][cIntermedia] != null) {
            return false; //case base 3: hay obstaculo
        }

        /*paso recursivo: si la casilla intermedia esta vacia, llama a la funciona para
        revisar si el camino desde el punto intermedio hasta el destino esta libre
        esto permite generalizar la funcin para distancias mayores si fuera necesario*/
        return esCaminoDespejado(fIntermedia, cIntermedia, f2, c2);
    }

    //recursividad 2: cuenta cuantas piezas de un tipo espefico tiene un jugador. ej zombie
    public int contarPiezasPorTipo(String color, String tipo, int fila, int columna, int contador) {
        //caso base 1: se ha recorrido la ultima fila, la busqueda termina
        if (fila >= tamaño) {
            return contador;
        }

        int nuevaFila = fila;
        int nuevaColumna = columna + 1;

        //si la columna excede le limite, pasaos a la siguiente fila
        if (nuevaColumna >= tamaño) {
            nuevaColumna = 0;
            nuevaFila++;
        }

        Pieza p = matriz[fila][columna];

        //logica del paso recursivo
        if (p != null && p.getColor().equals(color) && p.getTipo().equals(tipo)) {
            contador++; //se encontro la pieza, incrementa el  contador
        }
        //llamada recursiva al siguiente elemeno de la matriz
        return contarPiezasPorTipo(color, tipo, nuevaFila, nuevaColumna, contador);
    }

    //revisar si la casilla de destino esta vacia para conjurar el zombie
    public boolean ConjurarZombie(int fMuerte, int cMuerte, int destFila, int destColumna) {
        if (matriz[destFila][destColumna] == null) {
            //se asume que se sabe el color que cojura, el colo de la muerte
            String colorZombie = matriz[fMuerte][cMuerte].getColor();
            Zombie nuevoZombie = new Zombie(colorZombie);
            nuevoZombie.setPosicion(destFila, destColumna);
            matriz[destFila][destColumna] = nuevoZombie;
            return true;
        }
        return false;
    }

    //manejar los ataque especiales
    public String ejecutarAtaque(int origenFila, int origenColumna, int destFila, int destColumna, String tipoAtaque) {
        Pieza atacante = matriz[origenFila][origenColumna];
        Pieza defensor = matriz[destFila][destColumna];

        if (defensor == null || atacante.getColor().equals(defensor.getColor())) {
            return "Objetivo de ataque invalido";
        }

        int danioCausado = 0;
        boolean ignorarEscudo = false;

        //logica para el daño por cada tipo
        switch (tipoAtaque) {
            case "normal":
                danioCausado = atacante.getAtaque();
                ignorarEscudo = false;
                if (calcularDistancia(origenFila, origenColumna, destFila, destColumna) > 1) {
                    return "Ataque invalido, solo se puede atacar piezas adyacentes";
                }
                break;
            case "chuparSangre":
                //le quita 1 punti al enemigo y restaura 1 al vampiro
                danioCausado = 1;
                atacante.resturarVida(1);
                //validar si la pieza es adyacente
                if (calcularDistancia(origenFila, origenColumna, destFila, destColumna) > 1) {
                    return "Chupar Sangre solo es posible contra piezas adyacentes";
                }
                break;
            case "Lanza":
                //ataca a 2 casilla de distancia, ingora escudo y quita la mitad del poder - 2pts
                danioCausado = atacante.getAtaque() / 2;
                ignorarEscudo = true;
                if (calcularDistancia(origenFila, origenColumna, destFila, destColumna) != 2) {
                    return "Ataque de lanza solo es posible a 2 casillas de distacia";
                }
                break;
            case "ataqueZombie":
                //ataque de 1 pt si el rivsl esta adyacente a un zombie aliado
                if (!zombieAdyacente(destFila, destColumna, atacante.getColor())) {
                    return "El ataque Zombie solo se puedes realizar si tienes un zombie aliado adyacente a la pieza enemiga";
                }
                danioCausado = 1;
                ignorarEscudo = false;
                break;
            default:
                return "Tipo de ataque invalido";
        }

        //aplicar el daño y generar el resultado
        defensor.recibirDanio(danioCausado, ignorarEscudo);
        String mensaje;

        //verificar despues de los ataques si la pieza fue destruida
        if (defensor.getVidas() <= 0) {
            mensaje = "¡SE DESTRUYO LA PIEZA " + defensor.getTipo().toUpperCase() + " DEL JUGADOR " + defensor.getColor().toUpperCase() + "!";
            matriz[destFila][destColumna] = null;
        } else {
            mensaje = String.format("SE ATACO PIEZA %s Y SE HA QUITADO %d PUNTOS. LE QUEDAN %d PUNTOS DE ESCUDO Y %d VIDAS",
                    defensor.getTipo().toUpperCase(), danioCausado, defensor.getEscudo(), defensor.getVidas());
        }
        return mensaje;
    }

    //calcular la distancia en casillas, maximo de la direfencia entre filas o columnas
    public double calcularDistancia(int r1, int c1, int r2, int c2) {
        int dr = Math.abs(r1 - r2);
        int dc = Math.abs(c1 - c2);
        //para movimientos rectos y diagonales, la distancia es el maximo de las diferencias
        return Math.max(dr, dc);
    }

    //verificar si un movimiento es valido, recto o diagonal
    private boolean movimientoValido(int r1, int c1, int r2, int c2) {
        int dr = Math.abs(r1 - r2);
        int dc = Math.abs(c1 - c2);
        /*valido si el horizontal - dr = 0
        vertical - dc = 0 , o diagonal dr=dc*/
        return (dr == 0 || dc == 0 || dr == dc);
    }

    //verificar si un zombie del color especificado esta adyacente a la casilla
    public boolean zombieAdyacente(int r, int c, String color) {
        //coordenadas para revisar las 8 dirreciones adyacentes
        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int rAdy = r + dr[i];
            int cAdy = c + dc[i];

            //verificar limites del tablero
            if (rAdy >= 0 && rAdy < 6 && cAdy >= 0 && cAdy < 6) {
                Pieza p = matriz[rAdy][cAdy];

                //verificar si es un zombie y si es del color aliado
                if (p != null && p.getTipo().equals("Zombie") && p.getColor().equals(color)) {
                    return true;
                }
            }
        }
        return false;
    }
}
