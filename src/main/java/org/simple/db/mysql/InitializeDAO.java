package org.simple.db.mysql;

import java.sql.Connection;

/**
 * This interface must be implemented on the view controller which has a data access object (DAO) what needs to be initialized
 */
public interface InitializeDAO {

    void initializeDAO(Connection connection);

}
