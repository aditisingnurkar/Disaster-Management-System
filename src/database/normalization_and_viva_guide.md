# Disaster Management System - Database Design & Normalization Guide

---

## 1. Executive Summary & Architecture

The **Disaster Management System** is a normalized relational database application designed to streamline disaster response coordination, shelter allocations, resource logistics, victim tracking, volunteer mobilization, and incident logging during natural or human-made disasters.

The database is built on **MySQL 8.0+** using the **InnoDB storage engine** with strict foreign key constraints, composite primary keys for associative entities (junction tables), check constraints, and performance indexes.

---

## 2. Entity-Relationship (ER) Overview & Cardinality

### Summary Table of Relationships

| # | Relationship | Cardinality | Primary Key (PK) | Foreign Key(s) (FK) | Business Meaning |
|---|---|---|---|---|---|
| 1 | `DISASTER` ── `VICTIM` | **1 : M** | `VICTIM(victim_id)` | `VICTIM.disaster_id` → `DISASTER` | One disaster impacts multiple victims. |
| 2 | `DISASTER` ── `SHELTER` | **1 : M** | `SHELTER(shelter_id)` | `SHELTER.disaster_id` → `DISASTER` | One disaster has multiple emergency shelters established. |
| 3 | `SHELTER` ── `VICTIM` | **1 : M** | `VICTIM(victim_id)` | `VICTIM.shelter_id` → `SHELTER` | One shelter accommodates many victims. |
| 4 | `SHELTER` ── `RESOURCE` | **M : N** | `SHELTER_INVENTORY(shelter_id, resource_id)` | `shelter_id` → `SHELTER`, `resource_id` → `RESOURCE` | Resolved via `SHELTER_INVENTORY`. Tracks quantities per shelter-resource pair. |
| 5 | `VOLUNTEER` ── `DISASTER` | **M : N** | `VOLUNTEER_ASSIGNMENT(volunteer_id, disaster_id)` | `volunteer_id` → `VOLUNTEER`, `disaster_id` → `DISASTER` | Resolved via `VOLUNTEER_ASSIGNMENT`. Tracks volunteer roles and shift timings. |
| 6 | `DISASTER` ── `INCIDENT_REPORT` | **1 : M** | `INCIDENT_REPORT(report_id)` | `disaster_id` → `DISASTER` | One disaster generates multiple field incident reports. |
| 7 | `VOLUNTEER` ── `INCIDENT_REPORT` | **1 : M** | `INCIDENT_REPORT(report_id)` | `volunteer_id` → `VOLUNTEER` | One volunteer can submit multiple incident reports. |

---

## 3. Database Normalization Process (UNF → 1NF → 2NF → 3NF)

### 3.1 Unnormalized Form (UNF)
An unnormalized disaster management table would store repeating groups, multi-valued attributes, and mixed concepts in a single flat record:

```text
FLAT_DISASTER_RECORD(
    DisasterID, DisasterType, Location, StartDate, EndDate, Severity,
    VictimNames[], VictimAges[], VictimMedical[],
    ShelterNames[], ShelterLocations[], ShelterCapacities[],
    ResourceNames[], ResourceQuantities[],
    VolunteerNames[], VolunteerRoles[], VolunteerShifts[],
    IncidentDescriptions[], IncidentStatuses[]
)
```
**Anomalies in UNF:**
- **Insertion Anomaly:** Cannot add a resource type (e.g. "Thermal Blanket") without having an existing shelter or disaster to attach it to.
- **Deletion Anomaly:** Deleting a victim record might inadvertently delete shelter or volunteer information.
- **Update Anomaly:** If a shelter coordinator's phone number changes, it would have to be updated across dozens of duplicated records.

---

### 3.2 First Normal Form (1NF)
**Rule:** A relation is in 1NF if and only if all domain values are atomic (no repeating groups, no comma-separated arrays) and each record has a distinct primary key.

**Transformation:**
- Eliminate repeating groups.
- Split multi-valued fields into individual rows and discrete entity relations.
- Assign primary keys to each table: `DISASTER(disaster_id)`, `SHELTER(shelter_id)`, `VICTIM(victim_id)`, `RESOURCE(resource_id)`, `VOLUNTEER(volunteer_id)`, `INCIDENT_REPORT(report_id)`.

---

### 3.3 Second Normal Form (2NF)
**Rule:** A relation is in 2NF if it is in 1NF and **every non-prime attribute is fully functionally dependent on the entire primary key** (no partial functional dependencies on composite keys).

**Resolution of Partial Dependencies in Junction Tables:**

#### Example 1: Shelter Inventory
- **Bad Design:** `INVENTORY(shelter_id, resource_id, shelter_name, resource_name, unit, quantity)`
  - `shelter_id, resource_id` is the Composite PK.
  - Partial Dependency: `resource_name, unit` depend **only** on `resource_id`, not on `shelter_id`.
  - Partial Dependency: `shelter_name` depends **only** on `shelter_id`.
- **2NF Solution:**
  1. `RESOURCE(resource_id [PK], resource_name, description, unit)` — attributes depend purely on `resource_id`.
  2. `SHELTER(shelter_id [PK], shelter_name, address, capacity, ...)` — attributes depend purely on `shelter_id`.
  3. `SHELTER_INVENTORY(shelter_id [PK, FK], resource_id [PK, FK], quantity)` — `quantity` depends on the **entire composite key** `(shelter_id, resource_id)`.

#### Example 2: Volunteer Assignment
- **Bad Design:** `ASSIGNMENT(volunteer_id, disaster_id, volunteer_name, volunteer_phone, disaster_type, role, shift_timing)`
  - `volunteer_name` depends only on `volunteer_id` (Partial dependency).
  - `disaster_type` depends only on `disaster_id` (Partial dependency).
