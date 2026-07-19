-- Date demonstrative fictive pentru situatii de urgenta
INSERT INTO emergency_events (
    name,
    type,
    country,
    county,
    locality,
    affected_area,
    start_datetime,
    end_datetime,
    status,
    description,
    estimated_affected_people
)
VALUES
    (
        N'Inundații în județul Galați',
        'FLOOD',
        N'România',
        N'Galați',
        N'Pechea',
        N'Zona centrală și localitățile învecinate',
        '2026-07-10 06:30:00',
        NULL,
        'ACTIVE',
        N'Inundații provocate de precipitații abundente și creșterea nivelului apelor.',
        3200
    ),
    (
        N'Incendiu forestier în județul Hunedoara',
        'FIRE',
        N'România',
        N'Hunedoara',
        N'Petroșani',
        N'Zona forestieră din apropierea localității',
        '2026-07-12 14:00:00',
        NULL,
        'ACTIVE',
        N'Incendiu forestier care necesită evacuarea preventivă a locuitorilor.',
        850
    ),
    (
        N'Furtună severă în județul Cluj',
        'EXTREME_WEATHER',
        N'România',
        N'Cluj',
        N'Huedin',
        N'Partea de vest a județului',
        '2026-07-01 18:00:00',
        '2026-07-02 09:30:00',
        'CLOSED',
        N'Furtună severă cu vânt puternic și întreruperi ale alimentării cu energie.',
        1200
    );
GO

SELECT *
FROM emergency_events;

-- Date demonstrative fictive pentru adăposturi
INSERT INTO shelters (
    name,
    country,
    county,
    locality,
    address,
    total_capacity,
    status,
    phone_number,
    email,
    access_ramp,
    children_area,
    medical_room,
    accepts_pets,
    public_information
)
VALUES
    (
        N'Centrul Comunitar Pechea',
        N'România',
        N'Galați',
        N'Pechea',
        N'Strada Galați nr. 15',
        150,
        'OPEN',
        '0236123456',
        'pechea@shelterflow.ro',
        1,
        1,
        1,
        1,
        N'Adăpost accesibil persoanelor cu mobilitate redusă și familiilor cu animale.'
    ),
    (
        N'Sala Sporturilor Petroșani',
        N'România',
        N'Hunedoara',
        N'Petroșani',
        N'Strada Stadionului nr. 4',
        220,
        'OPEN',
        '0254123456',
        'petrosani@shelterflow.ro',
        1,
        1,
        0,
        0,
        N'Adăpost temporar destinat persoanelor evacuate din zona incendiului.'
    ),
    (
        N'Căminul Cultural Huedin',
        N'România',
        N'Cluj',
        N'Huedin',
        N'Strada Republicii nr. 28',
        80,
        'CLOSED',
        '0264123456',
        'huedin@shelterflow.ro',
        0,
        1,
        0,
        0,
        N'Adăpost închis după finalizarea situației de urgență.'
    );
GO

SELECT *
FROM shelters;

-- Date demonstrative fictive pentru persoane
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
VALUES
    (
        N'Andrei',
        N'Popescu',
        '1985-04-12',
        '0712345678',
        'andrei.popescu@example.com',
        N'România',
        N'Galați',
        N'Pechea',
        N'Strada Florilor nr. 8',
        NULL
    ),
    (
        N'Maria',
        N'Ionescu',
        '1992-09-23',
        '0723456789',
        'maria.ionescu@example.com',
        N'România',
        N'Galați',
        N'Pechea',
        N'Strada Libertății nr. 21',
        N'Însoțită de un copil minor.'
    ),
    (
        N'Elena',
        N'Dumitrescu',
        '1954-02-05',
        '0734567890',
        NULL,
        N'România',
        N'Galați',
        N'Pechea',
        N'Strada Școlii nr. 3',
        N'Are dificultăți de deplasare.'
    ),
    (
        N'Radu',
        N'Marinescu',
        '1978-11-17',
        '0745678901',
        'radu.marinescu@example.com',
        N'România',
        N'Hunedoara',
        N'Petroșani',
        N'Strada Minerilor nr. 14',
        NULL
    ),
    (
        N'Ioana',
        N'Stan',
        '2001-06-30',
        NULL,
        'ioana.stan@example.com',
        N'România',
        N'Hunedoara',
        N'Petroșani',
        N'Strada Aviatorilor nr. 6',
        N'Nu are telefonul personal asupra sa.'
    ),
    (
        N'Mihai',
        N'Pavel',
        NULL,
        NULL,
        NULL,
        N'România',
        N'Cluj',
        N'Huedin',
        NULL,
        N'Data nașterii și adresa exactă nu sunt cunoscute.'
    );
