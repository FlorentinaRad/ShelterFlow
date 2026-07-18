--verificare tabela emergency_events
INSERT INTO emergency_events (
    name,
    type,
    country,
    county,
    locality,
    affected_area,
    start_datetime,
    description,
    estimated_affected_people
)
VALUES (
           N'Inundaţii în judeţul Cluj',
           'FLOOD',
           N'România',
           N'Cluj',
           N'Cluj-Napoca',
           N'Zona Someşului',
           '2026-07-16T14:30:00',
           N'Creşterea nivelului apei în urma precipitaţiilor abundente.',
           100
       );

--afisare rezultat
SELECT *
FROM emergency_events;

--testare reguli
INSERT INTO emergency_events (
    name,
    type,
    country,
    county,
    start_datetime
)
VALUES (
           N'Eveniment de test',
           'TORNADO',
           N'România',
           N'Cluj',
           '2026-07-17T10:00:00'
       );
--regula functioneaza

INSERT INTO emergency_events (
    name,
    type,
    country,
    county,
    start_datetime,
    end_datetime,
    status
)
VALUES (
           N'Eveniment invalid',
           'FIRE',
           N'România',
           N'Cluj',
           '2026-07-17T10:00:00',
           '2026-07-17T12:00:00',
           'ACTIVE'
       );
--regula functioneaza

SELECT *
FROM emergency_events;

--modificare valida, am incheiat un eveniment existent
UPDATE emergency_events
SET status = 'CLOSED',
    end_datetime = '2026-07-18T09:15:00'
WHERE event_id=1;

SELECT *
FROM emergency_events
WHERE event_id = 1;

--verificare tabela shelters
INSERT INTO shelters (
    name,
    country,
    county,
    locality,
    address,
    total_capacity,
    phone_number,
    email,
    access_ramp,
    children_area,
    medical_room,
    accepts_pets,
    public_information
)
VALUES (
           N'Sala Sporturilor Cluj',
           N'România',
           N'Cluj',
           N'Cluj-Napoca',
           N'Strada Splaiul Independenței 6',
           200,
           '0264000000',
           'contact@shelterflow.ro',
           1,
           1,
           1,
           1,
           N'Accesul se face prin intrarea principală.'
       );

SELECT *
FROM shelters;

--testare capacitate invalida
INSERT INTO shelters (
    name,
    country,
    county,
    locality,
    address,
    total_capacity
)
VALUES (
           N'Adăpost invalid',
           N'România',
           N'Cluj',
           N'Cluj-Napoca',
           N'Strada Exemplu 10',
           0
       );

--testare status invalid
INSERT INTO shelters (
    name,
    country,
    county,
    locality,
    address,
    total_capacity,
    status
)
VALUES (
           N'Adăpost test',
           N'România',
           N'Cluj',
           N'Cluj-Napoca',
           N'Strada Exemplu 20',
           100,
           'FULL'
       );

--
INSERT INTO shelters (
    name,
    country,
    county,
    locality,
    address,
    total_capacity
)
VALUES (
           N'Căminul Municipal',
           N'România',
           N'Cluj',
           N'Turda',
           N'Strada Republicii 15',
           80
       );

SELECT *
FROM shelters;

-- tabela nu este referita prin foreign keys asa ca am preferat sa o sterg si sa adaug 'AIR_RAID'
DROP TABLE IF EXISTS emergency_events;
GO

INSERT INTO emergency_events (
    name,
    type,
    country,
    county,
    locality,
    start_datetime
)
VALUES (
    N'Alertă de atac aerian',
    'AIR_RAID',
    N'România',
    N'Tulcea',
    N'Tulcea',
    '2026-07-18T10:00:00'
);

SELECT *
FROM emergency_events;

--Testare tabela persons
INSERT INTO persons (
    first_name,
    last_name,
    birth_date,
    phone_number,
    email,
    home_country,
    home_county,
    home_locality,
    home_address,
    notes
)
VALUES (
           N'Ana',
           N'Popescu',
           '1994-05-18',
           '0712345678',
           'ana.popescu@example.com',
           N'România',
           N'Cluj',
           N'Cluj-Napoca',
           N'Strada Exemplu x',
           N'Persoană înregistrată pentru testarea sistemului.'
       );


SELECT *
FROM persons;

--eroare deoarece lipseste firt_name care nu permite NULL
INSERT INTO persons (
    last_name
)
VALUES (
           N'Ionescu'
       );

--Testare tabela evacuation_records
--ID-uri existente
SELECT person_id, first_name, last_name
FROM persons;

SELECT event_id, name, type
FROM emergency_events;

