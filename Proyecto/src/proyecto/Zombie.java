/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

public final class Zombie extends Pieza {
    private static final int ATAQUE_INICIAL = 1;
    private static final int VIDAS_INICIALES = 1;
    private static final int ESCUDO_INICIAL = 0;
    
    public Zombie(String color){
        super(ATAQUE_INICIAL, VIDAS_INICIALES, ESCUDO_INICIAL, color, "Zombie");
    }

    @Override
    public boolean esMovimientoValido(int destFila, int destColumna) {
        return false;
    }
    
    //el zombie ataca por orden de la muerte
    public int ataquePorOrden(Pieza rival){
        return this.ataque;
    }
}
