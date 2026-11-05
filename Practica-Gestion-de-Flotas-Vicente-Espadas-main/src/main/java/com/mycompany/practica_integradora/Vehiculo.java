package com.mycompany.practica_integradora;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Clase JavaBean que representa un vehículo de la flota.
 * Implementa Serializable para permitir su persistencia en formato binario nativo.
 *
 * @author vicen
 */
public class Vehiculo implements Serializable {

    /** Contador estático del total de vehículos instanciados. */
    private static Integer totalVehiculos = 0;

    /** Contador estático del total de kilómetros acumulados por la flota. */
    private static Integer totalKilometros = 0;

    private String matricula;
    private Marca marca;
    private Integer anio;

    @SerializedName("kms_recorridos")
    private Integer kilometraje;

    private CategoriaMantenimiento catMantenimiento;

    /**
     * Constructor por defecto.
     * Requerido por la convención JavaBean. Incrementa el contador de vehículos.
     */
    public Vehiculo() {
        totalVehiculos++;
    }

    /**
     * Constructor parametrizado para inicializar los atributos del vehículo.
     * Actualiza automáticamente los contadores globales de la flota.
     *
     * @param matricula Identificador único del vehículo.
     * @param marca Marca del vehículo (Enumerado).
     * @param anio Año de fabricación o matriculación.
     * @param kilometraje Kilómetros actuales del vehículo.
     * @param catMantenimiento Categoría asignada para revisiones (Enumerado).
     */
    public Vehiculo(String matricula, Marca marca,
                    Integer anio, Integer kilometraje,
                    CategoriaMantenimiento catMantenimiento) {

        this.matricula = matricula;
        this.marca = marca;
        this.anio = anio;
        this.kilometraje = kilometraje;
        this.catMantenimiento = catMantenimiento;

        totalVehiculos++;
        totalKilometros += kilometraje;
    }

    // ==========================================
    // GETTERS
    // ==========================================

    /**
     * Obtiene la matrícula del vehículo.
     *
     * @return Cadena de texto con la matrícula.
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * Obtiene la marca del vehículo.
     *
     * @return Valor del enumerado Marca.
     */
    public Marca getMarca() {
        return marca;
    }

    /**
     * Obtiene el año de fabricación.
     *
     * @return Año en formato numérico.
     */
    public Integer getAnio() {
        return anio;
    }

    /**
     * Obtiene el kilometraje actual.
     *
     * @return Número de kilómetros recorridos.
     */
    public Integer getKilometraje() {
        return kilometraje;
    }

    /**
     * Obtiene la categoría de mantenimiento.
     *
     * @return Valor del enumerado CategoriaMantenimiento.
     */
    public CategoriaMantenimiento getCatMantenimiento() {
        return catMantenimiento;
    }

    // ==========================================
    // SETTERS
    // ==========================================

    /**
     * Establece la matrícula del vehículo.
     *
     * @param matricula Nueva matrícula a asignar.
     */
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    /**
     * Establece la marca del vehículo.
     *
     * @param marca Nueva marca del enumerado.
     */
    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    /**
     * Establece el año del vehículo.
     *
     * @param anio Nuevo año de fabricación.
     */
    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    /**
     * Actualiza el kilometraje del vehículo.
     * Además, ajusta el contador global de kilómetros de la flota y sincroniza 
     * el cambio con el GestorConfiguracion.
     *
     * @param kilometraje El nuevo valor de kilómetros recorridos.
     */
    public void setKilometraje(Integer kilometraje) {
        totalKilometros += (kilometraje - this.kilometraje);
        this.kilometraje = kilometraje;

        GestorConfiguracion config = GestorConfiguracion.getConfiguracion();
        config.setTotalKilometros(totalKilometros);
    }

    /**
     * Establece la categoría de mantenimiento del vehículo.
     *
     * @param catMantenimiento Nueva categoría del enumerado.
     */
    public void setCatMantenimiento(CategoriaMantenimiento catMantenimiento) {
        this.catMantenimiento = catMantenimiento;
    }

    // ==========================================
    // TOSTRING
    // ==========================================

    /**
     * Representación en formato texto del vehículo y sus atributos.
     *
     * @return Cadena descriptiva del objeto.
     */
    @Override
    public String toString() {
        return "Vehiculo{" + "matricula=" + matricula + ", marca="
                + marca + ", anio=" + anio + ", kilometraje=" +
                kilometraje + ", catMantenimiento=" + catMantenimiento + '}';
    }

    // ==========================================
    // MÉTODOS ESTÁTICOS
    // ==========================================

    /**
     * Obtiene el total de vehículos registrados.
     *
     * @return Cantidad global de vehículos.
     */
    public static Integer getTotalVehiculos() {
        return totalVehiculos;
    }

    /**
     * Obtiene el total de kilómetros de toda la flota.
     *
     * @return Suma global de kilómetros.
     */
    public static Integer getTotalKilometros() {
        return totalKilometros;
    }

    /**
     * Establece manualmente el total de vehículos.
     *
     * @param totalVehiculos Nuevo total global.
     */
    public static void setTotalVehiculos(Integer totalVehiculos) {
        Vehiculo.totalVehiculos = totalVehiculos;
    }

    /**
     * Establece manualmente el total de kilómetros.
     *
     * @param totalKilometros Nuevo kilometraje global.
     */
    public static void setTotalKilometros(Integer totalKilometros) {
        Vehiculo.totalKilometros = totalKilometros;
    }
}