CREATE TABLE geoRoute (
	obj_id		bigint		NOT NULL,
	created_at	timestamp	NOT NULL,
	route		path		NOT NULL,
	PRIMARY KEY (obj_id, created_at),
	FOREIGN KEY (obj_id, created_at) REFERENCES geoPosition (obj_id, created_at)
);

ALTER TABLE geoRoute OWNER TO geoSearch_web;
