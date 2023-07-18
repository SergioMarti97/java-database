package db.mysql;

import db.PropertiesUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySQLConnectionUtils {

    private String ip;

    private String db;

    private String user;

    private String password;

    private Connection connection;

    public MySQLConnectionUtils(String ip, String db, String user, String password) {
        this.ip = ip;
        this.db = db;
        this.user = user;
        this.password = password;
    }

    public MySQLConnectionUtils(Properties properties) {
        this.ip = properties.getProperty("ip");
        this.db = properties.getProperty("db");
        this.user = properties.getProperty("user");
        this.password = properties.getProperty("password");
    }

    public MySQLConnectionUtils(String propertiesFiles) {
        Properties properties = PropertiesUtils.getProperties(propertiesFiles);
        if (!properties.isEmpty()) {
            this.ip = properties.getProperty("ip");
            this.db = properties.getProperty("db");
            this.user = properties.getProperty("user");
            this.password = properties.getProperty("password");
        }
    }

    public void connect() {
        try {
            String dbURL = this.getDbURL();
            this.connection = DriverManager.getConnection(dbURL, this.user, this.password);
            System.out.println("La conexión con la base de datos " + this.db + " ha sido existosa");
        } catch (SQLException var2) {
            System.out.println("No se pudo conectar con la base de datos.");
            var2.printStackTrace();
        }
    }

    public boolean connect(String userName, String password) {
        try {
            String dbURL = this.getDbURL();
            this.connection = DriverManager.getConnection(dbURL, userName, password);
            System.out.println("La conexión con la base de datos: " + this.db + " ha sido exitosa");
            return true;
        } catch (SQLException var4) {
            System.out.println("No se ha podido conectar con la base de datos: " + var4.getMessage());
            return false;
        }
    }

    private String getDbURL() {
        return "jdbc:mysql://" + this.ip + "/" + this.db + "?" + "characterEncoding=latin1";
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDb() {
        return this.db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Connection getConnection() {
        if (this.connection == null) {
            this.connect();
        }

        return this.connection;
    }

}
