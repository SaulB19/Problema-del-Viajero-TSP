package com.mycompany.proyectotsp;

import java.util.ArrayList;

import javax.swing.JPanel;

public class ACO {
    private Grafo grafo; // Mapa que se va a seguir
    private int cantidadHormigas;// Se recomiendan las mimsmas que las ciudades
    private int repeticiones; // cuantas veces se va a haer este proceso

    private JPanel panelMapa;

    // TODO: Cambiar esto para que el usuario eliga su ciudad inicial, o null en
    // caso de no haber
    private String ciudadInicial = "District of Columbia"; // Si es null, escoje la ciudad inicial aleatoriamente para
                                                           // cada hormiga

    public ACO(Grafo grafo, int cantidadHormigas, int repeticiones, int alpha, int beta, double evaporacion,
            double minimaFeromona, JPanel p) {
        this.cantidadHormigas = cantidadHormigas;
        this.repeticiones = repeticiones;
        this.grafo = grafo;
        panelMapa = p;
        grafo.crearGrafo(alpha, beta, evaporacion, minimaFeromona);
    }

    /*
     * Realiza el algoritmo para el TSP y regresa como resultado la mejor hormiga
     * que debe contener la mejor ruta
     */
    public Hormiga ACO() {
        Hormiga mejorHormiga = null;
        for (int i = 0; i < repeticiones; i++) {
            panelMapa.repaint();
            // paso 1 generar las hormigas que se van a lanzar
            Hormiga[] hormigas = new Hormiga[cantidadHormigas];
            for (int j = 0; j < cantidadHormigas; j++) {
                hormigas[j] = new Hormiga(grafo, grafo.getCiudades().get(ciudadInicial));// crea la hormiga
                if (!hormigas[j].viajar()) { // hace viajar a la hormiga
                    grafo.actualizarferomonas(hormigas[j]); // Disminuye las feromonas de este recorrido
                }
            }
            mejorHormiga = ObtenerMejorHormiga(hormigas);
            grafo.EvaporarFeromonas();// Evapora las feromonas
            grafo.actualizarferomonas(mejorHormiga);// Actualiza las feromonas de la mejor hormiga
        }
        return mejorHormiga;
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
    public void imprimirMejorRuta(Hormiga hormiga) {
        if (hormiga == null) {
            System.out.println("Ninguna hormiga recorrio el mapa completo");
            return;
        }
        ArrayList<Ccity> ruta = hormiga.getRuta();
        System.out.println("La ruta mas corta encontrada es:");
        for (Ccity ciudad : ruta) {
            System.out.print(ciudad);// imprime la ciudad
            System.out.print("-->");
        }
        System.out.print(ruta.get(0));
        System.out.println("\n El peso del recorrido es: " + hormiga.pesoRecorrido());
    }
}
