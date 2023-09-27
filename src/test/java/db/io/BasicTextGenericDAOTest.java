package db.io;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class BasicTextGenericDAOTest {

    static class Person {

        int id;

        String name;

        float height;

        LocalDate birthday;

        public Person(int id, String name, float height, LocalDate birthday) {
            this.id = id;
            this.name = name;
            this.height = height;
            this.birthday = birthday;
        }

        @Override
        public String toString() {
            return String.format("%d %s %.3f %s", id, name, height, birthday.toString());
        }

    }

    static class BasicTestPersonDAO extends BasicTextGenericDAO<Person> {

        final String splitCharacter = ";";

        final String pattern = "^\\d+;[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð ,.'-]+;[+-]?([0-9]+([,][0-9]*)?|[,][0-9]+);[0-9]{4}-[0-9]{2}-[0-9]{2}";

        public BasicTestPersonDAO(String fileName) {
            super(fileName);
        }

        @Override
        public boolean isMalformed(String line) {
            return !Pattern.matches(pattern, line);
        }

        @Override
        public Person build(String line) {
            String[] split = line.split(splitCharacter);
            if (split.length == 4) {
                try {
                    int id = Integer.parseInt(split[0]);
                    String name = split[1];
                    float height = Float.parseFloat(split[2].replace(",", "."));
                    LocalDate birthday = LocalDate.parse(split[3]);
                    return new Person(id, name, height, birthday);
                } catch (NumberFormatException | DateTimeParseException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        public String write(Person o) {
            return "" + o.id + splitCharacter + o.name + splitCharacter + String.format("%.3f", o.height) + splitCharacter + o.birthday.toString();
        }

    }

    static final String fileName = "C:\\Users\\Sergio\\IdeaProjects\\java-database\\src\\test\\resources\\test.txt";

    static BasicTestPersonDAO dao = new BasicTestPersonDAO(fileName);

    static Random rnd = new Random(123);

    static String generateRandomString(int len) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz ";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(len)));
        }
        return sb.toString();
    }

    static LocalDate generateRandomLocalDate() {
        long minDay = LocalDate.of(1970, 1, 1).toEpochDay();
        long maxDay = LocalDate.now().toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }

    void populate() {
        // This method was used to populate the test file
        final int lenName = 20;
        final int numPersons = 10001;
        for (int i = 0; i < numPersons; i++) {
            Person p = new Person(i, generateRandomString(lenName), rnd.nextFloat(), generateRandomLocalDate());
            dao.insert(p);
        }
        dao.commit();
    }

    @Test
    void insert() {
        final int lenName = 20;
        Person p = new Person(dao.size(), generateRandomString(lenName), rnd.nextFloat(), generateRandomLocalDate());
        boolean result = dao.insert(p);
        assertTrue(result);
    }

    @Test
    void read() {
        int id = rnd.nextInt(dao.size());
        Person p = dao.read(id);
        System.out.println(p);
        assertNotNull(p);
    }

    @Test
    void readAll() {
        var persons = dao.readAll();
        System.out.println(persons.size());
        assertNotEquals(persons.size(), 0);
    }

    @Test
    void update() {
        final int lenName = 20;
        int id = rnd.nextInt(dao.size());
        Person p = dao.read(id);

        System.out.println("old: " + p);
        p.name = generateRandomString(lenName);
        p.height = rnd.nextFloat();
        p.birthday = generateRandomLocalDate();
        System.out.println("new: " + p);

        boolean result = dao.update(id, p);
        dao.commit();
        assertTrue(result);
    }

    @Test
    void delete() {
        int id = rnd.nextInt(dao.size());

        Person p = dao.read(id);
        System.out.println("to delete: " + p);

        boolean result = dao.delete(id);
        assertTrue(result);
    }


}