- **2NF Solution:**
  1. `VOLUNTEER(volunteer_id [PK], name, age, gender, contact_number, skills, availability_status)`
  2. `DISASTER(disaster_id [PK], disaster_type, location, start_date, end_date, severity)`
  3. `VOLUNTEER_ASSIGNMENT(volunteer_id [PK, FK], disaster_id [PK, FK], role, shift_timing)` — `role` and `shift_timing` depend on the **whole composite key** `(volunteer_id, disaster_id)`.

---

### 3.4 Third Normal Form (3NF)
**Rule:** A relation is in 3NF if it is in 2NF and **no non-prime attribute is transitively dependent on the primary key** (if $X \rightarrow Y$ and $Y \rightarrow Z$, then $Z$ must be placed in a separate table where $Y$ is the primary key).

**Resolution of Transitive Dependencies:**

#### Example: Victims and Shelters
- **Bad Design:** `VICTIM(victim_id [PK], name, age, shelter_id, shelter_name, shelter_address, coordinator_name)`
  - Functional Dependencies: `victim_id` $\rightarrow$ `shelter_id` $\rightarrow$ `(shelter_name, shelter_address, coordinator_name)`.
  - Transitive Dependency: Shelter details depend on `shelter_id`, which in turn depends on `victim_id`.
- **3NF Solution:**
  - Remove shelter attributes from `VICTIM`.
  - Retain only the foreign key `shelter_id` in `VICTIM`:
    - `VICTIM(victim_id [PK], name, age, gender, contact_number, medical_condition, disaster_id [FK], shelter_id [FK])`
    - `SHELTER(shelter_id [PK], shelter_name, address, capacity, coordinator_name, contact_number, disaster_id [FK])`
  - When shelter name is needed, perform an **INNER/LEFT JOIN** on `shelter_id`.

---

## 4. Final 8-Table Schema Summary

```text
1. DISASTER(disaster_id [PK], disaster_type, location, start_date, end_date, severity)
2. SHELTER(shelter_id [PK], shelter_name, address, capacity, coordinator_name, contact_number, disaster_id [FK])
3. VICTIM(victim_id [PK], name, age, gender, contact_number, medical_condition, disaster_id [FK], shelter_id [FK])
4. RESOURCE(resource_id [PK], resource_name, description, unit)
5. SHELTER_INVENTORY(shelter_id [PK, FK], resource_id [PK, FK], quantity)
6. VOLUNTEER(volunteer_id [PK], name, age, gender, contact_number, skills, availability_status)
7. VOLUNTEER_ASSIGNMENT(volunteer_id [PK, FK], disaster_id [PK, FK], role, shift_timing)
8. INCIDENT_REPORT(report_id [PK], description, report_time, report_type, status, disaster_id [FK], volunteer_id [FK])
```

---

## 5. DBMS Viva Questions & Answers

### Q1: What is the degree and cardinality of the relationships in this system?
- **Degree:** All relationships are binary (connecting 2 entity types).
- **Cardinality:**
  - `DISASTER 1 : M VICTIM` (1 disaster affects many victims; each victim record is associated with 1 disaster).
  - `DISASTER 1 : M SHELTER` (1 disaster has many shelters).
  - `SHELTER 1 : M VICTIM` (1 shelter accommodates many victims).
  - `DISASTER M : N VOLUNTEER` (Many volunteers work on many disasters, resolved by `VOLUNTEER_ASSIGNMENT`).
  - `SHELTER M : N RESOURCE` (Many shelters stock many resources, resolved by `SHELTER_INVENTORY`).
  - `DISASTER 1 : M INCIDENT_REPORT` & `VOLUNTEER 1 : M INCIDENT_REPORT`.

### Q2: Why is `SHELTER_INVENTORY` a composite primary key table?
Because a specific resource quantity (e.g. 500 bottles of water) cannot be identified by `shelter_id` alone (a shelter has multiple resources) nor by `resource_id` alone (a resource is present in multiple shelters). The quantity depends on the **combination** `(shelter_id, resource_id)`. The composite primary key also inherently prevents duplicate rows for the same resource at the same shelter.

### Q3: What is the difference between `VOLUNTEER_ASSIGNMENT` and `INCIDENT_REPORT`?
- `VOLUNTEER_ASSIGNMENT` is a **many-to-many relationship junction entity** indicating *which volunteer is assigned to which disaster, in what role, and on what shift*.
- `INCIDENT_REPORT` is a **transactional event log entity** indicating *what incident occurred during a disaster (e.g., Road Blockage, Bridge Collapse) and which volunteer logged it*.

### Q4: How are referential integrity and cascading actions handled?
- When a `DISASTER` is deleted, all dependent `SHELTER`, `VICTIM`, `VOLUNTEER_ASSIGNMENT`, and `INCIDENT_REPORT` records are deleted automatically via `ON DELETE CASCADE`.
- When a `SHELTER` is deleted, victim records are preserved with `shelter_id` set to `NULL` via `ON DELETE SET NULL` so victim records are not lost.
- All foreign keys enforce `ON UPDATE CASCADE` to synchronize ID modifications.

### Q5: What SQL aggregate functions and clauses are demonstrated?
- `COUNT()`, `SUM()`, `AVG()`, `MIN()`, `MAX()`
- `GROUP BY` and `HAVING` (e.g., finding shelters with total inventory > 1000 items)
- `INNER JOIN`, `LEFT JOIN` (multi-table 3-way and 4-way joins)
- Subqueries: Correlated subqueries and nested subqueries with `IN` and `NOT IN`.
