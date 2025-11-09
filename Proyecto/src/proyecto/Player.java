/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import java.time.LocalDate;

public class Player {
    private String username;
    private String password;
    private int puntos;
    private LocalDate fechaIngreso;
    private boolean activo;
    //private String color;
    
    public Player(String username, String password){
        this.username = username;
        this.password = password;
        this.puntos = 0;
        this.fechaIngreso = LocalDate.now();
        this.activo = true;
        //this.color = color;
    }
    
    public String getUsername(){
        return username;
    }
    
    public String getPassword(){
        return password;
    }
    
    public void setPassword(String password){
        this.password = password;
    }
    
    public int getPuntos(){
        return puntos;
    }
    
    /*public String getColor(){
        return color;
    }*/
    
    public void agregarPuntos(int puntosGanados){
        this.puntos += puntosGanados;
    }
    
    public LocalDate getFechaIngreso(){
        return fechaIngreso;
    }
    
    public boolean isActivo(){
        return activo;
    }
    
    public void setActivo(boolean activo){
        this.activo = activo;
    }
}
