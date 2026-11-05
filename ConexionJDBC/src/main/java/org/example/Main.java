package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase principal que demuestra las operaciones CRUD básicas en PostgreSQL
 * utilizando JDBC, PreparedStatement y ResultSet.
 */
public class Main {

    /**
     * Método principal que establece la conexión a la base de datos y ejecuta
     * los métodos de prueba para crear tablas, insertar, actualizar y mostrar datos.
     *
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        // Asegúrate de añadir el nombre de tu base de datos al final de la URL
        String url = "jdbc:postgresql://localhost:5432/postghres";
        String username = "postgres";
        String password = "1234";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            System.out.println("¡Conexión establecida con éxito!\n");

            crearTabla(conn);

            // Insertar datos y recuperar las claves generadas
            int idGenerado1 = insertarDato(conn, "Vicente", 30);
            int idGenerado2 = insertarDato(conn, "Ana", 25);

            System.out.println("\nDatos después de la inserción:");
            mostrarDatos(conn);

            // Actualizar el primer registro insertado si se generó correctamente un ID
            if (idGenerado1 != -1) {
                actualizarDato(conn, idGenerado1, "Vicente Actualizado", 31);
            }

            System.out.println("\nDatos después de la actualización:");
            mostrarDatos(conn);

        } catch (SQLException e) {
            System.out.println("Error en la conexión o en la base de datos: " + e.getMessage());
        }
    }

    /**
     * Crea una tabla llamada 'usuarios' en la base de datos si no existe.
     *
     * @param conn La conexión activa a la base de datos.
     * @throws SQLException Si ocurre un error al ejecutar la sentencia SQL.
     */
    public static void crearTabla(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS usuarios ("
                + "id SERIAL PRIMARY KEY, "
                + "nombre VARCHAR(100) NOT NULL, "
                + "edad INT NOT NULL"
                + ");";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
            System.out.println("Tabla 'usuarios' creada o verificada correctamente.");
        }
    }

    /**
     * Inserta un nuevo usuario en la tabla 'usuarios' y recupera la clave primaria generada.
     *
     * @param conn   La conexión activa a la base de datos.
     * @param nombre El nombre del usuario a insertar.
     * @param edad   La edad del usuario a insertar.
     * @return El ID generado para el nuevo registro, o -1 si no se pudo generar.
     * @throws SQLException Si ocurre un error durante la inserción o al recuperar la clave.
     */
    public static int insertarDato(Connection conn, String nombre, int edad) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, edad) VALUES (?, ?)";
        int idGenerado = -1;

        // Se indica a la base de datos que devuelva las claves generadas
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, nombre);
            pstmt.setInt(2, edad);

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                // Obtenemos el ResultSet que contiene las claves generadas
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        // Dependiendo del driver, puede ser necesario usar el índice 1
                        idGenerado = rs.getInt(1);
                        System.out.println("Usuario '" + nombre + "' insertado con el ID generado: " + idGenerado);
                    }
                }
            }
        }
        return idGenerado;
    }

    /**
     * Actualiza el nombre y la edad de un usuario existente basándose en su ID.
     *
     * @param conn        La conexión activa a la base de datos.
     * @param id          El identificador único del usuario a actualizar.
     * @param nuevoNombre El nuevo nombre que se asignará al usuario.
     * @param nuevaEdad   La nueva edad que se asignará al usuario.
     * @throws SQLException Si ocurre un error durante la actualización.
     */
    public static void actualizarDato(Connection conn, int id, String nuevoNombre, int nuevaEdad) throws SQLException {
        String sql = "UPDATE usuarios SET nombre = ?, edad = ? WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nuevoNombre);
            pstmt.setInt(2, nuevaEdad);
            pstmt.setInt(3, id);

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Usuario con ID " + id + " actualizado correctamente.");
            } else {
                System.out.println("No se encontró ningún usuario con el ID " + id + ".");
            }
        }
    }

    /**
     * Realiza una consulta SELECT a la tabla 'usuarios' y muestra todos los registros
     * procesando el ResultSet devuelto por la base de datos.
     *
     * @param conn La conexión activa a la base de datos.
     * @throws SQLException Si ocurre un error al ejecutar la consulta o al leer los resultados.
     */
    public static void mostrarDatos(Connection conn) throws SQLException {
        String sql = "SELECT id, nombre, edad FROM usuarios";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("--- Lista de Usuarios ---");
            boolean hayDatos = false;

            while (rs.next()) {
                hayDatos = true;
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                int edad = rs.getInt("edad");

                System.out.println("ID: " + id + " | Nombre: " + nombre + " | Edad: " + edad);
            }

            if (!hayDatos) {
                System.out.println("La tabla está vacía.");
            }
            System.out.println("-------------------------");
        }
    }
}