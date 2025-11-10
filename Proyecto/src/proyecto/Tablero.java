/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;

public class Tablero {

    private static final int tamaño = 6;
    private Pieza[][] matriz;
    private Random random;

    public static final int puntosVictoria = 3;

    private List<Pieza> capturadasNegro;
    private List<Pieza> capturadasBlanco;

    public Tablero() {
        this.matriz = new Pieza[tamaño][tamaño];
        this.random = new Random();
        this.capturadasNegro = new ArrayList<>();
        this.capturadasBlanco = new ArrayList<>();
        inicializarTablero();
    }

    public List<Pieza> getPiezasCapturadas(String colorJugador) {
        if (colorJugador.equals("Negro")) {
            return capturadasNegro;
        } else {
            return capturadasBlanco;
        }
    }

    public Pieza getPieza(int fila, int columna) {
        if (fila >= 0 && fila < tamaño && columna >= 0 && columna < tamaño) {
            return matriz[fila][columna];
        }
        return null; 
    }

    private void inicializarTablero() {
        //jugador conpiezas negras
        matriz[0][0] = new HombreLobo("Negro");
        matriz[0][1] = new Vampiro("Negro");
        matriz[0][2] = new Muerte("Negro");
        matriz[0][3] = new Muerte("Negro");
        matriz[0][4] = new Vampiro("Negro");
        matriz[0][5] = new HombreLobo("Negro");

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
    public String seleccionarPiezaAlAzar(String colorTurno) {
        ArrayList<String> piezasDisponibles = new ArrayList<>();

        for (int i = 0; i < tamaño; i++) {
            for (int j = 0; j < tamaño; j++) {
                Pieza p = matriz[i][j];
                if (p != null && p.getColor().equals(colorTurno) && p.getVidas() > 0) {
                    String tipo = p.getTipo();
                    if ((tipo.equals("Vampiro") || tipo.equals("HombreLobo") || tipo.equals("Muerte")) && !piezasDisponibles.contains(tipo)) {
                        piezasDisponibles.add(tipo);
                    }
                }
            }
        }
        if (piezasDisponibles.isEmpty()) {
            return null;
        }

        int indiceAleatorio = random.nextInt(piezasDisponibles.size());
        String piezaSeleccionada = piezasDisponibles.get(indiceAleatorio);
        return piezaSeleccionada;
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
    }

    //contar el numero de piezas vicas que le quedan al jugador
    public int contarPiezasVivas(String color) {
        int contador = 0;
        for (int i = 0; i < tamaño; i++) {
            for (int j = 0; j < tamaño; j++) {
                Pieza p = matriz[i][j];
                if (p != null && p.getColor().equals(color) && p.getVidas() > 0) {
                    contador++;
                }
            }
        }
        return contador;
    }

    public boolean esCaminoDespejado(int f1, int c1, int f2, int c2) {
        if (Math.max(Math.abs(f2 - f1), Math.abs(c2 - c1)) == 1) {
            return true;
        }

        if (f1 < 0 || f1 >= tamaño || c1 < 0 || c1 >= tamaño) {
            return true;
        }
        int fIntermedia = f1;
        int cIntermedia = c1;

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

        if (matriz[fIntermedia][cIntermedia] != null) {
            return false; 
        }
        return esCaminoDespejado(fIntermedia, cIntermedia, f2, c2);
    }

    public int contarPiezasPorTipo(String color, String tipo, int fila, int columna, int contador) {
        if (fila >= tamaño) {
            return contador;
        }

        int nuevaFila = fila;
        int nuevaColumna = columna + 1;

        if (nuevaColumna >= tamaño) {
            nuevaColumna = 0;
            nuevaFila++;
        }

        Pieza p = matriz[fila][columna];

        if (p != null && p.getColor().equals(color) && p.getTipo().equals(tipo)) {
            contador++;
        }
        return contarPiezasPorTipo(color, tipo, nuevaFila, nuevaColumna, contador);
    }

    //revisar si la casilla de destino esta vacia para conjurar el zombie
    public boolean ConjurarZombie(int fMuerte, int cMuerte, int destFila, int destColumna) {
        if (matriz[destFila][destColumna] == null) {
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
            case "Normal":
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

        defensor.recibirDanio(danioCausado, ignorarEscudo);
        String mensaje;

        if (defensor.getVidas() <= 0) {
            if (defensor.getColor().equals("Blanco")) {
                capturadasNegro.add(defensor);
            } else {
                capturadasBlanco.add(defensor);
            }
            mensaje = "¡SE DESTRUYO LA PIEZA " + defensor.getTipo().toUpperCase() + " DEL JUGADOR " + defensor.getColor().toUpperCase() + "!";
            matriz[destFila][destColumna] = null;
        } else {
            mensaje = String.format("SE ATACO PIEZA %s DEL JUGADOR "+defensor.getColor().toUpperCase()+" Y SE HA QUITADO %d PUNTOS. LE QUEDAN %d PUNTOS DE ESCUDO Y %d VIDAS",
                    defensor.getTipo().toUpperCase(), danioCausado, defensor.getEscudo(), defensor.getVidas());
        }
        return mensaje;
    }

    public double calcularDistancia(int r1, int c1, int r2, int c2) {
        int dr = Math.abs(r1 - r2);
        int dc = Math.abs(c1 - c2);
        return Math.max(dr, dc);
    }

    private boolean movimientoValido(int r1, int c1, int r2, int c2) {
        int dr = Math.abs(r1 - r2);
        int dc = Math.abs(c1 - c2);

        return (dr == 0 || dc == 0 || dr == dc);
    }

    public boolean zombieAdyacente(int r, int c, String color) {
        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int rAdy = r + dr[i];
            int cAdy = c + dc[i];

            if (rAdy >= 0 && rAdy < 6 && cAdy >= 0 && cAdy < 6) {
                Pieza p = matriz[rAdy][cAdy];

                if (p != null && p.getTipo().equals("Zombie") && p.getColor().equals(color)) {
                    return true;
                }
            }
        }
        return false;
    }
}
