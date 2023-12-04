package org.simple.db.mysql.relationships.simple;

import org.simple.db.mysql.query.QueryBuilder;
import org.simple.db.mysql.relationships.ManyToMany;
import org.simple.db.mysql.relationships.extensible.MySQLManyToManyDAO;
import org.simple.db.mysql.utils.MySQLDatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MySQLSimpleManyToManyDAO extends MySQLManyToManyDAO<ManyToMany> {

    public MySQLSimpleManyToManyDAO(Connection con, String entityName, String idLeft, String idRight) {
        this.con = con;
        queryBuilder = new QueryBuilder(
                entityName,
                idLeft, // id
                idRight // field
        );
    }

    @Override
    public boolean insert(ManyToMany m) {
        String sql = queryBuilder.buildQueryInsert(false);
        PreparedStatement ps = MySQLDatabaseUtils.getPreparedStatement(this.con, sql);
        try {
            assert ps != null;
            ps.setInt(1, m.getIdLeft());
            ps.setInt(2, m.getIdRight());
        } catch (SQLException exception) {
            System.out.println("Error: " + exception.getMessage());
        }
        return MySQLDatabaseUtils.executeUpdate(this.con, ps) > 0;
    }

    public boolean insert(int id1, int id2) {
        return insert(new ManyToMany(id1, id2));
    }

    @Override
    public ManyToMany read(int id) {
        String sql = String.format(queryBuilder.buildQuerySelectById(), id);
        return super.read(sql);
    }

    @Override
    public List<ManyToMany> readAll() {
        return super.readAll(queryBuilder.buildQuerySelectAll());
    }

    @Override
    public boolean update(ManyToMany m) {
        PreparedStatement ps = MySQLDatabaseUtils.getPreparedStatement(this.con, queryBuilder.buildQueryUpdateById());
        try {
            assert ps != null;
            ps.setInt(1, m.getIdLeft());
            ps.setInt(2, m.getIdRight());
        } catch (SQLException exception) {
            System.out.println("Error: " + exception.getMessage());
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

    @Override
    public ManyToMany build(ResultSet rs) {
        try {
            return new ManyToMany(
                    rs.getInt(queryBuilder.getIdField()),
                    rs.getInt(queryBuilder.getField(0))
            );
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ManyToMany> readManyToMany(int id) {
        String sql = String.format(queryBuilder.buildQuerySelectById(), id);
        return super.readAll(sql);
    }

    @Override
    public ManyToMany read(int id1, int id2) {
        String sql = queryBuilder.buildQuerySelectByTwoIds(queryBuilder.getIdField(), queryBuilder.getField(0));
        sql = String.format(sql, id1, id2);
        return super.read(sql);
    }

    @Override
    public boolean delete(int id1, int id2) {
        String sql = queryBuilder.buildQueryDeleteByTwoIds(queryBuilder.getIdField(), queryBuilder.getField(0));
        PreparedStatement ps = MySQLDatabaseUtils.getPreparedStatement(this.con, sql);
        try {
            assert ps != null;
            ps.setInt(1, id1);
            ps.setInt(2, id2);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return MySQLDatabaseUtils.executeUpdate(this.con, ps) > 0;
    }

    @Override
    public boolean update(int id1, int id2, ManyToMany m) {
        String sql = queryBuilder.buildQueryUpdateByTwoIds(queryBuilder.getIdField(), queryBuilder.getField(0));
        PreparedStatement ps = MySQLDatabaseUtils.getPreparedStatement(this.con, sql);
        try {
            assert ps != null;
            ps.setInt(1, id1);
            ps.setInt(2, id2);
            ps.setInt(3, m.getIdLeft());
            ps.setInt(4, m.getIdRight());
        } catch (SQLException exception) {
            System.out.println("Error: " + exception.getMessage());
        }
        return MySQLDatabaseUtils.executeUpdate(this.con, ps) > 0;
    }

    public boolean update(int id1, int id2, int id1New, int id2New) {
        return update(id1, id2, new ManyToMany(id1New, id2New));
    }

}
