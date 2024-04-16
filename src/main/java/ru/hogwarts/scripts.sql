select s. * from student as s
where s.age > 11 and s.age <= 13

select student."name"  from student;

select s.* from student as s
where s."name" LIKE '%о%'

select s.* from student as s
where s.age  < s.id

select s.* from student as s
order by age