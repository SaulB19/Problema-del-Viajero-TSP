package com.mycompany.proyectotsp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.*;

public class VentanaMapa extends JFrame {
    private JPanel panelMapa, panelOpciones;
    private JComboBox<String> MenuCiudades;
    private JButton botonRuta;
    private JButton botonDetener;
    private JTextArea RutaDetallada = new JTextArea();
    private String texto;
    private Grafo grafo;
    private ACO aco;

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
                ImageIcon imagenM = new ImageIcon("mapaEUA.png");
                Image fondo = imagenM.getImage();
                g2.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);

                // Dibuja los nodos y enlaces del grafo
                dibujarEnlaces(g2);
                dibujarNodos(g2);
            }

            private void dibujarNodos(Graphics2D g) {
                g.setFont(new Font("times new roman", Font.PLAIN, 8));
                g.setColor(Color.BLACK);

                for (Map.Entry<String, double[]> entry : grafo.getPosiciones().entrySet()) {
                    String nodo = entry.getKey();
                    double[] coordenadas = entry.getValue();
                    int x = (int) coordenadas[0];
                    int y = (int) coordenadas[1];

                    g.fillOval(x - 6, y - 6, 12, 12);
                    g.setColor(Color.BLACK);
                    g.drawOval(x - 6, y - 6, 12, 12);
                    g.drawString(nodo, x, y - 8);
                }
            }

            private void dibujarEnlaces(Graphics2D g) {
                for (Map.Entry<String, Ccity> entry : grafo.getCiudades().entrySet()) {
                    String origen = entry.getKey();
                    Ccity nodoOrigen = entry.getValue();
                    double[] origenCoordenadas = grafo.getPosiciones().get(origen);

                    for (Map.Entry<Ccity, Double> enlace : nodoOrigen.getDistancias().entrySet()) {
                        Ccity destino = enlace.getKey();
                        double[] destinoCoordenadas = grafo.getPosiciones().get(destino.getNombre());

                        // Obtener la cantidad de feromonas
                        double feromonas = nodoOrigen.getFeromonas(destino);

                        // Ajustar el grosor del trazo basado en las feromonas
                        float grosor = (float) (1 + feromonas * 5); // Ajusta el factor multiplicativo según sea
                                                                    // necesario
                        g.setStroke(new BasicStroke(grosor));

                        // Establecer el color del enlace
                        g.setColor(new Color(0, 0, 255, (int) Math.min(255, feromonas * 255))); // Azul con opacidad
                                                                                                // proporcional

                        // Dibujar la línea del enlace
                        g.drawLine((int) origenCoordenadas[0], (int) origenCoordenadas[1],
                                (int) destinoCoordenadas[0], (int) destinoCoordenadas[1]);
                    }
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
                aco = new ACO(grafo, 50, 1, 9, 0.01, 0.05, panelMapa);

                // Instrucciones para tomar el la ciudad de origen
                String origenC = MenuCiudades.getSelectedItem().toString();
                aco.setCiudadInicial(origenC);

                aco.iniciar();

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
                texto = aco.imprimirMejorRuta();
                imprimirRuta(texto);

                botonRuta.setVisible(true);
                botonDetener.setVisible(false);
            }
        });
        panelOpciones.add(botonDetener);

        // Etiqueta para imprimir la ruta
        String texto = "~ La ruta más corta encontrada es: \n \n"
                + "Ciudad de origen -> \n"
                + "->\n->\n->\n->\n->\n->\n->\n-> \n"
                + "->\n->\n->\n->\n->\n->\n->\n-> \n"
                + "->\n->\n->\n"
                + "-> Ciudad de origen.";
        imprimirRuta(texto);

        this.add(panelOpciones, BorderLayout.EAST);
    }

    private void imprimirRuta(String texto) {
        // RutaDetallada = new JTextArea();
        RutaDetallada.setBounds(0, 10, 300, 400);
        RutaDetallada.setPreferredSize(new Dimension(250, 400));
        RutaDetallada.setText(texto);
        RutaDetallada.setFont(new Font("times new roman", Font.CENTER_BASELINE, 14));
        RutaDetallada.setForeground(Color.BLACK);
        // RutaDetallada.setEnabled(false);
        panelOpciones.add(RutaDetallada);
    }

    // private String StrtoHtml(String texto){
    // String cadena = texto.replace("\n", "<br>");
    // return "<html><p>" + cadena + "</p></html>";
    // }

}
