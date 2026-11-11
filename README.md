# Student Information Management System

## Run the application

From the project directory, run `npm run gui`. The launch command includes
`lib/postgresql-42.7.14.jar` on both the compile and runtime classpaths so the
PostgreSQL JDBC driver is available when the application connects to the
database. The application opens the login screen; student pages use the account
that signed in.

In VS Code, select **Launch SIMS** in Run and Debug to use the project's
explicit Java launch configuration, which also includes the PostgreSQL driver.

The enrollment page seeds four sample subjects into the database when the app
starts, without duplicating them on later launches. The sample course names
match the subjects shown on the Grades page. The class schedule page reads
saved schedule entries from the database; if the signed-in student has none,
it shows a clearly labeled sample preview using those same subjects.