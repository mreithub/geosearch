BEGIN;

DROP TRIGGER IF EXISTS objectTag_after_update on objectTag;
DROP FUNCTION IF EXISTS trig_objectTag_after_update();

CREATE FUNCTION trig_objectTag_after_update() RETURNS trigger AS $$
BEGIN
	
	-- check if the tag's name has changed
	if OLD.tag != NEW.tag THEN
		PERFORM pgq.insert_event('countStatistics', 'insertTag', NEW.tag);
		PERFORM pgq.insert_event('countStatistics', 'deleteTag', OLD.tag);
	END IF;
	
	RETURN NEW;
END
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER objectTag_after_update AFTER UPDATE ON objectTag EXECUTE PROCEDURE trig_objectTag_after_update();

COMMIT;
