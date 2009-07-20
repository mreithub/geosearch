BEGIN;

DROP TRIGGER IF EXISTS geoObject_before_delete on geoObject;
DROP FUNCTION IF EXISTS trig_geoObject_before_delete();

CREATE FUNCTION trig_geoObject_before_delete() RETURNS trigger AS $$
BEGIN
	-- decrement the object count for this service
	-- (overall count is set automatically by the queue processor)
	PERFORM pgq.insert_event('countStatistics', 'deleteService', OLD.svc_id::text);
	RETURN NULL;
END
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER geoObject_before_delete BEFORE DELETE ON geoObject EXECUTE PROCEDURE trig_geoObject_before_delete();


COMMIT;
