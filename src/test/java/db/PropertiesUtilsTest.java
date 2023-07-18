package db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesUtilsTest {

    @Test
    @DisplayName("Lee un archivo \"app.properties\"")
    void loadPropertiesFile() {
        Properties properties = PropertiesUtils.getProperties("app.properties");
        assertTrue(properties.size() > 0);
    }

}