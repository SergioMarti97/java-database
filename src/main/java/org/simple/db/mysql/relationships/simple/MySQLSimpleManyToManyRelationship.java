package org.simple.db.mysql.relationships.simple;

import org.simple.db.mysql.relationships.ManyToMany;
import org.simple.db.mysql.relationships.extensible.MySQLManyToManyRelationship;

import java.sql.Connection;

public class MySQLSimpleManyToManyRelationship extends MySQLManyToManyRelationship<MySQLSimpleManyToManyDAO> {

    public MySQLSimpleManyToManyRelationship(Connection con, String entityName, String idLeft, String idRight) {
        leftToRightDao = new MySQLSimpleManyToManyDAO(con, entityName, idLeft, idRight);
        rightToLeftDao = new MySQLSimpleManyToManyDAO(con, entityName, idRight, idLeft);
    }

}
