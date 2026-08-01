# Disaster Management System

A Java Swing desktop application for coordinating disaster response operations. The system manages disasters, victims, shelters, volunteers, resources, shelter inventory, and incident reports using a MySQL database backend.

Demo Video: https://drive.google.com/file/d/1cFpz6FmkIeaVo-H-PHHhfl2engojQXx-/view

---

## Features

* **Disaster Management** – create, update, delete, and view disaster records
* **Victim Management** – register victims, assign shelters, and store medical requirements
* **Shelter Management** – track shelter capacity, coordinators, and linked disasters
* **Volunteer Management** – maintain volunteer profiles, skills, and disaster assignments
* **Resource Management** – manage relief supplies and their locations
* **Shelter Inventory** – monitor resource allocation across shelters
* **Incident Reporting** – record field incidents linked to disasters and volunteers
* **Dashboard UI** – centralized navigation for all modules

---

## Tech Stack

| Component   | Technology              |
| ----------- | ----------------------- |
| Language    | Java                    |
| UI          | Java Swing              |
| Database    | MySQL                   |
| JDBC Driver | MySQL Connector/J 9.4.0 |

---

## Project Structure

```text
Disaster-Management-System/
├── ERD_Normalisation/
│   ├── ER_diagram.jpeg
│   └── Normalisation_file.pdf
├── lib/
│   └── mysql-connector-j-9.4.0.jar
├── src/
│   ├── DisasterManagement.java
│   ├── DashboardUI.java
│   ├── DatabaseConnection.java
│   ├── SplashScreen.java
│   ├── DisasterUI.java
│   ├── VictimUI.java
│   ├── ShelterUI.java
│   ├── VolunteerUI.java
│   ├── Volunteer_AssignmentsUI.java
│   ├── ResourceUI.java
│   ├── ShelterInventoryUI.java
│   ├── IncidentReportUI.java
│   ├── TestConnection.java
│   └── qr_code.png
└── DisasterM Queries and solution .pdf
```

---

## Database Setup

Run the SQL queries provided in:

```text
DisasterM Queries and solution .pdf
```

This creates the `disaster_management_system` database and all required tables.

The ER diagram and normalization details are available in the `ERD_Normalisation/` folder.

---

## Configuration

Update the database credentials in `src/DatabaseConnection.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/disaster_management_system";
private static final String USER = "your_username";
private static final String PASSWORD = "your_password";
```

---

## Compile and Run

### Compile

```bash
javac -cp lib/mysql-connector-j-9.4.0.jar -d out src/*.java
```

### Run the Application

**Windows**

```bash
java -cp "out;lib/mysql-connector-j-9.4.0.jar" DisasterManagement
```

**Linux / macOS**

```bash
java -cp "out:lib/mysql-connector-j-9.4.0.jar" DisasterManagement
```

### Test Database Connectivity

**Windows**

```bash
java -cp "out;lib/mysql-connector-j-9.4.0.jar" TestConnection
```

**Linux / macOS**

```bash
java -cp "out:lib/mysql-connector-j-9.4.0.jar" TestConnection
```

---

Contributors
@aditiaingnurkar
@ankitanahire
@Jui-baviskar
@bhoomishekapure

