/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_integradora;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Clase encargada de gestionar la importación y exportación del inventario de la flota
 * utilizando el formato JSON. Trabaja manteniendo en memoria las estructuras de datos
 * necesarias para un acceso secuencial y de búsqueda directa rápida.
 *
 * @author vicen
 */
public class GestorInventarioJSON {

    /**
     * Lista que mantiene el orden secuencial de los vehículos de la flota.
     */
    private ArrayList<Vehiculo> listaVehiculos;

    /**
     * Mapa para indexar los vehículos por su matrícula y permitir búsquedas directas.
     */
    private HashMap<String, Vehiculo> mapaVehiculo;

    /**
     * Objeto Gson configurado para procesar y formatear los datos JSON.
     */
    private Gson gson;

    /**
     * Constructor por defecto.
     * Inicializa las colecciones en memoria completamente vacías y configura el
     * objeto Gson para que la salida del archivo JSON tenga un formato legible (pretty printing).
     */
    public GestorInventarioJSON() {
        this.listaVehiculos = new ArrayList<>();
        this.mapaVehiculo = new HashMap<>();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Exporta la lista actual de vehículos en memoria a un archivo físico en formato JSON.
     * Convierte la colección entera y escribe los datos en la ruta "datos/json/flota.json".
     * Gestiona la apertura y el cierre del flujo de escritura automáticamente.
     */
    public void exportarDatosJson() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("datos/json/flota.json"))) {
            gson.toJson(listaVehiculos, bw);
        } catch (IOException e) {
            System.out.println("Error inesperado: " + e.getLocalizedMessage());
        }
    }

    /**
     * Importa los datos de los vehículos desde el archivo JSON físico hacia la memoria.
     * Lee el archivo "datos/json/flota.json", reconstruye los objetos y los inyecta
     * simultáneamente en el ArrayList y en el HashMap.
     * Antes de volcar los datos nuevos, se asegura de limpiar las colecciones actuales
     * para evitar que se duplique la información.
     */
    public void importarDatosJson() {
        try (BufferedReader br = new BufferedReader(new FileReader("datos/json/flota.json"))) {
            Vehiculo[] arrayVehiculos = gson.fromJson(br, Vehiculo[].class);

            listaVehiculos.clear();
            mapaVehiculo.clear();

            // Protegemos el bucle por si el archivo estaba vacío y devuelve null
            if (arrayVehiculos != null) {
                for (Vehiculo vehiculo : arrayVehiculos) {
                    listaVehiculos.add(vehiculo);
                    mapaVehiculo.put(vehiculo.getMatricula(), vehiculo);
                }
            }
        } catch (IOException e) {
            System.out.println("Error inesperado: " + e.getLocalizedMessage());
        }
    }
}