BEGIN;

CREATE TABLE geoRoute (
	obj_id		bigint		NOT NULL,
	route		path		NOT NULL,
	PRIMARY KEY (obj_id),
	FOREIGN KEY (obj_id) REFERENCES geoObject (obj_id)
);

ALTER TABLE geoRoute OWNER TO ${psql.user};

COMMIT;
