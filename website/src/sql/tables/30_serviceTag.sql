BEGIN;

CREATE TABLE serviceTag (
	svc_id		integer		NOT NULL,
	tag		varchar(255)	NOT NULL,
	PRIMARY KEY (svc_id, tag),
	FOREIGN KEY (svc_id) REFERENCES service (svc_id)
);

ALTER TABLE serviceTag OWNER TO ${psql.user};

COMMIT;
