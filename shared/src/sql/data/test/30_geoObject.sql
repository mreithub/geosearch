-- Testdaten für geoObject

BEGIN;

-- Wien: Karlskirche 
INSERT INTO geoObject
(svc_id, uid, title, link, lat, lng, last_updated)
VALUES (
(SELECT svc_id FROM service WHERE name='example.com'),
'test_karlskirche', 'Karlskirche', 'http://de.wikipedia.org/wiki/Wiener_Karlskirche', 48.198247, 16.371422, now());

-- Wien: Stephansdom
INSERT INTO geoObject
(svc_id, uid, title, link, lat, lng, last_updated)
VALUES (
(SELECT svc_id FROM service WHERE name='example.com'),
'test_stephansdom', 'Stephansdom', 'http://de.wikipedia.org/wiki/Stephansdom_%28Wien%29', 48.208333, 16.372778, now());

-- Wien: Schönbrunn
INSERT INTO geoObject
(svc_id, uid, title, link, lat, lng, last_updated)
VALUES (
(SELECT svc_id FROM service WHERE name='example.com'),
'test_schoenbrunn', 'Schönbrunn', 'http://de.wikipedia.org/wiki/Schloss_Sch%C3%B6nbrunn', 48.184517, 16.311864, now());

-- Innsbruck: Goldenes Dachl
INSERT INTO geoObject
(svc_id, uid, title, link, lat, lng, last_updated)
VALUES (
(SELECT svc_id FROM service WHERE name='example.com'),
'test_goldenes_dachl', 'Goldenes Dachl', 'http://de.wikipedia.org/wiki/Goldenes_Dachl', 47.268583, 11.393264, now());

-- Salzburg: Hohensalzburg
INSERT INTO geoObject
(svc_id, uid, title, link, lat, lng, last_updated)
VALUES (
(SELECT svc_id FROM service WHERE name='example.com'),
'test_hohensalzburg', 'Festung Hohensalzburg', 'http://de.wikipedia.org/wiki/Festung_Hohensalzburg', 47.794967, 13.047256, now());

-- Linz: Pöstlingberg
INSERT INTO geoObject
(svc_id, uid, title, link, lat, lng, last_updated)
VALUES (
(SELECT svc_id FROM service WHERE name='example.com'),
'test_poestlingberg', 'Pöstlingberg', 'http://de.wikipedia.org/wiki/P%C3%B6stlingberg', 48.323889, 14.258333, now());

-- Graz: Uhrturm
INSERT INTO geoObject
(svc_id, uid, title, link, lat, lng, last_updated)
VALUES (
(SELECT svc_id FROM service WHERE name='example.com'),
'test_uhrturm', 'Uhrturm', 'http://de.wikipedia.org/wiki/Grazer_Uhrturm', 47.075463, 15.436746, now());

-- Bregenz: Martinsturm
INSERT INTO geoObject
(svc_id, uid, title, link, lat, lng, last_updated)
VALUES (
(SELECT svc_id FROM service WHERE name='example.com'),
'test_martinsturm', 'Martinsturm', 'http://de.wikipedia.org/wiki/Bregenz', 47.505, 9.749167, now());

-- Eisenstadt: Schloss Esterhazy
INSERT INTO geoObject
(svc_id, uid, title, link, lat, lng, last_updated)
VALUES (
(SELECT svc_id FROM service WHERE name='example.com'),
'test_esterhazy', 'Schloss Esterhazy', 'http://de.wikipedia.org/wiki/Schloss_Esterh%C3%A1zy_%28Eisenstadt%29', 47.848611, 16.520833, now());

-- Klagenfurt: Lindwurm
INSERT INTO geoObject
(svc_id, uid, title, link, lat, lng, last_updated)
VALUES (
(SELECT svc_id FROM service WHERE name='example.com'),
'test_lindwurm', 'Lindwurmbrunnen', 'http://de.wikipedia.org/wiki/Lindwurmbrunnen', 46.623997, 14.3077, now());

-- St. Pölten: Rathaus
INSERT INTO geoObject
(svc_id, uid, title, link, lat, lng, last_updated)
VALUES (
(SELECT svc_id FROM service WHERE name='example.com'),
'test_rathaus', 'Rathaus', 'http://de.wikipedia.org/wiki/St._P%C3%B6ltner_Rathaus', 48.204444, 15.622778, now());

COMMIT;
