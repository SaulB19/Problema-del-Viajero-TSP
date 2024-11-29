package com.mycompany.proyectotsp;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class VentanaMapa extends JFrame {
    private JPanel panelMapa, panelOpciones;
    private JComboBox<String> MenuCiudades;
    private JButton botonRuta;
    private JButton botonDetener;
    private JTextArea RutaDetallada = new JTextArea();
    private JScrollPane cajaTexto = new JScrollPane(RutaDetallada);

    // LLeva el seguimiento de las posiciones graficas de los nodos
    private Map<String, double[]> posiciones;

    private Grafo grafo;
    private ACO aco;

    /**
     * Metodo constructor
     */
    public VentanaMapa() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Mapa de EUA");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        grafo = new Grafo();

        String[] ciudades = {
                "Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado",
                "Connecticut", "Delaware", "District of Columbia", "Florida", "Georgia",
                "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky",
                "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota",
                "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire",
                "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota",
                "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina",
                "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington",
                "West Virginia", "Wisconsin", "Wyoming", "Puerto Rico",
        };

        // TODO: Reajustar las posiciones en pantalla de los nodos de las ciudades, o
        // calcularlas dinamicamente
        double[][] coordenadas = {
                { 516, 548 }, { 92, 660 }, { 150, 484 }, { 430, 496 }, { 48, 372 },
                { 248, 366 }, { 676, 244 }, { 650, 326 }, { 628, 330 }, { 606, 672 },
                { 564, 534 }, { 224, 694 }, { 154, 218 }, { 466, 350 }, { 504, 340 },
                { 414, 292 }, { 344, 390 }, { 528, 404 }, { 434, 618 }, { 698, 124 },
                { 646, 344 }, { 678, 218 }, { 522, 250 }, { 396, 154 }, { 468, 572 },
                { 424, 394 }, { 206, 132 }, { 330, 300 }, { 104, 312 }, { 678, 182 },
                { 654, 296 }, { 238, 496 }, { 636, 224 }, { 604, 442 }, { 330, 130 },
                { 546, 320 }, { 356, 482 }, { 80, 168 }, { 610, 288 }, { 690, 234 },
                { 590, 490 }, { 324, 226 }, { 520, 454 }, { 318, 596 }, { 166, 338 },
                { 662, 174 }, { 610, 384 }, { 94, 82 }, { 574, 364 }, { 440, 212 },
                { 230, 250 }, { 758, 648 },
        };

        posiciones = new HashMap<>();
        for (int i = 0; i < ciudades.length; i++) {
            posiciones.put(ciudades[i], new double[] { coordenadas[i][0] / 800, coordenadas[i][1] / 800 });
        }

        iniciaMapa();
    }

    // -------------| Metodos Privados |------------- //

    private void iniciaMapa() {
        colocarPanelMapa();
        colocarPanelOpciones();
    }

    private void colocarPanelMapa() {
        panelMapa = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                ImageIcon imagenM = new ImageIcon("mapaEUA.png");
                Image fondo = imagenM.getImage();
                g2.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);

                dibujarEnlaces(g2);
                dibujarNodos(g2);
            }

            private void dibujarNodos(Graphics2D g) {
                g.setFont(new Font("times new roman", Font.PLAIN, 8));
                g.setColor(Color.BLACK);

                int anchoActual = getWidth();
                int altoActual = getHeight();

                for (Map.Entry<String, double[]> entry : posiciones.entrySet()) {
                    String nodo = entry.getKey();
                    double[] coordenadas = entry.getValue();

                    // Adaptar las coordenadas al tamaño de la pantalla
                    int x = (int) (coordenadas[0] * anchoActual);
                    int y = (int) (coordenadas[1] * altoActual);

                    g.fillOval(x - 6, y - 6, 12, 12);
                    g.setColor(Color.BLACK);
                    g.drawOval(x - 6, y - 6, 12, 12);
                    g.drawString(nodo, x, y - 8);
                }
            }

            private void dibujarEnlaces(Graphics2D g) {
                int anchoActual = getWidth();
                int altoActual = getHeight();

                for (Map.Entry<String, Ccity> entry : grafo.getCiudades().entrySet()) {
                    String origen = entry.getKey();
                    Ccity nodoOrigen = entry.getValue();
                    double[] origenCoordenadas = posiciones.get(origen);

                    for (Map.Entry<Ccity, Double> enlace : nodoOrigen.getDistancias().entrySet()) {
                        Ccity destino = enlace.getKey();
                        double[] destinoCoordenadas = posiciones.get(destino.getNombre());

                        // Convertir coordenadas  a absolutas
                        int x1 = (int) (origenCoordenadas[0] * anchoActual);
                        int y1 = (int) (origenCoordenadas[1] * altoActual);
                        int x2 = (int) (destinoCoordenadas[0] * anchoActual);
                        int y2 = (int) (destinoCoordenadas[1] * altoActual);

                        // Dibujar línea con grosor proporcional a feromonas
                        double feromonas = nodoOrigen.getFeromonas(destino);
                        float grosor = (float) Math.min(4, feromonas * 255);
                        g.setStroke(new BasicStroke(grosor));
                        g.setColor(new Color(150, 150, 255, (int) Math.min(255, feromonas * 255)));
                        g.drawLine(x1, y1, x2, y2);
                    }
                }

                ArrayList<Ccity> mejorRuta;
                if (aco == null || (mejorRuta = aco.getMejorRuta()) == null) {
                    return;
                }

                Ccity anterior = mejorRuta.get(mejorRuta.size() - 1);

                for (Ccity ciudad : aco.getMejorRuta()) {
                    double[] anteriorCoordenadas = posiciones.get(anterior.getNombre());
                    double[] ciudadCoordenadas = posiciones.get(ciudad.getNombre());

                    int x1 = (int) (anteriorCoordenadas[0] * anchoActual);
                    int y1 = (int) (anteriorCoordenadas[1] * altoActual);
                    int x2 = (int) (ciudadCoordenadas[0] * anchoActual);
                    int y2 = (int) (ciudadCoordenadas[1] * altoActual);

                    float grosor = 2;

                    g.setStroke(new BasicStroke(grosor));

                    g.setColor(new Color(0, 0, 0));

                    g.drawLine(x1, y1, x2, y2);

                    anterior = ciudad;
                }
            }
        };
        panelMapa.setBackground(Color.WHITE);

        this.add(panelMapa, BorderLayout.CENTER);
    }

    private void colocarPanelOpciones() {
        panelOpciones = new JPanel(new FlowLayout());
        panelOpciones.setPreferredSize(new Dimension(350, 700));
        panelOpciones.setBackground(Color.GRAY);

        JLabel ImagenI = new JLabel();
        ImagenI.setBounds(0, 0, 350, 200);
        ImageIcon imagenInst = new ImageIcon("ImagenInstrucciones.png");
        ImagenI.setIcon(new ImageIcon(
                imagenInst.getImage().getScaledInstance(ImagenI.getWidth(), ImagenI.getHeight(), Image.SCALE_SMOOTH)));
        panelOpciones.add(ImagenI);

        String[] ciudades = { "Ciudad aleatoria", "Alabama", "Alaska", "Arizona", "Arkansas", "California",
                "Colorado", "Connecticut", "Delaware", "District of Columbia",
                "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana",
                "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland",
                "Massachusetts", "Michigan", "Minnesota", "Mississippi",
                "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire",
                "New Jersey", "New Mexico", "New York", "North Carolina",
                "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania",
                "Rhode Island", "South Carolina", "South Dakota", "Tennessee",
                "Texas", "Utah", "Vermont", "Virginia", "Washington",
                "West Virginia", "Wisconsin", "Wyoming", "Puerto Rico" };
        MenuCiudades = new JComboBox<>(ciudades);
        MenuCiudades.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        MenuCiudades.setBounds(0, 10, 150, 50);
        panelOpciones.add(MenuCiudades);

        // Botón para invocar el algoritmo
        botonRuta = new JButton();
        botonRuta.setText("Calcular ruta");
        botonRuta.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        botonRuta.setBounds(0, 10, 100, 50);
        botonRuta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para calcular rutas
                aco = new ACO(grafo, 1, 9, 0.01, 0.05, panelMapa);
                grafo.reiniciarFeromonas();
                // Instrucciones para tomar el la ciudad de origen
                String origenC = MenuCiudades.getSelectedItem().toString();
                aco.setCiudadInicial(origenC);

                aco.iniciar();
                String texto = "Calculando ruta...\n";
                texto += "Presione 'Detener' para conocer la ruta y su\n";
                texto += "costo";
                mostrarTexto(texto);

                botonRuta.setVisible(false);
                botonDetener.setVisible(true);
            }
        });
        panelOpciones.add(botonRuta);

        // Botón para invocar el algoritmo
        botonDetener = new JButton();
        botonDetener.setText("Detener");
        botonDetener.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        botonDetener.setBounds(0, 10, 100, 50);
        botonDetener.setVisible(false);
        botonDetener.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para calcular rutas
                aco.detener();

                // Instrucciones para imprimir la ruta de forma escrita
                String texto = aco.imprimirMejorRuta();
                mostrarTexto("Ciudad de origen: " + aco.getCiudadInicial() + "\n" + texto);

                botonRuta.setText("Recalcular ruta");
                botonRuta.setVisible(true);
                botonDetener.setVisible(false);
            }
        });
        panelOpciones.add(botonDetener);

        // Etiqueta para imprimir la ruta
        String texto = "Seleccione una ciudad inicial para el recorrido\n";
        texto += "y presione 'Calcular ruta'";
        mostrarTexto(texto);

        this.add(panelOpciones, BorderLayout.EAST);
    }

    private void mostrarTexto(String texto) {
        // // RutaDetallada = new JTextArea();
        // RutaDetallada.setBounds(0, 10, 300, 400);
        // RutaDetallada.setPreferredSize(new Dimension(300, 400));
        RutaDetallada.setText(textoOrdenado(texto));
        RutaDetallada.setFont(new Font("times new roman", Font.CENTER_BASELINE, 12));
        RutaDetallada.setForeground(Color.BLACK);
        cajaTexto.setPreferredSize(new Dimension(250, 400));
        // RutaDetallada.setEnabled(false);
        panelOpciones.add(cajaTexto);
    }

    private String textoOrdenado(String texto) {
        String cadena = texto.replace("->", "->\n");
        return cadena;
    }
}
