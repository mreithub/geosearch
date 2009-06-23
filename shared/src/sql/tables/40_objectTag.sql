BEGIN;

CREATE TABLE objectTag (
	obj_id	bigint		NOT NULL,
	tag	varchar(255)	NOT NULL,
	PRIMARY KEY (obj_id, tag),
	FOREIGN KEY (obj_id) REFERENCES geoObject (obj_id)
);

CREATE INDEX objectTag_obj_id ON objectTag (obj_id);
CREATE INDEX objectTag_tag ON objectTag (tag);

ALTER TABLE objectTag OWNER TO ${psql.user};

COMMIT;
