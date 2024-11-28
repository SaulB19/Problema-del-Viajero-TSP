package com.mycompany.proyectotsp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.*;

public class VentanaMapa extends JFrame {
    private JPanel panelMapa, panelOpciones;
    private JButton botonRuta;
    private Grafo grafo;

    public VentanaMapa() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Mapa de EUA");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        grafo = new Grafo(); // Inicializa el grafo con nodos

        iniciaMapa();
    }

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

                // Dibuja la imagen de fondo
                ImageIcon imagenM = new ImageIcon("mapaEUA.jpg");
                Image fondo = imagenM.getImage();
                g2.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);

                // Dibuja los nodos y enlaces del grafo
                dibujarEnlaces(g2);
                dibujarNodos(g2);
            }

            private void dibujarNodos(Graphics2D g) {
                g.setFont(new Font("Arial", Font.PLAIN, 10));
                g.setColor(Color.BLACK);

                for (Map.Entry<String, double[]> entry : grafo.getPosiciones().entrySet()) {
                    String nodo = entry.getKey();
                    double[] coordenadas = entry.getValue();
                    int x = (int) coordenadas[0];
                    int y = (int) coordenadas[1];

                    g.fillOval(x - 10, y - 10, 20, 20);
                    g.setColor(Color.BLACK);
                    g.drawOval(x - 10, y - 10, 20, 20);
                    g.drawString(nodo, x, y - 15);
                }
            }

            private void dibujarEnlaces(Graphics2D g) {
                g.setColor(Color.BLACK);

                for (Map.Entry<String, Ccity> entry : grafo.getCiudades().entrySet()) {
                    String origen = entry.getKey();
                    Ccity nodoOrigen = entry.getValue();
                    double[] origenCoordenadas = grafo.getPosiciones().get(origen);

                    // TODO: dibujar los enlaces de manera que representen la cantidad de feromonas
                    for (Map.Entry<Ccity, Double> enlace : nodoOrigen.getDistancias().entrySet()) {
                        Ccity destino = enlace.getKey();
                        double[] destinoCoordenadas = grafo.getPosiciones().get(destino.getNombre());

                        g.drawLine((int) origenCoordenadas[0], (int) origenCoordenadas[1],
                                (int) destinoCoordenadas[0], (int) destinoCoordenadas[1]);

                        double mitadX = (origenCoordenadas[0] + destinoCoordenadas[0]) / 2;
                        double mitadY = (origenCoordenadas[1] + destinoCoordenadas[1]) / 2;
                        g.drawString(String.format("%.2f", nodoOrigen.getFeromonas(destino) * 100),
                                (int) mitadX, (int) mitadY);
                    }
                }
            }
        };
        panelMapa.setBackground(Color.WHITE);

        this.add(panelMapa, BorderLayout.CENTER);
    }

    private void colocarPanelOpciones() {
        panelOpciones = new JPanel(new BorderLayout(5, 5));
        panelOpciones.setBackground(Color.GRAY);

        JLabel ImagenI = new JLabel();
        ImagenI.setBounds(0, 0, 350, 200);
        ImageIcon imagenInst = new ImageIcon("ImagenInstrucciones.png");
        ImagenI.setIcon(new ImageIcon(
                imagenInst.getImage().getScaledInstance(ImagenI.getWidth(), ImagenI.getHeight(), Image.SCALE_SMOOTH)));
        panelOpciones.add(ImagenI, BorderLayout.NORTH);

        String[] ciudades = { "Alabama", "Alaska", "Arizona", "Arkansas", "California",
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
        JComboBox<String> MenuCiudades = new JComboBox<>(ciudades);
        MenuCiudades.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        MenuCiudades.setBounds(0, 200, 300, 100);
        panelOpciones.add(MenuCiudades, BorderLayout.CENTER);

        botonRuta = new JButton();
        botonRuta.setText("Calcular ruta");
        botonRuta.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        botonRuta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para calcular rutas
                ACO aco = new ACO(grafo, 50, 200, 4, 1, 0.64, 0.005, panelMapa);
                Hormiga mejorHormiga = aco.ACO();
                aco.imprimirMejorRuta(mejorHormiga);

                panelMapa.repaint();
            }
        });
        panelOpciones.add(botonRuta, BorderLayout.EAST);

        this.add(panelOpciones, BorderLayout.EAST);
    }

}
