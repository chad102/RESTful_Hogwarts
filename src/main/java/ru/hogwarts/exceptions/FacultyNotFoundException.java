package ru.hogwarts.exceptions;

public class FacultyNotFoundException extends RuntimeException {
    public FacultyNotFoundException(String message) {
        super (message);
    }
}
