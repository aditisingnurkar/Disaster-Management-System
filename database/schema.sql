-- ====================================================================
-- DISASTER MANAGEMENT SYSTEM
-- Relational Database Schema (3NF Normalized - 8 Core Tables)
-- Target RDBMS: MySQL 8.0+
-- ====================================================================

DROP DATABASE IF EXISTS disaster_management;
CREATE DATABASE disaster_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE disaster_management;

-- --------------------------------------------------------------------
-- 1. DISASTER TABLE
-- Represents disaster events (e.g. Floods, Earthquakes, Cyclones)
-- --------------------------------------------------------------------
CREATE TABLE DISASTER (
    disaster_id INT AUTO_INCREMENT,
    disaster_type VARCHAR(50) NOT NULL,
    location VARCHAR(100) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NULL,
    severity ENUM('Low', 'Medium', 'High', 'Critical') NOT NULL DEFAULT 'Medium',
    CONSTRAINT pk_disaster PRIMARY KEY (disaster_id),
    CONSTRAINT chk_disaster_dates CHECK (end_date IS NULL OR end_date >= start_date)
) ENGINE=InnoDB;

-- --------------------------------------------------------------------
-- 2. SHELTER TABLE
-- Represents relief shelters established for a disaster
-- Cardinality: DISASTER 1 : M SHELTER
-- --------------------------------------------------------------------
CREATE TABLE SHELTER (
    shelter_id INT AUTO_INCREMENT,
    shelter_name VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    capacity INT NOT NULL,
    coordinator_name VARCHAR(100) NOT NULL,
    contact_number VARCHAR(20) NOT NULL,
    disaster_id INT NOT NULL,
    CONSTRAINT pk_shelter PRIMARY KEY (shelter_id),
    CONSTRAINT chk_shelter_capacity CHECK (capacity > 0),
    CONSTRAINT fk_shelter_disaster FOREIGN KEY (disaster_id)
        REFERENCES DISASTER (disaster_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- --------------------------------------------------------------------
-- 3. VICTIM TABLE
-- Represents affected persons accommodated in shelters or affected by disasters
-- Cardinality: DISASTER 1 : M VICTIM, SHELTER 1 : M VICTIM
-- --------------------------------------------------------------------
CREATE TABLE VICTIM (
    victim_id INT AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender ENUM('Male', 'Female', 'Other') NOT NULL,
    contact_number VARCHAR(20) NULL,
    medical_condition VARCHAR(255) DEFAULT 'Normal / None',
    disaster_id INT NOT NULL,
    shelter_id INT NULL,
    CONSTRAINT pk_victim PRIMARY KEY (victim_id),
    CONSTRAINT chk_victim_age CHECK (age >= 0 AND age <= 125),
    CONSTRAINT fk_victim_disaster FOREIGN KEY (disaster_id)
        REFERENCES DISASTER (disaster_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_victim_shelter FOREIGN KEY (shelter_id)
        REFERENCES SHELTER (shelter_id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- --------------------------------------------------------------------
-- 4. RESOURCE TABLE
-- Master catalog of relief resource items (e.g. Water, Food, Medical Kit)
-- --------------------------------------------------------------------
CREATE TABLE RESOURCE (
    resource_id INT AUTO_INCREMENT,
    resource_name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255) NULL,
    unit VARCHAR(30) NOT NULL, -- e.g. 'Bottles', 'Packets', 'Kits', 'Boxes', 'Liters'
    CONSTRAINT pk_resource PRIMARY KEY (resource_id)
) ENGINE=InnoDB;

-- --------------------------------------------------------------------
-- 5. SHELTER_INVENTORY (Junction Table for SHELTER M:N RESOURCE)
-- Stores quantities of resources stocked at specific shelters
-- Cardinality: SHELTER 1 : M SHELTER_INVENTORY M : 1 RESOURCE
-- Composite Primary Key: (shelter_id, resource_id)
-- --------------------------------------------------------------------
CREATE TABLE SHELTER_INVENTORY (
    shelter_id INT NOT NULL,
    resource_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    CONSTRAINT pk_shelter_inventory PRIMARY KEY (shelter_id, resource_id),
    CONSTRAINT chk_inventory_qty CHECK (quantity >= 0),
    CONSTRAINT fk_si_shelter FOREIGN KEY (shelter_id)
        REFERENCES SHELTER (shelter_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_si_resource FOREIGN KEY (resource_id)
        REFERENCES RESOURCE (resource_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- --------------------------------------------------------------------
-- 6. VOLUNTEER TABLE
-- Master registry of registered relief volunteers
-- --------------------------------------------------------------------
CREATE TABLE VOLUNTEER (
    volunteer_id INT AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender ENUM('Male', 'Female', 'Other') NOT NULL,
    contact_number VARCHAR(20) NOT NULL,
    skills VARCHAR(255) NOT NULL, -- e.g. 'First Aid, Search & Rescue, Driving'
    availability_status ENUM('Available', 'Assigned', 'On Leave') NOT NULL DEFAULT 'Available',
    CONSTRAINT pk_volunteer PRIMARY KEY (volunteer_id),
    CONSTRAINT chk_volunteer_age CHECK (age >= 18 AND age <= 100)
) ENGINE=InnoDB;

-- --------------------------------------------------------------------
-- 7. VOLUNTEER_ASSIGNMENT (Junction Table for VOLUNTEER M:N DISASTER)
-- Maps volunteers to disaster deployments with specific roles and shifts
-- Cardinality: VOLUNTEER 1 : M VOLUNTEER_ASSIGNMENT M : 1 DISASTER
-- Composite Primary Key: (volunteer_id, disaster_id)
-- --------------------------------------------------------------------
CREATE TABLE VOLUNTEER_ASSIGNMENT (
    volunteer_id INT NOT NULL,
    disaster_id INT NOT NULL,
    role VARCHAR(100) NOT NULL, -- e.g. 'Rescue Coordinator', 'Medical Assistant', 'Food Distribution'
    shift_timing VARCHAR(50) NOT NULL, -- e.g. 'Morning (06:00-14:00)', 'Evening (14:00-22:00)', 'Night (22:00-06:00)'
    CONSTRAINT pk_volunteer_assignment PRIMARY KEY (volunteer_id, disaster_id),
    CONSTRAINT fk_va_volunteer FOREIGN KEY (volunteer_id)
        REFERENCES VOLUNTEER (volunteer_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_va_disaster FOREIGN KEY (disaster_id)
        REFERENCES DISASTER (disaster_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- --------------------------------------------------------------------
-- 8. INCIDENT_REPORT TABLE
-- Stores emergency incident logs submitted by assigned volunteers
-- Cardinality: DISASTER 1 : M INCIDENT_REPORT, VOLUNTEER 1 : M INCIDENT_REPORT
-- --------------------------------------------------------------------
CREATE TABLE INCIDENT_REPORT (
    report_id INT AUTO_INCREMENT,
    description TEXT NOT NULL,
    report_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    report_type VARCHAR(50) NOT NULL, -- e.g. 'Road Blockage', 'Building Collapse', 'Medical Emergency', 'Resource Shortage', 'Missing Person'
    status ENUM('Open', 'In Progress', 'Resolved', 'Closed') NOT NULL DEFAULT 'Open',
    disaster_id INT NOT NULL,
    volunteer_id INT NOT NULL,
    CONSTRAINT pk_incident_report PRIMARY KEY (report_id),
    CONSTRAINT fk_ir_disaster FOREIGN KEY (disaster_id)
        REFERENCES DISASTER (disaster_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_ir_volunteer FOREIGN KEY (volunteer_id)
        REFERENCES VOLUNTEER (volunteer_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- --------------------------------------------------------------------
-- Performance Indexes for Common Foreign Key Lookups
-- --------------------------------------------------------------------
CREATE INDEX idx_victim_disaster ON VICTIM (disaster_id);
CREATE INDEX idx_victim_shelter ON VICTIM (shelter_id);
CREATE INDEX idx_shelter_disaster ON SHELTER (disaster_id);
CREATE INDEX idx_incident_disaster ON INCIDENT_REPORT (disaster_id);
CREATE INDEX idx_incident_volunteer ON INCIDENT_REPORT (volunteer_id);
CREATE INDEX idx_incident_status ON INCIDENT_REPORT (status);
