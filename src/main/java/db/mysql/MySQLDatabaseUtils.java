package db.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLDatabaseUtils {

    /**
     * Este método ejecuta una sentencia de sql de tipo consulta (select solamente).
     * No realiza modificaciones sobre la base de datos.
     * @param con conexión con la base de datos
     * @param sql sentencia de sql a ejecutar
     * @return el ResultSet con el resultado de la consulta
     */
    public static ResultSet executeQuery(Connection con, String sql) {
        ResultSet rs = null;
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            rs = statement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la sentencia sql: " + e.getMessage());
        }
        return rs;
    }

    /**
     * Este método ejecuta una setencia de sql de cualquier tipo
     * Puede realizar modificaciones sobre la base de datos
     * @param statement objeto PreparedStatement con la sentencia a ejecutar
     * @return el ResultSet con el resultado de la consulta
     */
    public static ResultSet executeQuery(PreparedStatement statement) {
        ResultSet rs = null;
        try {
            rs = statement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la sentencia sql: " + e.getMessage());
        }
        return rs;
    }

    /**
     * Este método ejecuta un rollback (retornar al estado previo al commit)
     * @param con conexión a la base de datos
     * @return verdadero o falso según se ha podido o no restaurar
     */
    public static boolean rollback(Connection con) {
        try {
            con.rollback();
            return true;
        } catch (SQLException e) {
            System.out.println("No se ha podido restaurar la base de datos a la versión anterior: " + e.getMessage());
        }
        return false;
    }

    /**
     * Este método obtiene un objeto PreparedStatement a partir de la sentencia de sql y la
     * conexión con la base de datos.
     * @param con la conexión con la base de datos
     * @param sql la sentencia de sql
     * @return un objeto prepared statement
     */
    public static PreparedStatement getPreparedStatement(Connection con, String sql) {
        try {
            return con.prepareStatement(sql);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Este método ejecuta una PreparedStatement que puede realizar modificaciones sobre la base de datos
     * @param con la conexión a la base de datos
     * @param statement objeto PreparedStatement con la sentencia a ejecutar
     * @return el número de registros afectados
     */
    public static int executeUpdate(Connection con, PreparedStatement statement) {
        try {
            if (con.getAutoCommit()) {
                con.setAutoCommit(false);
            }
            if (statement != null) {
                int affectedRegisters = statement.executeUpdate();
                con.commit();
                return affectedRegisters;
            }
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la sentencia sql: " + e.getMessage());
            rollback(con);
        }
        return -1;
    }

    public static <T> List<T> searchLike(Connection con, BuildFromResultSet<T> builder, String tableName, String textPattern) {
        String query = "select * from `%s` where `name` like '%%%s%%';";
        query = String.format(query, tableName, textPattern);

        ResultSet rs = MySQLDatabaseUtils.executeQuery(con, query);
        ArrayList<T> list = new ArrayList<>();
        try {
            while (rs.next()) {
                T item = builder.build(rs);
                if (item != null) {
                    list.add(item);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al acceder a los datos de la consulta: " + e.getMessage());
        }
        return list;
    }

}
