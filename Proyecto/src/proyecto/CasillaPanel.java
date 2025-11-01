/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CasillaPanel extends JPanel{
    private int fila;
    private int columna; 
    private JLabel imgPieza;
    private vtnJuego vtnJuego; //referenca a la vtn principal para manejar el click vtnPadre

    public CasillaPanel(int fila, int columna, vtnJuego vtnJuego) {
        //initComponents();
        this.fila = fila;
        this.columna = columna;;
        this.vtnJuego = vtnJuego;
        
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        //asignar el color de fondo para simular el tablero
        Color colorCasilla = (fila+columna) % 2 == 0 ? new Color(240, 217, 181) : new Color(181, 136, 99);
        setBackground(colorCasilla);
        
        imgPieza = new JLabel("", SwingConstants.CENTER);
        add(imgPieza, BorderLayout.CENTER);
        
        //manejo del evento de click
        this.addMouseListener(new MouseAdapter(){
            public void mouseCliked(MouseEvent e){
                //notifica a la vtnJuego las oordenas de la casilla clickeada
                vtnJuego.manejarClickCasilla(fila, columna);
            }
        });
    }
    
    //metodo para la asignacion de la img basada en la pieza - PiezaVisual
    public void PiezaVisual(Pieza pieza){
        if(pieza == null){
            imgPieza.setIcon(null); //casilla vacia
        } else{
            try{
                String tipo = pieza.getTipo();
                String color = pieza.getColor();
                
                String rutaImg = "/img/"+tipo+color+".png";
                
                ImageIcon icon = new ImageIcon(getClass().getResource(rutaImg));
                
                //redimensionar si es necesario
                Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                imgPieza.setIcon(new ImageIcon(img));
            } catch(Exception e){
                System.err.println("Error cargando la imagen para "+pieza.getTipo()+": "+e.getMessage());
                imgPieza.setText(pieza.getTipo().substring(0, 1)); //muestra la inicial si la img falla
            }
        }
    }
    
    public void resaltar(Color color){
        setBorder(BorderFactory.createLineBorder(color, 3));
    }
    
    public void desresaltar(){
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
    
}
