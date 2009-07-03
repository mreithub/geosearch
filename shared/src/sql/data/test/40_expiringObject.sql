--
-- Test data: expiring Objects
--

BEGIN;

INSERT INTO expiringObject (obj_id, valid_until)
VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_schoenbrunn'), now());

COMMIT;
