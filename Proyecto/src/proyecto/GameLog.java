/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import java.time.LocalDateTime;

public class GameLog {
    private LocalDateTime fechaJuego;
    private String ganador;
    private String perdedor;
    private String mensajeFinal;
    
    public GameLog(String ganador, String perdedor, String mensajeFinal){
        this.fechaJuego = LocalDateTime.now();
        this.ganador = ganador;
        this.perdedor = perdedor;
        this.mensajeFinal = mensajeFinal;
    }
    
    public LocalDateTime getFechaJuego(){
        return fechaJuego;
    }
    
    public String getGanador(){
        return ganador;
    }
    
    public String getPerdedor(){
        return perdedor;
    }
    
    public String getMensajeFinal(){
        return mensajeFinal;
    }
    
    @Override
    public String toString(){
        return fechaJuego.toLocalDate()+" - "+mensajeFinal;
    }
}
