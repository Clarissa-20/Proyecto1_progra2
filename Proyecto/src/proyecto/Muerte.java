/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import javax.swing.JOptionPane;

public class Muerte extends PiezaPrincipal{
    private static final int ATAQUE_INICIAL = 4;
    private static final int VIDAS_INICIALES = 3;
    private static final int ESCUDO_INICIAL = 1;
    
    public Muerte(String color){
        super(ATAQUE_INICIAL, VIDAS_INICIALES, ESCUDO_INICIAL, color, "Muerte");
    }
    
    @Override
    public boolean esMovimientoValido(int destFila, int destColunma){
        //la muerte se mueve como una pieza principal (adyacente)
        return esMovimientoAdyacente(destFila, destColunma);
    }
    
    //metodo especial: conjurar un zombie
    public Zombie conjurarZombie(){
        JOptionPane.showMessageDialog(null, "Muerte conjura un Zombie");
        return new Zombie(this.getColor());
    }
    
    //metodo especila: ataque de lanza a 2 casilla
    //usar una funcion recursiva para revisar obstrucciones
    public int ataqueLanza(int destFila, int destColumna){
        return this.ataque*2; //el ataque de lanza es doble
    }
}
