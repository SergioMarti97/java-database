# Repositorio acceso a bases de datos tipo MySQL

Este repositorio contiene las clases genéricas necesarias para implementar una capa de acceso a datos a una base de datos tipo MySQL. 
Utilizando las clases de este repositorio se puede trabajar con una base de datos MySQL de una forma sencilla. 
Esto permite que un programa tenga persistencia de datos.

**version**: 1.0

**fecha**: 2023/07/01

**autor**: Sergio Martí Torregrosa

## Actualizaciones

En este apartado se recopilan todos los cambios que se han realizado en el repositorio.

Fecha 17/07/2023

  * Añadida la nueva clase "QueryBuilder": contiene los métodos necesarios para construir queries simples, indicando el nombre de la entidad, el campo de la clave primaria y el resto de campos.
  * Nuevo constructor para "MySQLConnectionUtils": indicando el path del archivo "app.properties" que contiene los datos necesarios para realizar la conexión a la base de datos.

Fecha 18/07/2023

  * QueryBuilder contiene ahora métodos para seleccionar que campos se quieren incluir en la query
  * MySQLGenericDAO contiene un objeto QueryBuilder

## Data Access Object

Los objetos "*Data Access Object*" o "DAO" corresponden a la capa de acceso a datos para una tabla o entidad en una base de datos MySQL.

## Acciones CRUD

Las acciones "CRUD" son las siglas de: *Create*, *Read*, *Update*, *Delete*. Es decir, crear registros, leer registros, actualizar registros y eliminarlos.

## Clases

El repositorio contiene las siguientes clases:

- **Interfaces**
  * InitializeDAO: implementa la función "*initializeDAO*" la cual se puede llamar para asignar el objeto de la conexión sql.
  * BuildFromResultSet: implementa la función "*build*" la cual sirve para instanciar objetos a partir de la información contenida de un objeto *ResultSet*.
  * SearchLike: implementa la función "*searchLike*" la cual sirve para implementar la funcionalidad de búsqueda de SQL utilizando la palabra reservada "*like*". 
  * GenericDAO: implementa las funciones necesarias para poder realizar las acciones básicas CRUD:
    * *create*: insertar un nuevo registro.
    * *read*: obtener los datos de un registro.
    * *update*: actualizar un registro.
    * *delete*: eliminar un registro.
- **Clases Abstractas**:
  * MySQLGenericDAO: implementa el código necesario para desarrollar un objeto tipo DAO. Permite realizar tanto las acciones 
- **Clases**
  * PropertiesUtils: contiene una función estática para leer un archivo ".properties" y generar un objeto *Properties*.
  * MySQLConnectionUtils: contiene las funciones necesarias para conectarse con una base de datos MySQL. Requiere instanciarse.
  * MySQLDatabaseUtils: contiene las funciones estáticas para manejar los objetos y realizar las acciones necesarias para la base de datos.
    * *executeQuery*: devuelve un objeto result set a partir de una *query* o un objeto *PreparedStatement*.
    * *rollBack*: ejecuta un rollback, retornar la base de datos al estado previo al commit.
    * *getPreparedStatement*: devuelve un objeto *PreparedStatement* a partir de una *query*.
    * *executeUpdate*: ejecuta una sentencia contenida en un objeto *PreparedStatement* que puede realizar modificaciones sobre la base de datos.
    * *searchLike*: devuelve una lista de registros. Implementa el código necesario para la funcionalidad de búsqueda de SQL utilizando la palabra reservada "*like*".
  
## Ejemplo

Se parte de las siguientes clases:

```java
public abstract class AbstractMinecraftObject {

    private final static long serialVersionUID = 1;

    protected int id;

    protected String name;

    protected String urlImg;

    protected Image img;
    
    // Métodos propios 

}

public class Farm extends AbstractMinecraftObject {

  private String author;

  private String link;

  private int buildComplexity;

  private int productionRates;

  public Farm(int id, String name, String urlImg, String author, String link, String version, int buildComplexity, int productionRates) {
    this.id = id;
    this.name = name;
    this.urlImg = urlImg;
    this.author = author;
    this.link = link;
    this.minecraftVersion = version;
    this.buildComplexity = buildComplexity;
    this.productionRates = productionRates;
    initList();
  }
  
  // Métodos propios
}
``` 

