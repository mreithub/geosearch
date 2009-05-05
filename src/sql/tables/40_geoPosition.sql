CREATE TABLE geoPosition (
	obj_id		bigint		NOT NULL,
	created_at	timestamp	NOT NULL	DEFAULT now(),
	pos		point		NOT NULL,
	valid_until	timestamp	NULL,
	PRIMARY KEY (obj_id, created_at),
	FOREIGN KEY (obj_id) REFERENCES geoObject(obj_id)
);

ALTER TABLE geoPosition OWNER TO geoSearch_web;
