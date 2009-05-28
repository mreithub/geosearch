CREATE TABLE expiringObject (
	obj_id		bigint		NOT NULL,
	valid_until	timestamp	NOT NULL,
	PRIMARY KEY (obj_id),
	FOREIGN KEY (obj_id) REFERENCES geoObject (obj_id)
);

ALTER TABLE expiringObject OWNER TO ${psql.user};
