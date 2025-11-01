/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JOptionPane;

/**
 *
 * @author HP
 */
public class SistemaJuego implements GestionDatos{
    //arreglo para simular el almacenimeinto de la informacion
    private ArrayList<Player> jugadores;
    private ArrayList<GameLog> logs;
    
    public SistemaJuego(){
        this.jugadores = new ArrayList<>();
        this.logs = new ArrayList<>();
    }
    
    /*implementacion de los metodos de GestionDatos
    metodo para buscar un player por username, util para la mayoria de los metodos*/
    private Player buscarPlayer(String username){
        for(Player p : jugadores){
            /*aqui se revisa solo los jugadores activos para ek login, pero para crear player
            se necesita ver si el nombre ya existe*/
            if(p.getUsername().equalsIgnoreCase(username)){
                return p;
            }
        }
        return null;
    }
    
    //menu inicio
    @Override
    public boolean crearPlayer(String username, String password){
        //validar que el username sea UNICO
        if(buscarPlayer(username) != null){
            JOptionPane.showMessageDialog(null, "ERROR: El username "+username+" ya existe en el sistema");
            return false;
        }
        
        //validar qeu el password sea exacgtamente de 5 caracteres
        //validar los demas caracteres especiales
        if(password == null || password.length() != 5){
            JOptionPane.showMessageDialog(null, "ERROR: La contraseña debe ser de 5 caracteres");
            return false;
        }
        
        //creacion y almacenamiento
        try{
            Player nuevoPlayer = new Player(username, password);
            jugadores.add(nuevoPlayer);
            JOptionPane.showMessageDialog(null, "Jugador "+username+" creado co exito");
            return true;
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error al crear el jugador: "+e.getMessage());
            return false;
        }
    }
    
    @Override
    public Player logIn(String username, String password){
        Player p = buscarPlayer(username);
        
        //verificar si el usuario existe y esta activo
        if(p == null || !p.isActivo()){
            return null;
        }
        
        //verificar la contraseña
        if(p.getPassword().equals(password)){
            JOptionPane.showMessageDialog(null, "Log In exitoso para: "+username);
            return p;
        } else{
            JOptionPane.showMessageDialog(null, "ERROR: Usuario o contraseña incorrectos");
            return null;
        }
    }
    
    //continuar con la implementacio de los otros 5 metodos
    @Override
    public boolean cambiarPassword(String username, String viejaPassword, String nuevaPassword){
        /*buscar el player
        validar que el viejaPssword sea correcta
        valiodar que la nuevaPassword sea de 5 caracteres
        si es valido, usar p.setPassword(nuevaPassword)*/
        
        try{ 
            Player p = buscarPlayer(username);
            
            //verificr existencia y actividad
            if(p == null || !p.isActivo()){
                JOptionPane.showMessageDialog(null, "ERROR: Usuario no encontrado o inactivo");
                return false;
            }
            
            //validar que la viejaPassword sea correcta
            if(!p.getPassword().equals(viejaPassword)){
                JOptionPane.showMessageDialog(null, "ERROR: Contraseña actual incorrecta");
                return false;
            }
            
            //validar que la nuevaPassword sea de 5 caracteres
            if(nuevaPassword == null || nuevaPassword.length() != 5){
                JOptionPane.showMessageDialog(null, "ERROR: La contraseña debe ser de 5 caracteres");
                return false;
            }
            
            //actualizar la contraseña
            p.setPassword(nuevaPassword);
            JOptionPane.showMessageDialog(null, "Contraseña de "+username+" cambiada con exito");
            return true;
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error al cambiar contraseña: "+e.getMessage());
        }
        return false;
    }
    
    @Override
    public boolean eliminarCuenta(String username, String password){
        /*buscar el player
        vslidar el password actual
        si es valido, usar p.setActivo(false) o eliminarlo de la lista*/
        
        try{
            Player p = buscarPlayer(username);
            
            //verificar existencia y actividad
            if(p == null || !p.isActivo()){
                JOptionPane.showMessageDialog(null, "ERROR: La cuenta ya he sido eliminada o no existe");
                return false;
            }
            
            //validar el password actual, confirmacion
            if(!p.getPassword().equals(password)){
                JOptionPane.showMessageDialog(null, "ERROR: Contraseña de confirmacion incorrecta. No se elimina la cuenta");
                return false;
            }
            
            //establece el estado como inactivo
            p.setActivo(false);
            JOptionPane.showMessageDialog(null, "Cuenta de "+username+" eliminada con exito");
            return true;
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error al intentar eliminar la cuenta: "+e.getMessage());
        }
        return false;
    }
    
    //menu principal(reportes)
    
    @Override
    public void guardarLog(GameLog log){
        //agg el log al arreglo
        if(log != null){
            logs.add(log);
            JOptionPane.showMessageDialog(null, "Log de juego guardado");
        }
    }
    
    @Override
    public ArrayList<Player> obtenerRanking(){
        /*crear una copia de la lista de jugadores activos
        ordenarls de forma descendente (el que tiene mas puntos primero)
        retornar la lista ordenada*/
        
        //filtrar solo jugadores activos
        ArrayList<Player> ranking = new ArrayList<>();
        for(Player p : jugadores){
            if(p.isActivo()){
                ranking.add(p);
            }
        }
        
        //ordenar por puntos, de mayor a menor
        //usamos un comparator para definir el criterio de ordenacion
        Collections.sort(ranking, new Comparator<Player>(){
            @Override
            public int compare(Player p1, Player p2){
                return p2.getPuntos() - p1.getPuntos();
            }
        });
        return ranking;
    }
    
    @Override
    public ArrayList<GameLog> obtenerLogs(String username){
        /*filtrar la lista "logs" para incluir solo los logs donde el "username" es ganador o perdedor
        ordenarla del mas reciente al mas viejo
        retornar la lista filtrada y ordenada*/
        
        ArrayList<GameLog> logsUsuario = new ArrayList<>();
        
        //filtrar los logs donde el usuario es el ganador o el perdedor
        for(GameLog log : logs){
            if(log.getGanador().equalsIgnoreCase(username) || log.getPerdedor().equalsIgnoreCase(username)){
                logsUsuario.add(log);
            }
        }
        
        //ordenar del mas reciente al mas viejo (descendiente por fecha)
        //la fecha es un LocalDateTime que se puede compara directamente
        Collections.sort(logsUsuario, new Comparator<GameLog>(){
            @Override
            public int compare(GameLog log1, GameLog log2){
                return log2.getFechaJuego().compareTo(log1.getFechaJuego());
            }
        });
        return logsUsuario;
    }
}
