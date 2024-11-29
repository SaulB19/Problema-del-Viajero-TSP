package com.mycompany.proyectotsp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.*;

public class VentanaMapa extends JFrame {
    private JPanel panelMapa, panelOpciones;
    private JComboBox<String> MenuCiudades;
    private JButton botonRuta;
    private JButton botonDetener;
    private JTextArea RutaDetallada = new JTextArea();
    private JScrollPane cajaTexto = new JScrollPane(RutaDetallada);
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

                int anchoActual = getWidth();
                int altoActual = getHeight();

                for (Map.Entry<String, double[]> entry : grafo.getPosiciones().entrySet()) {
                    String nodo = entry.getKey();
                    double[] coordenadasRelativas = entry.getValue();

                    // Convertir coordenadas relativas a absolutas
                    int x = (int) (coordenadasRelativas[0] * anchoActual);
                    int y = (int) (coordenadasRelativas[1] * altoActual);

                    // Dibujar el nodo
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
                    double[] origenCoordenadasRelativas = grafo.getPosiciones().get(origen);

                    for (Map.Entry<Ccity, Double> enlace : nodoOrigen.getDistancias().entrySet()) {
                        Ccity destino = enlace.getKey();
                        double[] destinoCoordenadasRelativas = grafo.getPosiciones().get(destino.getNombre());

                        // Convertir coordenadas relativas a absolutas
                        int x1 = (int) (origenCoordenadasRelativas[0] * anchoActual);
                        int y1 = (int) (origenCoordenadasRelativas[1] * altoActual);
                        int x2 = (int) (destinoCoordenadasRelativas[0] * anchoActual);
                        int y2 = (int) (destinoCoordenadasRelativas[1] * altoActual);

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
                    double[] anteriorCoordenadas = grafo.getPosiciones().get(anterior.getNombre());
                    double[] ciudadCoordenadas = grafo.getPosiciones().get(ciudad.getNombre());

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
                aco = new ACO(grafo, 50, 1, 9, 0.01, 0.05, panelMapa);
                grafo.reiniciarFeromonas();
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
                imprimirRuta("Ciudad de origen: " + aco.getCiudadInicial() + "\n" + texto);

                botonRuta.setText("Recalcular ruta");
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
