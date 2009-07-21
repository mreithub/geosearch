DROP FUNCTION IF EXISTS _recSearch (
	IN p_tags character varying[],
	IN p_tagIndex integer,
	IN p_objId bigint
	);

CREATE FUNCTION _recSearch (
	IN p_tags character varying[],
	IN p_tagIndex integer,
	IN p_objId bigint
	) RETURNS BOOLEAN AS
$$
BEGIN
IF (p_tagIndex > array_upper(p_tags, 1)) THEN 
	RETURN true;
END IF;
-- recursively call _recSearch if the current object has the current tag
IF (SELECT COUNT(*) FROM objectTag WHERE tag = p_tags[p_tagIndex] AND obj_id = p_objId) = 1 OR
    (SELECT COUNT(*) FROM serviceTag WHERE tag = p_tags[p_tagIndex] AND svc_id IN (select svc_id FROM geoObject WHERE obj_id = p_objId)) = 1 THEN
	RETURN _recSearch(p_tags, p_tagIndex+1, p_objId);
ELSE
	RETURN false;
END IF;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

