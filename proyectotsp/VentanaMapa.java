package com.mycompany.proyectotsp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class VentanaMapa extends JFrame{
    public JPanel panelMapa, panelOpciones;
    public JButton botonRuta;
    
    public VentanaMapa(){    
        setDefaultCloseOperation(EXIT_ON_CLOSE);    // Escondemos la ventana para volver al menu
        setTitle("Mapa de EUA");                    // Título de la ventana
        setSize(1200, 700);                         // Tamaño (Ancho, Alto)
        setLocationRelativeTo(null);                // Fija la ventana al centro
        setLayout(new BorderLayout(10, 10));        // Uso de un Layout para separar entre el mapa y las opciones
        
        iniciaMapa();
    }
    
    // Este metodo resume las instrucciones para crear la ventana 
    private void iniciaMapa(){
        colocarPanelMapa();
        colocarPanelOpciones();
    }
    
    // Se implementa el panel donde se selecciona la ciudad
    private void colocarPanelMapa(){
        panelMapa = new JPanel();
        panelMapa.setBackground(Color.WHITE);
        
        // Etiqueta para colocar el mapa de fondo
        JLabel ImagenMapa = new JLabel();
        ImagenMapa.setBounds(0, 2000, 650, 400);
        ImageIcon imagenM = new ImageIcon("mapaEUA.jpg");
        ImagenMapa.setIcon(new ImageIcon(imagenM.getImage().getScaledInstance(ImagenMapa.getWidth(), ImagenMapa.getHeight(), Image.SCALE_SMOOTH)));
        panelMapa.add(ImagenMapa);
        
        this.add(panelMapa, BorderLayout.CENTER);
    }
    
    private void colocarPanelOpciones(){
        //Este panel también tiene un Layout para acomodar correctamente ambos elementos
        panelOpciones = new JPanel(new BorderLayout(5, 5));
        panelOpciones.setBackground(Color.gray);
        
        // Etiqueta para instrucciones
        JLabel ImagenI = new JLabel();
        ImagenI.setBounds(0, 0, 350, 200);
        ImageIcon imagenInst = new ImageIcon("ImagenInstrucciones.png");
        ImagenI.setIcon(new ImageIcon(imagenInst.getImage().getScaledInstance(ImagenI.getWidth(), ImagenI.getHeight(), Image.SCALE_SMOOTH)));
        panelOpciones.add(ImagenI, BorderLayout.NORTH);
                
        // Creacion del menú de ciudades
        String [] ciudades = {"Alabama","Alaska","Arizona","Arkansas","California",
                              "Colorado","Connecticut","Delaware","District of Columbia",
                              "Florida","Georgia","Hawaii","Idaho","Illinois","Indiana",
                              "Iowa","Kansas","Kentucky","Louisiana","Maine","Maryland",
                              "Massachusetts","Michigan","Minnesota","Mississippi",
                              "Missouri","Montana","Nebraska","Nevada","New Hampshire",
                              "New Jersey","New Mexico","New York","North Carolina",
                              "North Dakota","Ohio","Oklahoma","Oregon","Pennsylvania",
                              "Rhode Island","South Carolina","South Dakota","Tennessee",
                              "Texas","Utah","Vermont","Virginia","Washington",
                              "West Virginia","Wisconsin","Wyoming","Puerto Rico"};
        JComboBox MenuCiudades = new JComboBox(ciudades);
        MenuCiudades.setFont(new Font("times new roman", Font.ITALIC, 18));
        MenuCiudades.setBounds(0, 200, 300, 100);
        panelOpciones.add(MenuCiudades, BorderLayout.CENTER);
        
        // Botón para calcular la ruta
        botonRuta = new JButton();
        botonRuta.setText("Calcular ruta");
        botonRuta.setBounds(0, 400, 75, 25);
        botonRuta.setFont(new Font("times new roman", Font.ITALIC, 18));
        panelOpciones.add(botonRuta, BorderLayout.EAST);
        
        // Etiqueta para imprimir la ruta
        String texto = "~ La ruta más corta encontrada es: \n \n"
                       + "Wisconsin -> North Dakota->\n Nebraska -> \n"
                       + "Mississipi -> Florida ->\n Maine -> Massachusetts ->"
                       + "Indiana -> Arkansas ->\n "
                       + "Colorado -> \n"
                       + "Montana ->\n South Carolina ->\n"
                       + "->\n"
                       + "->\n"
                       + "->\n->\n->\n->\n->"
                       + "->\n->\n->\n->\n->\n->\n->\n-> \n"
                       + "-> Wisconsin \n";
               
        JLabel RutaDetallada = new JLabel();
        RutaDetallada.setText(StrtoHtml(texto));
        RutaDetallada.setHorizontalAlignment(SwingConstants.CENTER);
        RutaDetallada.setBounds(0,0, 900, 100);
        RutaDetallada.setFont(new Font("times new roman", Font.BOLD, 14));
        RutaDetallada.setForeground(Color.WHITE);
        panelOpciones.add(RutaDetallada, BorderLayout.SOUTH);
        
        this.add(panelOpciones, BorderLayout.EAST);
    }
    
    private String StrtoHtml(String texto){
        String cadena = texto.replace("\n", "<br>");
        return "<html><p>" + cadena + "</p></html>";
    }
    
}
