/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proyecto;

/**
 *
 * @author HP
 */
public class Proyecto {
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SistemaJuego sistema = new SistemaJuego();
                MenuInicio menuInicio = new MenuInicio(sistema);
                menuInicio.setVisible(true);
            }
        });
    }
    
}
