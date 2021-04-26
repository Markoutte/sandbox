package me.markoutte.sandbox.db.dao;

import javax.persistence.*;

@Entity
@Table(name = "owner2")
public class Owner2 {

    private int id;
    private String name;
    private int age;

    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
