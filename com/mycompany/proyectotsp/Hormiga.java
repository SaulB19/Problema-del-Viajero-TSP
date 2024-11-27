package com.mycompany.proyectotsp;

import java.awt.IllegalComponentStateException;

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

    public Hormiga(Grafo grafo, Ccity ciudadInicial) {
        this.grafo = grafo;
        inicio = ciudadInicial;

        ruta = new ArrayList<>();
        // yaFueoNo = new HashSet<>();
        noHaIdo = new ArrayList<>();

        crearCiudadesNoVisitadas();
    }

    public ArrayList<Ccity> getRuta() {
        return ruta;
    }

    // Agrega al arreglo todas las ciudades para saber cuales no han sido visitadas
    public void crearCiudadesNoVisitadas() {

        // Si no hay ninguna ciudad inicial especifica, agrega todos los nodos y
        // verifica que todos esten conectados
        if (inicio == null) {
            noHaIdo = explorarNodos(VerticeRandom());

            for (Ccity ciudad : grafo.getCiudades().values()) {
                if (!noHaIdo.contains(ciudad)) {
                    // TODO: Cambiar esta excepcion a algo mas adecuado
                    throw new IllegalComponentStateException(
                            "Se debe agregar una ciudad inicial si todos los nodos no estan interconectados");
                }
            }

            return;
        }

        noHaIdo = explorarNodos(inicio);
    }

    private ArrayList<Ccity> explorarNodos(Ccity inicial) {
        ArrayList<Ccity> nodosEncontrados = new ArrayList<>();
        nodosEncontrados.add(inicial);

        HashMap<Ccity, Boolean> yaExplorados = new HashMap<>();
        for (Ccity ccity : grafo.getCiudades().values()) {
            yaExplorados.put(ccity, false);
        }
        yaExplorados.put(inicial, true);

        Stack<Ccity> pila = new Stack<>();
        pila.push(inicial);

        while (!pila.isEmpty()) {
            Ccity actual = pila.pop();

            for (Map.Entry<Ccity, Boolean> item : actual.getEnlaces().entrySet()) {
                Ccity siguiente = item.getKey();
                boolean estanEnlazadas = item.getValue();

                if (!estanEnlazadas || yaExplorados.get(siguiente)) {
                    continue;
                }

                pila.push(siguiente);
                yaExplorados.put(siguiente, true);
                nodosEncontrados.add(siguiente);
            }

        }

        return nodosEncontrados;
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
     */
    public void viajar() {
        if (inicio == null)
            inicio = VerticeRandom();

        noHaIdo.remove(inicio);// quita de las que faltan por visitar
        Ccity actual = inicio;
        /*
         * Cuando no ha ido esta vacio significa que ya se han recorrido todas las
         * ciudades
         */

        // TODO: (imporate) Resolver el problema de los callejones sin salida
        // Las hormigas no pueden pasar 2 veces por el mismo nodo. Si una hormiga llega
        // a un nodo cuyos enlaces ya han sido todos explorados anteriormente, la
        // hormiga ya no puede moverse a ningun otro nodo. 
        while (!noHaIdo.isEmpty()) {// siempre que no ha ido no este vacia
            double random = Math.random();// genera un numero del 0 al 1

            Map<Ccity, Double> probabilidades = probabilidadDeIr(actual);

            for (Map.Entry<Ccity, Double> probabilidad : probabilidades.entrySet()) {
                double sigCiudad = probabilidad.getValue();

                if (random < sigCiudad) {
                    actual = probabilidad.getKey(); // se mueve a la siguiente ciudad
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
    public HashMap<Ccity, Double> probabilidadDeIr(Ccity actual) {
        HashMap<Ccity, Double> probabilidades = new HashMap<>();

        double acumulado = 0;

        double sumatoriaDeseos = sumaDeseos(actual);
        for (Ccity siguiente : noHaIdo) {
            if (actual.estaEnlazadaCon(siguiente)) {
                double probabilidad = deseo(actual, siguiente) / sumatoriaDeseos;
                probabilidades.put(siguiente, probabilidad + acumulado);

                acumulado += probabilidad;
            }
        }

        return probabilidades;
    }

    // Obtiene la suma de los deseos (denominador)
    public double sumaDeseos(Ccity actual) {
        double suma = 0;
        for (Ccity siguiente : noHaIdo) {
            if (actual.estaEnlazadaCon(siguiente)) {
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

}
