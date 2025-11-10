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
    private int puntosGanados;
    
    public GameLog(String ganador, String perdedor, String mensajeFinal, int puntosGanados){
        this.fechaJuego = LocalDateTime.now();
        this.ganador = ganador;
        this.perdedor = perdedor;
        this.mensajeFinal = mensajeFinal;
        this.puntosGanados = puntosGanados;
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
    
    public int getPuntosGanados(){
        return puntosGanados;
    }
    
    @Override
    public String toString(){
        return fechaJuego.toLocalDate()+" - "+mensajeFinal+" - JUGADOR "+ganador.toUpperCase()+" VENCIO A JUGADOR "+perdedor.toUpperCase()+" - PUNTOS GANADOS: "+puntosGanados;
    }
}
