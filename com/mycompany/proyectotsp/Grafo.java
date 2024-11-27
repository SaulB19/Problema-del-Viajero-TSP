package com.mycompany.proyectotsp;

import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Grafo {
    // Parametros iniciales
    private final int alpha;
    private final int beta;
    private final double evaporacion;
    private final double minimaFeromona;
    private int ciudadesCantidad;
    private Map<String, Ccity> ciudades;// Contiene las ciudades del grafo
    private Map<String, double[]> posiciones = new HashMap<>(); // LLeva el seguimiento de las posiciones graficas de
                                                                // los nodos

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

    public Grafo(int alpha, int beta, double evaporacion, double minimaFeromona) {
        this.alpha = alpha;
        this.beta = beta;
        this.evaporacion = evaporacion;
        this.minimaFeromona = minimaFeromona;
        // ciudadesG=new HashMap<>();

        crearGrafo();
    }

    private void crearGrafo() {
        // TODO: Realizar el procesamiento del .json en Java y no en python
        try (BufferedReader reader = leerMatriz()) {
            String linea = reader.readLine();
            String[] encabezados = linea.split(",");

            ciudades = new HashMap<>();
            for (String ciudad : encabezados) {
                if (ciudad != "") {
                    ciudades.put(ciudad, new Ccity(ciudad));
                }
            }
            ciudadesCantidad = ciudades.size();

            for (int i = 0; i < encabezados.length - 1; i++) {
                linea = reader.readLine();

                String[] datosNodo = linea.split(",");

                Ccity origen = ciudades.get(datosNodo[0]);

                for (int j = 1; j < datosNodo.length; j++) {
                    Ccity destino = ciudades.get(encabezados[j]);

                    String[] datosEnlace = datosNodo[j].split(";");

                    float distancia = Float.parseFloat(datosEnlace[0]);
                    boolean hayEnlace = Boolean.parseBoolean(datosEnlace[1]);

                    origen.agregarConexion(destino, distancia, hayEnlace);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        inicializarPosiciones();
    }

    // TODO: Realizar el procesamiento del .json en Java y no en python
    private BufferedReader leerMatriz() {
        BufferedReader salida = null;

        try {
            // TODO: Usar la formula de Haversine para calcular la distancia en lugar del teorema de Pitagoras
            ProcessBuilder pb = new ProcessBuilder("python3", "com/mycompany/proyectotsp/leerJSON.py");

            Process process = pb.start();

            // Lee la salida estandar del script, que tiene formato de csv
            salida = process.inputReader();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return salida;
    }

    private void inicializarPosiciones() {
        String[] ciudades = {
                "Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado",
                "Connecticut", "Delaware", "District of Columbia", "Florida", "Georgia",
                "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky",
                "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota",
                "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire",
                "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota",
                "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina",
                "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington",
                "West Virginia", "Wisconsin", "Wyoming", "Puerto Rico",
        };

        // TODO: Reajustar las posiciones en pantalla de los nodos de las ciudades, o calcularlas dinamicamente
        double[][] coordenadas = {
                { 516, 548 }, { 92, 660 }, { 150, 484 }, { 430, 496 }, { 48, 372 },
                { 248, 366 }, { 676, 244 }, { 650, 326 }, { 628, 330 }, { 606, 672 },
                { 564, 534 }, { 224, 694 }, { 154, 218 }, { 466, 350 }, { 504, 340 },
                { 414, 292 }, { 344, 390 }, { 528, 404 }, { 434, 618 }, { 698, 124 },
                { 646, 344 }, { 678, 218 }, { 522, 250 }, { 396, 154 }, { 468, 572 },
                { 424, 394 }, { 206, 132 }, { 330, 300 }, { 104, 312 }, { 678, 182 },
                { 654, 296 }, { 238, 496 }, { 636, 224 }, { 604, 442 }, { 330, 130 },
                { 546, 320 }, { 356, 482 }, { 80, 168 }, { 610, 288 }, { 690, 234 },
                { 590, 490 }, { 324, 226 }, { 520, 454 }, { 318, 596 }, { 166, 338 },
                { 662, 174 }, { 610, 384 }, { 94, 82 }, { 574, 364 }, { 440, 212 },
                { 230, 250 }, { 758, 648 },
        };

        for (int i = 0; i < ciudades.length; i++) {
            posiciones.put(ciudades[i], coordenadas[i]);
        }
    }

    public Map<String, double[]> getPosiciones() {
        return this.posiciones;
    }

    /*
     * Actualiza las feromonas unicamente del mejor recorrido
     */
    public void actualizarferomonas(Hormiga hormiga) {
        Ccity puntoa = null;
        Ccity puntob = null;
        ArrayList<Ccity> ciudades = hormiga.getRuta();
        /*
         * Pseudocodigo
         * Aumentar la cantidad de feromonas de la siguiente manera:
         * "feromonas += 1 / costo_del_recorrido"
         */
        double feromonasAgregadas = 1 / hormiga.pesoRecorrido();
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
            double newFeromonaAalb = puntoa.getFeromonas(puntob) + feromonasAgregadas;
            // Actualiza las feromonas de A a B
            puntoa.setFeromonas(puntob, newFeromonaAalb);
            // Obtiene las feromonas de B a A y le suma la nueva cantidad
            double newFeromonasBalA = puntob.getFeromonas(puntoa) + feromonasAgregadas;
            // Actualiza las feromonas de B a a
            puntob.setFeromonas(puntoa, newFeromonasBalA);

        }

    }

    public void EvaporarFeromonas() {
        // Primero evaporo todas las feromonas y luego le sumo la sumatoria de cada
        // variacion
        // Evapora las feromonas para cada ciudad del grafo
        for (Ccity c : ciudades.values()) {
            for (Ccity temp : ciudades.values()) {
                if (!c.contiene(temp)) {
                    continue;
                }

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
}
