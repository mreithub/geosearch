BEGIN;

INSERT INTO serviceTag (svc_id, tag) VALUES (
(SELECT svc_id FROM service where name='example.com'),
'stag1');
INSERT INTO serviceTag (svc_id, tag) VALUES (
(SELECT svc_id FROM service where name='example.com'),
'stag2');


COMMIT;
