DROP DATABASE HELIOS;
CREATE DATABASE HELIOS;

use HELIOS;

CREATE TABLE tPerson
(
	nPersonId serial,
	cForename VARCHAR(60) NOT NULL,
	cSurname VARCHAR(60) NOT NULL,
	cEMTNumber VARCHAR(20) NOT NULL,
	cPassword VARCHAR(60) NOT NULL,
	cEmail VARCHAR(60),
	nMaxShiftCount INTEGER,
	nMinShiftInterval INTEGER
);

CREATE TABLE tDay
(
	nDayId serial,
	cName VARCHAR(20),
	cCode VARCHAR(2)
);

CREATE TABLE tRule
(
	nRuleId serial,
	nRuleOwnerId INTEGER,
	bLikes BOOL,
	nDayId INTEGER,
	nFriendId INTEGER,
	dRelevant DATE,
	cShifttype VARCHAR(10),
	
	FOREIGN KEY(nRuleOwnerId) REFERENCES tPerson(nPersonId),
	FOREIGN KEY(nDayId) REFERENCES tDay(nDayId),
	FOREIGN KEY(nFriendId) REFERENCES tPerson(nPersonId)
);

CREATE TABLE tTraining
(
	nTrainingId serial,
	cName VARCHAR(60),
	cCode VARCHAR(5)
);

CREATE TABLE tPersonTraining
(
	nPersonTrainingId serial,
	nPersonId INTEGER,
	nTrainingId INTEGER,
	nMaxShiftCount INTEGER DEFAULT -1,
	FOREIGN KEY(nPersonId) REFERENCES tPerson(nPersonId),
	FOREIGN KEY(nTrainingId) REFERENCES tTraining(nTrainingId)
);

CREATE TABLE tDepartment
(
	nDepartmentId serial,
	cName VARCHAR(60),
	nNEFCount INTEGER,
	nRTWCount INTEGER,
	nSTBCount INTEGER,
	cUsername VARCHAR(60),
	cPassword VARCHAR(60)
);

CREATE TABLE tPersonDepartment
(
	nPersonDepartmentId serial,
	nPersonId INTEGER,
	nDepartmentId INTEGER,
	FOREIGN KEY(nPersonId) REFERENCES tPerson(nPersonId),
	FOREIGN KEY(nDepartmentId) REFERENCES tDepartment(nDepartmentId)
);

CREATE TABLE tEvent
(
	nEventId serial,
	cName VARCHAR(120),
	dEvent DATE,
	cFrom VARCHAR(5),
	cTo VARCHAR(5)
);

CREATE TABLE tPersonEvent
(
	nPersonEventId serial,
	nPersonId INTEGER,
	nEventId INTEGER,
	FOREIGN KEY(nPersonId) REFERENCES tPerson(nPersonId),
	FOREIGN KEY(nEventId) REFERENCES tEvent(nEventId)
);

CREATE TABLE tTraningEvent
(
	nTrainingEventId serial,
	nEventId INTEGER,
	nTrainingId INTEGER,
	nNeededPersons INTEGER,
	FOREIGN KEY(nEventId) REFERENCES tEvent(nEventId),
	FOREIGN KEY(nTrainingId) REFERENCES tTraining(nTrainingId)
);

CREATE TABLE tShiftplan
(
	nShiftplanId serial,
	nDepartmentId INTEGER,
	cName VARCHAR(120),
	cFilepath VARCHAR(120),
	bConfirmed BOOLEAN DEFAULT FALSE,
	FOREIGN KEY(nDepartmentId) REFERENCES tDepartment(nDepartmentId)
);

CREATE TABLE tShiftTree
(
	nShiftTreeId serial,
	nShiftplanId INTEGER,
	dPeriodStart DATE,
	dPeriodEnd DATE,
	nTeamCount INTEGER,
	cTeamType VARCHAR(10),
	FOREIGN KEY(nShiftplanId) REFERENCES tShiftplan(nShiftplanId)
);

CREATE TABLE tShiftNode
(
	nShiftId serial,
	nDriverId INTEGER,
	nMediId INTEGER,
	nAdditionalId INTEGER,
	nShiftTreeId INTEGER,
	dShift DATE,
	cShifttype VARCHAR(10),
	nTeamNumber INTEGER,
	FOREIGN KEY(nDriverId) REFERENCES tPerson(nPersonId),
	FOREIGN KEY(nMediId) REFERENCES tPerson(nPersonId),
	FOREIGN KEY(nAdditionalId) REFERENCES tPerson(nPersonId),
	FOREIGN KEY(nShiftTreeId) REFERENCES tShiftTree(nShiftTreeId)
);

INSERT INTO tDay(cName, cCode) VALUES('Montag', 'MO');
INSERT INTO tDay(cName, cCode) VALUES('Dienstag', 'DI');
INSERT INTO tDay(cName, cCode) VALUES('Mittwoch', 'MI');
INSERT INTO tDay(cName, cCode) VALUES('Donnerstag', 'DO');
INSERT INTO tDay(cName, cCode) VALUES('Freitag', 'FR');
INSERT INTO tDay(cName, cCode) VALUES('Samstag', 'SA');
INSERT INTO tDay(cName, cCode) VALUES('Sonntag', 'SO');

INSERT INTO tTraining(nTrainingId, cName, cCode) VALUES (1, 'RTW-Fahrer', 'RTWF');
INSERT INTO tTraining(nTrainingId, cName, cCode) VALUES (2, 'RTW-Sanitäter', 'RTWS');
INSERT INTO tTraining(nTrainingId, cName, cCode) VALUES (3, 'NEF-Fahrer', 'NEFF');
INSERT INTO tTraining(nTrainingId, cName, cCode) VALUES (4, 'NEF-Sanitäter', 'NEFS');
INSERT INTO tTraining(nTrainingId, cName, cCode) VALUES (5, 'Bereitschafts-Fahrer', 'STBF');
INSERT INTO tTraining(nTrainingId, cName, cCode) VALUES (6, 'Bereitschafts-Sanitäter', 'STBS');
INSERT INTO tTraining(nTrainingId, cName, cCode) VALUES (7, 'Probehelfer', 'ADD');