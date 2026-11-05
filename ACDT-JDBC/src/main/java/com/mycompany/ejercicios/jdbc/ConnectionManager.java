/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ejercicios.jdbc;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

/**
 *
 * @author apm25
 */
public class ConnectionManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASS = "1234";
    
    private static Connection instancia = null;

    public ConnectionManager() {
    }
    
    public static Connection getConnection() throws SQLException {
        if (instancia == null || instancia.isClosed()) {
            instancia = (Connection)DriverManager.getConnection(URL, USER, PASS);
            System.out.println("-> Conexión física abierta con el servidor.");
        }
        return instancia;
    }
    
    public static void cerrarConexion() {
        try {
            if (instancia != null && !instancia.isClosed()) {
                instancia.close();
                System.out.println("-> Conexión física cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
