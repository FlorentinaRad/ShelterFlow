--Interogari de raportare pentru ShelterFlow

--Raportul persoanelor cazate in prezent
SELECT
    p.person_id,
    p.first_name,
    p.last_name,
    s.name AS shalter_name,
    s.locality AS shalter_locality,
    e.name AS emergency_event,
    a.check_in_datetime,
    CASE
        WHEN er.needs_assistance = 1 THEN N'DA'
        ELSE N'NU'
        END AS needs_assistance,
    er.assistance_details
FROM accommodations a
         JOIN evacuation_records er
              ON a.evacuation_id = er.evacuation_id
         JOIN persons p
              ON p.person_id = er.person_id
         JOIN shelters s
              ON s.shelter_id = a.shelter_id
         JOIN emergency_events e
              ON er.event_id = e.event_id
WHERE a.check_out_datetime IS NULL
ORDER BY s.name, p.last_name, p.first_name;
GO

--Locuri disponibile pentru fiecare adapost
SELECT
    s.shelter_id,
    s.name AS shelter_name,
    s.locality,
    s.status,
    s.total_capacity,
    COUNT(a.accommodation_id) AS current_occupancy,
    s.total_capacity - COUNT(a.accommodation_id) AS available_capacity
FROM shelters s
--LEFT JOIN ca sa apara si adaposturile fara cazari active
         LEFT JOIN accommodations a
                   ON s.shelter_id=a.shelter_id
                       AND a.check_out_datetime IS NULL
GROUP BY
    s.shelter_id, s.name, s.locality, s.status, s.total_capacity
ORDER BY available_capacity DESC;
GO

--Istoricul cazarilor unei persoane
DECLARE @person_id INT;
SELECT @person_id=person_id
FROM persons
WHERE first_name = N'Andrei'
  AND last_name=N'Popescu';

SELECT
    p.person_id,
    p.first_name,
    p.last_name,
    e.name AS emergency_event,
    s.name AS shelter_name,
    a.check_in_datetime,
    a.check_in_datetime,
    CASE
        WHEN a.check_out_datetime IS NULL THEN N'ACTIVA'
        ELSE N'INCHEIATA'
        END AS accommodation_status,
    a.notes
FROM accommodations a
         JOIN evacuation_records er
              ON a.evacuation_id=er.evacuation_id
         JOIN persons p
              ON p.person_id = er.person_id
         JOIN shelters s
              ON s.shelter_id = a.shelter_id
         JOIN emergency_events e
              ON er.event_id = e.event_id
WHERE p.person_id=@person_id
ORDER BY a.check_in_datetime;
GO

--Persoanele evacuate pentru un anumit tip de eveniment
DECLARE @event_id INT=3; --sau 1 2
SELECT
    e.event_id,
    e.name AS emergency_event,
    p.person_id,
    p.first_name,
    p.last_name,
    er.evacuation_location,
    CASE
        WHEN er.needs_assistance = 1 THEN N'DA'
        ELSE N'NU'
        END AS needs_assistance,
    er.assistance_details
FROM evacuation_records er
         JOIN persons p
              ON er.person_id = p.person_id
         JOIN emergency_events e
              ON er.event_id = e.event_id
WHERE e.event_id = @event_id
ORDER BY er.registration_datetime;
GO

--Rapoarte active de disparitie
SELECT
    mpr.report_id,
    p.person_id,
    p.first_name,
    p.last_name,
    e.name AS emergency_event,
    mpr.reported_datetime,
    mpr.last_seen_datetime,
    mpr.last_known_location,
    mpr.reported_by_name,
    mpr.reported_by_phone,
    mpr.notes
FROM missing_person_reports mpr
         JOIN persons p
              ON mpr.person_id = p.person_id
         JOIN emergency_events e
              ON e.event_id=mpr.event_id
WHERE mpr.status='MISSING'
ORDER BY mpr.reported_datetime;
GO

--Numarul persoanelor evacuate pentru fiecare eveniment
SELECT
    e.event_id,
    e.name AS emergency_event,
    e.type,
    e.status,
    COUNT(er.evacuation_id) AS total_evacuated
FROM emergency_events e
--LEFT JOIN ca sa apara si evenimentele care inca nu au persoane evacuate
         LEFT JOIN evacuation_records er
                   ON er.event_id=e.event_id
GROUP BY e.event_id, e.name, e.type, e.status
ORDER BY total_evacuated DESC;
GO