El objeto de tipo DAO para la clase anterior: 

```java
public class FarmDAO extends MySQLGenericDAO<Farm> implements SearchLike<Farm> {

    // Consultas a lanzar

    private final String QUERY_INSERT_FARM = "insert into " +
            "farm(`name`, `urlImg`, `author`, `link`, `version`, `complexity`, `production_rates`) " +
            "values(?, ?, ?, ?, ?, ?, ?);"; // No es necesario el id

    private final String QUERY_SELECT_FARM = "select * from farm where id = %s;";

    private final String QUERY_SELECT_ALL_FARMS = "select * from farm;";

    private final String QUERY_UPDATE_FARM = "update farm set " +
            "`name` = ?, " +
            "`urlImg` = ?, " +
            "`author` = ?, " +
            "`link` = ?, " +
            "`version` = ?, " +
            "`complexity` = ?, " +
            "`production_rates` = ? " +
            "where id = ?;";

    private final String QUERY_DELETE_FARM = " delete from farm where id = ?;";

    // Constructor

    public FarmDAO(Connection con) {
        this.con = con;
    }

    // Methods
    
    @Override
    public Farm build(ResultSet rs) {
        try {
            return new Farm(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("urlImg"),
                    rs.getString("author"),
                    rs.getString("link"),
                    rs.getString("version"),
                    rs.getInt("complexity"),
                    rs.getInt("production_rates")
                    );
        } catch (SQLException e) {
            System.out.println("Error al leer los datos de la consulta: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public boolean insert(Farm f) {
        PreparedStatement ps = MySQLDatabaseUtils.getPreparedStatement(this.con, QUERY_INSERT_FARM);
        try {
            assert ps != null;
            ps.setString(1, f.getName());
            ps.setString(2, f.getUrlImg());
            ps.setString(3, f.getAuthor());
            ps.setString(4, f.getLink());
            ps.setString(5, f.getMinecraftVersion());
            ps.setInt(6, f.getBuildComplexity());
            ps.setInt(7, f.getProductionRates());
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return MySQLDatabaseUtils.executeUpdate(this.con, ps) > 0;
    }

    @Override
    public Farm read(int id) {
        String sql = String.format(QUERY_SELECT_FARM, id);
        return read(sql);
    }

    @Override
    public List<Farm> readAll() {
        return readAll(QUERY_SELECT_ALL_FARMS);
    }

    @Override
    public boolean update(Farm f) {
        PreparedStatement ps = MySQLDatabaseUtils.getPreparedStatement(this.con, QUERY_UPDATE_FARM);
        try {
            assert ps != null;
            ps.setString(1, f.getName());
            ps.setString(2, f.getUrlImg());
            ps.setString(3, f.getAuthor());
            ps.setString(4, f.getLink());
            ps.setString(5, f.getMinecraftVersion());
            ps.setInt(6, f.getBuildComplexity());
            ps.setInt(7, f.getProductionRates());
            ps.setInt(8,f.getId());
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return MySQLDatabaseUtils.executeUpdate(this.con, ps) > 0;
    }

    @Override
    public boolean delete(int id) {
        PreparedStatement ps = MySQLDatabaseUtils.getPreparedStatement(this.con, QUERY_DELETE_FARM);
        try {
            assert ps != null;
            ps.setInt(1, id);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return MySQLDatabaseUtils.executeUpdate(this.con, ps) > 0;
    }

    @Override
    public List<Farm> searchLike(String textPattern) {
        return MySQLDatabaseUtils.searchLike(this.con, this, "Farm", textPattern);
    }

}
``` 