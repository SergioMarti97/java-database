package test;

import java.time.LocalDate;

public class Person {

    private int id;

    private String name;

    private float height;

    private LocalDate birthday;

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

    // Getters & Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

}
