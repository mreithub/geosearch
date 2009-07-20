BEGIN;

DROP TRIGGER IF EXISTS objectTag_before_delete on objectTag;
DROP FUNCTION IF EXISTS trig_objectTag_before_delete();

CREATE FUNCTION trig_objectTag_before_delete() RETURNS trigger AS $$
BEGIN
	PERFORM pgq.insert_event('countStatistics', 'deleteTag', OLD.tag);
	RETURN NULL;
END
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER objectTag_before_delete BEFORE DELETE ON objectTag EXECUTE PROCEDURE trig_objectTag_before_delete();

COMMIT;
