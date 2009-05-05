CREATE TABLE serviceProperty (
	svc_id	integer		NOT NULL,
	p_name	varchar(255)	NOT NULL,
	value	varchar(255)	NOT NULL,
	PRIMARY KEY (svc_id, p_name),
	FOREIGN KEY (svc_id) REFERENCES service (svc_id),
	FOREIGN KEY (p_name) REFERENCES property (p_name)
);

ALTER TABLE serviceProperty OWNER TO geoSearch_web;
