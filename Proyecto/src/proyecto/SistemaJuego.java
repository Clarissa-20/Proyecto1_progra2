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
public class SistemaJuego implements GestionDatos {

    private ArrayList<Player> jugadores;
    private ArrayList<GameLog> logs;

    public SistemaJuego() {
        this.jugadores = new ArrayList<>();
        this.logs = new ArrayList<>();
    }

    public Player buscarPlayer(String username) {
        for (Player p : jugadores) {
            if (p.getUsername().equalsIgnoreCase(username)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public boolean crearPlayer(String username, String password) {
        if (buscarPlayer(username) != null) {
            JOptionPane.showMessageDialog(null, "ERROR: El username " + username + " ya existe en el sistema");
            return false;
        }

        if (password == null || password.length() != 5) {
            return false;
        }

        try {
            Player nuevoPlayer = new Player(username, password);
            jugadores.add(nuevoPlayer);
            System.out.println("Jugador " + username + " creado con exito");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Player logIn(String username, String password) {
        Player p = buscarPlayer(username);

        if (p == null || !p.isActivo()) {
            return null;
        }

        if (p.getPassword().equals(password)) {
            System.out.println("Log In exitoso para: " + username);
            return p;
        } else {
            return null;
        }
    }

    @Override
    public boolean cambiarPassword(String username, String viejaPassword, String nuevaPassword) {
        
        try {
            Player p = buscarPlayer(username);

            if (p == null || !p.isActivo()) {
                JOptionPane.showMessageDialog(null, "ERROR: Usuario no encontrado o inactivo");
                return false;
            }

            if (!p.getPassword().equals(viejaPassword)) {
                JOptionPane.showMessageDialog(null, "ERROR: Contraseña actual incorrecta");
                return false;
            }

            if (nuevaPassword == null || nuevaPassword.length() != 5) {
                JOptionPane.showMessageDialog(null, "ERROR: La contraseña debe ser de 5 caracteres");
                return false;
            }

            p.setPassword(nuevaPassword);
            JOptionPane.showMessageDialog(null, "Contraseña de " + username + " cambiada con exito");
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cambiar contraseña: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean eliminarCuenta(String username, String password) {
       
        try {
            Player p = buscarPlayer(username);

            if (p == null || !p.isActivo()) {
                JOptionPane.showMessageDialog(null, "ERROR: La cuenta ya he sido eliminada o no existe");
                return false;
            }

            if (!p.getPassword().equals(password)) {
                JOptionPane.showMessageDialog(null, "ERROR: Contraseña de confirmacion incorrecta. No se elimina la cuenta");
                return false;
            }

            p.setActivo(false);
            System.out.println("Cuenta de " + username + " eliminada con exito");
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al intentar eliminar la cuenta: " + e.getMessage());
        }
        return false;
    }

    //menu principal(reportes)
    @Override
    public void guardarLog(GameLog log) {
        if (log != null) {
            logs.add(log);
            JOptionPane.showMessageDialog(null, "Log del juego guardado");
        }
    }

    @Override
    public ArrayList<Player> obtenerRanking() {
        //filtrar solo jugadores activos
        ArrayList<Player> ranking = new ArrayList<>();
        for (Player p : jugadores) {
            if (p.isActivo()) {
                ranking.add(p);
            }
        }

        //ordenar por puntos, de mayor a menor
        Collections.sort(ranking, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return p2.getPuntos() - p1.getPuntos();
            }
        });
        return ranking;
    }

    @Override
    public ArrayList<GameLog> obtenerLogs(String username) {
        
        ArrayList<GameLog> logsUsuario = new ArrayList<>();
        for (GameLog log : logs) {
            if (log.getGanador().equalsIgnoreCase(username) || log.getPerdedor().equalsIgnoreCase(username)) {
                logsUsuario.add(log);
            }
        }

        Collections.sort(logsUsuario, new Comparator<GameLog>() {
            @Override
            public int compare(GameLog log1, GameLog log2) {
                return log2.getFechaJuego().compareTo(log1.getFechaJuego());
            }
        });
        return logsUsuario;
    }

    public String[] obtenerNombresOponentes(String nombrePrincipal) {
        ArrayList<String> nombres = new ArrayList<>();

        for (Player p : jugadores) {

            boolean esActivo = p.isActivo();

            boolean esOponente = !p.getUsername().equalsIgnoreCase(nombrePrincipal);

            if (esActivo && esOponente) {
                nombres.add(p.getUsername());
            }
        }
        return nombres.toArray(new String[0]);
    }
}
