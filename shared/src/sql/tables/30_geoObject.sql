BEGIN;

CREATE TABLE geoObject (
	obj_id		bigserial	NOT NULL,
	svc_id		integer		NOT NULL,
	uid		varchar(255)	NOT NULL,
	title		varchar(255)	NOT NULL,
	link		varchar(255)	NOT NULL,
	pos		point		NOT NULL,
	last_updated	timestamp	NOT NULL	DEFAULT now(),
	PRIMARY KEY (obj_id),
	FOREIGN KEY (svc_id) REFERENCES service (svc_id),
	UNIQUE (svc_id, uid)
);

ALTER TABLE geoObject OWNER TO ${psql.user};

COMMIT;