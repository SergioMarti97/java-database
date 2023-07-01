package db;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtils {

    /**
     * Este método instancia un objeto Properties con la información del archivo indicado por parámetro
     * @param fileName el nombre del archivo .properties
     * @return objeto Properties
     */
    public static Properties getProperties(String fileName) {
        Properties properties = null;
        try {
            properties = new Properties();
            properties.load(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            System.out.println("Archivo: " + fileName + " no encontrado");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + fileName);
            e.printStackTrace();
        }
        return properties;
    }

}
