package org.simple.db.mysql.relationships;

import java.util.List;

public interface IManyToMany<T> {

    T read(int id1, int id2);

    List<T> readManyToMany(int id);

    boolean delete(int id1, int id2);

    boolean update(int id1, int id2, T object);

}
