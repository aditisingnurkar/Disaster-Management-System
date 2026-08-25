-- ====================================================================
-- DISASTER MANAGEMENT SYSTEM
-- Sample Seed Data (8 Tables)
-- ====================================================================

USE disaster_management;

-- Disable Foreign Key checks temporarily during bulk clean insert
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE INCIDENT_REPORT;
TRUNCATE TABLE VOLUNTEER_ASSIGNMENT;
TRUNCATE TABLE VOLUNTEER;
TRUNCATE TABLE SHELTER_INVENTORY;
TRUNCATE TABLE RESOURCE;
TRUNCATE TABLE VICTIM;
TRUNCATE TABLE SHELTER;
TRUNCATE TABLE DISASTER;
SET FOREIGN_KEY_CHECKS = 1;

-- --------------------------------------------------------------------
-- 1. SEED DISASTERS
-- --------------------------------------------------------------------
INSERT INTO DISASTER (disaster_id, disaster_type, location, start_date, end_date, severity) VALUES
(1, 'Flood', 'Wayanad, Kerala', '2026-07-10', '2026-07-28', 'Critical'),
(2, 'Earthquake', 'Uttarkashi, Uttarakhand', '2026-08-01', NULL, 'High'),
(3, 'Cyclone', 'Puri Coastal Zone, Odisha', '2026-08-15', '2026-08-20', 'High'),
(4, 'Landslide', 'Shimla Valley, Himachal Pradesh', '2026-08-18', NULL, 'Critical'),
(5, 'Urban Flood', 'Chennai Central, Tamil Nadu', '2026-08-22', NULL, 'Medium');

-- --------------------------------------------------------------------
-- 2. SEED SHELTERS
-- --------------------------------------------------------------------
INSERT INTO SHELTER (shelter_id, shelter_name, address, capacity, coordinator_name, contact_number, disaster_id) VALUES
(1, 'St. Mary Community Relief Center', 'Main Road, Meppadi, Wayanad', 250, 'Fr. Thomas Varghese', '+91-9847123450', 1),
(2, 'Greenwood Higher Secondary School', 'Kalpetta West, Wayanad', 400, 'Dr. Sunita Menon', '+91-9847556781', 1),
(3, 'Himalayan Base Relief Camp', 'NH-34 Near District Hospital, Uttarkashi', 300, 'Col. Rajesh Rawat', '+91-9412098765', 2),
(4, 'Coastal Cyclone Shelter #4', 'Marine Drive Extension, Puri', 500, 'Debasish Mohanty', '+91-9437112233', 3),
(5, 'Pine Crest Community Hall', 'Mall Road Lower Bypass, Shimla', 150, 'Ananya Sharma', '+91-9816043210', 4),
(6, 'Anna Nagar Relief Complex', '2nd Avenue, Anna Nagar, Chennai', 350, 'K. Karthik', '+91-9840192837', 5);

-- --------------------------------------------------------------------
-- 3. SEED VICTIMS
-- --------------------------------------------------------------------
INSERT INTO VICTIM (victim_id, name, age, gender, contact_number, medical_condition, disaster_id, shelter_id) VALUES
(1, 'Ramesh Nair', 48, 'Male', '+91-9447102938', 'Mild Dehydration', 1, 1),
(2, 'Deepa Nair', 42, 'Female', '+91-9447102939', 'Asthma (Inhaler Required)', 1, 1),
(3, 'Anu Ramesh', 12, 'Female', NULL, 'Normal / None', 1, 1),
(4, 'Mathew Joseph', 67, 'Male', '+91-9847001122', 'Hypertension & Diabetes', 1, 2),
(5, 'Mariamma Joseph', 63, 'Female', '+91-9847001123', 'Normal / None', 1, 2),
(6, 'Virender Negi', 35, 'Male', '+91-9412334455', 'Fractured Left Arm (Bandaged)', 2, 3),
(7, 'Kavita Negi', 31, 'Female', '+91-9412334456', 'Pregnancy (2nd Trimester)', 2, 3),
(8, 'Aarav Negi', 5, 'Male', NULL, 'Fever and Cough', 2, 3),
(9, 'Bikram Jena', 52, 'Male', '+91-9437889900', 'Minor Cuts & Bruises', 3, 4),
(10, 'Sujata Jena', 49, 'Female', '+91-9437889901', 'Normal / None', 3, 4),
(11, 'Pradeep Chauhan', 29, 'Male', '+91-9816998877', 'Head Injury (Under Observation)', 4, 5),
(12, 'Meenakshi Sundaram', 58, 'Female', '+91-9840556677', 'Diabetic Medication Needed', 5, 6),
(13, 'Suresh Kumar', 24, 'Male', '+91-9840223344', 'Normal / None', 5, 6);

