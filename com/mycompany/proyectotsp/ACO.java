package com.mycompany.proyectotsp;

import java.util.ArrayList;

import javax.swing.JPanel;

public class ACO {
    // Mapa que se va a seguir
    private Grafo grafo;

    // Se recomiendan las mimsmas que las ciudades
    private int cantidadHormigas;

    // Usado para redibujar el grafo
    private JPanel panelMapa;

    // Booleano que indica cuando el usuario presiona 'Detener'
    private boolean detenerAlgoritmo = false;

    private Thread hilo;

    private Hormiga mejorHormiga = null;

    private String ciudadInicial = null; // Si es null, escoje la ciudad inicial aleatoriamente

    /**
     * Metodo Constructor
     */
    public ACO(Grafo grafo, int alpha, int beta, double evaporacion,
            double minimaFeromona, JPanel p) {

        this.grafo = grafo;
        cantidadHormigas = grafo.getCiudadesCantidad();
        panelMapa = p;

        grafo.crearGrafo(alpha, beta, evaporacion, minimaFeromona);

        hilo = new Thread(() -> {
            Hormiga[] hormigas = new Hormiga[cantidadHormigas];

            while (!detenerAlgoritmo) {
                for (int j = 0; j < cantidadHormigas; j++) {
                    hormigas[j] = new Hormiga(grafo, grafo.getCiudades().get(ciudadInicial));

                    // En caso de que la hormiga quede encerrada, disminuye las feromonas de su
                    // camino
                    if (!hormigas[j].viajar()) {
                        grafo.actualizarferomonas(hormigas[j]);
                    }
                }

                Hormiga mejorHormigaLocal = ObtenerMejorHormiga(hormigas);

                grafo.EvaporarFeromonas();

                // Aumenta las feromonas solo de la mejor hormiga
                grafo.actualizarferomonas(mejorHormigaLocal);

                panelMapa.repaint();

                if (mejorHormigaLocal != null &&
                        (mejorHormiga == null ||
                                mejorHormigaLocal.pesoRecorrido() < mejorHormiga.pesoRecorrido())) {

                    mejorHormiga = mejorHormigaLocal;
                }
            }
        });
    }

    // --------------| Metodos Getter |-------------- //

    public ArrayList<Ccity> getMejorRuta() {
        if (mejorHormiga == null) {
            return null;
        }

        return mejorHormiga.getRuta();
    }

    public Grafo getGrafo() {
        return grafo;
    }

    public int getCantidadHormigas() {
        return cantidadHormigas;
    }

    public JPanel getPanelMapa() {
        return panelMapa;
    }

    public String getCiudadInicial() {
        return ciudadInicial;
    }

    // --------------| Metodos Setter |-------------- //

    public void setGrafo(Grafo grafo) {
        this.grafo = grafo;
    }

    public void setCantidadHormigas(int cantidadHormigas) {
        this.cantidadHormigas = cantidadHormigas;
    }

    public void setPanelMapa(JPanel panelMapa) {
        this.panelMapa = panelMapa;
    }

    public void setCiudadInicial(String ciudadInicial) {
        this.ciudadInicial = ciudadInicial;
    }

    // -------------| Metodos Publicos |------------- //

    /**
     * Comienza la ejecucion del hilo del algoritmo
     */
    public void iniciar() {
        hilo.start();
    }

    /**
     * Termina la iteracion actual y finaliza la ejecucion
     */
    public void detener() {
        detenerAlgoritmo = true;
    }

    /**
     * Obtiene la mejor hormiga hasta el momento para actualizar las feromonas en su
     * ruta
     * 
     * @param hormigas Un arreglo de hormigas de donde elejir la mejor
     * @return La hormiga con el menor costo, o null si ninguna hormiga esta viva
     */
    public Hormiga ObtenerMejorHormiga(Hormiga[] hormigas) {
        Hormiga mejorHormiga = null;
        for (Hormiga hormiga : hormigas) {
            if (!hormiga.getEstaViva()) {
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

    /**
     * Obtiene la mejor hormiga hasta ahora y representa su ruta
     * 
     * @return Una cadena describiendo la ruta de la mejor hormiga
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
}
