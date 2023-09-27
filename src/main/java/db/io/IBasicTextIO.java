package db.io;

public interface IBasicTextIO<T> {

    boolean isMalformed(String line);

    T build(String line);

    String write(T o);

}
