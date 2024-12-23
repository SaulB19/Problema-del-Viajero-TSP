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
import javax.swing.SwingConstants;
import static javax.swing.WindowConstants.HIDE_ON_CLOSE;

public class VentanaPresentacion extends JFrame{
    public JPanel panelPresentacion;
    public JButton BotonOK;
    
    public VentanaPresentacion(){
        setDefaultCloseOperation(HIDE_ON_CLOSE);    // Escondemos la ventana para volver al menu
        setTitle("Acerca del problema...");                    // Título de la ventana
        setSize(1200, 600);                         // Tamaño (Ancho, Alto)
        setLocationRelativeTo(null);                // Fija la ventana al centro
        iniciaPresentacion();
    }
    
    // Este metodo resume las instrucciones para crear la ventana 
    private void iniciaPresentacion(){
        colocarPanelPresentacion();
        colocarInstrucciones();
    }
    
    // Se implementa el panel donde se va a trabajar
    // Nuevamente, no requerimos de un layout específico
    private void colocarPanelPresentacion(){
        panelPresentacion = new JPanel();
        panelPresentacion.setLayout(null);
        panelPresentacion.setBackground(Color.BLACK);
        this.add(panelPresentacion);
    }
    
    // Coloca los elementos para el panel
    private void colocarInstrucciones(){
              
        // Etiqueta para instrucciones
        String texto = "~~~ ¿Qué es el TSP? ~~~ \n \n"
                       + ">>> El Problema del Agente Viajero, o también conocido como Travelling Salesman \n"
                       + " Problem (TSP), es un porblema del tipo NP; es decir, no tiene un algoritmo en  \n" 
                       + " específico para solucionar el problema pero se puede verificar soluciones para \n"
                       + " lograrlo. \n"
                       + ">>> El enunciado del problemas nos indica que un comerciante debe hacer el \n" 
                       + " recorrido de todas las ciudades de un mapa, evitando que cruce dos veces una \n"
                       + " misma ciudad y que a la vez vuelva al punto de origen. \n"
                       + ">>> Dentro de este proyecto, se exploraron diferentes maneras de solucionar este\n"
                       + " problema, desde la fuerza bruta hasta las heurísticas. En particular, se presenta\n"
                       + " la solución con el Algoritmo de la Colonia de Hormigas (Ant Colony Optimization, \n"
                       + " ACO, en inglés), el cual asemeja el comportamiento de las colonias de hormigas   \n"
                       + " en la búsqueda de alimento. \n \n"
                       + ">>> Para este código, basta con escoger una ciudad de origen y con ello podrás    \n"
                       + " explorar la ruta más óptima de uno de los problemas más trascendentales de la    \n"
                       + " programación. Pulsa >>OK! para iniciar :D  \n";
               
        JLabel Introduccion = new JLabel();
        Introduccion.setText(StrtoHtml(texto));
        Introduccion.setHorizontalAlignment(SwingConstants.CENTER);
        Introduccion.setBounds(50, 50, 700, 550);
        Introduccion.setFont(new Font("times new roman", Font.BOLD, 20));
        Introduccion.setForeground(Color.WHITE);
        panelPresentacion.add(Introduccion);
        
        // Etiqueta de imagenes
        JLabel ImagenTutorial = new JLabel();
        ImagenTutorial.setBounds(800, 200, 350, 250);
        ImageIcon imagenT = new ImageIcon("ImagenHormigas.png");
        ImagenTutorial.setIcon(new ImageIcon(imagenT.getImage().getScaledInstance(ImagenTutorial.getWidth(), ImagenTutorial.getHeight(), Image.SCALE_SMOOTH)));
        panelPresentacion.add(ImagenTutorial);
        
        // Boton para confirmar
        BotonOK = new JButton();
        BotonOK.setText("OK!");
        BotonOK.setBounds(650, 50, 120, 100);
        BotonOK.setFont(new Font("times new roman", Font.ITALIC, 36));
        cerrarTutorial();
        panelPresentacion.add(BotonOK);
    }
    
    // Este metodo ayuda a convertir nuestro texto en un formato correcto,
    // respetando los saltos de línea.
    private String StrtoHtml(String texto){
        String cadena = texto.replace("\n", "<br>");
        return "<html><p>" + cadena + "</p></html>";
    }
    
    // Este metodo asigna un evento al BotonOk, el cual es confirmar las 
    // indicaciones y ocultar esta ventana
    private void cerrarTutorial(){
        ActionListener tutorial = new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        };
        
        BotonOK.addActionListener(tutorial);
    }
    
}