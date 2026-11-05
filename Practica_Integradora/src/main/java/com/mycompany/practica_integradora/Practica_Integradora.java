/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.practica_integradora;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal que orquesta la ejecución del Sistema de Gestión de Flotas.
 * Se encarga de inicializar la estructura de directorios necesaria mediante NIO.2
 * y de simular el flujo de trabajo ejecutando todos los módulos de persistencia.
 *
 * @author vicen
 */
public class Practica_Integradora {

    /**
     * Método principal (punto de entrada) de la aplicación.
     *
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
    public static void main(String[] args) {

        // ==========================================
        // 1. CREACIÓN DE DIRECTORIOS (Módulo 6)
        // ==========================================
        List<String> rutas = new ArrayList<>();
        rutas.add("datos/config");
        rutas.add("datos/json");
        rutas.add("datos/backup");
        rutas.add("datos/reports");

        try {
            for (String ruta : rutas) {
                Path directorio = Paths.get(ruta);
                Files.createDirectories(directorio);
            }
            System.out.println("Estructura de directorios inicializada correctamente.");
        } catch (IOException e) {
            System.out.println("Error al crear los directorios: " + e.getLocalizedMessage());
        }

        // ==========================================
        // 2. CREACIÓN DE DATOS DE PRUEBA
        // ==========================================
        ArrayList<Vehiculo> flotaPrueba = new ArrayList<>();
        Vehiculo v1 = new Vehiculo("1111AAA", Marca.Seat, 2020, 45000, CategoriaMantenimiento.Mecanica);
        Vehiculo v2 = new Vehiculo("2222BBB", Marca.Mercedes, 2023, 15000, CategoriaMantenimiento.Electrica);
        Vehiculo v3 = new Vehiculo("3333CCC", Marca.Ford, 2018, 120000, CategoriaMantenimiento.Neumatica);

        flotaPrueba.add(v1);
        flotaPrueba.add(v2);
        flotaPrueba.add(v3);

        // ==========================================
        // 3. PRUEBA DE CONFIGURACIÓN (Módulo 2)
        // ==========================================
        System.out.println("\n--- Probando Configuración Binaria ---");
        GestorConfiguracion config = GestorConfiguracion.getConfiguracion();
        config.crearArchConfigBinaria();
        config.cargarConfiguracionBinaria();
        System.out.println("Empresa: " + config.getNombreEmpresa() + " | Vehículos instanciados: " + Vehiculo.getTotalVehiculos());

        // ==========================================
        // 4. PRUEBA DE BACKUP (Módulo 4)
        // ==========================================
        System.out.println("\n--- Probando Backup Binario Nativo ---");
        GestorBackup gestorBackup = new GestorBackup();
        gestorBackup.exportarBackup(flotaPrueba);

        ArrayList<Vehiculo> flotaRecuperada = new ArrayList<>();
        flotaRecuperada = gestorBackup.importarBackup(flotaRecuperada);
        System.out.println("Backup recuperado con " + (flotaRecuperada != null ? flotaRecuperada.size() : 0) + " vehículos.");

        // ==========================================
        // 5. PRUEBA DE XML (Módulo 5)
        // ==========================================
        System.out.println("\n--- Probando Reportes XML (DOM y SAX) ---");
        GestorReportesXML gestorXML = new GestorReportesXML();
        gestorXML.generarInformeDOM(flotaPrueba);
        gestorXML.leerAuditoriaSAX();

        // ==========================================
        // 6. PRUEBA DE JSON (Módulo 3)
        // ==========================================
        System.out.println("\n--- Probando Inventario JSON ---");
        GestorInventarioJSON gestorJSON = new GestorInventarioJSON();

        /*
         * IMPORTANTE: Para que estas tres líneas funcionen y no den error,
         * debes ir a tu clase GestorInventarioJSON y añadirle este método rápido:
         *
         * public void setListaVehiculos(ArrayList<Vehiculo> listaVehiculos) {
         *     this.listaVehiculos = listaVehiculos;
         * }
         */

        // gestorJSON.setListaVehiculos(flotaPrueba);
        // gestorJSON.exportarDatosJson();
        // gestorJSON.importarDatosJson();

        System.out.println("\nFin de la simulación del sistema.");
    }
}