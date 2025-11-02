/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import java.util.ArrayList;
import javax.swing.*;
//import java.awt.*;

public class Reportes extends JFrame{
    private SistemaJuego sistema;
    private Player jugadorActual;

    public Reportes(SistemaJuego sistema, Player jugador) {
        this.sistema = sistema;
        this.jugadorActual = jugador;
        
        setTitle("Reportes del Juego");
        setSize(500, 500);
        setLocationRelativeTo(null);
        
        //JTabbedPane para tener dos pestañas: ranking y logs
        JTabbedPane tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("Ranking de Jugadores", crearPanelRanking());
        tabbedPane.addTab("Logs de Mis Ultimos Juegos", crearPanelLogs());
        add(tabbedPane);
    }
    
    private JScrollPane crearPanelRanking(){
        JTextArea rankingArea = new JTextArea();
        rankingArea.setEditable(false);
        
        ArrayList<Player> ranking = sistema.obtenerRanking();
        StringBuilder sb = new StringBuilder();
        sb.append("POSICION   |   USUARIO   |   PUNTOS\n");
        sb.append("------------------------------------------\n");
        
        int posicion = 1;
        for(Player p : ranking){
            sb.append(String.format("%-8d  |  %-10s  |  %d\n", posicion++, p.getUsername(), p.getPuntos()));
        }
        rankingArea.setText(sb.toString());
        return new JScrollPane(rankingArea);
    }
    
    private JScrollPane crearPanelLogs(){
        JTextArea logsArea = new JTextArea();
        logsArea.setEditable(false);
        ArrayList<GameLog> logs = sistema.obtenerLogs(jugadorActual.getUsername());
        StringBuilder sb = new StringBuilder();
        sb.append("---LOGS DE JUEGOS(DEL MAS RECIENTE AL MAS VIEJO)---\n");
        sb.append("---------------------------------------------------------\n");
        
        if(logs.isEmpty()){
            sb.append("Aun no tines partidas registradas");
        } else{
            for(GameLog log : logs){
                sb.append(log.toString()).append("\n");
            }
        }
        logsArea.setText(sb.toString());
        return new JScrollPane(logsArea);
    }
}
