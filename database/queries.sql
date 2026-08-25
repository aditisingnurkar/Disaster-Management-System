-- ====================================================================
-- DISASTER MANAGEMENT SYSTEM
-- Comprehensive DBMS Demonstration Queries
-- Covers: Basic DML, Aggregates, Grouping, 3NF Joins, Subqueries
-- ====================================================================

USE disaster_management;

-- ====================================================================
-- SECTION 1: BASIC QUERIES (SELECT, WHERE, ORDER BY, DISTINCT, LIKE, BETWEEN)
-- ====================================================================

-- 1.1 List all active/ongoing disasters (end_date is NULL) ordered by severity
SELECT disaster_id, disaster_type, location, start_date, severity
FROM DISASTER
WHERE end_date IS NULL
ORDER BY FIELD(severity, 'Critical', 'High', 'Medium', 'Low');

-- 1.2 Find distinct disaster types currently recorded
SELECT DISTINCT disaster_type
FROM DISASTER;

-- 1.3 Search for volunteers who possess 'First Aid' in their skills
SELECT volunteer_id, name, age, contact_number, skills, availability_status
FROM VOLUNTEER
WHERE skills LIKE '%First Aid%';

-- 1.4 Find all victims aged between 18 and 60 with medical conditions
SELECT victim_id, name, age, gender, medical_condition
FROM VICTIM
WHERE age BETWEEN 18 AND 60
  AND medical_condition NOT LIKE '%None%'
ORDER BY age ASC;

-- 1.5 Retrieve all open or in-progress incident reports
SELECT report_id, report_type, status, report_time, description
FROM INCIDENT_REPORT
WHERE status IN ('Open', 'In Progress')
ORDER BY report_time DESC;


-- ====================================================================
-- SECTION 2: AGGREGATE FUNCTIONS (COUNT, SUM, AVG, MIN, MAX)
-- ====================================================================

-- 2.1 Overall System Summary Statistics
SELECT 
    (SELECT COUNT(*) FROM DISASTER) AS total_disasters,
    (SELECT COUNT(*) FROM VICTIM) AS total_victims,
    (SELECT COUNT(*) FROM SHELTER) AS total_shelters,
    (SELECT COUNT(*) FROM VOLUNTEER) AS total_volunteers,
    (SELECT SUM(quantity) FROM SHELTER_INVENTORY) AS total_inventory_units,
    (SELECT COUNT(*) FROM INCIDENT_REPORT WHERE status = 'Open') AS open_incidents;

-- 2.2 Calculate Shelter Capacity Metrics (Total, Average, Min, Max Capacity)
SELECT 
    COUNT(*) AS total_shelters,
    SUM(capacity) AS total_bed_capacity,
    AVG(capacity) AS average_capacity,
    MIN(capacity) AS smallest_shelter_capacity,
    MAX(capacity) AS largest_shelter_capacity
FROM SHELTER;

-- 2.3 Victim Demographics Aggregations (Average, Min, Max Age)
SELECT 
    COUNT(*) AS total_registered_victims,
    ROUND(AVG(age), 1) AS average_victim_age,
    MIN(age) AS youngest_victim_age,
    MAX(age) AS oldest_victim_age
FROM VICTIM;


-- ====================================================================
-- SECTION 3: GROUP BY AND HAVING CLAUSES
-- ====================================================================

-- 3.1 Count victims grouped by disaster type
SELECT 
    d.disaster_type,
    d.location,
    COUNT(v.victim_id) AS victim_count
FROM DISASTER d
LEFT JOIN VICTIM v ON d.disaster_id = v.disaster_id
GROUP BY d.disaster_id, d.disaster_type, d.location
ORDER BY victim_count DESC;

-- 3.2 Total resource quantities aggregated across all shelters
SELECT 
    r.resource_id,
    r.resource_name,
    r.unit,
    COALESCE(SUM(si.quantity), 0) AS total_quantity_stocked
FROM RESOURCE r
LEFT JOIN SHELTER_INVENTORY si ON r.resource_id = si.resource_id
GROUP BY r.resource_id, r.resource_name, r.unit
ORDER BY total_quantity_stocked DESC;

-- 3.3 Find shelters with inventory quantities exceeding 1,000 units (HAVING)
SELECT 
    s.shelter_id,
    s.shelter_name,
    SUM(si.quantity) AS total_items_stocked
FROM SHELTER s
JOIN SHELTER_INVENTORY si ON s.shelter_id = si.shelter_id
GROUP BY s.shelter_id, s.shelter_name
HAVING SUM(si.quantity) > 1000
ORDER BY total_items_stocked DESC;

-- 3.4 Group incident reports by status and report type
SELECT 
    report_type,
    status,
    COUNT(*) AS incident_count
FROM INCIDENT_REPORT
GROUP BY report_type, status
ORDER BY report_type;


-- ====================================================================
-- SECTION 4: JOINS (INNER JOIN, LEFT JOIN, MULTI-TABLE 3NF JOINS)
-- ====================================================================

-- 4.1 Victims with their associated Disaster and Shelter details (3-Table JOIN)
SELECT 
    v.victim_id,
    v.name AS victim_name,
    v.age,
    v.gender,
    v.medical_condition,
    d.disaster_type,
    d.location AS disaster_location,
    COALESCE(s.shelter_name, 'Unassigned / Field') AS shelter_name
