BEGIN;

-- de.wikipedia.org
INSERT INTO service
(name, title, homepage, description, stype_id, bubbleHTML, thumbnail) VALUES
('de.wikipedia.org', 'Wikipedia', 'http://de.wikipedia.org/', 'Deutsche Wikipedia (freies Online-Lexicon)',
(SELECT stype_id FROM serviceType WHERE name='lexicon'),
'%summary%', 'images/service/wikipedia.png');

-- last.fm
INSERT INTO service
(name, title, homepage, description, stype_id, bubbleHTML, thumbnail) VALUES
('last.fm', 'last.fm', 'http://www.last.fm/', 'Last.fm Events',
(SELECT stype_id FROM serviceType WHERE name='event'),
'Ort: %location%<br/>Beginn: %begin%<br/>Artisten: %artists%<br/><br/>%description%',
'images/service/lastfm.png');

-- Panoramio
INSERT INTO service
(name, title, homepage, description, stype_id, bubbleHTML, thumbnail) VALUES
('panoramio.com', 'Panoramio', 'http://www.panoramio.com/', 'Online-Foto-Service',
(SELECT stype_id FROM serviceType WHERE name='picture'),
'<img src="%photo_url%"/><br/>User: %owner%', 'images/service/panoramio.png');

-- Locr
INSERT INTO service
(name, title, homepage, description, stype_id, bubbleHTML, thumbnail) VALUES
('locr.com', 'Locr', 'http://www.locr.com/', 'Online-Foto-Service',
(SELECT stype_id FROM serviceType WHERE name='picture'),
'<img src="%img%"/><br/>User: %user%', 'images/service/locr.png');

COMMIT;
