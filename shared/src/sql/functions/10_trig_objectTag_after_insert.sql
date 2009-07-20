BEGIN;

DROP TRIGGER IF EXISTS objectTag_after_insert on objectTag;
DROP FUNCTION IF EXISTS trig_objectTag_after_insert();

CREATE FUNCTION trig_objectTag_after_insert() RETURNS trigger AS $$
BEGIN
	PERFORM pgq.insert_event('countStatistics', 'insertTag', NEW.tag);
	RETURN NULL;
END
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER objectTag_after_insert AFTER INSERT ON objectTag EXECUTE PROCEDURE trig_objectTag_after_insert();


COMMIT;
