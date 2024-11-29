package com.mycompany.proyectotsp;

import java.util.ArrayList;

import javax.swing.JPanel;

public class ACO {
    private Grafo grafo; // Mapa que se va a seguir
    private int cantidadHormigas;// Se recomiendan las mimsmas que las ciudades

    private JPanel panelMapa;

    private boolean pararAlgoritmo = false;

    private Thread hilo;

    Hormiga mejorHormiga = null;

    // TODO: Cambiar esto para que el usuario eliga su ciudad inicial, o null en
    // caso de no haber
    private String ciudadInicial = null; // Si es null, escoje la ciudad inicial aleatoriamente para
                                         // cada hormiga

    public ACO(Grafo grafo, int cantidadHormigas, int alpha, int beta, double evaporacion,
            double minimaFeromona, JPanel p) {
        this.cantidadHormigas = cantidadHormigas;
        this.grafo = grafo;
        panelMapa = p;
        grafo.crearGrafo(alpha, beta, evaporacion, minimaFeromona);

        hilo = new Thread(() -> { // Correr en un hilo separado
            while (!pararAlgoritmo) {
                // paso 1: generar las hormigas que se van a lanzar
                Hormiga[] hormigas = new Hormiga[cantidadHormigas];
                for (int j = 0; j < cantidadHormigas; j++) {
                    hormigas[j] = new Hormiga(grafo, grafo.getCiudades().get(ciudadInicial)); // crea la hormiga
                    if (!hormigas[j].viajar()) { // hace viajar a la hormiga
                        grafo.actualizarferomonas(hormigas[j]); // Disminuye las feromonas de los recorridos sin salida
                    }
                }

                // paso 2: encontrar la mejor hormiga
                mejorHormiga = ObtenerMejorHormiga(hormigas);

                // paso 3: evaporar y actualizar feromonas
                grafo.EvaporarFeromonas(); // Evapora las feromonas
                grafo.actualizarferomonas(mejorHormiga); // Actualiza las feromonas de la mejor hormiga

                panelMapa.repaint();
            }
        });
    }

    /*
     * Obtiene la mejor hormiga hasta el momento para actualizar las feromonas en su
     * ruta
     */
    public Hormiga ObtenerMejorHormiga(Hormiga[] hormigas) {
        Hormiga mejorHormiga = null;
        for (Hormiga hormiga : hormigas) {
            if (!hormiga.estaViva()) {
                continue;
            }
            if (mejorHormiga == null) {
                mejorHormiga = hormiga;
            }
            if (hormiga.pesoRecorrido() < mejorHormiga.pesoRecorrido()) {
                mejorHormiga = hormiga;
            }
        }
        return mejorHormiga;
    }

    /*
     * public void ActualizarFeromonas(Hormiga mejorHormiga){
     * grafo.EvaporarFeromonas();
     * }
     */
    public String imprimirMejorRuta() {
        if (mejorHormiga == null) {
            return "Ninguna hormiga recorrio el mapa completo";
        }

        String retorno = "";

        ArrayList<Ccity> ruta = mejorHormiga.getRuta();
        retorno += "La ruta mas corta encontrada es:\n";
        for (Ccity ciudad : ruta) {
            retorno += ciudad.toString();
            retorno += "-->";
        }
        retorno += ruta.get(0).toString() + "\n";
        retorno += "El peso del recorrido es: " + mejorHormiga.pesoRecorrido();

        return retorno;
    }

    public Grafo getGrafo() {
        return grafo;
    }

    public void setGrafo(Grafo grafo) {
        this.grafo = grafo;
    }

    public int getCantidadHormigas() {
        return cantidadHormigas;
    }

    public void setCantidadHormigas(int cantidadHormigas) {
        this.cantidadHormigas = cantidadHormigas;
    }

    public JPanel getPanelMapa() {
        return panelMapa;
    }

    public void setPanelMapa(JPanel panelMapa) {
        this.panelMapa = panelMapa;
    }

    public String getCiudadInicial() {
        return ciudadInicial;
    }

    public void setCiudadInicial(String ciudadInicial) {
        this.ciudadInicial = ciudadInicial;
    }

    public void iniciar() {
        hilo.start();
    }

    public void detener() {
        pararAlgoritmo = true;
    }

}
