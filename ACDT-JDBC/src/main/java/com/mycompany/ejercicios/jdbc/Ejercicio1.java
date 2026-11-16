package com.mycompany.ejercicios.jdbc;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Ejercicio1 {

    static class LecturaSensor {
        private Integer id;
        private int sensorId;
        private BigDecimal temperatura;
        private LocalDateTime fechaLectura;

        /**
         * Constructor para instanciar lecturas antes de ser persistidas en la base de datos.
         *
         * @param sensorId Identificador único del sensor.
         * @param temperatura Valor medido de la temperatura.
         * @param fechaLectura Marca temporal de la lectura.
         */
        public LecturaSensor(int sensorId, BigDecimal temperatura, LocalDateTime fechaLectura) {
            this.sensorId = sensorId;
            this.temperatura = temperatura;
            this.fechaLectura = fechaLectura;
        }

        /**
         * Obtiene el identificador de la lectura.
         *
         * @return Identificador primario de la lectura.
         */
        public Integer getId() {
            return id;
        }

        /**
         * Establece el identificador de la lectura.
         *
         * @param id Identificador primario generado por la base de datos.
         */
        public void setId(Integer id) {
            this.id = id;
        }

        /**
         * Obtiene el identificador del sensor.
         *
         * @return Identificador del sensor.
         */
        public int getSensorId() {
            return sensorId;
        }

        /**
         * Establece el identificador del sensor.
         *
         * @param sensorId Identificador del sensor.
         */
        public void setSensorId(int sensorId) {
            this.sensorId = sensorId;
        }

        /**
         * Obtiene la temperatura registrada.
         *
         * @return Valor de temperatura con escala decimal.
         */
        public BigDecimal getTemperatura() {
            return temperatura;
        }

        /**
         * Establece la temperatura registrada.
         *
         * @param temperatura Valor de temperatura.
         */
        public void setTemperatura(BigDecimal temperatura) {
            this.temperatura = temperatura;
        }

        /**
         * Obtiene la fecha y hora de la lectura.
         *
         * @return Instante temporal de la captura.
         */
        public LocalDateTime getFechaLectura() {
            return fechaLectura;
        }

        /**
         * Establece la fecha y hora de la lectura.
         *
         * @param fechaLectura Instante temporal de la captura.
         */
        public void setFechaLectura(LocalDateTime fechaLectura) {
            this.fechaLectura = fechaLectura;
        }
    }

    public List<String> listarTitulosDisponibles(int anioMinimo) throws SQLException{
        String sql = "SELECT id, titulo, anio FROM libros WHERE disponible = TRUE AND anio >= ? ORDER BY anio DESC, titulo ASC;";

        List<String> listaTitulosDisponible = new ArrayList<>();
        try(Connection dm = ConnectionManager.getConnection(); PreparedStatement ps = dm.prepareStatement(sql)){

            ps.setInt(1, anioMinimo);

            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    listaTitulosDisponible.add(rs.getString("titulo"));

                }
            }
        }
        return listaTitulosDisponible;
    }


    public static long registrarIncidencias(Connection conn, String descripcion, int prioridad, Integer idTecnico, LocalDate fecha) throws SQLException{

        String sql = "INSERT INTO incidencias (descripcion, prioridad, id_tecnico, fecha_alta) VALUES (?, ?, ?, ?);";

        try(PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, descripcion);
            ps.setInt(2, prioridad);
            if(idTecnico == null){
                ps.setNull(3, Types.INTEGER);
            }else{
                ps.setInt(3, idTecnico);
            }
            ps.setDate(4, java.sql.Date.valueOf(fecha));

            int filasAfectadas = ps.executeUpdate();
            try(ResultSet rs = ps.getGeneratedKeys()){
                while(rs.next()){
                    return rs.getLong("id");
                }
            }
        }
        return 1;

    }

    public void normailizarReservas(Connection conn, int id, String cliente,
                                    int descuento, boolean confirmada) throws SQLException {
        String sql = "SELECT id, cliente, descuento, confirmada FROM reservas ORDER BY id;";

        try(PreparedStatement ps = conn.prepareStatement(sql,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = ps.executeQuery()){
            while(rs.next()){


                //Primera forma de manejar null
            /*
            rs.getInt(3);
            if(rs.wasNull()){
                System.out.println("Valor nulo");
            }
            */

            //Segunda forma de manejar null
            Integer descuentoWrapper = rs.getObject(3, Integer.class);

            if(descuentoWrapper == null){
                rs.updateInt("descuento", 0);
                rs.updateBoolean("confirmada", true);
                rs.updateRow();
            }
            }
            rs.moveToInsertRow();
            rs.updateInt(1, 10);
            rs.updateString(2, cliente);
            rs.updateInt(3, descuento);
            rs.updateBoolean(4, confirmada);

            rs.insertRow();
            rs.moveToCurrentRow();
        }
    }


    public void confirmarPedido(Connection conn, int pedidoId, int productoId, int cantidad){
        String sqlStock = "UPDATE stock\n" +
                "SET cantidad = cantidad - ?\n" +
                "WHERE producto_id = ?\n" +
                " AND cantidad >= ?;\n";

        String sqlPedidos ="UPDATE pedidos\n" +
                "SET estado = 'CONFIRMADO'\n" +
                "WHERE id = ?;";

        String sqlMovimientos ="INSERT INTO movimientos (pedido_id, producto_id, cantidad, tipo)\n" +
                "VALUES (?, ?, ?, 'SALIDA');\n";

        Savepoint savepoint =null;

        try {
            conn.setAutoCommit(false);

            try(PreparedStatement psStock = conn.prepareStatement(sqlStock)){
                psStock.setInt(1, cantidad);
                psStock.setInt(2, cantidad);
                psStock.setInt(3, cantidad);

                psStock.execute();

                savepoint = conn.setSavepoint("Upadate pedidos");
            }catch(SQLException e){
                System.out.println("Rollback stock");
                conn.rollback();
            }

            try (PreparedStatement psPedidos = conn.prepareCall(sqlPedidos)){
                psPedidos.setInt(1, pedidoId);

                psPedidos.execute();

                savepoint = conn.setSavepoint("Update pedidos");
            }catch(SQLException e){
                System.out.println("Rollback pedido");
                conn.rollback();
            }
            try(PreparedStatement psMovimientos = conn.prepareStatement(sqlMovimientos)){
                psMovimientos.setInt(1, pedidoId);
                psMovimientos.setInt(2, productoId);
                psMovimientos.setInt(3, cantidad);

                psMovimientos.execute();
            }catch(SQLException e){
                conn.rollback(savepoint);
            }

            conn.commit();

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void insertarLecturas(Connection conn, List<LecturaSensor> lecturas) {
        String sql = "INSERT INTO lecturas_sensor (sensor_id, temperatura, fecha_lectura)\n" +
                "VALUES (?, ?, ?);\n";

        int contador = 0;

        try {
            boolean estado = conn.getAutoCommit();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for(LecturaSensor linea : lecturas) {
                    ps.setInt(1, linea.getId());
                    ps.setBigDecimal(2, linea.getTemperatura());
                    ps.setDate(3, linea.getFechaLectura());

                    ps.addBatch();

                    contador++;

                    if (contador == 500) {
                        ps.executeBatch();
                        contador = 0;

                        ps.clearBatch();
                    }
                }

                if (contador > 0) {
                    ps.executeBatch();
                    ps.clearBatch();
                }

                conn.commit();
                System.out.println("Terminado.");

            } catch (SQLException e) {
                conn.rollback();
                throw e;

            } finally {
                conn.setAutoCommit(estado);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void recorrerEmpleados(Connection conn){
        try {
            conn.setAutoCommit(false);

            String sql = "SELECT id, nombre, salario, departamento\n" +
                    "FROM empleados;";

            try(PreparedStatement ps = conn.prepareStatement(sql,
                    ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY)){
                ps.setFetchSize(1000);

                DatabaseMetaData dmd = conn.getMetaData();
                try(ResultSet rs = ps.executeQuery()){
                    ResultSetMetaData rsmd = rs.getMetaData();

                    for(int i = 1; i <= rsmd.getColumnCount(); i++){
                        System.out.println("Indice: "+ rsmd.getColumnLabel(i) + "Tipo: " + rsmd.getColumnTypeName(i) + "Nombre: " + rsmd.getColumnName(i));
                    }

                    while(rs.next()){
                        System.out.println(rs.getInt(1)+"\t"+rs.getString(2)+"\t"+ rs.getInt(3) +"\t"+rs.getString(4));
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        public static BigDecimal obtenerBono(Connection conn, int empleadoId) throws SQLException{

        }

    }

}
