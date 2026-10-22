# Student Information Management System

A Java Swing demo for student and administrator access to student information,
grades, schedules, and enrollment.

## Run

Run `npm run gui` or compile and start the Java application:

```powershell
javac -d bin src\*.java
java -cp bin main
```

## Demo access

- Student: choose **Student**, use `student@example.com`, and enter any
  non-empty password.
- Administrator: choose **Admin** and enter any non-empty email and password.

The student dashboard shows the sample student's profile, grades, schedule,
and enrollment. The administrator dashboard can add, edit, and delete records
in each section. Related grade, schedule, and enrollment entries are removed
when a student is deleted, and their student IDs are updated if the student ID
is edited.

This is a local demo: login does not authenticate credentials, sample and
edited records are held in memory, and all changes are lost when the app exits.
Do not use real student information or treat this as a production system.