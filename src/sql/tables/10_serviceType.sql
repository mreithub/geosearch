CREATE TABLE serviceType (
	stype_id	integer		NOT NULL,
	name		varchar(255)	NOT NULL,
	thumbnail	varchar(255)	NOT NULL,
	bubbleHTML	text		NOT NULL,
	PRIMARY KEY (stype_id)
);

ALTER TABLE serviceType OWNER TO geoSearch_web;
