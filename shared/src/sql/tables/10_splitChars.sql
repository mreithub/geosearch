BEGIN;

create table splitChars (
	c char(1) not null,
	primary key (c)
);

ALTER TABLE splitChars OWNER TO ${psql.user};

COMMIT;
