package org.simple.db.mysql.relationships.extensible;

import org.simple.db.mysql.MySQLGenericDAO;
import org.simple.db.mysql.relationships.IManyToMany;
import org.simple.db.mysql.relationships.ManyToMany;

public abstract class MySQLManyToManyDAO<T extends ManyToMany> extends MySQLGenericDAO<T> implements IManyToMany<T> {

}
