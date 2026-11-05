/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_integradora;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Clase que gestiona la configuración global del sistema utilizando el patrón Singleton.
 * Permite almacenar y recuperar los parámetros básicos en un archivo binario primitivo.
 *
 * @author vicen
 */
public class GestorConfiguracion {
    
    /**
     * Instancia única y estática de la clase.
     */
    private static GestorConfiguracion configuracion;
    
    /** Nombre de la configuración o empresa. */
    private String nombreEmpresa;
    
    /** Versión actual del software. */
    private String verSoftware;
    
    /** Cantidad total de vehículos registrados. */
    private Integer totalVehiculos;
    
    /** Cantidad total de kilómetros registrados. */
    private Integer totalKilometros;


    /**
     * Constructor privado para evitar la instanciación externa y mantener el Singleton.
     * Inicializa las variables con valores por defecto.
     */
    private GestorConfiguracion() {
        this.nombreEmpresa = "VicEspCat";
        this.verSoftware = "1.0";
        this.totalVehiculos = 0;
        this.totalKilometros = 0;
    }
    
    /**
     * Guarda la configuración actual del sistema en un archivo binario (.dat).
     * Escribe las variables miembro de forma secuencial utilizando DataOutputStream.
     */
    public void crearArchConfigBinaria() {
        try(DataOutputStream dos = new DataOutputStream(new FileOutputStream("datos/config/empresa.dat"))) {
            dos.writeUTF(nombreEmpresa);
            dos.writeUTF(verSoftware);
            dos.writeInt(totalKilometros);
            dos.writeInt(totalVehiculos);
        } catch(IOException e) {
            System.err.println("Error inesperado al guardar la configuración: " + e.getLocalizedMessage());
        }
    }

    /**
     * Carga la configuración del sistema desde el archivo binario (.dat).
     * Lee los datos respetando el orden estricto en el que fueron escritos.
     */
    public void cargarConfiguracionBinaria() {
        try(DataInputStream dis = new DataInputStream(new FileInputStream("datos/config/empresa.dat"))) {
            this.nombreEmpresa = dis.readUTF();
            this.verSoftware = dis.readUTF();
            this.totalKilometros = dis.readInt();
            this.totalVehiculos = dis.readInt();
        } catch (EOFException e) {
            System.out.println("Lectura del archivo de configuración finalizada correctamente.");
        } catch (IOException e) {
            System.err.println("Error inesperado al leer el archivo: " + e.getLocalizedMessage());
        }
    }
    
    /**
     * Obtiene la instancia única de la clase GestorConfiguracion.
     * Si la instancia aún no ha sido creada, la inicializa.
     *
     * @return La instancia única de {@link GestorConfiguracion}.
     */
    public static GestorConfiguracion getConfiguracion() {
        if(configuracion == null){
            configuracion = new GestorConfiguracion();
        }
        return configuracion;
    }

    /**
     * Obtiene el nombre de la empresa.
     *
     * @return Un String con el nombre establecido.
     */
    public String getNombreEmpresa() {

        return nombreEmpresa;
    }

    /**
     * Obtiene la versión del software.
     *
     * @return Un String con la versión actual.
     */
    public String getVerSoftware() {

        return verSoftware;
    }

    /**
     * Obtiene el total de vehículos.
     *
     * @return El número total de vehículos.
     */
    public Integer getTotalVehiculos() {

        return totalVehiculos;
    }

    /**
     * Obtiene el total de kilómetros registrados.
     *
     * @return El número total de kilómetros.
     */
    public Integer getTotalKilometros() {

        return totalKilometros;
    }

    /**
     * Establece el nombre de la empresa.
     *
     * @param nombreEmpresa El nuevo nombre a asignar.
     */
    public void setNombreEmpresa(String nombreEmpresa) {

        this.nombreEmpresa = nombreEmpresa;
    }

    /**
     * Establece la versión del software.
     *
     * @param verSoftware La nueva versión a asignar.
     */
    public void setVerSoftware(String verSoftware) {

        this.verSoftware = verSoftware;
    }

    /**
     * Establece el total de vehículos.
     *
     * @param totalVehiculos La cantidad de vehículos a asignar.
     */
    public void setTotalVehiculos(Integer totalVehiculos) {

        this.totalVehiculos = totalVehiculos;
    }

    /**
     * Establece el total de kilómetros.
     *
     * @param totalKilometros La cantidad de kilómetros a asignar.
     */
    public void setTotalKilometros(Integer totalKilometros) {

        this.totalKilometros = totalKilometros;
    }
}