FROM VICTIM v
INNER JOIN DISASTER d ON v.disaster_id = d.disaster_id
LEFT JOIN SHELTER s ON v.shelter_id = s.shelter_id
ORDER BY v.victim_id;

-- 4.2 Shelter Occupancy vs Capacity (Shelter joined with Victim count)
SELECT 
    s.shelter_id,
    s.shelter_name,
    d.disaster_type,
    s.capacity AS max_capacity,
    COUNT(v.victim_id) AS current_occupancy,
    (s.capacity - COUNT(v.victim_id)) AS available_space,
    ROUND((COUNT(v.victim_id) / s.capacity) * 100, 2) AS occupancy_percentage
FROM SHELTER s
JOIN DISASTER d ON s.disaster_id = d.disaster_id
LEFT JOIN VICTIM v ON s.shelter_id = v.shelter_id
GROUP BY s.shelter_id, s.shelter_name, d.disaster_type, s.capacity
ORDER BY occupancy_percentage DESC;

-- 4.3 Volunteer Assignments (Resolving M:N Volunteer - Disaster)
SELECT 
    vol.volunteer_id,
    vol.name AS volunteer_name,
    vol.contact_number,
    d.disaster_type,
    d.location AS disaster_location,
    va.role,
    va.shift_timing
FROM VOLUNTEER_ASSIGNMENT va
INNER JOIN VOLUNTEER vol ON va.volunteer_id = vol.volunteer_id
INNER JOIN DISASTER d ON va.disaster_id = d.disaster_id
ORDER BY d.disaster_type, vol.name;

-- 4.4 Shelter Inventory Details (Resolving M:N Shelter - Resource)
SELECT 
    s.shelter_id,
    s.shelter_name,
    r.resource_name,
    si.quantity,
    r.unit
FROM SHELTER_INVENTORY si
INNER JOIN SHELTER s ON si.shelter_id = s.shelter_id
INNER JOIN RESOURCE r ON si.resource_id = r.resource_id
ORDER BY s.shelter_name, r.resource_name;

-- 4.5 Incident Reports with Reporter Volunteer & Disaster details
SELECT 
    ir.report_id,
    ir.report_type,
    ir.description,
    ir.status,
    ir.report_time,
    d.disaster_type,
    d.location,
    v.name AS reported_by_volunteer,
    v.contact_number AS volunteer_contact
FROM INCIDENT_REPORT ir
INNER JOIN DISASTER d ON ir.disaster_id = d.disaster_id
INNER JOIN VOLUNTEER v ON ir.volunteer_id = v.volunteer_id
ORDER BY ir.report_time DESC;


-- ====================================================================
-- SECTION 5: SUBQUERIES (NESTED & CORRELATED)
-- ====================================================================

-- 5.1 Find volunteers who have NOT been assigned to any disaster yet
SELECT volunteer_id, name, contact_number, skills
FROM VOLUNTEER
WHERE volunteer_id NOT IN (
    SELECT DISTINCT volunteer_id FROM VOLUNTEER_ASSIGNMENT
);

-- 5.2 Find shelters whose capacity is greater than the average shelter capacity
SELECT shelter_id, shelter_name, capacity, coordinator_name
FROM SHELTER
WHERE capacity > (
    SELECT AVG(capacity) FROM SHELTER
);

-- 5.3 Find disasters that have more than 2 registered victims (Correlated Subquery)
SELECT d.disaster_id, d.disaster_type, d.location, d.severity
FROM DISASTER d
WHERE (
    SELECT COUNT(*) FROM VICTIM v WHERE v.disaster_id = d.disaster_id
) >= 2;

-- 5.4 Find resources that are NOT stocked in any shelter
SELECT resource_id, resource_name, unit
FROM RESOURCE
WHERE resource_id NOT IN (
    SELECT DISTINCT resource_id FROM SHELTER_INVENTORY WHERE quantity > 0
);


-- ====================================================================
-- SECTION 6: DATA MODIFICATION (INSERT, UPDATE, DELETE)
-- ====================================================================

-- 6.1 Insert a new victim
INSERT INTO VICTIM (name, age, gender, contact_number, medical_condition, disaster_id, shelter_id)
VALUES ('Rahul Verma', 34, 'Male', '+91-9876543210', 'Minor Sprain', 1, 1);

-- 6.2 Update victim medical condition
UPDATE VICTIM
SET medical_condition = 'Recovered / Fully Stabilized'
WHERE name = 'Rahul Verma';

-- 6.3 Increase inventory stock for a resource at a shelter
UPDATE SHELTER_INVENTORY
SET quantity = quantity + 100
WHERE shelter_id = 1 AND resource_id = 1;

-- 6.4 Resolve an incident report
UPDATE INCIDENT_REPORT
SET status = 'Resolved'
WHERE report_id = 1;

-- 6.5 Delete a volunteer assignment (Clean junction record removal)
DELETE FROM VOLUNTEER_ASSIGNMENT
WHERE volunteer_id = 1 AND disaster_id = 4;
