BEGIN;

CREATE TABLE serviceTag (
	svc_id		integer		NOT NULL,
	tag		varchar(255)	NOT NULL,
	PRIMARY KEY (svc_id, tag),
	FOREIGN KEY (svc_id) REFERENCES service (svc_id),
	FOREIGN KEY (tag) REFERENCES tag (tag)
);

CREATE INDEX serviceTag_svc_id ON serviceTag (svc_id);
CREATE INDEX serviceTag_tag ON serviceTag (tag);


ALTER TABLE serviceTag OWNER TO ${psql.user};

COMMIT;
