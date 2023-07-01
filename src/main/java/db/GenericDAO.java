package db;

import java.util.List;

/**
 * Esta interfaz implementa los métodos necesarios para la lógica de negocio.
 * Es una clase genérica, por lo tanto, tiene un tipo genérico.
 *
 * Se requieren las acciones CRUD
 * - Create: insertar un nuevo registro
 * - Read: obtener los datos de un registro
 * - Update: actualizar un registro
 * - Delete: eliminar un registro
 */
public interface GenericDAO<T> {

    /**
     * Insertar un registro
     */
    boolean insert(T something);

    /**
     * Leer registro de la base de datos
     */
    T read(int id);

    /**
     * Obtener todos los registros de la base de datos
     */
    List<T> readAll();

    /**
     * Actualizar un registro de la base de datos
     */
    boolean update(T something);

    /**
     * Eliminar un registro de la base de datos por su id
     */
    boolean delete(int id);

}
