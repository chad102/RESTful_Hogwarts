package ru.hogwarts.exceptions;

public class StudentNotFoundException extends NotFoundException {
    private final long studentId;

    public StudentNotFoundException(long studentId) {
        this.studentId = studentId;
    }
    @Override
    public String getMessage() {
        return "Студент с id = %d не найден".formatted(studentId);
    }
}
