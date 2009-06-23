BEGIN;

CREATE TABLE objectProperty (
	obj_id	bigint		NOT NULL,
	name	varchar(255)	NOT NULL,
	value	varchar(1000)	NOT NULL,
	PRIMARY KEY (obj_id, name),
	FOREIGN KEY (obj_id) REFERENCES geoObject(obj_id)
);

CREATE INDEX objectProperty_obj_id ON objectProperty (obj_id);

ALTER TABLE objectProperty OWNER TO ${psql.user};

COMMIT;