GO

SELECT *
FROM persons;

-- Date demonstrative pentru înregistrările de evacuare
INSERT INTO evacuation_records (
    person_id,
    event_id,
    registration_datetime,
    evacuation_location,
    needs_assistance,
    assistance_details,
    notes
)
VALUES
    (
        (SELECT person_id
         FROM persons
         WHERE first_name = N'Andrei' AND last_name = N'Popescu'),

        (SELECT event_id
         FROM emergency_events
         WHERE name = N'Inundații în județul Galați'),

        '2026-07-10 08:10:00',
        N'Zona centrală Pechea',
        0,
        NULL,
        N'Persoană evacuată preventiv.'
    ),
    (
        (SELECT person_id
         FROM persons
         WHERE first_name = N'Maria' AND last_name = N'Ionescu'),

        (SELECT event_id
         FROM emergency_events
         WHERE name = N'Inundații în județul Galați'),

        '2026-07-10 08:25:00',
        N'Strada Libertății, Pechea',
        0,
        NULL,
        N'Evacuată împreună cu copilul minor.'
    ),
    (
        (SELECT person_id
         FROM persons
         WHERE first_name = N'Elena' AND last_name = N'Dumitrescu'),

        (SELECT event_id
         FROM emergency_events
         WHERE name = N'Inundații în județul Galați'),

        '2026-07-10 09:00:00',
        N'Strada Școlii, Pechea',
        1,
        N'Necesită ajutor pentru deplasare și transport adaptat.',
        NULL
    ),
    (
        (SELECT person_id
         FROM persons
         WHERE first_name = N'Radu' AND last_name = N'Marinescu'),

        (SELECT event_id
         FROM emergency_events
         WHERE name = N'Incendiu forestier în județul Hunedoara'),

        '2026-07-12 15:20:00',
        N'Zona de nord a municipiului Petroșani',
        0,
        NULL,
        N'Evacuare preventivă din apropierea zonei forestiere.'
    ),
    (
        (SELECT person_id
         FROM persons
         WHERE first_name = N'Ioana' AND last_name = N'Stan'),

        (SELECT event_id
         FROM emergency_events
         WHERE name = N'Incendiu forestier în județul Hunedoara'),

        '2026-07-12 15:45:00',
        N'Strada Aviatorilor, Petroșani',
        0,
        NULL,
        N'Înregistrată fără telefon personal.'
    ),
    (
        (SELECT person_id
         FROM persons
         WHERE first_name = N'Mihai' AND last_name = N'Pavel'),

        (SELECT event_id
         FROM emergency_events
         WHERE name = N'Furtună severă în județul Cluj'),

        '2026-07-01 19:10:00',
        N'Zona centrală Huedin',
        0,
        NULL,
        N'Identificat cu informații personale incomplete.'
    );
GO

SELECT
    er.evacuation_id,
    p.first_name,
    p.last_name,
    e.name AS emergency_event,
    er.registration_datetime,
    er.evacuation_location,
    er.needs_assistance,
    er.assistance_details
FROM evacuation_records er
         JOIN persons p
              ON er.person_id = p.person_id
         JOIN emergency_events e
              ON er.event_id = e.event_id
ORDER BY er.evacuation_id;

