package db.io;

public interface IPlainTextIO<T> {

    boolean isMalformed(String line);

    T build(String line);

    String write(T o);

}
