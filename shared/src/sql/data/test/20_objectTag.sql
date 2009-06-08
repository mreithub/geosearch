--
-- Testdaten: objectTag
--

BEGIN;

-- Wien: Karlskirche
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_karlskirche'),
'karlskirche');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_karlskirche'),
'kirche');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_karlskirche'),
'karlsplatz');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_karlskirche'),
'sehenswürdigkeit');

-- Wien: Stephansdom
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_stephansdom'),
'stephansdom');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_stephansdom'),
'kirche');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_stephansdom'),
'stephansplatz');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_stephansdom'),
'wahrzeichen');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_stephansdom'),
'wien');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_stephansdom'),
'sehenswürdigkeit');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_stephansdom'),
'mittelalter');

-- Wien: Schönbrunn
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_schoenbrunn'),
'schloss');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_schoenbrunn'),
'schönbrunn');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_schoenbrunn'),
'schlosspark');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_schoenbrunn'),
'neptunbrunnen');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_schoenbrunn'),
'sehenswürdigkeit');

-- Innsbruck: Goldenes Dachl
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_goldenes_dachl'),
'goldenes');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_goldenes_dachl'),
'dachl');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_goldenes_dachl'),
'innsbruck');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_goldenes_dachl'),
'wahrzeichen');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_goldenes_dachl'),
'sehenswürdigkeit');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_goldenes_dachl'),
'mittelalter');

-- Salzburg: Hohensalzburg
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_hohensalzburg'),
'festung');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_hohensalzburg'),
'hohensalzburg');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_hohensalzburg'),
'salzburg');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_hohensalzburg'),
'wahrzeichen');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_hohensalzburg'),
'burg');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_hohensalzburg'),
'sehenswürdigkeit');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_hohensalzburg'),
'mittelalter');

-- Linz: Pöstlingberg
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_poestlingberg'),
'wallfahrtsbasilika');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_poestlingberg'),
'pöstlingberg');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_poestlingberg'),
'linz');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_poestlingberg'),
'wahrzeichen');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_poestlingberg'),
'kirche');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_poestlingberg'),
'sehenswürdigkeit');

-- Graz: Uhrturm
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_uhrturm'),
'uhrturm');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_uhrturm'),
'turm');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_uhrturm'),
'graz');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_uhrturm'),
'wahrzeichen');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_uhrturm'),
'sehenswürdigkeit');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_uhrturm'),
'mittelalter');

-- Bregenz: Martinsturm
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_martinsturm'),
'martinsturm');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_martinsturm'),
'turm');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_martinsturm'),
'bregenz');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_martinsturm'),
'wahrzeichen');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_martinsturm'),
'sehenswürdigkeit');

-- Eisenstadt: Schloss Esterhazy
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_esterhazy'),
'schloss');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_esterhazy'),
'esterhazy');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_esterhazy'),
'eisenstadt');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_esterhazy'),
'wahrzeichen');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_esterhazy'),
'sehenswürdigkeit');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_esterhazy'),
'mittelalter');

-- Klagenfurt: Lindwurm
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_lindwurm'),
'lindwurm');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_lindwurm'),
'lindwurmbrunnen');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_lindwurm'),
'klagenfurt');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_lindwurm'),
'wahrzeichen');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_lindwurm'),
'sehenswürdigkeit');

-- St. Pölten: Rathaus
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_rathaus'),
'rathaus');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_rathaus'),
'sankt');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_rathaus'),
'st');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_rathaus'),
'pölten');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_rathaus'),
'wahrzeichen');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_rathaus'),
'sehenswürdigkeit');
INSERT INTO objectTag (obj_id, tag) VALUES ((SELECT obj_id FROM geoObject WHERE uid='test_rathaus'),
'mittelalter');


COMMIT;
