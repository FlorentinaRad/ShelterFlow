--creare baza de date
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

                                  status VARCHAR(10) NOT NULL
                                      DEFAULT 'ACTIVE',
    --Statusuri acceptate
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