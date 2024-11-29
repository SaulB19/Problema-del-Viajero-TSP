package com.mycompany.proyectotsp;

import java.util.HashMap;

public class Ccity {
    // Es la respresentacion de los vertices (nodos)
    private String nombre;

    // Hashmap clave ciudad guarda distancias
    private HashMap<Ccity, Double> distancias;

    // Hashmap clave ciudad guarda feromonas desde esta ciudad
    private HashMap<Ccity, Double> feromonas;

    /**
     * Metodo Constructor
     */
    public Ccity(String nombre) {
        this.nombre = nombre;
        distancias = new HashMap<>();
        feromonas = new HashMap<>();
    }

    // --------------| Metodos Getter |-------------- //

    public double getPesoCiudad(Ccity newCity) {
        return distancias.get(newCity);
    }

    public int getCantidadCiudades() {
        return distancias.size();
    }

    public double getFeromonas(Ccity ciudad) {
        return feromonas.get(ciudad);
    }

    public HashMap<Ccity, Double> getDistancias() {
        return distancias;
    }

    public double getDistancia(Ccity destino) {
        return distancias.get(destino);
    }

    public String getNombre() {
        return nombre;
    }

    // --------------| Metodos Setter |-------------- //

    public void setFeromonas(Ccity newCity, double feromonas) {
        this.feromonas.replace(newCity, feromonas);
    }

    // -------------| Metodos Publicos |------------- //

    /**
     * Metodo que agrega una enlace desde esta ciudad a otra
     * 
     * @param newCity   La ciudad a enlazar
     * @param distancia La distancia entre estas
     */
    public void agregarEnlace(Ccity newCity, double distancia) {
        distancias.put(newCity, distancia);

        // Cada que agrega un nuevo enlace inicializa la ruta con un nivel de feromonas
        feromonas.put(newCity, 0.4);
    }

    /**
     * Metodo que nos dice si podemos ir a otra ciudad desde esta
     * 
     * @param newCity La ciudad a la que se quiere ir
     * @return true si las ciudades estan enlazadas, o false si no es asi
     */
    public boolean contiene(Ccity newCity) {
        return distancias.containsKey(newCity);
    }

    @Override
    public String toString() {
        return nombre;
    }
}
