BEGIN;

CREATE TABLE stopWords (
	word varchar(255) NOT NULL,
	PRIMARY KEY (word)
);

ALTER TABLE stopWords OWNER TO ${psql.user}

COMMIT;
