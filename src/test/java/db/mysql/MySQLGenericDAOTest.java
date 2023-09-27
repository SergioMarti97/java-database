package db.mysql;

import db.mysql.utils.MySQLDatabaseUtils;
import org.junit.jupiter.api.Test;
import test.Person;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MySQLGenericDAOTest {

    static class PersonDAO extends MySQLGenericDAO<Person> {

        public PersonDAO(Connection con) {
            this.con = con;
            queryBuilder = new QueryBuilder("Person", "id", "name", "height", "birthday");
        }

        @Override
        public Person build(ResultSet rs) {
            try {
                return new Person(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getFloat("height"),
                        rs.getDate("birthday").toLocalDate()
                );
            } catch (SQLException e) {
                System.out.println("Error al leer los datos de la consulta: " + e.getMessage());
            }
            return null;
        }

        @Override
        public boolean insert(Person p) {
            PreparedStatement ps = MySQLDatabaseUtils.getPreparedStatement(this.con, queryBuilder.buildQueryInsert());
            try {
                assert ps != null;
                ps.setString(1, p.getName());
                ps.setFloat(2, p.getHeight());
                ps.setDate(3, Date.valueOf(p.getBirthday()));
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
            return MySQLDatabaseUtils.executeUpdate(this.con, ps) > 0;
        }

        @Override
        public Person read(int id) {
            String sql = String.format(queryBuilder.buildQuerySelectById(), id);
            return read(sql);
        }

        @Override
        public List<Person> readAll() {
            return readAll(queryBuilder.buildQuerySelectAll());
        }

        @Override
        public boolean update(Person p) {
            PreparedStatement ps = MySQLDatabaseUtils.getPreparedStatement(this.con, queryBuilder.buildQueryUpdateById());
            try {
                assert ps != null;
                ps.setString(1, p.getName());
                ps.setFloat(2, p.getHeight());
                ps.setDate(3, Date.valueOf(p.getBirthday()));
                ps.setInt(4, p.getId());
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
            return MySQLDatabaseUtils.executeUpdate(this.con, ps) > 0;
        }

        @Override
        public boolean delete(int id) {
            PreparedStatement ps = MySQLDatabaseUtils.getPreparedStatement(this.con, queryBuilder.buildQueryDeleteById());
            try {
                assert ps != null;
                ps.setInt(1, id);
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
            return MySQLDatabaseUtils.executeUpdate(this.con, ps) > 0;
        }

    }

    @Test
    void insert() {

    }

    @Test
    void update() {

    }

    @Test
    void read() {

    }

    @Test
    void readAll() {

    }

    @Test
    void delete() {

    }

}