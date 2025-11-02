/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import java.awt.*;
import java.net.URL;
import javax.swing.*;

public class fondoPanel extends JPanel{
    private Image imgFondo;
    
    public fondoPanel(String img){
        URL imgRuta = getClass().getResource(img);
        if(imgRuta != null){
            this.imgFondo = new ImageIcon(imgRuta).getImage();
        } else{
            System.err.println("Error al cargar la img: "+img);
            setBackground(Color.DARK_GRAY);
        }
        setLayout(new BorderLayout());
        setOpaque(false);
    }
    
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if(imgFondo != null){
            g.drawImage(imgFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
