package com.mycompany.proyectotsp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Grafo {
    // Parametros iniciales
    private int alpha;
    private int beta;
    private double evaporacion;
    private double minimaFeromona;
    private int ciudadesCantidad;
    private Map<String, Ccity> ciudades;// Contiene las ciudades del grafo

    /**
     * Metodo constructor
     */
    public Grafo() {
        ciudades = new HashMap<>();

        // La cadena debe de ser el nombre del archivo como lo tengan guardado
        // Se pone la cadena como parametro de FileReader
        try (BufferedReader bf = new BufferedReader(new FileReader("matriz_adyacencia_estados1.csv"))) {
            String linea;
            String[] ciudadesName = new String[53];
            // IMPORTANTE la posicion 0 de ciudades name no tiene nada
            linea = bf.readLine();
            ciudadesName = linea.split(",");
            ArrayList<Ccity> citiesList = new ArrayList<>();
            // La posicion 0 de citiesList no tiene una ciudad
            for (String nombre : ciudadesName) {
                Ccity city = new Ccity(nombre);
                citiesList.add(city);
                if (nombre == "") {
                    continue;
                }
                ciudades.put(nombre, city);
            }

            ciudadesCantidad = citiesList.size();

            String linea2;
            while ((linea2 = bf.readLine()) != null) {
                String[] conexiones = linea2.split(",");
                Ccity ciudad = ciudades.get(conexiones[0]);
                for (int i = 1; i < conexiones.length; i++) {
                    double peso = Double.parseDouble(conexiones[i]);
                    if (peso != 0) {
                        Ccity conexion = citiesList.get(i);
                        ciudad.agregarEnlace(conexion, peso);
                    }
                }

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // --------------| Metodos Getter |-------------- //

    public int getAlpha() {
        return alpha;
    }

    public int getBeta() {
        return beta;
    }

    public double getEvaporacion() {
        return evaporacion;
    }

    public Map<String, Ccity> getCiudades() {
        return ciudades;
    }

    public int getCiudadesCantidad() {
        return ciudadesCantidad;
    }

    // -------------| Metodos Publicos |------------- //

    /**
     * Establece los valores relevantes para el algoritmo ACO
     * 
     * @param alpha          Influencia de las feromonas en la probabilidad
     * @param beta           Influencia del valor heuristico en la probabilidad
     * @param evaporacion    Valor entre 0 y 1 aplicado a cada iteracion
     * @param minimaFeromona Feromonas minimas al iniciar el grafo
     */
    public void crearGrafo(int alpha, int beta, double evaporacion, double minimaFeromona) {
        this.alpha = alpha;
        this.beta = beta;
        this.evaporacion = evaporacion;
        this.minimaFeromona = minimaFeromona;
    }

    /**
     * Actualiza las feromonas unicamente del mejor recorrido, o disminuye las de un
     * camino si salida
     * 
     * @param hormiga Una hormiga viva para aumentar las feromonas de su camino, de
     *                lo contrario disminuye las feromonas
     */
    public void actualizarferomonas(Hormiga hormiga) {
        if (hormiga == null) {
            return;
        }

        Ccity puntoa = null;
        Ccity puntob = null;
        ArrayList<Ccity> ciudades = hormiga.getRuta();
        if (!hormiga.getEstaViva()) {
            Ccity anterior = null;

            for (Ccity ciudad : hormiga.getRuta()) {
                if (anterior == null) {
                    anterior = ciudad;
                    continue;
                }

                double feromonas = anterior.getFeromonas(ciudad);
                feromonas *= 0.9992;

                if (feromonas < minimaFeromona) {
                    anterior.setFeromonas(ciudad, minimaFeromona);
                } else {
                    anterior.setFeromonas(ciudad, feromonas);
                }

                anterior = ciudad;
            }

            return;
        }
        /*
         * Pseudocodigo
         * Aumentar la cantidad de feromonas de la siguiente manera:
         * "feromonas += 1 / costo_del_recorrido"
         */
        double feromonasAgregadas = 10 / hormiga.pesoRecorrido();
        for (int i = 0; i < ciudades.size(); i++) {
            // Va regresando la ciudad y su siguiente
            puntoa = ciudades.get(i);
            try { // Cuando se llega a la ultima ciudad la siguiente es null
                puntob = ciudades.get(i + 1);
            } catch (Exception e) { // Evitamos error por null y regresamos la ciudad original
                puntob = ciudades.get(0);
            }
            /*
             * Se tienen que actualizar las feromonas de A a B y B a A porque son un
             * mismo camino
             */
            // obtiene las feromonas que hay de A a B y le suma la nueva cantidad
            double newFeromona = puntoa.getFeromonas(puntob) + feromonasAgregadas;
            // Actualiza las feromonas de A a B
            puntoa.setFeromonas(puntob, newFeromona);
            // Actualiza las feromonas de B a a
            puntob.setFeromonas(puntoa, newFeromona);

        }

    }

    /**
     * Metodo para aplicar el coeficiente de evaporacion a todos los enlaces del
     * grafo
     */
    public void EvaporarFeromonas() {
        // Primero evaporo todas las feromonas y luego le sumo la sumatoria de cada
        // variacion
        // Evapora las feromonas para cada ciudad del grafo
        for (Ccity c : ciudades.values()) {
            for (Ccity temp : c.getDistancias().keySet()) {

                double newFeromona = c.getFeromonas(temp) * (1 - evaporacion);
                /*
                 * Se asegura de que las feromonas no disminuyan de un cierto minimo
                 * Para que siempre haya una posibiliadd de que se elija el camino
                 */
                if (newFeromona < minimaFeromona) {
                    c.setFeromonas(temp, minimaFeromona);
                } else {
                    c.setFeromonas(temp, newFeromona);
                }
            }
        }
    }

    /**
     * Metodo para reinicializar las feromonas al mismo valor
     */
    public void reiniciarFeromonas() {
        for (Ccity origen : ciudades.values()) {
            for (Ccity destino : origen.getDistancias().keySet()) {
                origen.setFeromonas(destino, 0.4);
            }
        }
    }
}