-- --------------------------------------------------------------------
-- 4. SEED RESOURCES
-- --------------------------------------------------------------------
INSERT INTO RESOURCE (resource_id, resource_name, description, unit) VALUES
(1, 'Mineral Water Bottles (1L)', 'Sealed potable drinking water bottles', 'Bottles'),
(2, 'Ready-to-Eat Food Packets', 'Nutritious sealed meal ration packs', 'Packets'),
(3, 'Emergency First Aid Kit', 'Bandages, antiseptics, gauze, analgesics', 'Kits'),
(4, 'Thermal Blankets', 'Water-resistant thermal insulation blankets', 'Units'),
(5, 'Baby Food & Formula', 'Infant milk powder and cereal packs', 'Boxes'),
(6, 'Sanitation & Hygiene Kit', 'Soap, disinfectant, sanitary pads, toothbrushes', 'Kits'),
(7, 'LED Emergency Torches', 'Rechargeable multi-mode survival flashlights', 'Units'),
(8, 'Portable Water Purification Tablets', 'Chlorine-based purification strips (50 tabs/pack)', 'Packs');

-- --------------------------------------------------------------------
-- 5. SEED SHELTER_INVENTORY (Shelter M:N Resource)
-- --------------------------------------------------------------------
INSERT INTO SHELTER_INVENTORY (shelter_id, resource_id, quantity) VALUES
(1, 1, 1200), -- Shelter 1 has 1200 Water Bottles
(1, 2, 850),  -- Shelter 1 has 850 Food Packets
(1, 3, 45),   -- Shelter 1 has 45 First Aid Kits
(1, 4, 300),  -- Shelter 1 has 300 Blankets
(2, 1, 2000), -- Shelter 2 has 2000 Water Bottles
(2, 2, 1500), -- Shelter 2 has 1500 Food Packets
(2, 6, 250),  -- Shelter 2 has 250 Hygiene Kits
(3, 1, 800),  -- Shelter 3 has 800 Water Bottles
(3, 3, 60),   -- Shelter 3 has 60 First Aid Kits
(3, 4, 450),  -- Shelter 3 has 450 Blankets
(3, 7, 80),   -- Shelter 3 has 80 Emergency Torches
(4, 1, 2500), -- Shelter 4 has 2500 Water Bottles
(4, 2, 1800), -- Shelter 4 has 1800 Food Packets
(4, 8, 300),  -- Shelter 4 has 300 Water Purification Packs
(5, 1, 600),  -- Shelter 5 has 600 Water Bottles
(5, 3, 35),   -- Shelter 5 has 35 First Aid Kits
(5, 4, 200),  -- Shelter 5 has 200 Blankets
(6, 1, 1500), -- Shelter 6 has 1500 Water Bottles
(6, 2, 1100), -- Shelter 6 has 1100 Food Packets
(6, 6, 180);  -- Shelter 6 has 180 Hygiene Kits

