/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import java.util.ArrayList;

public interface GestionDatos {
    /*logica menu inicio
    
    intentar crear y guardar un nuevo jugador
    retornar true si fue exitoso, false si fallo (username no unico o pass invalido)*/
    boolean crearPlayer(String username, String password);
    
    /*intentar iniciar sesion
    retornar el objeto player si el login es exitoso, o null si falla*/
    Player logIn(String username, String password);
    
    /*logica menu principal (mi cuenta)
    permite modificar el password de un usuario logeado, validando el passwword actual*/
    boolean cambiarPassword(String username, String viejaPassword, String Nuevapassword);
    
    //elimina el usuario del sistema (poner activo = false o borrandolo)
    boolean eliminarCuenta(String username, String password);
    
    /*logica menu principal (reporttes y juego)
    guarda ek registro de un juego terminado*/
    void guardarLog(GameLog log);
    
    //retorna la lista de jugadores activos ordenas or puntos (ranking)
    ArrayList<Player> obtenerRanking();
    
    /*retorna los logs donde el usuario con "username" estuvo involucrado, ordenados
    del mas reciente al mas viejo*/
    ArrayList<GameLog> obtenerLogs(String username);
}
