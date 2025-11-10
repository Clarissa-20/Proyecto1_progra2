/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class CasillaPanel extends JPanel{
    private int fila;
    private int columna; 
    private JLabel imgPieza;
    private vtnJuego vtnJuego; 

    public CasillaPanel(int fila, int columna, vtnJuego vtnJuego) {
        this.fila = fila;
        this.columna = columna;;
        this.vtnJuego = vtnJuego;
        
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        Color colorCasilla = (fila+columna) % 2 == 0 ? new Color(230, 230, 230) : new Color(35, 35, 35);
        setBackground(colorCasilla);
        
        imgPieza = new JLabel("", SwingConstants.CENTER);
        add(imgPieza, BorderLayout.CENTER);
        
        //manejo del evento de click
        this.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                vtnJuego.manejarClickCasilla(fila, columna);
            }
        });
    }
    
    public void PiezaVisual(Pieza pieza){
        if(pieza == null){
            imgPieza.setIcon(null);
        } else{
            try{
                String tipo = pieza.getTipo();
                String color = pieza.getColor();
                
                String rutaImg = "/img/"+tipo+color+".png";
                ImageIcon icon = new ImageIcon(getClass().getResource(rutaImg));
                
                Image imgOriginal = icon.getImage();
                int ancho = 120;
                int alto = 120;
                
                BufferedImage scaledImage = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
                
                Graphics2D g2d = scaledImage.createGraphics();
                
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.drawImage(imgOriginal, 0, 0, ancho, alto, null);
                g2d.dispose();
                
                imgPieza.setIcon(new ImageIcon(scaledImage));
                
                //redimensionar si es necesario
                Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                imgPieza.setIcon(new ImageIcon(img));
            } catch(Exception e){
                System.err.println("Error cargando la imagen para "+pieza.getTipo()+": "+e.getMessage());
                imgPieza.setText(pieza.getTipo().substring(0, 1));
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