-- --------------------------------------------------------------------
-- 6. SEED VOLUNTEERS
-- --------------------------------------------------------------------
INSERT INTO VOLUNTEER (volunteer_id, name, age, gender, contact_number, skills, availability_status) VALUES
(1, 'Arjun Menon', 27, 'Male', '+91-9895012345', 'First Aid, Water Rescue, Driving', 'Assigned'),
(2, 'Pooja Iyer', 24, 'Female', '+91-9895098765', 'Medical Triage, Counseling, Logistics', 'Assigned'),
(3, 'Vikram Rathore', 32, 'Male', '+91-9414011223', 'Search & Rescue, Mountaineering, Radio Ops', 'Assigned'),
(4, 'Sneha Joshi', 29, 'Female', '+91-9414099887', 'Nursing, Child Care, Food Distribution', 'Assigned'),
(5, 'Manoj Patnaik', 26, 'Male', '+91-9437012345', 'Boat Operations, Heavy Vehicle Driving', 'Assigned'),
(6, 'Rohan Verma', 23, 'Male', '+91-9816012345', 'Civil Engineering, Debris Clearance', 'Assigned'),
(7, 'Divya Balakrishnan', 28, 'Female', '+91-9840012345', 'Community Outreach, Supply Chain Management', 'Assigned'),
(8, 'Karan Malhotra', 35, 'Male', '+91-9811099882', 'Ham Radio, Drone Aerial Survey', 'Available'),
(9, 'Nandini Das', 22, 'Female', '+91-9830012345', 'First Aid, Translation, Data Entry', 'Available'),
(10, 'Sameer Khan', 30, 'Male', '+91-9820098765', 'Paramedic, Emergency Vehicle Driving', 'Available');

-- --------------------------------------------------------------------
-- 7. SEED VOLUNTEER_ASSIGNMENT (Volunteer M:N Disaster)
-- --------------------------------------------------------------------
INSERT INTO VOLUNTEER_ASSIGNMENT (volunteer_id, disaster_id, role, shift_timing) VALUES
(1, 1, 'Water Rescue Team Lead', 'Morning (06:00 - 14:00)'),
(2, 1, 'Medical Aid Coordinator', 'Evening (14:00 - 22:00)'),
(3, 2, 'Search & Debris Specialist', 'Morning (06:00 - 14:00)'),
(4, 2, 'Emergency Nursing Staff', 'Night (22:00 - 06:00)'),
(5, 3, 'Coastal Evacuation Pilot', 'Morning (06:00 - 14:00)'),
(6, 4, 'Slope Stabilization & Rescue', 'Morning (06:00 - 14:00)'),
(7, 5, 'Urban Relief & Food Dispatch', 'Evening (14:00 - 22:00)'),
(1, 4, 'Emergency Backup Paramedic', 'Night (22:00 - 06:00)'); -- Volunteer 1 assigned to 2 disasters

-- --------------------------------------------------------------------
-- 8. SEED INCIDENT_REPORTS
-- (Report submitted by assigned volunteer for corresponding disaster)
-- --------------------------------------------------------------------
INSERT INTO INCIDENT_REPORT (report_id, description, report_time, report_type, status, disaster_id, volunteer_id) VALUES
(1, 'Bridge connecting Meppadi to Chooralmala washed away. Heavy vehicles cannot pass.', '2026-07-11 08:30:00', 'Road Blockage', 'In Progress', 1, 1),
(2, 'Urgent need for 50 additional pediatric medical kits at St. Mary Shelter.', '2026-07-12 14:15:00', 'Resource Shortage', 'Resolved', 1, 2),
(3, 'Rockfall reported on NH-34 kilometer marker 14. Clearing team requested.', '2026-08-02 09:45:00', 'Road Blockage', 'In Progress', 2, 3),
(4, 'Power grid failure in Sector 3 relief camp; backup generator fuel needed.', '2026-08-03 21:10:00', 'Resource Shortage', 'Open', 2, 4),
(5, 'Storm surge flooded low-lying coastal road near Puri lighthouse.', '2026-08-16 11:20:00', 'Flooding', 'Resolved', 3, 5),
(6, 'Secondary landslide risk identified near Upper Shimla bypass school.', '2026-08-19 16:00:00', 'Building Damage', 'Open', 4, 6),
(7, 'Subway water logging at Anna Nagar junction hindering ambulance movement.', '2026-08-23 07:50:00', 'Road Blockage', 'Open', 5, 7);
