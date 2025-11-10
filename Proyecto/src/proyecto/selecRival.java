/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class selecRival extends JFrame {

    private JTextField nombreOponenteField;
    private JFrame ventanaPrincipalDelJuego;
    private SistemaJuego miSistema;
    private Player jugadorPrincipal;
    private JList<String> listaOponentes;
    private JScrollPane scrollLista;

    public selecRival(JFrame ventanaPrincipal, SistemaJuego miSistema, Player jugadorPrincipal) {
        super("Seleccionar Oponente");
        this.ventanaPrincipalDelJuego = ventanaPrincipal;
        this.miSistema = miSistema;
        this.jugadorPrincipal = jugadorPrincipal;

        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        fondoPanel fp = new fondoPanel("/img/fondoo.png");
        fp.setLayout(new BorderLayout(10, 10));
        fp.setBorder(BorderFactory.createEmptyBorder(60, 90, 90, 90));

        fp.add(getPanelCentral(), BorderLayout.CENTER);
        this.setContentPane(fp);
    }

    private JPanel getPanelCentral() {
        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        JPanel panelInput = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JLabel label = new JLabel("Nombre del Oponente:");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.BLACK);
        label.setFont(new Font("Bodoni Bd BT", Font.BOLD, 20));
        nombreOponenteField = new JTextField(15);

        panelInput.add(label);
        panelInput.add(nombreOponenteField);

        String[] nombres = miSistema.obtenerNombresOponentes(jugadorPrincipal.getUsername());
        listaOponentes = new JList<>(nombres);

        listaOponentes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaOponentes.setFont(new Font("Bodoni Bd BT", Font.BOLD, 25));
        listaOponentes.setBackground(new Color(204, 204, 204));
        listaOponentes.setForeground(Color.BLACK);

        scrollLista = new JScrollPane(listaOponentes);
        scrollLista.setBorder(BorderFactory.createTitledBorder("OPONENTES DISPONIBLES"));
        scrollLista.setPreferredSize(new Dimension(200, 120));

        JPanel abajo = new JPanel();
        
        // Botón de Aceptar
        JButton btnAceptar = new JButton("Comenzar Partida");
        btnAceptar.setFont(new Font("Bodoni Bd BT", Font.BOLD, 25));
        btnAceptar.setBackground(Color.BLACK);
        btnAceptar.setForeground(new Color(255, 204, 51));
        btnAceptar.setBorder(BorderFactory.createLineBorder(new Color(255, 204, 51),5));
        abajo.add(btnAceptar);
        
        panelCentral.add(panelInput, BorderLayout.NORTH);
        panelCentral.add(scrollLista, BorderLayout.CENTER);
        panelCentral.add(abajo, BorderLayout.SOUTH);

        this.add(panelCentral);

        listaOponentes.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    String selecNombre = listaOponentes.getSelectedValue();
                    nombreOponenteField.setText(selecNombre);
                }
            }
        });

        btnAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String oponenteIngresado = nombreOponenteField.getText().trim();

                if (oponenteIngresado.isEmpty()) {
                    JOptionPane.showMessageDialog(selecRival.this,
                            "Por favor, ingrese un nombre.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    iniciarJuego(oponenteIngresado);
                }
            }
        });
        return panelCentral;
    }


    private void iniciarJuego(String nombreOponente) {
        //String nombreOponente = nombreOponenteField.getText().trim(); 

        if (nombreOponente.equals(this.jugadorPrincipal.getUsername())) {
            JOptionPane.showMessageDialog(this, "No puedes jugar contra ti mismo. Ingresa un oponente válido.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Player oponenteEncontrado = miSistema.buscarPlayer(nombreOponente);

        if (oponenteEncontrado == null) {
            JOptionPane.showMessageDialog(this, "El oponente '" + nombreOponente + "' no existe. Asegúrate de que haya sido creado.", "Error de Oponente", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Player jugadorBlanco = this.jugadorPrincipal;
        Player jugadorNegro = new Player(nombreOponente, "Negro");

        vtnJuego juego = new vtnJuego(miSistema, jugadorBlanco, jugadorNegro);
        juego.setVisible(true);
        this.dispose();
    }
}
