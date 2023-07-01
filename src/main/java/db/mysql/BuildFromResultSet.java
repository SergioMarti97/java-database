package db.mysql;

import java.sql.ResultSet;

/**
 * This interfaces extract the common method to instance an object from a ResultSet
 *
 * @param <T> The class to instance from result set
 */
public interface BuildFromResultSet<T> {

    T build(ResultSet rs);

}
