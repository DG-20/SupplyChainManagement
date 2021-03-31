DROP DATABASE IF EXISTS INVENTORY;
CREATE DATABASE INVENTORY; 
USE INVENTORY;


DROP TABLE IF EXISTS MANUFACTURER;
CREATE TABLE MANUFACTURER (
	ManuID			char(3) not null,
	Name			varchar(25),
	Phone			char(12),
    Province		char(2),
	primary key (ManuID)
);

INSERT INTO MANUFACTURER (ManuID, Name, Phone, Province)
VALUES
('001',	'Academic Desks',	'236-145-2542',	'BC'),
('002',	'Office Furnishings',	'587-890-4387',	'AB'),
('003',	'Chairs R Us',	'705-667-9481',	'ON'),
('004',	'Furniture Goods',	'306-512-5508',	'SK'),
('005',	'Fine Office Supplies',	'403-980-9876',	'AB');

DROP TABLE IF EXISTS CHAIR;
CREATE TABLE CHAIR (
	ID				char(5)	not null,
	Type			varchar(25),
	Legs			char(1),
	Arms			char(1),
	Seat			char(1),
	Cushion			char(1),
    Price			integer,
	ManuID			char(3),
	primary key (ID),
	foreign key (ManuID) references MANUFACTURER(ManuID) ON UPDATE CASCADE
);

INSERT INTO CHAIR (ID, Type, Legs, Arms, Seat, Cushion, Price, ManuID)
VALUES
('C1320',	'Kneeling',	'Y',	'N',	'N',	'N',	50,	'002'),
('C3405',	'Task',	'Y',	'Y',	'N',	'N',	100,	'003'),
('C9890',	'Mesh',	'N',	'Y',	'N',	'Y',	50,	'003'),
('C7268',	'Executive',	'N',	'N',	'Y',	'N',	75,	'004'),
('C0942',	'Mesh',	'Y',	'N',	'Y',	'Y',	100,	'005'),
('C4839',	'Ergonomic',	'N',	'N',	'N',	'Y',	50,	'002'),
('C2483',	'Executive',	'Y',	'Y',	'N',	'N',	175,	'002'),
('C5789',	'Ergonomic',	'Y',	'N',	'N',	'Y',	125,	'003'),
('C3819',	'Kneeling',	'N',	'N',	'Y',	'N',	75,	'005'),
('C5784',	'Executive',	'Y',	'N',	'N',	'Y',	150,	'004'),
('C6748',	'Mesh',	'Y',	'N',	'N',	'N',	75,	'003'),
('C0914',	'Task',	'N',	'N',	'Y',	'Y',	50,	'002'),
('C1148',	'Task',	'Y',	'N',	'Y',	'Y',	125,	'003'),
('C5409',	'Ergonomic',	'Y',	'Y',	'Y',	'N',	200,	'003'),
('C8138',	'Mesh',	'N',	'N',	'Y',	'N',	75,	'005');

DROP TABLE IF EXISTS DESK;
CREATE TABLE DESK (
	ID				char(5)	not null,
	Type			varchar(25),
	Legs			char(1),
	Top			char(1),
	Drawer			char(1),
    Price			integer,
	ManuID			char(3),
	primary key (ID),
	foreign key (ManuID) references MANUFACTURER(ManuID) ON UPDATE CASCADE
);

