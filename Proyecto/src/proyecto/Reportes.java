/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class Reportes extends JFrame{
    private SistemaJuego sistema;
    private Player jugadorActual;

    public Reportes(SistemaJuego sistema, Player jugador) {
        this.sistema = sistema;
        this.jugadorActual = jugador;
        
        setTitle("Reportes del Juego: "+jugador.getUsername());
        setSize(800, 550);
        setLocationRelativeTo(null);
        
        //JTabbedPane para tener dos pestañas: ranking y logs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Bodoni Bd BT", Font.BOLD, 20));
        tabbedPane.setForeground(Color.BLACK);
        
        tabbedPane.addTab("Ranking de Jugadores", crearPanelRanking());
        tabbedPane.addTab("Logs de Mis Ultimos Juegos", crearPanelLogs());
        add(tabbedPane);
    }
    
    private JScrollPane crearPanelRanking(){
        JPanel fp = new fondoPanel("/img/fondoReporte.png");
        fp.setLayout(new BorderLayout());
        
        JTextArea rankingArea = new JTextArea();
        rankingArea.setEditable(false);
        
        rankingArea.setBackground(new Color(25, 25, 25));
        rankingArea.setForeground(new Color(220, 220, 220));
        rankingArea.setFont(new Font("Bodoni Bd BT", Font.BOLD, 20));
        rankingArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        ArrayList<Player> ranking = sistema.obtenerRanking();
        StringBuilder sb = new StringBuilder();
        //sb.append(String.format("%-10s | %-15s | %s\n", "POSICION", "USUARIO", "PUNTOS"));
        sb.append("   POSICION   |   USUARIO   |   PUNTOS\n");
        sb.append("----------------------------------------------------\n");
        
        int posicion = 1;
        for(Player p : ranking){
            //sb.append(String.format("%-10d | %-15s | %d\n", posicion++, p.getUsername(), p.getPuntos()));
            sb.append(String.format(" %-8d | %-10s | %d\n", posicion++, p.getUsername(), p.getPuntos()));
        }
        rankingArea.setText(sb.toString());
        JScrollPane scrollPane = new JScrollPane(rankingArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180, 2)));
        fp.add(scrollPane, BorderLayout.CENTER);
        return new JScrollPane(fp);
    }
    
    private JScrollPane crearPanelLogs(){
        JPanel fp = new fondoPanel("/img/fondoReporte.png");
        fp.setLayout(new BorderLayout());
        
        JTextArea logsArea = new JTextArea();
        logsArea.setEditable(false);
        logsArea.setBackground(new Color(25, 25, 25));
        logsArea.setForeground(new Color(220, 220, 220));
        logsArea.setFont(new Font("Bodoni Bd BT", Font.BOLD, 20));
        logsArea.setCaretColor(new Color(25, 25, 25));
        logsArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        ArrayList<GameLog> logs = sistema.obtenerLogs(jugadorActual.getUsername());
        StringBuilder sb = new StringBuilder();
        sb.append("---LOGS DE JUEGOS(DEL MAS RECIENTE AL MAS VIEJO)---\n");
        sb.append("----------------------------------------------------------------------------\n\n");
        
        if(logs == null || logs.isEmpty()){
            sb.append("Aun no tines partidas registradas");
        } else{
            for(GameLog log : logs){
                sb.append(log.toString()).append("\n\n");
            }
        }
        logsArea.setText(sb.toString());
        JScrollPane scrollPane = new JScrollPane(logsArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180, 2)));
        fp.add(scrollPane, BorderLayout.CENTER);
        return new JScrollPane(logsArea);
    }
}
