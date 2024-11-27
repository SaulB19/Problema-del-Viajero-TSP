package com.mycompany.proyectotsp;

import java.util.*;

public class Hormiga {
    /*
     * La variable ruta guarda las ciudades en el orden que se visitaron
     * NO repite las ciudades por lo que despues de la ultima ciudad
     * se debe agregar el peso de la primera
     */private ArrayList<Ccity> ruta;
    // Usado para indicar el grafo que recorre
    private Grafo grafo;
    private ArrayList<Ccity> noHaIdo;

    public Hormiga(Grafo grafo) {
        this.grafo = grafo;
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
            peso += a.GetpesoCiudad(b);
        }
        return peso;
    }

    /*
     * Obtiene el recorrido de la hormiga basandose en las probabilidades
     */
    public void viajar() {
        Ccity inicio = VerticeRandom();
        ruta.add(inicio);
        noHaIdo.remove(inicio);// quita de las que faltan por visitar
        Ccity actual = inicio;
        /*
         * Cuando no ha ido esta vacio significa que ya se han recorrido todas las
         * ciudades
         */
        while (!noHaIdo.isEmpty()) {// siempre que no ha ido no este vacia
            double random = Math.random();// genera un numero del 0 al 1
            ArrayList<Par<Ccity, Double>> probabilidades = probabilidadDeIr(actual);
            for (int i = 0; i < probabilidades.size(); i++) {
                double sigCiudad = probabilidades.get(i).valor;
                if (random < sigCiudad) {
                    actual = probabilidades.get(i).ciudad; // se mueve a la siguiente ciudad
                    noHaIdo.remove(actual);
                    ruta.add(actual);
                    break; // sale del ciclo for y vuelve al while
                }
            }

        }

    }

    /*
     * probabilidades es un metodo que almacena en un hashmap la probabilidad de ir
     * a cada ciudad
     * La ciudad es la clave (para poder obtenerla) y la probabilidad es el dato
     */
    public ArrayList<Par<Ccity, Double>> probabilidadDeIr(Ccity actual) {
        ArrayList<Par<Ccity, Double>> probabilidades = new ArrayList<>();
        int valorAObtener = 0;
        for (Ccity siguiente : grafo.getCiudades().values()) {
            // si no ha ido contiene a la ciudad, significa que no hemos ido y podemos
            // agregarla
            if (noHaIdo.contains(siguiente)) {
                double sumar;
                if (valorAObtener == 0) {
                    double probabilidad = ((deseo(actual, siguiente)) / sumaDeseos(actual));
                    Par<Ccity, Double> par = new Par<>();
                    par.ciudad = siguiente;
                    par.valor = probabilidad;
                    probabilidades.add(par);
                } else {
                    sumar = probabilidades.get(valorAObtener - 1).valor;
                    double probabilidad = ((deseo(actual, siguiente)) / sumaDeseos(actual)) + sumar;
                    Par<Ccity, Double> par = new Par<>();
                    par.ciudad = siguiente;
                    par.valor = probabilidad;
                    probabilidades.add(par);
                }
                valorAObtener++;
            }
        }
        return probabilidades;
    }

    // Obtiene la suma de los deseos (denominador)
    public double sumaDeseos(Ccity actual) {
        double suma = 0;
        for (Ccity siguiente : noHaIdo) {
            suma += deseo(actual, siguiente);
        }
        return suma;
    }

    // Obtiene el numerador (deseo) de la hormiga de ir de una ciudad a otra
    public double deseo(Ccity actual, Ccity siguiente) {
        double feromonas = actual.getFeromonas(siguiente);
        double distancia = (double) 1 / actual.GetpesoCiudad(siguiente);
        feromonas = Math.pow(feromonas, grafo.getAlpha());
        distancia = Math.pow(distancia, grafo.getBeta());
        return feromonas * distancia;
    }

    public class Par<E, T> {
        E ciudad;
        T valor;
    }

}
