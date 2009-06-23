BEGIN;

CREATE TABLE expiringObject (
	obj_id		bigint		NOT NULL,
	valid_until	timestamp	NOT NULL,
	PRIMARY KEY (obj_id),
	FOREIGN KEY (obj_id) REFERENCES geoObject (obj_id)
);

CREATE INDEX expiringObject_valid_until ON expiringObject (valid_until); 

ALTER TABLE expiringObject OWNER TO ${psql.user};

COMMIT;
