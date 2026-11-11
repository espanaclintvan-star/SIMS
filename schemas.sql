create table if not exists login (
	login_id int generated always as identity primary key,
	username varchar(50) not null unique,
	passwords varchar(50) not null,
	roles varchar(50)not null
);
create table if not exists StudentsInfo(
	student_id int primary key,
	s_name varchar(50),
	s_year varchar(50),
	status varchar(50),
	address varchar(50),
	contact_no varchar(50),
	login_id int,
	foreign key (login_id) references login(login_id)
);
create table if not exists AdminInfo(
	admin_id int primary key,
	a_name varchar(50),
	status varchar(50),
	address varchar(50),
	contact_no varchar(50),
	login_id int,
	foreign key (login_id) references login(login_id)
);

create table if not exists courses(
	course_id int generated always as identity primary key,
	course_name varchar (50)
);

INSERT INTO courses (course_name)
SELECT subjects.course_name
FROM (VALUES
	('Programming Fundamentals'),
	('Database Management Systems'),
	('Web Development'),
	('Computer Networks')
) AS subjects(course_name)
WHERE NOT EXISTS (
	SELECT 1
	FROM courses
	WHERE LOWER(courses.course_name) = LOWER(subjects.course_name)
);

create table if not exists  grades(
	grade_id int generated always as identity primary key,
	student_id int,
	course_id int,
	grade NUMERIC(4, 2) CHECK (grade >= 1.0 AND grade <= 5.0),
	foreign key (student_id) references studentsinfo(student_id),
	foreign key (course_id) references courses(course_id)
);

create table if not exists clearances(
	clearance_id int generated always as identity primary key,
	student_id int,
	c_name varchar(1000),
	status varchar(50),
	foreign key (student_id) references studentsinfo(student_id)
);

create table if not exists enrollments(
	enrollment_id int generated always as identity primary key,
	student_id int,
	status varchar(50),
	course_id int,
	foreign key (student_id) references studentsinfo(student_id),
	foreign key (course_id) references courses(course_id)
);

create table if not exists schedules(
	schedule_id int generated always as identity primary key,
	student_id int,
	schedule TIMESTAMPTZ NOT NULL,
	course_id int,
	foreign key (student_id) references studentsinfo(student_id),
	foreign key (course_id) references courses(course_id)
);

WITH missing_students AS (
	SELECT l.login_id, ROW_NUMBER() OVER (ORDER BY l.login_id) AS row_number
	FROM login l
	LEFT JOIN StudentsInfo s ON s.login_id = l.login_id
	WHERE l.roles = 'student' AND s.login_id IS NULL
), next_student_id AS (
	SELECT COALESCE(MAX(student_id), 0) AS max_id FROM StudentsInfo
)
INSERT INTO StudentsInfo (student_id, login_id)
SELECT next_student_id.max_id + missing_students.row_number, missing_students.login_id
FROM missing_students CROSS JOIN next_student_id;
