/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import javax.swing.JOptionPane;

public abstract class PiezaPrincipal extends Pieza {
    
    public PiezaPrincipal(int ataque, int vidas, int escudo, String color, String tipo){
        super(ataque, vidas, escudo, color, tipo);
    }
    
    //metodo que implementa la logica de ataque normal adyacente que comparten
    public int ataqueNormal(){
        JOptionPane.showMessageDialog(null, this.tipo+" ataca con su ataque base de "+this.ataque);
        return this.ataque;
    }
    
    //logica para el movimiento basico adyacente , compartido por las 3 piezas
    //esto se usa dentro del metodo abstracto esMovimientoValido de cadasubclase
    protected boolean esMovimientoAdyacente(int destFila, int destColumna){
        int difFila = Math.abs(destFila - this.fila);
        int difColumna = Math.abs(destColumna - this.columna);
        
        //verficar si el movimiento es una casilla vecina (horizontal, vertical o diagonal)
        return (difFila <= 1 && difColumna <= 1) && (difFila != 0 || difColumna != 0);
    }
}
