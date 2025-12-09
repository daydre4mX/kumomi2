# Employee Management System
Final Project for CS3350 Software Development at Georgia State University

An employee management system using MySQL that allows a hypothetical employee to access data about themselves or others in the database, and also allows an administrator to modify the database in a user-friendly way.

## Usage instructions
One must modify the config.properties within ```/src``` in order to match the settings corresponding to the user's MySQL database. The program assumes the port of the database is 3306.
```
# The url where the database is located
db.url=localhost
# The username to use when connecting to the database
db.user=(change)
# The password to use when connecting to the database
db.password=(change)
```

After this, the main entry point is through ```EMS.java```
