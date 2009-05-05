CREATE TABLE objectTag (
	tag	varchar(255)	NOT NULL,
	obj_id	bigint		NOT NULL,
	PRIMARY KEY (tag, obj_id),
	FOREIGN KEY (tag) REFERENCES tag (tag),
	FOREIGN KEY (obj_id) REFERENCES geoObject (obj_id)
);

ALTER TABLE objectTag OWNER TO geoSearch_web;
