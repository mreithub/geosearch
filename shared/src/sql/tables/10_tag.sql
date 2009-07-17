BEGIN;

create table tag (
	tag varchar(255) not null,
	objectCount integer not null default 0,
	PRIMARY KEY (tag)
);

ALTER TABLE tag OWNER TO ${psql.user};

COMMIT;
