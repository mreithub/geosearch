BEGIN;

CREATE TABLE geoObject (
	obj_id		bigserial	NOT NULL,
	svc_id		integer		NOT NULL,
	uid		varchar(255)	NOT NULL,
	title		varchar(255)	NOT NULL,
	link		varchar(4096)	NOT NULL,
	lat		float		NOT NULL,
	lng		float		NOT NULL,
	last_updated	timestamp	NOT NULL	DEFAULT now(),
	rndVal		int		NOT NULL	DEFAULT random()*2147483647,
	PRIMARY KEY (obj_id),
	FOREIGN KEY (svc_id) REFERENCES service (svc_id),
	UNIQUE (svc_id, uid)
);

CREATE INDEX geoObject_svc_id ON geoObject (svc_id);
CREATE INDEX geoObject_uid ON geoObject (uid);
CREATE INDEX geoObject_lat ON geoObject (lat);
CREATE INDEX geoObject_lng ON geoObject (lng);
CREATE INDEX geoObject_rndVal ON geoObject(rndVal);

ALTER TABLE geoObject OWNER TO ${psql.user};

COMMIT;
