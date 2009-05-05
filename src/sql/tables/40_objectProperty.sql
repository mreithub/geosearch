CREATE TABLE objectProperty (
	obj_id	bigint		NOT NULL,
	p_name	varchar(255)	NOT NULL,
	value	varchar(255)	NOT NULL,
	PRIMARY KEY (obj_id, p_name),
	FOREIGN KEY (obj_id) REFERENCES geoObject(obj_id),
	FOREIGN KEY (p_name) REFERENCES property(p_name)
);

ALTER TABLE objectProperty OWNER TO geoSearch_web;
