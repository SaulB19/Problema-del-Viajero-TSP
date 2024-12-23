package com.mycompany.proyectotsp;

import java.util.ArrayList;

public class ACO {
    private Grafo grafo; // Mapa que se va a seguir
    private int cantidadHormigas;// Se recomiendan las mimsmas que las ciudades
    private int repeticiones; // cuantas veces se va a hacer este proceso

    public ACO(int cantidadHormigas, int repeticiones, int alpha, int beta, double evaporacion, double minimaFeromona) {
        this.cantidadHormigas = cantidadHormigas;
        this.repeticiones = repeticiones;
        this.grafo = new Grafo(alpha, beta, evaporacion, minimaFeromona);
    }

    /*
     * Realiza el algoritmo para el TSP y regresa como resultado la mejor hormiga
     * que debe contener la mejor ruta
     */
    public Hormiga ACO() {
        Hormiga mejorHormiga = null;
        for (int i = 0; i < repeticiones; i++) {
            // paso 1 generar las hormigas que se van a lanzar
            Hormiga[] hormigas = new Hormiga[cantidadHormigas];
            for (int j = 0; j < cantidadHormigas; j++) {
                hormigas[j] = new Hormiga(grafo);// crea la hormiga
                hormigas[j].viajar();// hace viajar a la hormiga
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
