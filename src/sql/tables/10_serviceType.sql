CREATE TABLE serviceType (
	stype_id	serial		NOT NULL,
	name		varchar(255)	NOT NULL,
	thumbnail	varchar(255)	NOT NULL,
	PRIMARY KEY (stype_id)
);

ALTER TABLE serviceType OWNER TO geoSearch_web;
