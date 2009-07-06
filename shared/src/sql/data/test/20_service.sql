BEGIN;

-- example.com
INSERT INTO service
(name, title, homepage, description, stype_id, bubbleHTML, thumbnail) VALUES
('example.com', 'TestService', 'http://www.example.com/', 'Test-Service',
(SELECT stype_id FROM serviceType WHERE name='testType'),
'%description%', 'images/service/wikipedia.png');

COMMIT;
