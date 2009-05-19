CREATE TABLE objectTag (
	obj_id	bigint		NOT NULL,
	tag	varchar(255)	NOT NULL,
	PRIMARY KEY (tag, obj_id),
	FOREIGN KEY (obj_id) REFERENCES geoObject (obj_id)
);

ALTER TABLE objectTag OWNER TO geoSearch_web;
