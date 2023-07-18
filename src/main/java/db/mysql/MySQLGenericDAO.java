package db.mysql;

import db.GenericDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class MySQLGenericDAO<T> implements GenericDAO<T>, BuildFromResultSet<T> {

    /**
     * Connection to MySQL database
     */
    protected Connection con;

    /**
     * Query builder for simple queries
     */
    protected QueryBuilder queryBuilder;

    // Common methods for all DAOs

    /**
     * Common code to obtain one register from a query
     * @param query the query to find one register from a table
     * @return instance of the register from a table
     */
    public T read(String query) {
        ResultSet rs = MySQLDatabaseUtils.executeQuery(con, query);
        try {
            if (rs.next()) {
                return build(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al acceder a los datos de la consulta: " + e.getMessage());
        }
        return null;
    }

    public T read(PreparedStatement statement) {
        ResultSet rs = MySQLDatabaseUtils.executeQuery(statement);
        try {
            if (rs.next()) {
                return build(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al acceder a los datos de la consulta: " + e.getMessage());
        }
        return null;
    }

    /**
     * Common code to obtain all registers from a query
     * @param query the query to find all the registers from a table
     * @return list with all registers from a table
     */
    public List<T> readAll(String query) {
        ResultSet rs = MySQLDatabaseUtils.executeQuery(con, query);
        ArrayList<T> list = new ArrayList<>();
        try {
            while (rs.next()) {
                T item = build(rs);
                if (item != null) {
                    list.add(item);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al acceder a los datos de la consulta: " + e.getMessage());
        }
        return list;
    }

    public List<T> readAll(PreparedStatement statement) {
        ResultSet rs = MySQLDatabaseUtils.executeQuery(statement);
        ArrayList<T> list = new ArrayList<>();
        try {
            while (rs.next()) {
                T item = build(rs);
                if (item != null) {
                    list.add(item);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al acceder a los datos de la consulta: " + e.getMessage());
        }
        return list;
    }

    // Same methods of MySQLDatabaseUtils

    public ResultSet executeQuery(String query) {
        return MySQLDatabaseUtils.executeQuery(this.con, query);
    }

    public ResultSet executeQuery(PreparedStatement statement) {
        return MySQLDatabaseUtils.executeQuery(statement);
    }

    public boolean rollBack() {
        return MySQLDatabaseUtils.rollback(this.con);
    }

    public PreparedStatement getPreparedStatement(String query) {
        return MySQLDatabaseUtils.getPreparedStatement(this.con, query);
    }

    public int executeUpdate(PreparedStatement statement) {
        return MySQLDatabaseUtils.executeUpdate(this.con, statement);
    }

    // Getters & Setters

    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }

    public QueryBuilder getQueryBuilder() {
        return queryBuilder;
    }

    public void setQueryBuilder(QueryBuilder queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

}
