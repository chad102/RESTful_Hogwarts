package ru.hogwarts.exceptions;

public class FacultyNotFoundException extends NotFoundException {
    private final int facultyId;
    public FacultyNotFoundException(int facultyId) {
        this.facultyId = facultyId;
    }
    @Override
    public String getMessage() {
        return "Факультет с id = %d не найден".formatted(facultyId);
    }
}
