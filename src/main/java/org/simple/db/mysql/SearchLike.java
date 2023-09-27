package org.simple.db.mysql;

import java.util.List;

public interface SearchLike<T> {

    List<T> searchLike(String textPattern);

}
