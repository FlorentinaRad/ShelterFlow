--Creare baza de date
CREATE DATABASE ShelterFlow;
GO

USE ShelterFlow;
GO

--Creare tabela pentru situatiile de urgenta inregistrate
CREATE TABLE emergency_events (
                                  event_id INT IDENTITY(1,1) PRIMARY KEY,

                                  name NVARCHAR(100) NOT NULL,

--Tipul evenimentului este limitat la valorile acceptate de sistem
                                  type VARCHAR(30) NOT NULL,
                                  CONSTRAINT chk_emergency_event_type
                                      CHECK(type IN ('FLOOD',
                                                     'FIRE',
                                                     'EARTHQUAKE',
                                                     'LANDSLIDE',
                                                     'EXTREME_WEATHER',
                                                     'AIR_RAID',
                                                     'OTHER'
                                          )),

--Zona geografica afectata
                                  country NVARCHAR(100) NOT NULL,
                                  county NVARCHAR(100) NOT NULL,
                                  locality NVARCHAR(100) NULL,
                                  affected_area NVARCHAR(300) NULL,

--Data de incheiere ramane NULL cat timp evenimentul este activ
                                  start_datetime DATETIME2(0) NOT NULL,
                                  end_datetime DATETIME2(0) NULL,

--Data incheierii nu poate fi inaintea datei de inceput
                                  CONSTRAINT chk_emergency_event_dates
                                      CHECK ( end_datetime IS NULL
                                          OR end_datetime >= start_datetime

                                          ),
--Statusuri acceptate
                                  status VARCHAR(10) NOT NULL
                                      DEFAULT 'ACTIVE',

                                  CONSTRAINT chk_emergency_event_status
                                      CHECK (status IN('ACTIVE', 'CLOSED')),
--Evenimentul activ nu are data de finalizare, iar un eveniment inchis trebuie sa aiba una
                                  CONSTRAINT chk_emergency_event_status_dates
                                      CHECK (
                                          (status='ACTIVE' AND end_datetime IS NULL)
                                              OR
                                          (status = 'CLOSED' AND end_datetime IS NOT NULL)
                                          ),

                                  description NVARCHAR(1000) NULL,

                                  estimated_affected_people INT NULL,
                                  CONSTRAINT chk_estimated_affected_people
                                      CHECK (
                                          estimated_affected_people IS NULL
                                              OR estimated_affected_people >= 0
                                          )
);
GO

--Creare tabela pentru adaposturi
CREATE TABLE shelters (
                          shelter_id INT IDENTITY(1,1) PRIMARY KEY,
                          name NVARCHAR(150) NOT NULL,

--Localizare geografica
                          country NVARCHAR(100) NOT NULL,
                          county NVARCHAR(100) NOT NULL,
                          locality NVARCHAR(100) NOT NULL,
                          address NVARCHAR(250) NOT NULL,

--Capacitatea totala trebuie sa fie mai mare decat 0
                          total_capacity INT NOT NULL
                              CONSTRAINT chk_shelter_capacity
                                  CHECK (total_capacity > 0),

--Status
                          status VARCHAR(10) NOT NULL DEFAULT 'OPEN'
                              CONSTRAINT chk_shelter_status
                                  CHECK ( status IN ('OPEN', 'CLOSED')),

--Date de contact
                          phone_number VARCHAR(20) NULL,
                          email VARCHAR(100) NULL,

--Facilitati
                          access_ramp BIT NOT NULL DEFAULT 0,
                          children_area BIT NOT NULL DEFAULT 0,
                          medical_room BIT NOT NULL DEFAULT 0,
                          accepts_pets BIT NOT NULL DEFAULT 0,

                          public_information NVARCHAR(500) NULL
);
GO

--Creare tabela persoane
CREATE TABLE persons (
                         person_id INT IDENTITY(1,1) PRIMARY KEY,

--Date personale
                         first_name NVARCHAR(100) NOT NULL,
                         last_name NVARCHAR(100) NOT NULL,
                         birth_date DATE NULL,

--Date de contact
                         phone_number VARCHAR(20) NULL,
                         email VARCHAR(100) NULL,

--Adresa de domiciliu
                         home_country NVARCHAR(100) NULL,
                         home_county NVARCHAR(100) NULL,
                         home_locality NVARCHAR(100) NULL,
                         home_address NVARCHAR(250) NULL,

--Informatii suplimentare
                         notes NVARCHAR(500) NULL
);
GO

