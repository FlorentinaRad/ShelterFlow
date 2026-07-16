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