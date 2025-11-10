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
    
    public int ataqueNormal(){
        JOptionPane.showMessageDialog(null, this.tipo+" ataca con su ataque base de "+this.ataque);
        return this.ataque;
    }
    
    protected boolean esMovimientoAdyacente(int destFila, int destColumna){
        int difFila = Math.abs(destFila - this.fila);
        int difColumna = Math.abs(destColumna - this.columna);
        
        return (difFila <= 1 && difColumna <= 1) && (difFila != 0 || difColumna != 0);
    }
}
