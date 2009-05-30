BEGIN;

-- wikipedia
INSERT INTO serviceTag
(svc_id, tag) VALUES
((SELECT svc_id FROM service WHERE name='de.wikipedia.org'), 'wikipedia');

INSERT INTO serviceTag
(svc_id, tag) VALUES
((SELECT svc_id FROM service WHERE name='de.wikipedia.org'), 'lexikon');

INSERT INTO serviceTag
(svc_id, tag) VALUES
((SELECT svc_id FROM service WHERE name='de.wikipedia.org'), 'wiki');

INSERT INTO serviceTag
(svc_id, tag) VALUES
((SELECT svc_id FROM service WHERE name='de.wikipedia.org'), 'enzyklopädie');

-- last.fm
INSERT INTO serviceTag
(svc_id, tag) VALUES
((SELECT svc_id FROM service WHERE name='last.fm'), 'lastfm');

INSERT INTO serviceTag
(svc_id, tag) VALUES
((SELECT svc_id FROM service WHERE name='last.fm'), 'event');

INSERT INTO serviceTag
(svc_id, tag) VALUES
((SELECT svc_id FROM service WHERE name='last.fm'), 'ereignis');

-- panoramio

INSERT INTO serviceTag
(svc_id, tag) VALUES
((SELECT svc_id FROM service WHERE name='panoramio.com'), 'panoramio');

INSERT INTO serviceTag
(svc_id, tag) VALUES
((SELECT svc_id FROM service WHERE name='panoramio.com'), 'foto');

INSERT INTO serviceTag
(svc_id, tag) VALUES
((SELECT svc_id FROM service WHERE name='panoramio.com'), 'bild');

INSERT INTO serviceTag
(svc_id, tag) VALUES
((SELECT svc_id FROM service WHERE name='panoramio.com'), 'picture');

INSERT INTO serviceTag
(svc_id, tag) VALUES
((SELECT svc_id FROM service WHERE name='panoramio.com'), 'photo');

COMMIT;
