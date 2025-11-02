/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import javax.swing.JOptionPane;

public abstract class Pieza {

    protected int ataque;
    protected int vidas;
    protected int escudo;
    protected String color; //blanco o negro

    //posicion actual en el tablero
    protected int fila;
    protected int columna;

    //identificador para el tipo de pieza
    protected String tipo;

    public Pieza(int ataque, int vidas, int escudo, String color, String tipo) {
        this.ataque = ataque;
        this.vidas = vidas;
        this.escudo = escudo;
        this.color = color;
        this.tipo = tipo;
    }

    /*metodos abstractos - polimorfismo
    por lo menos una funcion abstracta
    define el comportamiento de movimiento unico de cada pieza
    sera implementado por Vampiro, HombreLobo y Muerte*/
    public abstract boolean esMovimientoValido(int destFila, int destColumna);

    /*metodo final
    logica inmutable de como la pieza recibe daño
    primero se absorbe el daño con el escudo luego afecta las vidas*/
    public final void recibirDanio(int danio, boolean ignorarEscudo) {
        if (ignorarEscudo) {
            this.vidas -= danio;
        } else {
            //logica de absorcion
            int danioResidual = danio - this.escudo;
            if (danioResidual > 0) {
                //si el daño supera el escudo, queda en 0 y pasa a las vidas
                this.escudo = 0;
                this.vidas -= danioResidual;
            } else {
                //si el daño es menor o igual al escudo, solo le reduce el escudo
                this.escudo -= danio;
            }
        }
        if (this.vidas < 0) {
            this.vidas = 0; //evitar vidas negativas
        }
        JOptionPane.showMessageDialog(null, this.tipo + " ha recibido daño.Vidas restantes: " + this.vidas);
    }

    public void resturarVida(int cantidad) {
        this.vidas += cantidad;

        /*if(this.vidas > this.vidasMax){
            this.vidas = this.vidasMax
;        }*/
    }

    public void setPosicion(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public int getVidas() {
        return vidas;
    }

    public String getColor() {
        return color;
    }

    public String getTipo() {
        return tipo;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getEscudo() {
        return escudo;
    }
}
