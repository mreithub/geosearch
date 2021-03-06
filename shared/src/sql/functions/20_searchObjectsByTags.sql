BEGIN;

DROP FUNCTION IF EXISTS searchObjectsByTags (
	IN p_tags character varying[],
	IN p_lat1 double precision,
	IN p_lat2 double precision,
	IN p_lng1 double precision,
	IN p_lng2 double precision,
	IN p_resultLimit int
	);

CREATE FUNCTION searchObjectsByTags(
	IN p_tags character varying[],
	IN p_lat1 double precision,
	IN p_lat2 double precision,
	IN p_lng1 double precision,
	IN p_lng2 double precision,
	IN p_resultLimit int
	) RETURNS SETOF bigint AS
$$
DECLARE
	existingCount integer := COUNT(tag) FROM tag WHERE tag = ANY(p_tags);
	tagCount integer := ARRAY_UPPER(p_tags, 1);
	tagSorted varchar[] := ARRAY(SELECT tag FROM tag WHERE tag = any(p_tags) ORDER BY objectCount);

	var_objId bigint;
	var_lat double precision;
	var_lng double precision;
	var_objCount int := 0;
BEGIN
	-- if no tags were entered
	if (tagCount IS NULL) then
		FOR var_objId IN EXECUTE 'SELECT obj_id FROM geoObject
		    WHERE lat BETWEEN '||p_lat1||' AND '||p_lat2||
		    'AND lng BETWEEN '||p_lng1||' AND '||p_lng2||
		    'ORDER BY rndVal DESC LIMIT '||p_resultLimit
		LOOP
			RETURN NEXT var_objId;
		END LOOP;
	ELSIF COUNT(*) from servicetag where tag = tagSorted[1] then
		-- a serviceTag exists => just search for the servicetag
		SET LOCAL enable_sort = off;
		FOR var_objId IN
			SELECT obj_id FROM geoObject
			WHERE svc_id IN (SELECT svc_id FROM serviceTag WHERE tag = tagSorted[1]) AND lat between p_lat1 and p_lat2 and lng between p_lng1 and p_lng2 ORDER BY rndVal DESC
		LOOP
			IF var_objCount >= p_resultLimit THEN
				SET LOCAL enable_sort = on;
				RETURN;
			END IF;
			IF _recSearch(tagSorted, 2, var_objId) THEN
				RETURN NEXT var_objId;
				var_objCount := var_objCount+1;
			END IF;
		END LOOP;
		SET LOCAL enable_sort = on;
	ELSE
		-- just search for the objectTag
		FOR var_objId, var_lat, var_lng IN
			SELECT obj_id, lat, lng FROM geoObject
			WHERE obj_id IN (SELECT obj_id FROM objectTag WHERE tag = tagSorted[1])
			ORDER BY rndVal DESC
		LOOP 
			IF var_objCount >= p_resultLimit THEN
				return;
			END IF;
			IF var_lat BETWEEN p_lat1 AND p_lat2 AND var_lng BETWEEN p_lng1 AND p_lng2 THEN
				IF _recSearch(tagSorted, 2, var_objId) THEN
					return next var_objId;
					var_objCount := var_objCount+1;
				END IF;
			END IF;
		END LOOP;
	END IF;
END
$$ LANGUAGE 'plpgsql' VOLATILE;

COMMIT;
