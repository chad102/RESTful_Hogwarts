-- liquibase formatted sql

-- changeset chad102:1
CREATE INDEX students_name_index ON student (name);

-- changeset chad102:2
CREATE INDEX faculty_name_and_color_index ON faculty (name, color);