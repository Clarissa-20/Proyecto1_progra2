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
        ArrayList<Pieza> piezasDisponibles = new ArrayList<>();

        //recorrer el tablero para encontrar todas las piezas del colorTurno
        for (int i = 0; i < tamaño; i++) {
            for (int j = 0; j < tamaño; j++) {
                Pieza p = matriz[i][j];
                if (p != null && p.getColor().equals(colorTurno) && p.getVidas() > 0) {
                    if (p.getTipo().equals("Vampiro") || p.getTipo().equals("HombreLobo") || p.getTipo().equals("Muerte")) {
                        piezasDisponibles.add(p);
                    }
                }
            }
        }
        if (piezasDisponibles.isEmpty()) {
            return null; //el jugador no tiene piezas
        }

        //seleccionar una pieza al azar, simulacion de la ruleta
        int indiceAleatorio = random.nextInt(piezasDisponibles.size());
        Pieza piezaSeleccionada = piezasDisponibles.get(indiceAleatorio);
        JOptionPane.showMessageDialog(null, "La ruleta selecciono: " + piezaSeleccionada.getTipo() + " de " + colorTurno);
        return piezaSeleccionada.getTipo();
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

        if (piezaOrigen == null) {
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
            /*aqui se asume que es ataque normal, el especial debe ser manejada antes 
             de llamar a este metodo, poner un boton*/

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
        return false;
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

    public static void main(String[] args) {
        Tablero t = new Tablero();
    }
}
