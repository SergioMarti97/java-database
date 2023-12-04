package examples;

import org.simple.db.mysql.utils.MySQLConnectionUtils;

import java.sql.Connection;
import java.time.LocalDate;

public class PersonDAOExample {

    public static void main(String[] args) {
        // Realizar conexión
        MySQLConnectionUtils conUtils = new MySQLConnectionUtils("C:\\Users\\Sergio\\IdeaProjects\\java-database\\app.properties");
        Connection con = conUtils.getConnection();

        // Capa de acceso de datos
        PersonDAO dao = new PersonDAO(con);

        // Insert
        dao.insert(new Person(-1, "Pepe", 183.00f, LocalDate.of(1997, 10, 31)));
        dao.insert(new Person(-1, "Luisa", 167.30f, LocalDate.of(1996, 11, 29)));
        dao.insert(new Person(-1, "Paco", 175.40f, LocalDate.of(2001, 2, 15)));
        dao.insert(new Person(-1, "Maria", 178.00f, LocalDate.of(1998, 8, 20)));

        // Leer
        var person = dao.read(2);
        System.out.println(person);

        for (var p : dao.readAll()) {
            System.out.println(p);
        }

        // Update
        person.setName("Magdalena");
        person.setBirthday(LocalDate.of(1996, 01, 28));
        dao.update(person);

        // Delete
        dao.delete(person.getId());

    }

}
