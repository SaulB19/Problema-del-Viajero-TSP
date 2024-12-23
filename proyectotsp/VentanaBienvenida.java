package com.mycompany.proyectotsp;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class VentanaBienvenida extends JFrame{
    public JPanel panelPrincipal;
    public JLabel Presentacion, ImagenNodoIzq, ImagenBrujula;
    public JButton BotonMapa, BotonPresentacion;
    
    public VentanaBienvenida(){      
        setDefaultCloseOperation(EXIT_ON_CLOSE);    // Finalizamos el programa al cerrar la ventana
        setTitle("Problema del Viajante(TSP)");     // Título de la ventana
        setSize(1200, 600);                         // Tamaño (Ancho, Alto)
        setLocationRelativeTo(null);                // Fija la ventana al centro
        iniciarMenu();
    }
    
    // Este metodo resume las instrucciones para crear la ventana 
    private void iniciarMenu(){                 
        colocarPanel();                            
        colocarOpciones();
    }
    
    // Se implementa el panel donde se va a trabajar
    // No requerimos de un layout específico
    private void colocarPanel(){                
        panelPrincipal = new JPanel();          
        panelPrincipal.setLayout(null);
        panelPrincipal.setBackground(Color.DARK_GRAY);
        this.add(panelPrincipal);
        
    }
    
    // Coloca los elementos para el panel
    private void colocarOpciones(){             
        // Etiqueta de imagen como referencia
        Presentacion = new JLabel();
        Presentacion.setBounds(275, 50, 600, 200);
        ImageIcon imagenP = new ImageIcon("BienvenidaTSP.png");
        Presentacion.setIcon(new ImageIcon(imagenP.getImage().getScaledInstance(Presentacion.getWidth(), Presentacion.getHeight(), Image.SCALE_SMOOTH)));
        panelPrincipal.add(Presentacion);
        
        // Imagen decoracion 1
        ImagenNodoIzq = new JLabel();
        ImagenNodoIzq.setBounds(45, 150, 150, 250);
        ImageIcon imagenNIzq = new ImageIcon("NodoIzq.png");
        ImagenNodoIzq.setIcon(new ImageIcon(imagenNIzq.getImage().getScaledInstance(ImagenNodoIzq.getWidth(), ImagenNodoIzq.getHeight(), Image.SCALE_SMOOTH)));
        panelPrincipal.add(ImagenNodoIzq);
        
        // Imagen decoracion 2
        ImagenBrujula = new JLabel();
        ImagenBrujula.setBounds(900, 200, 250, 150);
        ImageIcon imagenBru = new ImageIcon("Brujula.png");
        ImagenBrujula.setIcon(new ImageIcon(imagenBru.getImage().getScaledInstance(ImagenBrujula.getWidth(), ImagenBrujula.getHeight(), Image.SCALE_SMOOTH)));
        panelPrincipal.add(ImagenBrujula);
        
        // Botón para conocer el progama o tutorial
        BotonPresentacion = new JButton();
        BotonPresentacion.setText("¿Qué es el TSP?");
        BotonPresentacion.setBounds(275, 300, 600, 100);
        BotonPresentacion.setFont(new Font("times new roman", Font.ITALIC, 36));
        abrirPresentacion();
        panelPrincipal.add(BotonPresentacion);
        
        // Botón para iniciar el mapa
        BotonMapa = new JButton();
        BotonMapa.setText("Abrir mapa! :D");
        BotonMapa.setBounds(275, 425, 600, 100);
        BotonMapa.setFont(new Font("times new roman", Font.ITALIC, 36));
        abrirMapa();
        panelPrincipal.add(BotonMapa);
    }
    
    // Este metodo asigna un evento al BotonPresentacion, el cual es dirigir a una introduccion
    // pero en una ventana nueva
    private void abrirPresentacion(){
        ActionListener intro = new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaPresentacion venP = new VentanaPresentacion();
                venP.setVisible(true);
//                setVisible(false);
            }
        };
        BotonPresentacion.addActionListener(intro);
    }
        
    // Este metodo asigna un evento al BotonMapa, el cual es crear 
    // una ventana con el mapa del ejercicio
    private void abrirMapa(){
        ActionListener mapa = new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaMapa venT = new VentanaMapa();
                venT.setVisible(true);
                setVisible(false);
            }
        };
        BotonMapa.addActionListener(mapa);
    }    
}