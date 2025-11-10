/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import java.util.ArrayList;

public interface GestionDatos {
    boolean crearPlayer(String username, String password);
    
    Player logIn(String username, String password);
    
    boolean cambiarPassword(String username, String viejaPassword, String Nuevapassword);
    
    boolean eliminarCuenta(String username, String password);
    
    void guardarLog(GameLog log);
    
    ArrayList<Player> obtenerRanking();
    
    ArrayList<GameLog> obtenerLogs(String username);
}
