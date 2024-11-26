package com.mycompany.proyectotsp;

import java.util.*;

public class Ccity implements Iterator<Ccity> {
    // Es la respresentacion de los vertices (nodos)
    private String nombre;
    // Hashmap clave ciudad guarda distancias
    private HashMap<Ccity, Double> distancias;
    // Hashmap clave ciudad guarda booleano que indica si las ciudades estan
    // conectadas
    private HashMap<Ccity, Boolean> enlacesExistentes;
    // Hashmap clave ciudad guarda feromonas desde esta ciudad
    private HashMap<Ccity, Double> feromonas;
    // Hashmap clave, ciudad con la que conecta
    // Guarda la suma de las nuevas porciones de feromonas para luego agregarselas a
    // las feromonas
    private HashMap<Ccity, Double> temporal;

    public Ccity(String nombre) {
        this.nombre = nombre;
        distancias = new HashMap<>();
        enlacesExistentes = new HashMap<>();
        feromonas = new HashMap<>();
        temporal = new HashMap<>();
    }

    // Metodo que agrega una conexion desde esta ciudad a otra (con el peso)
    public void agregarConexion(Ccity newCity, double distancia, boolean hayEnlace) {
        distancias.put(newCity, distancia);
        enlacesExistentes.put(newCity, hayEnlace);

        // Cada que agrega una nueva conexion inicializa la ruta con un nivel de
        // feromonas
        feromonas.put(newCity, 0.0354);
        // En temporal la suma hasta el momento es 0
        temporal.put(newCity, 0.0);
    }

    // Metodo que regresa el peso conociendo la ciudad
    public double GetpesoCiudad(Ccity newCity) {
        return distancias.get(newCity);
    }

    // Metodo que nos dice si podemos ir a otra ciudad desde esta
    public boolean contiene(Ccity newCity) {
        return enlacesExistentes.get(newCity);
    }

    public int CantidadCiudades() {
        return distancias.size();
    }

    public void setFeromonas(Ccity newCity, double feromonas) {
        this.feromonas.replace(newCity, feromonas);
    }

    public double getFeromonas(Ccity ciudad) {
        return feromonas.get(ciudad);
    }

    public void SetSumaFeromonas(Ccity newCity, double suma) {
        this.temporal.replace(newCity, suma);
    }

    public double GetSumaFeromonas(Ccity ciudad) {
        return temporal.get(ciudad);
    }

    public HashMap<Ccity, Boolean> getEnlaces() {
        return enlacesExistentes;
    }

    public double getDistancia(Ccity destino) {
        return distancias.get(destino);
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Ccity next() {
        return null;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