INSERT INTO DESK (ID, Type, Legs, Top, Drawer, Price, ManuID)
VALUES
('D3820',	'Standing',	'Y',	'N',	'N',	150,	'001'),
('D4475',	'Adjustable',	'N',	'Y',	'Y',	200,	'002'),
('D0890',	'Traditional',	'N',	'N',	'Y',	25,	'002'),
('D2341',	'Standing',	'N',	'Y',	'N',	100,	'001'),
('D9387',	'Standing',	'Y',	'Y',	'N',	250,	'004'),
('D7373',	'Adjustable',	'Y',	'Y',	'N',	350,	'005'),
('D2746',	'Adjustable',	'Y',	'N',	'Y',	250,	'004'),
('D9352',	'Traditional',	'Y',	'N',	'Y',	75,	'002'),
('D4231',	'Traditional',	'N',	'Y',	'Y',	50,	'005'),
('D8675',	'Traditional',	'Y',	'Y',	'N',	75,	'001'),
('D1927',	'Standing',	'Y',	'N',	'Y',	200,	'005'),
('D1030',	'Adjustable',	'N',	'Y',	'N',	150,	'002'),
('D4438',	'Standing',	'N',	'Y',	'Y',	150,	'004'),
('D5437',	'Adjustable',	'Y',	'N',	'N',	50,	'001'),
('D3682',	'Adjustable',	'N',	'N',	'Y',	50,	'005');

DROP TABLE IF EXISTS LAMP;
CREATE TABLE LAMP (
	ID				char(4)	not null,
	Type			varchar(25),
	Base			char(1),
	Bulb			char(1),
    Price			integer,
	ManuID			char(3),
	primary key (ID),
	foreign key (ManuID) references MANUFACTURER(ManuID) ON UPDATE CASCADE
);

INSERT INTO LAMP (ID, Type, Base, Bulb, Price, ManuID)
VALUES
('L132',	'Desk',	'Y',	'N',	18,	'005'),
('L980',	'Study',	'N',	'Y',	2,	'004'),
('L487',	'Swing Arm',	'Y',	'N',	27,	'002'),
('L564',	'Desk',	'Y',	'Y',	20,	'004'),
('L342',	'Desk',	'N',	'Y',	2,	'002'),
('L982',	'Study',	'Y',	'N',	8,	'002'),
('L879',	'Swing Arm',	'N',	'Y',	3,	'005'),
('L208',	'Desk',	'N',	'Y',	2,	'005'),
('L223',	'Study',	'N',	'Y',	2,	'005'),
('L928',	'Study',	'Y',	'Y',	10,	'002'),
('L013',	'Desk',	'Y',	'N',	18,	'004'),
('L053',	'Swing Arm',	'Y',	'N',	27,	'002'),
('L112',	'Desk',	'Y',	'N',	18,	'005'),
('L649',	'Desk',	'Y',	'N',	18,	'004'),
('L096',	'Swing Arm',	'N',	'Y',	3,	'002');

DROP TABLE IF EXISTS FILING;
CREATE TABLE FILING (
	ID				char(4)	not null,
	Type			varchar(25),
	Rails			char(1),
	Drawers			char(1),
	Cabinet			char(1),
    Price			integer,
	ManuID			char(3),
	primary key (ID),
	foreign key (ManuID) references MANUFACTURER(ManuID) ON UPDATE CASCADE
);

INSERT INTO FILING (ID, Type, Rails, Drawers, Cabinet, Price, ManuID)
VALUES
('F001',	'Small',	'Y',	'Y',	'N',	50,	'005'),
('F002',	'Medium',	'N',	'N',	'Y',	100,	'004'),
('F003',	'Large',	'N',	'N',	'Y',	150,	'002'),
('F004',	'Small',	'N',	'Y',	'Y',	75,	'004'),
('F005',	'Small',	'Y',	'N',	'Y',	75,	'005'),
('F006',	'Small',	'Y',	'Y',	'N',	50,	'005'),
('F007',	'Medium',	'N',	'Y',	'Y',	150,	'002'),
('F008',	'Medium',	'Y',	'N',	'N',	50,	'005'),
('F009',	'Medium',	'Y',	'Y',	'N',	150,	'004'),
('F010',	'Large',	'Y',	'N',	'Y',	225,	'002'),
('F011',	'Large',	'N',	'Y',	'Y',	225,	'005'),
('F012',	'Large',	'N',	'Y',	'N',	75,	'005'),
('F013',	'Small',	'N',	'N',	'Y',	50,	'002'),
('F014',	'Medium',	'Y',	'Y',	'Y',	200,	'002'),
('F015',	'Large',	'Y',	'N',	'N',	75,	'004');
