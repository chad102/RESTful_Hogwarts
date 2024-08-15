SELECT student.name, student.age, faculty.name
FROM student
FULL JOIN faculty

SELECT student.name, avatar.student_id
FROM student
INNER JOIN avatar ON avatar.student_id NOT NULL

