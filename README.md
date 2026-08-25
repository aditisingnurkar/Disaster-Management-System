# Disaster Management System

A complete, normalized **Relational Database Management System** for disaster coordination, emergency shelters, supply logistics, victim registration, volunteer mobilization, and field incident logging.

## 1. Project Structure

```text
src/main/java/     Java application source
database/          MySQL schema, seed data, and queries
assets/            Runtime images
lib/               MySQL JDBC driver
scripts/           Windows launch scripts
docs/              ERD, normalization material, and project PDF
target/classes/    Compiled output (generated)
```

---

## 2. Database Architecture (8 Core 3NF Tables)

```
                       ┌──────────────┐
                       │   DISASTER   │
                       └──────┬───────┘
                              │
     ┌────────────────────────┼────────────────────────┬──────────────────────┐
     │ 1:M                    │ 1:M                    │ 1:M                  │ 1:M
     ▼                        ▼                        ▼                      ▼
┌──────────┐            ┌───────────┐           ┌──────────────┐     ┌──────────────────────┐
│  VICTIM  │◄───────────┤  SHELTER  │           │INCIDENT_REPORT│    │ VOLUNTEER_ASSIGNMENT │
└──────────┘  1:M       └─────┬─────┘           └──────┬───────┘     └──────────┬───────────┘
                              │ 1:M                    │ M:1                    │ M:1
                              ▼                        │                        ▼
                     ┌─────────────────┐               │                  ┌───────────┐
                     │SHELTER_INVENTORY│               └─────────────────►│ VOLUNTEER │
                     └────────┬────────┘                                  └───────────┘
                              │ M:1
                              ▼
                        ┌──────────┐
                        │ RESOURCE │
                        └──────────┘
```

### Table Definitions & Primary Keys

1. **`DISASTER`**
   - `disaster_id` (**PK**, Auto Increment)
   - `disaster_type`, `location`, `start_date`, `end_date`, `severity`
2. **`SHELTER`**
   - `shelter_id` (**PK**, Auto Increment)
   - `shelter_name`, `address`, `capacity`, `coordinator_name`, `contact_number`
   - `disaster_id` (**FK** → `DISASTER`)
3. **`VICTIM`**
   - `victim_id` (**PK**, Auto Increment)
   - `name`, `age`, `gender`, `contact_number`, `medical_condition`
   - `disaster_id` (**FK** → `DISASTER`), `shelter_id` (**FK** → `SHELTER`)
4. **`RESOURCE`**
   - `resource_id` (**PK**, Auto Increment)
   - `resource_name` (UNIQUE), `description`, `unit`
5. **`SHELTER_INVENTORY`** *(Junction Table resolving SHELTER M:N RESOURCE)*
   - `shelter_id` (**PK, FK** → `SHELTER`), `resource_id` (**PK, FK** → `RESOURCE`)
   - `quantity`
   - **Composite Primary Key:** `(shelter_id, resource_id)`
6. **`VOLUNTEER`**
   - `volunteer_id` (**PK**, Auto Increment)
   - `name`, `age`, `gender`, `contact_number`, `skills`, `availability_status`
7. **`VOLUNTEER_ASSIGNMENT`** *(Junction Table resolving VOLUNTEER M:N DISASTER)*
   - `volunteer_id` (**PK, FK** → `VOLUNTEER`), `disaster_id` (**PK, FK** → `DISASTER`)
   - `role`, `shift_timing`
   - **Composite Primary Key:** `(volunteer_id, disaster_id)`
8. **`INCIDENT_REPORT`**
   - `report_id` (**PK**, Auto Increment)
   - `description`, `report_time`, `report_type`, `status`
   - `disaster_id` (**FK** → `DISASTER`), `volunteer_id` (**FK** → `VOLUNTEER`)

---

## 3. Quick Setup Guide

### Step 1: Import Database in MySQL
Open MySQL Workbench, MySQL CLI, or phpMyAdmin and execute:
```sql
SOURCE database/schema.sql;
SOURCE database/seed.sql;
```
Or via terminal:
```bash
mysql -u root -p < database/schema.sql
mysql -u root -p disaster_management < database/seed.sql
```

### Step 2: Configure Database Credentials
You can configure credentials using either:
1. `db.properties` file in the repository root:
   ```properties
   db.host=localhost
   db.port=3306
   db.name=disaster_management
   db.user=root
   db.password=YOUR_MYSQL_PASSWORD
   ```
2. Environment variables (`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`).
3. Open the GUI and click **"⚙ DB Settings"** in the top right corner of the dashboard.

### Step 3: Run the Application
- **GUI Mode**: Double-click `scripts/run_gui.bat`.
- **CLI Mode**: Double-click `scripts/run_cli.bat` or run from the repository root:
  ```bash
   java -cp "target/classes;lib/mysql-connector-j-9.4.0.jar" ManagementSystem
  ```

Verify the connection and schema before launching:
```powershell
$sources = Get-ChildItem src/main/java -Recurse -Filter *.java
javac -cp ".;lib/mysql-connector-j-9.4.0.jar" -d target/classes $sources.FullName
java -cp "target/classes;lib/mysql-connector-j-9.4.0.jar" TestConnection
```

---

## 4. Key DBMS Features for Viva Presentation

- **Live SQL Dashboard Metrics:** Total Disasters, Victims, Shelters, Volunteers, Inventory Stock, and Open Incidents are computed directly using `COUNT()`, `SUM()`, and `GROUP BY` queries in MySQL.
- **Interactive DBMS Query Runner (`DBMSQueryRunnerUI`):** Demonstrates 10+ preset SQL queries (Multi-table 3NF JOINs, Subqueries, Group By with Having, Aggregations) during your project presentation.
- **Foreign Key Dropdowns (`JComboBox<ComboItem>`):** Ensures users select existing entities without typing raw IDs.
- **Business Rule Enforcement:** Incident reports validate volunteer assignment to the selected disaster.
- **Complete SQL Script Files:**
  - `database/schema.sql` (Full DDL with constraints and indexes)
  - `database/seed.sql` (Realistic sample data)
  - `database/queries.sql` (25+ demonstration queries)
   - `database/normalization_and_viva_guide.md` (Detailed 1NF → 2NF → 3NF breakdown and Viva Q&A)
