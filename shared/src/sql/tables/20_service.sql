BEGIN;

CREATE TABLE service (
	svc_id		serial		NOT NULL,
	name		varchar(255)	NOT NULL,
	title		varchar(255)	NOT NULL,
	homepage	varchar(255)	NOT NULL,
	description	text 		NOT NULL,
	stype_id	integer		NOT NULL,
	bubbleHTML	text		NOT NULL,
	thumbnail	varchar(255)	NOT NULL,
	PRIMARY KEY (svc_id),
	FOREIGN KEY (stype_id) REFERENCES serviceType (stype_id),
	UNIQUE (name)
);

ALTER TABLE service OWNER TO ${psql.user};

COMMIT;
