package examples;

import org.simple.db.mysql.relationships.ManyToMany;
import org.simple.db.mysql.relationships.simple.MySQLSimpleManyToManyRelationship;
import org.simple.db.mysql.utils.MySQLConnectionUtils;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class PersonManyToManyExample {

    public static PersonDAO dao;

    public static MySQLSimpleManyToManyRelationship mm;

    public static List<Person> getFriends(Person person) {
        List<Person> friends = new ArrayList<>();
        List<ManyToMany> list = mm.getLeftToRight().readManyToMany(person.getId());
        for (var mtm : list) {
            friends.add(dao.read(mtm.getIdRight()));
        }
        return friends;
    }

    public static void updateFriendsCache(List<Person> people) {
        for (var person : people) {
            var friends = getFriends(person);
            person.getFriends().clear();
            person.getFriends().addAll(friends);
        }
    }

    public static void showPerson(Person person) {
        StringBuilder out = new StringBuilder();
        out.append(person.getName()).append(", ");
        if (person.getFriends().isEmpty()) {
            out.append("no tiene amigos.\n");
        } else {
            out.append("es amigo de:\n");
            for (var friend : person.getFriends()) {
                out.append(friend.getName()).append('\n');
            }
        }
        System.out.println(out);
    }

    public static void showPeople(List<Person> people) {
        updateFriendsCache(people);
        System.out.println("~~~ Grupo de amigos ~~~");
        for (var person : people)
            showPerson(person);
    }

    public static void addFriend(Person person, Person friend) {
        System.out.printf("Ejecutar insert: %s es amigo de %s\n", person.getName(), friend.getName());
        mm.getLeftToRight().insert(person.getId(), friend.getId());
    }

    public static void updateFriend(Person person, Person oldFriend, Person newFriend) {
        System.out.printf("Ejecutar update: %s era amigo de %s y ahora es amigo de %s\n",
                person.getName(),
                oldFriend.getName(),
                newFriend.getName());
        mm.getLeftToRight().update(person.getId(), oldFriend.getId(), person.getId(), newFriend.getId());
    }

    public static void deleteFriend(Person person, Person friend) {
        System.out.printf("Ejecutar delete: %s ya no es amigo de %s\n", person.getName(), friend.getName());
        mm.getLeftToRight().delete(person.getId(), friend.getId());
    }

    public static void main(String[] args) {
        String propertiesFile = "C:\\Users\\Sergio\\IdeaProjects\\java-database\\app.properties";
        Connection con = new MySQLConnectionUtils(propertiesFile).getConnection();

        dao = new PersonDAO(con);
        mm = new MySQLSimpleManyToManyRelationship(con,
                "person_has_friend",
                "person_id",
                "friend_id");

        List<Person> people = dao.readAll();
        var pepe = people.get(0); // Pepe
        var paco = people.get(1); // Paco
        var maria = people.get(2); // Maria

        // Read
        // Mostrar la info
        showPeople(people);

        // Insert
        addFriend(paco, maria);
        showPeople(people);

        // Update
        updateFriend(maria, paco, pepe);
        showPeople(people);

        // Delete
        deleteFriend(paco, pepe);
        showPeople(people);

    }

}