--Inserare
INSERT INTO evacuation_records (
    person_id,
    event_id,
    evacuation_location,
    need_assistance,
    assistance_details,
    notes
)
VALUES (
           1,
           1,
           N'Tulcea, zona centrală',
           1,
           N'Necesită ajutor pentru transport.',
           N'Înregistrare de test.'
       );

SELECT *
FROM evacuation_records

SELECT
    er.evacuation_id,
    p.first_name,
    p.last_name,
    ee.name AS emergency_event
FROM evacuation_records er
         JOIN persons p
              ON er.person_id = p.person_id
         JOIN emergency_events ee
              ON er.event_id = ee.event_id;

--afisare mai detaliata
SELECT
    er.evacuation_id,
    p.first_name,
    p.last_name,
    ee.name AS emergency_event,
    er.registration_datetime,
    er.evacuation_location,
    CASE
        WHEN er.need_assistance = 1 THEN 'YES'
        ELSE 'NO'
        END AS needs_assistance,
    er.assistance_details
FROM evacuation_records er
         JOIN persons p
              ON er.person_id = p.person_id
         JOIN emergency_events ee
              ON er.event_id = ee.event_id;


--testare regula UNIQUE --inserare respinsa
INSERT INTO evacuation_records (
    person_id,
    event_id
)
VALUES (
           1,
           1
       );

--testare cheie externa --respin, nu exista persoana 999
INSERT INTO evacuation_records (
    person_id,
    event_id
)
VALUES (
           9999,
           1
       );

SELECT *
FROM evacuation_records;

--Testare tabela accommodation
INSERT INTO accommodations (
    evacuation_id,
    shelter_id,
    notes
)
VALUES (
           1,
           2,
           N'Prima cazare.'
       );

SELECT shelter_id, name
FROM shelters;

SELECT evacuation_id, person_id, event_id
FROM evacuation_records;

SELECT
    accommodation_id,
    evacuation_id,
    shelter_id,
    check_in_datetime,
    check_out_datetime
FROM accommodations
ORDER BY accommodation_id;

--check-out inainte de check-in --eroare
--id cazare activa
SELECT *
FROM accommodations
WHERE check_out_datetime IS NULL;

UPDATE accommodations
SET check_out_datetime = DATEADD(MINUTE, -10, check_in_datetime)
WHERE accommodation_id = 2;

--check-out valid
UPDATE accommodations
SET check_out_datetime = SYSDATETIME()
WHERE accommodation_id = 2;


--evacuare inexistenta
INSERT INTO accommodations (
    evacuation_id,
    shelter_id
)
VALUES (
           9999,
           2
       );

--adapost inexistent
INSERT INTO accommodations (
    evacuation_id,
    shelter_id
)
VALUES (
           1,
           9999
       );

--istoric complet
SELECT
    a.accommodation_id,
    p.first_name,
    p.last_name,
    s.name AS shelter,
    a.check_in_datetime,
    a.check_out_datetime,
    CASE
        WHEN a.check_out_datetime IS NULL THEN 'ACTIVE'
        ELSE 'CLOSED'
        END AS accommodation_status
FROM accommodations a
         JOIN evacuation_records er
              ON a.evacuation_id = er.evacuation_id
         JOIN persons p
              ON er.person_id = p.person_id
         JOIN shelters s
              ON a.shelter_id = s.shelter_id
ORDER BY a.check_in_datetime;


--Testare tabela missing_person_reports
--verificare ID existente
SELECT person_id, first_name, last_name
FROM persons;

SELECT event_id, name
FROM emergency_events;


--raport valid
INSERT INTO missing_person_reports (
    person_id,
    event_id,
    last_seen_datetime,
    last_known_location,
    reported_by_name,
    reported_by_phone,
    notes
)
VALUES (
           1,
           1,
           DATEADD(HOUR, -2, SYSDATETIME()),
           N'Gara centrală',
           N'Maria Popescu',
           '0712345678',
           N'Persoana purta o geacă albastră.'
       );

--afisare
SELECT
    mpr.report_id,
    p.first_name,
    p.last_name,
    ee.name AS emergency_event,
    mpr.reported_datetime,
    mpr.last_seen_datetime,
    mpr.last_known_location,
    mpr.status,
    mpr.resolved_datetime
FROM missing_person_reports mpr
         JOIN persons p
              ON mpr.person_id = p.person_id
         JOIN emergency_events ee
              ON mpr.event_id = ee.event_id;

--raport duplicat
INSERT INTO missing_person_reports (
    person_id,
    event_id,
    reported_by_name
)
VALUES (
           1,
           1,
           N'Alt raportor'
       );

--rezolvare valida
UPDATE missing_person_reports
SET status = 'LOCATED_SAFE',
    resolved_datetime = SYSDATETIME()
WHERE report_id = 1;

SELECT *
FROM missing_person_reports;