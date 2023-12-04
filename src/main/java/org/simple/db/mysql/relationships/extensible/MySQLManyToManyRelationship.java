package org.simple.db.mysql.relationships.extensible;

import org.simple.db.mysql.relationships.ManyToMany;

public abstract class MySQLManyToManyRelationship<T extends MySQLManyToManyDAO> {

    protected T leftToRightDao;

    protected T rightToLeftDao;

    public T getLeftToRight() {
        return leftToRightDao;
    }

    public T getRightToLeft() {
        return rightToLeftDao;
    }

}
