BEGIN;

DROP TRIGGER IF EXISTS geoObject_after_update on geoObject;
DROP FUNCTION IF EXISTS trig_geoObject_after_update();

CREATE FUNCTION trig_geoObject_after_update() RETURNS trigger AS $$
BEGIN
	-- check if the object's service has changed
	if OLD.svc_id != NEW.svc_id THEN
		PERFORM pgq.insert_event('countStatistics', 'insertService', NEW.svc_id::text);
		PERFORM pgq.insert_event('countStatistics', 'deleteService', OLd.svc_id::text);
	END IF;
	
	RETURN NEW;
END
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER geoObject_after_update AFTER UPDATE ON geoObject EXECUTE PROCEDURE trig_geoObject_after_update();


COMMIT;
