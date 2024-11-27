/* Instrucciones para declarar las diferentes ventanas y el algoritmo para las rutas*/

package com.mycompany.proyectotsp;

/* AUTORES:
    Bautista Cerón Saúl Rodrigo
    Campos Téllez Diego Arturo
    Contreras Domínguez Saúl
    Fernández Chan Karla Sofía
    Fragoso Siliceo Edgar
    Moncada Mompala Luis Alejandro
    Mejía Mérida Emili Ximena
    Pantoja Martínez Hugo
    Valles Hernández Ricardo Arturo
    Velázquez Argáez Carlos Andrés Emmanuel
    Zamora Uceda Paulo Román
    Zavala Mayorga ángel */

public class ProyectoTSP {
    public static void main(String[] args) {

        // Este es nuestro método main, aquí solamente tendrá que crear
        // un objeto del tipo VentanaMenu y este nos dirigirá después al
        // juego por medio de la interfaz gráfica

        VentanaBienvenida bienvenida = new VentanaBienvenida();
        bienvenida.setVisible(true);

        // TODO: Reubicar este codigo de manera que se ejecute cuando el usuario presione 'Calcular ruta'
        ACO aco = new ACO(5, 2, 1, 1, 0.64, 0.005);
        Hormiga mejorHormiga = aco.ACO();
        aco.imprimirMejorRuta(mejorHormiga);

    }
}