-- Date demonstrative pentru cazări
INSERT INTO accommodations (
    evacuation_id,
    shelter_id,
    check_in_datetime,
    check_out_datetime,
    notes
)
VALUES
    (
        (
            SELECT er.evacuation_id
            FROM evacuation_records er
                     JOIN persons p ON er.person_id = p.person_id
                     JOIN emergency_events e ON er.event_id = e.event_id
            WHERE p.first_name = N'Andrei'
              AND p.last_name = N'Popescu'
              AND e.name = N'Inundații în județul Galați'
        ),
        (
            SELECT shelter_id
            FROM shelters
            WHERE name = N'Centrul Comunitar Pechea'
        ),
        '2026-07-10 10:00:00',
        NULL,
        N'Cazare activă.'
    ),
    (
        (
            SELECT er.evacuation_id
            FROM evacuation_records er
                     JOIN persons p ON er.person_id = p.person_id
                     JOIN emergency_events e ON er.event_id = e.event_id
            WHERE p.first_name = N'Maria'
              AND p.last_name = N'Ionescu'
              AND e.name = N'Inundații în județul Galați'
        ),
        (
            SELECT shelter_id
            FROM shelters
            WHERE name = N'Centrul Comunitar Pechea'
        ),
        '2026-07-10 10:15:00',
        NULL,
        N'Cazată împreună cu copilul minor.'
    ),
    (
        (
            SELECT er.evacuation_id
            FROM evacuation_records er
                     JOIN persons p ON er.person_id = p.person_id
                     JOIN emergency_events e ON er.event_id = e.event_id
            WHERE p.first_name = N'Elena'
              AND p.last_name = N'Dumitrescu'
              AND e.name = N'Inundații în județul Galați'
        ),
        (
            SELECT shelter_id
            FROM shelters
            WHERE name = N'Centrul Comunitar Pechea'
        ),
        '2026-07-10 10:30:00',
        NULL,
        N'Cazată într-o zonă accesibilă persoanelor cu mobilitate redusă.'
    ),
    (
        (
            SELECT er.evacuation_id
            FROM evacuation_records er
                     JOIN persons p ON er.person_id = p.person_id
                     JOIN emergency_events e ON er.event_id = e.event_id
            WHERE p.first_name = N'Radu'
              AND p.last_name = N'Marinescu'
              AND e.name = N'Incendiu forestier în județul Hunedoara'
        ),
        (
            SELECT shelter_id
            FROM shelters
            WHERE name = N'Sala Sporturilor Petroșani'
        ),
        '2026-07-12 16:30:00',
        NULL,
        N'Cazare activă.'
    ),
    (
        (
            SELECT er.evacuation_id
            FROM evacuation_records er
                     JOIN persons p ON er.person_id = p.person_id
                     JOIN emergency_events e ON er.event_id = e.event_id
            WHERE p.first_name = N'Mihai'
              AND p.last_name = N'Pavel'
              AND e.name = N'Furtună severă în județul Cluj'
        ),
        (
            SELECT shelter_id
            FROM shelters
            WHERE name = N'Căminul Cultural Huedin'
        ),
        '2026-07-01 20:00:00',
        '2026-07-02 08:30:00',
        N'Cazare încheiată după închiderea evenimentului.'
    );
GO

SELECT
    a.accommodation_id,
    p.first_name,
    p.last_name,
    s.name AS shelter,
    e.name AS emergency_event,
    a.check_in_datetime,
    a.check_out_datetime,
    CASE
        WHEN a.check_out_datetime IS NULL THEN N'ACTIVĂ'
        ELSE N'ÎNCHEIATĂ'
        END AS accommodation_status
FROM accommodations a
         JOIN evacuation_records er
              ON a.evacuation_id = er.evacuation_id
         JOIN persons p
              ON er.person_id = p.person_id
         JOIN emergency_events e
              ON er.event_id = e.event_id
         JOIN shelters s
              ON a.shelter_id = s.shelter_id
ORDER BY a.accommodation_id;

-- Date demonstrative pentru rapoarte de dispariție
INSERT INTO missing_person_reports (
    person_id,
    event_id,
    reported_datetime,
    last_seen_datetime,
    last_known_location,
    reported_by_name,
    reported_by_phone,
    status,
    resolved_datetime,
    notes
)
VALUES
    (
        (
            SELECT person_id
            FROM persons
            WHERE first_name = N'Ioana'
              AND last_name = N'Stan'
        ),
        (
            SELECT event_id
            FROM emergency_events
            WHERE name = N'Incendiu forestier în județul Hunedoara'
        ),
        '2026-07-12 17:30:00',
        '2026-07-12 15:10:00',
        N'Strada Aviatorilor, Petroșani',
        N'Adrian Stan',
        '0756789012',
        'MISSING',
        NULL,
        N'Persoana nu a mai putut fi contactată după evacuarea zonei.'
    ),
    (
        (
            SELECT person_id
            FROM persons
            WHERE first_name = N'Mihai'
              AND last_name = N'Pavel'
        ),
        (
            SELECT event_id
            FROM emergency_events
            WHERE name = N'Furtună severă în județul Cluj'
        ),
        '2026-07-01 19:30:00',
        '2026-07-01 18:45:00',
        N'Zona centrală Huedin',
        N'Cristina Pavel',
        '0767890123',
        'LOCATED_SAFE',
        '2026-07-01 20:15:00',
        N'Persoana a fost identificată și cazată în adăpost.'
    );
GO

SELECT
    mpr.report_id,
    p.first_name,
    p.last_name,
    e.name AS emergency_event,
    mpr.reported_datetime,
    mpr.last_seen_datetime,
    mpr.last_known_location,
    mpr.reported_by_name,
    mpr.status,
    mpr.resolved_datetime
FROM missing_person_reports mpr
         JOIN persons p
              ON mpr.person_id = p.person_id
         JOIN emergency_events e
              ON mpr.event_id = e.event_id
ORDER BY mpr.report_id;