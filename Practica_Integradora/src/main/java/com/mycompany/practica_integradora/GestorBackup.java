package com.mycompany.practica_integradora;

import java.io.*;
import java.util.ArrayList;

/**
 * Clase encargada de gestionar las copias de seguridad de la flota.
 * Utiliza la serialización nativa de Java para guardar y recuperar
 * la colección de vehículos en formato binario.
 *
 * @author vicen
 */
public class GestorBackup {

    /**
     * Exporta la colección actual de vehículos a un archivo binario nativo.
     * Crea un archivo .ser en la ruta especificada que contiene el estado
     * completo de los objetos.
     *
     * @param vehiculos Colección ArrayList de vehículos que se va a guardar.
     */
    public void exportarBackup(ArrayList<Vehiculo> vehiculos){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("datos/backup/flota_backup.ser")) ){
            oos.writeObject(vehiculos);
        }catch (IOException e){
            System.out.println("Error inesperado: " + e.getLocalizedMessage());
        }
    }

    /**
     * Importa la colección de vehículos desde la copia de seguridad binaria.
     * Lee el archivo .ser y deserializa los datos para reconstruir los objetos
     * en memoria.
     *
     * @param vehiculos Colección base que será reemplazada por los datos del archivo.
     * @return Un ArrayList con los vehículos recuperados del backup.
     */
    public ArrayList<Vehiculo> importarBackup(ArrayList<Vehiculo> vehiculos){
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("datos/backup/flota_backup.ser"))){
            vehiculos = (ArrayList<Vehiculo>) ois.readObject();
        }catch (IOException | ClassNotFoundException | RuntimeException e){
            System.out.println("Error inesperado: " + e.getLocalizedMessage());
        }
        return vehiculos;
    }
}