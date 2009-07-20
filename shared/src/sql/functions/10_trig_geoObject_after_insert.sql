BEGIN;

DROP TRIGGER IF EXISTS geoObject_after_insert on geoObject;
DROP FUNCTION IF EXISTS trig_geoObject_after_insert();

CREATE FUNCTION trig_geoObject_after_insert() RETURNS trigger AS $$
BEGIN
	-- increment object count for this service
	-- (overall count is set automatically by the queue processor)
	PERFORM pgq.insert_event('countStatistics', 'insertService', NEW.svc_id::text);
	RETURN NULL;
END
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER geoObject_after_insert AFTER INSERT ON geoObject EXECUTE PROCEDURE trig_geoObject_after_insert();

COMMIT;
