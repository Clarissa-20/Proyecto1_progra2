/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import javax.swing.JOptionPane;

public class Vampiro extends PiezaPrincipal {
     private static final int ATAQUE_INICIAL = 3;
    private static final int VIDAS_INICIALES = 4;
    private static final int ESCUDO_INICIAL = 5;
    
    public Vampiro(String color){
        super(ATAQUE_INICIAL, VIDAS_INICIALES, ESCUDO_INICIAL, color, "Vampiro");
    }

    @Override
    public boolean esMovimientoValido(int destFila, int destColumna) {
        return esMovimientoAdyacente(destFila, destColumna);
    }
    
    //metodo especial del vampiro : chupar sangre
    public void ataqueEspecial(Pieza piezaRival){
        JOptionPane.showMessageDialog(null, "Vampiro usa Chupar Sangre");
    }
}
