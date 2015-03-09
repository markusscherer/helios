ALTER TABLE tShiftplan ADD COLUMN
cFilepath VARCHAR(120);

ALTER TABLE tShiftplan ADD COLUMN
bConfirmed BOOLEAN DEFAULT FALSE;

ALTER TABLE tShiftNode CHANGE  cTeamNumber nTeamNumber INTEGER;

ALTER TABLE tPersonTraining CHANGE  nMaxShiftCount nMaxShiftCount INTEGER DEFAULT -1;

INSERT INTO tTraining(cName, cCode) VALUES ('Probehelfer', 'ADD');

ALTER TABLE tDepartment ADD COLUMN
cUsername VARCHAR(60);

ALTER TABLE tDepartment ADD COLUMN
cPassword VARCHAR(60);

INSERT INTO tTraining(cName, cCode) VALUES ('Bereitschaft-Fahrer', 'STBF');
INSERT INTO tTraining(cName, cCode) VALUES ('Bereitschaft-Sanitäter', 'STBS');