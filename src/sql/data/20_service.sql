BEGIN;

-- de.wikipedia.org
INSERT INTO service
(name, title, homepage, description, stype_id, bubbleHTML, thumbnail) VALUES
('de.wikipedia.org', 'Wikipedia', 'http://de.wikipedia.org/', 'Deutsche Wikipedia (freies Online-Lexicon)',
(SELECT stype_id FROM serviceType WHERE name='lexicon'),
'<div>%description%</div>', 'images/service/wikipedia.png');

-- last.fm
INSERT INTO service
(name, title, homepage, description, stype_id, bubbleHTML, thumbnail) VALUES
('last.fm', 'last.fm', 'http://www.last.fm/', 'Last.fm Events',
(SELECT stype_id FROM serviceType WHERE name='event'),
'<div>%description%</div>', 'images/service/lastfm.png');

-- Panoramio
INSERT INTO service
(name, title, homepage, description, stype_id, bubbleHTML, thumbnail) VALUES
('panoramio.com', 'Panoramio', 'http://www.panoramio.com/', 'Online-Foto-Service',
(SELECT stype_id FROM serviceType WHERE name='picture'),
'<img src="%img%" />User: %user%', 'images/service/panoramio.png');


COMMIT;
