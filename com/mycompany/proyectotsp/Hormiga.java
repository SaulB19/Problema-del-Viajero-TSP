package com.mycompany.proyectotsp;

import java.util.*;

public class Hormiga {
    /*
     * La variable ruta guarda las ciudades en el orden que se visitaron
     * NO repite las ciudades por lo que despues de la ultima ciudad
     * se debe agregar el peso de la primera
     */
    private ArrayList<Ccity> ruta;
    // Usado para indicar el grafo que recorre
    private Grafo grafo;
    private ArrayList<Ccity> noHaIdo;

    private Ccity inicio;

    private boolean estaViva;

    public Hormiga(Grafo grafo, Ccity ciudadInicial) {
        this.grafo = grafo;
        estaViva = true;
        inicio = ciudadInicial;

        ruta = new ArrayList<>();
        // yaFueoNo = new HashSet<>();
        noHaIdo = new ArrayList<>();

        CiudadesNoVisitadas();
    }

    public ArrayList<Ccity> getRuta() {
        return ruta;
    }

    // Agrega al arreglo todas las ciudades para saber cuales no han sido visitadas
    public void CiudadesNoVisitadas() {
        for (Ccity ciudad : grafo.getCiudades().values()) {
            noHaIdo.add(ciudad);
        }
    }

    // Este metodo sirve para elegir la ciudad de inicio de la hormiga
    public Ccity VerticeRandom() {
        Ccity c = null;
        int r;
        do {
            r = new Random().nextInt(grafo.getCiudadesCantidad());
        } while (r == 0);// Si r =0 obtiene otro numero

        Iterator<Ccity> it = grafo.getCiudades().values().iterator();
        for (int i = 0; i < r; i++) {
            c = it.next();
        }
        return c;
    }

    // Calcula el peso de la ruta hecha por la hormiga
    /*
     * El recorrido son todas las ciudades, cuando llega a la ciudad final e
     * intenta obtener la siguiente, no puede porque la siguiente es el inicio.
     * Por eso obtiene la ciudad del inicio
     */
    public double pesoRecorrido() {
        double peso = 0;
        Ccity a;
        Ccity b = null;
        for (int i = 0; i < ruta.size(); i++) {
            a = ruta.get(i);
            try {
                b = ruta.get(i + 1);
            } catch (Exception e) {
                b = ruta.get(0);
            }
            peso += a.getDistancia(b);
        }
        return peso;
    }

    /*
     * Obtiene el recorrido de la hormiga basandose en las probabilidades
     * Regresa true si logra finalizar el recorrido, de lo contrario regresa false
     */
    public boolean viajar() {
        if (inicio == null)
            inicio = VerticeRandom();

        noHaIdo.remove(inicio);// quita de las que faltan por visitar
        Ccity actual = inicio;
        ruta.add(inicio);
        /*
         * Cuando no ha ido esta vacio significa que ya se han recorrido todas las
         * ciudades
         */
        while (!noHaIdo.isEmpty()) {// siempre que no ha ido no este vacia
            double random = Math.random();// genera un numero del 0 al 1

            ArrayList<Par> probabilidades = probabilidadDeIr(actual);

            if (probabilidades.isEmpty()) {
                estaViva = false;
                return false;
            }

            for (Par probabilidad : probabilidades) {
                double sigCiudad = probabilidad.probabilidad;

                if (random < sigCiudad) {
                    actual = probabilidad.ciudad; // se mueve a la siguiente ciudad
                    noHaIdo.remove(actual);
                    ruta.add(actual);
                    break; // sale del ciclo for y vuelve al while
                }
            }
        }

        Ccity ultima = ruta.get(ruta.size() - 1);

        estaViva = ultima.contiene(inicio);
        return estaViva;
    }

    /*
     * probabilidades es un metodo que almacena en un hashmap la probabilidad de ir
     * a cada ciudad
     * La ciudad es la clave (para poder obtenerla) y la probabilidad es el dato
     */
    class Par {
        public Ccity ciudad;
        public double probabilidad;

        public Par(Ccity ciudad, double probabilidad) {
            this.ciudad = ciudad;
            this.probabilidad = probabilidad;
        }
    }

    public ArrayList<Par> probabilidadDeIr(Ccity actual) {
        ArrayList<Par> probabilidades = new ArrayList<>();

        double acumulado = 0;

        double sumatoriaDeseos = sumaDeseos(actual);
        for (Ccity siguiente : noHaIdo) {
            if (actual.contiene(siguiente)) {
                double probabilidad = deseo(actual, siguiente) / sumatoriaDeseos;
                probabilidades.add(new Par(siguiente, probabilidad + acumulado));

                acumulado += probabilidad;
            }
        }

        return probabilidades;
    }

    // Obtiene la suma de los deseos (denominador)
    public double sumaDeseos(Ccity actual) {
        double suma = 0;
        for (Ccity siguiente : noHaIdo) {
            if (actual.contiene(siguiente)) {
                suma += deseo(actual, siguiente);
            }
        }
        return suma;
    }

    // Obtiene el numerador (deseo) de la hormiga de ir de una ciudad a otra
    public double deseo(Ccity actual, Ccity siguiente) {
        double feromonas = actual.getFeromonas(siguiente);
        double valorHeuristico = 1 / actual.getDistancia(siguiente);

        feromonas = Math.pow(feromonas, grafo.getAlpha());
        valorHeuristico = Math.pow(valorHeuristico, grafo.getBeta());

        return feromonas * valorHeuristico;
    }

    public boolean estaViva() {
        return estaViva;
    }

}