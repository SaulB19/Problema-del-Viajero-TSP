/* En esta ventana habrá una introducción para que el usuario escoja las opciones del programa*/

package com.mycompany.proyectotsp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import java.awt.*;
import javax.swing.*;

import java.awt.image.BufferedImage;

public class VentanaBienvenida extends JFrame {

    private Grafo grafo;
    private BufferedImage fondo;

    public VentanaBienvenida() {

        setTitle("Visualización de Grafo");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new GrafoPanel();

        add(panel);

        grafo = new Grafo(1, 1, 0.5, 0.5);
    }

    public BufferedReader ejecutarPython() {
        BufferedReader salida = null;

        try {
            // Construye el comando para ejecutar el script
            ProcessBuilder pb = new ProcessBuilder("python3", "com/mycompany/proyectotsp/leerJSON.py");

            Process process = pb.start();
            // process.waitFor();

            // Lee la salida estándar del script
            salida = process.inputReader();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return salida;
    }

    private void cargarFondo() {
        try {
            fondo = javax.imageio.ImageIO.read(new File("imagenes/mapaEUA.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class GrafoPanel extends JPanel {
        public GrafoPanel() {
            // cargarFondo();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            if (fondo != null) {
                g2.drawImage(fondo, 0, 0, 800, 800, null);
            }

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

                for (Map.Entry<Ccity, Boolean> enlace : nodoOrigen.getEnlaces().entrySet()) {
                    // Dibujar solo los enlaces existentes en la matriz de adyacencia
                    if (!enlace.getValue()) {
                        continue;
                    }

                    Ccity destino = enlace.getKey();
                    double[] destinoCoordenadas = grafo.getPosiciones().get(destino.getNombre());

                    g.drawLine((int) origenCoordenadas[0], (int) origenCoordenadas[1], (int) destinoCoordenadas[0],
                            (int) destinoCoordenadas[1]);

                    double mitadX = (origenCoordenadas[0] + destinoCoordenadas[0]) / 2;
                    double mitadY = (origenCoordenadas[1] + destinoCoordenadas[1]) / 2;
                    g.drawString(String.format("%.2f", nodoOrigen.getDistancia(destino)), (int) mitadX, (int) mitadY);
                }
            }
        }
    }
}
