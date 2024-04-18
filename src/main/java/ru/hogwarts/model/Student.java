package ru.hogwarts.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String name;
    private int age;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "faculty_Id")
    private Faculty faculty;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }
    public Faculty getFaculty() {
        return faculty;
    }
}
