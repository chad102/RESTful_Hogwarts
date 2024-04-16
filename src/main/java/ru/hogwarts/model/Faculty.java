package ru.hogwarts.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.Collection;
import java.util.List;

@Entity
public class Faculty {
    @Id
    @GeneratedValue
    private int id;

    private String name;
    private String color;
    @JsonIgnore
    @OneToMany(mappedBy = "faculty")
    private List<Student> students;
    public Faculty(int id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Faculty(){

    }

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Student> getStudents() {
        return students;
    }
}
