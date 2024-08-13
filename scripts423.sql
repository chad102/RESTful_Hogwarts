SELECT student.name, student.age, faculty.name
FROM student
RIGHT JOIN faculty ON student.avatar NOT NULL

SELECT student.name, student.age, faculty.name, avatar.student_id
FROM student
RIGHT JOIN faculty ON avatar.student_id NOT NULL

Комментарий: Не уверен какой из вариантов правильный, но склоняюсь больше ко второму, так как у студента нет поля avatar
а у avatar есть поле student_id. И еще смутило наличие трёх таблиц.