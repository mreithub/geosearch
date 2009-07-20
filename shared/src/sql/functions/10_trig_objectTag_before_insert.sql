BEGIN;

DROP TRIGGER IF EXISTS objectTag_before_insert on objectTag;
DROP FUNCTION IF EXISTS trig_objectTag_before_insert();

CREATE FUNCTION trig_objectTag_before_insert() RETURNS trigger AS $$
BEGIN
	-- add item to tag list if it doesn't yet exist
	IF (COUNT(tag) from tag where tag = NEW.tag) = 0 THEN
		INSERT INTO tag (tag, objectCount) VALUES (NEW.tag, 0);
	END IF;
	RETURN NEW;
END
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER objectTag_before_insert BEFORE INSERT ON objectTag EXECUTE PROCEDURE trig_objectTag_before_insert();


COMMIT;
