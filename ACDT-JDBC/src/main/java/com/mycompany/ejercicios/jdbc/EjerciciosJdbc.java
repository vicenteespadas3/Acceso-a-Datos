/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.ejercicios.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author apm25
 */
public class EjerciciosJdbc {

    public static void main(String[] args) {
        String sql;
        try(Connection conn = ConnectionManager.getConnection()){
            try(Scanner sc = new Scanner(System.in)){
                System.out.println("Conectado!");
                System.out.println("\nIntroduce la sentencia SQL: ");
                sql = sc.nextLine();
            }
            if(sql.contains("?")){
                executePreparedStatement(sql);
            } else {
                //query estatica
                executeStatement(sql);
            }
        } catch(SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        } finally {
            ConnectionManager.cerrarConexion();
        }
    }
    
    public static void executeStatement(String sql) throws SQLException {
        try(Connection conn = ConnectionManager.getConnection(); 
                Statement st = conn.createStatement()){
            if(st.execute(sql)){
                ResultSet rs = st.getResultSet();
                //Recorremos el ResultSet para mostrar los valores del insert.
                while(rs.next()){
                    int id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    int edad = rs.getInt("edad");

                    System.out.printf("ID: %d, Nombre: %s, Edad: %d\n", id, nombre, edad);
                }
            } else {
                System.out.println("Update count: " + st.getUpdateCount());
            }
                
        }
    }
    
    public static void executePreparedStatement(String sql) throws SQLException {
        try(Connection conn = ConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            // Este if controla que si no es una select ejecute con exectueUpdate()
            // para obtener las filas afectadas por la query.
            if(!sql.toUpperCase().contains("SELECT")){
                int filasAfectadas = ps.executeUpdate(sql);
                System.out.println("Filas afectadas: " + filasAfectadas);
            }
            
            
            // Obtenemos el ResultSet de una forma u otra en funcion de si es un
            // select u otra query.
            try(ResultSet rs = sql.contains("SELECT") ? ps.executeQuery() : ps.getGeneratedKeys()){
                // Una vez tenemos el ResultSet podemos recorrerlo con el while(rs.next())
                // Mostrar los datos
                if(!sql.toUpperCase().contains("SELECT")){
                    System.out.println("------------Resultados de consulta----------");
                    while(rs.next()){
                        int id = rs.getInt("id");
                        String nombre = rs.getString("nombre");
                        int edad = rs.getInt("edad");
                        System.out.printf("ID: %d, Nombre: %s, Edad: %d", id, nombre, edad);
                    }
                } else {
                    System.out.println("------Claves generadas-------");
                    while(rs.next()){
                        Object claveGenerada = rs.getObject(1);
                        System.out.println("ID generada: " + claveGenerada);
                    }
                }
            }
        }
    }
    
    public static void mostrarColumnasTabla(Connection conn, String nombreTabla) {
        try {
            DatabaseMetaData metaData = conn.getMetaData();

            // 1. Primero averiguamos qué columna(s) son PRIMARY KEY usando getPrimaryKeys()
            Set<String> clavesPrimarias = new HashSet<>();
            try (ResultSet rsPK = metaData.getPrimaryKeys(conn.getCatalog(), null, nombreTabla)) {
                while (rsPK.next()) {
                    // "COLUMN_NAME" aquí nos da el nombre de la columna que es PK
                    clavesPrimarias.add(rsPK.getString("COLUMN_NAME"));
                }
            }

            // 2. Ahora recorremos todas las columnas y comprobamos si están en el Set de PKs
            try (ResultSet columnas = metaData.getColumns(conn.getCatalog(), null, nombreTabla, "%")) {

                System.out.println("--- Columnas de la tabla '" + nombreTabla + "' ---");
                boolean existe = false;

                while (columnas.next()) {
                    existe = true;
                    String nombreCol = columnas.getString("COLUMN_NAME");
                    String tipoCol   = columnas.getString("TYPE_NAME");
                    int tamanoCol    = columnas.getInt("COLUMN_SIZE");
                    String esNulo    = columnas.getString("IS_NULLABLE");
                    String esAutoInc = columnas.getString("IS_AUTOINCREMENT"); // "YES" o "NO"

                    // Comprobamos si esta columna está en nuestra lista de claves primarias
                    String marcaPK = clavesPrimarias.contains(nombreCol) ? " [🔑 PK]" : "";
                    String marcaAI = "YES".equals(esAutoInc) ? " [AUTO_INC]" : "";

                    System.out.printf("- %-18s | Tipo: %-10s (%d) | NULL: %-3s%s%s%n", 
                            nombreCol, tipoCol, tamanoCol, esNulo, marcaPK, marcaAI);
                }

                if (!existe) {
                    System.out.println("No se encontraron columnas (revisa el nombre de la tabla).");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las columnas: " + e.getMessage());
        }
    }
    public static void showMetadata(Connection conn) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        System.out.println("Motor de BD: " + meta.getDatabaseProductName());
        System.out.println("URL activa:  " + meta.getURL());
        try (ResultSet tablas = meta.getTables(conn.getCatalog(), null, "%", new String[]{"TABLE"})) {
            System.out.println("--- Listado de tablas ---");
            while (tablas.next()) {
                System.out.println(" - " + tablas.getString("TABLE_NAME"));
            }
        }
    }
}
