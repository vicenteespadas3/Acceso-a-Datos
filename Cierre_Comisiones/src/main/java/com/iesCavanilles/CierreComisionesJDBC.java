package com.iesCavanilles;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CierreComisionesJDBC {
    static void main() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement insertar = null;
        PreparedStatement actualizar = null;
        Savepoint svptLote = null;
        Savepoint svptActualizar = null;

        public static String sqlLeer ="SELECT id" +
                " FROM VENDEDORES" +
                " WHERE activo = TRUE" +
                " ORDER BY id DESC";

        try {
            leerVendedor(conn = ConexionBD.getConnection());
            conn.setAutoCommit(false);
            stmt = conn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        
    }

    private static void leerVendedor(Connection conexion) throws SQLException {
        List ListaVendedores = new ArrayList();

        try(Statement stmt = conexion.createStatement(); conexion){
            try(ResultSet rs = stmt.executeQuery()){

            }
        }

    }


    //Clase estatica ConexionBD (Singleton)
    public static class ConexionBD {
        private static final String URL = "jdbc:postgresql://localhost:5432/cierre_comisiones";
        private static final String USER = "postgres";
        private static final String PASS = "1234";

        private static Connection instancia = null;

        private ConexionBD() {
        }

        public static Connection getConnection() throws SQLException {
            if (instancia == null || instancia.isClosed()) {
                instancia = DriverManager.getConnection(URL, USER, PASS);
                System.out.println("-> Conexión abierta con el servidor.");
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
}