--Creare tabela pentru asocierea unei persoane la un eveniment de urgenta
CREATE TABLE evacuation_records (
                                    evacuation_id INT IDENTITY(1,1) PRIMARY KEY,

--Persoana inregistrata
                                    person_id INT NOT NULL,
                                    CONSTRAINT fk_evacuation_person
                                        FOREIGN KEY (person_id)
                                            REFERENCES persons(person_id),
--Evenimentul de urgenta in cadrul careia are loc evacuarea
                                    event_id INT NOT NULL,
                                    CONSTRAINT fk_evacuation_event
                                        FOREIGN KEY (event_id)
                                            REFERENCES emergency_events(event_id),
--O persoana poate fi inregistrata o singura data in cadrul aceluiasi eveniment
                                    CONSTRAINT uq_evacuation_person_event
                                        UNIQUE (person_id, event_id),

--Data si ora inregistrarii
                                    registration_datetime DATETIME2(0) NOT NULL DEFAULT SYSDATETIME(),

--Locul din care persoana a fost evacuata
                                    evacuation_location NVARCHAR(250) NULL,

--Indica daca persoana are nevoie de ajutor suplimentar
                                    needs_assistance BIT NOT NULL DEFAULT 0,
--Detalii despre transport, mobilitate sau alte nevoi
                                    assistance_details NVARCHAR(250) NULL,

--Informatii suplimentare
                                    notes NVARCHAR(500) NULL
);
GO

--Creare tabela care pastreaza istoricul cazarilor persoanelor evacuate
CREATE TABLE accommodations(
                               accommodation_id INT IDENTITY(1,1) PRIMARY KEY,

--Inregistrare evacuare
                               evacuation_id INT NOT NULL,
                               CONSTRAINT fk_accommodation_evacuation
                                   FOREIGN KEY (evacuation_id)
                                       REFERENCES evacuation_records(evacuation_id),
--Inregistrare adapost
                               shelter_id INT NOT NULL,
                               CONSTRAINT fk_accommodation_shelter
                                   FOREIGN KEY (shelter_id)
                                       REFERENCES shelters(shelter_id),

--Check-in si check-out
                               check_in_datetime DATETIME2(0) NOT NULL DEFAULT SYSDATETIME(),
                               check_out_datetime DATETIME2(0) NULL,
                               CONSTRAINT chk_accommodation_dates
                                   CHECK (
                                       check_out_datetime IS NULL --persoana este inca in adapost
                                           OR check_out_datetime >= check_in_datetime --check-out-ul nu poate fi inaintea check-in-ului
                                       ),

                               notes NVARCHAR(500) NULL
);

--Impiedica doua cazari active simultan pentru aceeasi evacuare
CREATE UNIQUE INDEX uq_active_accommodation_per_evacuation
    ON accommodations(evacuation_id)
    WHERE check_out_datetime IS NULL;
GO

--Creare tabela pentru rapoartele de disparitie
CREATE TABLE missing_person_reports (
                                        report_id INT IDENTITY(1,1) PRIMARY KEY,

--Persoana raportata disparuta
                                        person_id INT NOT NULL,
                                        CONSTRAINT fk_missing_report_person
                                            FOREIGN KEY (person_id)
                                                REFERENCES persons (person_id),
--Evenimentul in cadrul careia a fost raportata disparitia
                                        event_id INT NOT NULL,
                                        CONSTRAINT fk_missing_report_event
                                            FOREIGN KEY (event_id)
                                                REFERENCES emergency_events(event_id),
                                        CONSTRAINT uq_missing_report_person_event
                                            UNIQUE(person_id, event_id),

                                        reported_datetime DATETIME2(0) NOT NULL DEFAULT SYSDATETIME(),

                                        last_seen_datetime DATETIME2(0) NULL,
                                        last_known_location NVARCHAR(250) NULL,

                                        reported_by_name NVARCHAR(200) NOT NULL,
                                        reported_by_phone VARCHAR(20) NULL,

                                        status VARCHAR(20) NOT NULL DEFAULT 'MISSING',
                                        CONSTRAINT chk_missing_report_status
                                            CHECK(status IN ('MISSING',
                                                             'LOCATED_SAFE',
                                                             'HOSPITALIZED',
                                                             'DECEASED',
                                                             'CANCELLED'
                                                )),

                                        resolved_datetime DATETIME2(0) NULL,
--Un raport activ nu are data de rezolvare in timp ce un raport rezolvat trebuie sa aiba una
                                        CONSTRAINT chk_missing_report_resolution
                                            CHECK(
                                                (status ='MISSING' AND resolved_datetime IS NULL)
                                                    OR
                                                (status <> 'MISSING' AND resolved_datetime IS NOT NULL)
                                                ),
--Rezolvarea nu poate avea loc inaintea raportarii
                                        CONSTRAINT chk_missing_report_resolution_date
                                            CHECK(
                                                resolved_datetime IS NULL
                                                    OR
                                                resolved_datetime >= reported_datetime
                                                ),
--Persoana nu poate fi vazuta ultima data dupa raportarea disparitiei
                                        CONSTRAINT chk_missing_report_last_seen
                                            CHECK(
                                                last_seen_datetime IS NULL
                                                    OR
                                                last_seen_datetime <= reported_datetime
                                                ),

                                        notes NVARCHAR(1000) NULL
);
GO