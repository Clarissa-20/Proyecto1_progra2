/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import javax.swing.JOptionPane;

public class HombreLobo extends PiezaPrincipal {
    private static final int ATAQUE_INICIAL = 5;
    private static final int VIDAS_INICIALES = 5;
    private static final int ESCUDO_INICIAL = 2;
    private boolean movimientoDobleActivo = false;
    
    public HombreLobo(String color){
        super(ATAQUE_INICIAL, VIDAS_INICIALES, ESCUDO_INICIAL, color, "HombreLobo");
    }

    @Override
    public boolean esMovimientoValido(int destFila, int destColumna) {
        int difFila = Math.abs(destFila - this.fila);
        int difColumna = Math.abs(destColumna - this.columna);
        
        if(movimientoDobleActivo){
            return (difFila <=2 && difColumna <= 2) && (difFila != 0 || difColumna != 0);
        } else{
            return esMovimientoAdyacente(destFila, destColumna);
        }
    }
    
    //metodo especial: que se puede mover hasta d2 casillas
    public void alternarMovimientoDble(){
        this.movimientoDobleActivo = !this.movimientoDobleActivo;
        JOptionPane.showMessageDialog(null, "Movimiento doble de Hombre Lobo: "+(movimientoDobleActivo ? "ACTIVO" : "INACTIVO"));
    }
}
