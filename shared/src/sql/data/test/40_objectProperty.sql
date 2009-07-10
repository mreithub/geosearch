--
-- Test Data: Object Properties 
--

BEGIN;

-- Wien: Karlskirche
INSERT INTO objectProperty (obj_id, name, value) VALUES (
(SELECT obj_id FROM geoObject WHERE uid='test_karlskirche'),
'description', 'Beschreibung Karlskirche');

-- Wien: Stephansdom
INSERT INTO objectProperty (obj_id, name, value) VALUES (
(SELECT obj_id FROM geoObject WHERE uid='test_stephansdom'),
'description', '<p>Der Stephansdom (eigentlich: Domkirche St. Stephan zu Wien) am Wiener Stephansplatz (Bezirk Innere Stadt) ist seit 1365 Domkirche (Sitz eines Domkapitels), seit 1469/1479 Kathedrale (Bischofssitz) und seit 1723 Metropolitankirche des Erzbischofs von Wien. Der von Wienern mitunter auch kurz Steffl genannte Dom gilt als Wahrzeichen Wiens und wird häufig auch als österreichisches Nationalheiligtum bezeichnet. Namensgeber ist der Heilige Stephanus, der als erster christlicher Märtyrer gilt.</p>
<p>Das Bauwerk ist 107 Meter lang und 34 Meter breit. Der Dom ist eines der wichtigsten gotischen Bauwerke in Österreich. Teile des spätromanischen Baues von 1230/40–1263 sind noch erhalten. Er besitzt vier Türme: Der höchste davon ist der Südturm mit 136,4 Meter, der Nordturm wurde nicht fertig gestellt und ist nur 68 Meter hoch. Links und rechts vom Haupteingang befinden sich die beiden Heidentürme, die etwa 65 Meter hoch sind.</p>');

-- Wien: Schönbrunn
INSERT INTO objectProperty (obj_id, name, value) VALUES (
(SELECT obj_id FROM geoObject WHERE uid='test_schoenbrunn'),
'description', '<p>Das Schloss Schönbrunn ist eines der bedeutendsten Kulturgüter Österreichs und seit den 1960er Jahren eine der meistbesuchten Sehenswürdigkeiten Wiens. Es liegt westlich der Wiener Innenstadt im Bezirk Hietzing.</p>
<p>Sein Name geht auf einen Kaiser Matthias zugeschriebenen Ausspruch zurück, der hier auf der Jagd einen artesischen Brunnen „entdeckt“ und ausgerufen haben soll: „Welch'' schöner Brunn“. Aus diesem Brunnen wurde bis zum Bau der Hochquellwasserleitung auch Trinkwasser für den Hof gezapft.</p>
<p>Eine Hauptattraktion im Schlosspark ist der älteste noch bestehende Zoo der Welt, der Tiergarten Schönbrunn.</p>
<p>1996 wurden Schloss und Park von Schönbrunn (englisch Palace and Gardens of Schönbrunn) von der UNESCO zum Weltkulturerbe erklärt.</p>
<p>Das Schloss und die Gärten von Schönbrunn bilden eine der 89 Wiener Katastralgemeinden.</p>');

-- Tirol: Goldenes Dachl
INSERT INTO objectProperty (obj_id, name, value) VALUES (
(SELECT obj_id FROM geoObject WHERE uid='test_goldenes_dachl'),
'description', '<p>Das Goldene Dachl ist ein Gebäude mit spätgotischem Prunkerker in der Innsbrucker Altstadt und gilt als Wahrzeichen der Stadt. Das Dach des Erkers wurde mit 2.657 feuervergoldeten Kupferschindeln gedeckt.</p>
<p>Erbaut wurde das Gebäude 1420 als Residenz („Neuhof“) der Tiroler Landesfürsten. Aus Anlass der Zeitenwende (1500) fügte Niklas Türing der Ältere im Auftrag Kaiser Maximilians I. 1497/98-1500 den Prunkerker hinzu.</p>
<p>Reliefs am Erker zeigen Kaiser Maximilian I. mit seinen beiden Gemahlinnen, Kanzler, Hofnarr, Moriskentänzer und Wappen (Originalreliefs im Tiroler Landesmuseum).</p>
<p>1996 wurde im Gebäude das Museum Maximilianeum eingerichtet und 2007 nach umfangreichen Erweiterungs- und Sanierungsumbauten als Museum Goldenes Dachl neu eröffnet.</p>
<p>Seit 2003 befindet sich im selben Gebäude auch das Ständige Sekretariat der Alpenkonvention.</p>');

-- Salzburg: Hohensalzburg
INSERT INTO objectProperty (obj_id, name, value) VALUES (
(SELECT obj_id FROM geoObject WHERE uid='test_hohensalzburg'),
'description', '<p>Die Festung Hohensalzburg ist das Wahrzeichen der Stadt Salzburg. Sie liegt auf einem Stadtberg oberhalb der Stadt Salzburg, dem Festungsberg, der sich nach Nordwesten in den Mönchsberg fortsetzt. Der Ausläufer im Osten des Festungsberges heißt Nonnberg. Auf dem Nonnberg befindet sich direkt unter den östlichen Außenanlagen der Festung – den Nonnbergbasteien – das Stift Nonnberg. Die Festung Hohensalzburg ist mit über 7.000 m² bebauter Fläche (einschließlich der Basteien über 14.000 m²) eine der größten Burgen Europas. Sie ist Europas größte Burganlage aus dem 11. Jahrhundert, die größte vollständig erhaltene Burg Mitteleuropas und mit jährlich über 950.000 Besuchern die am häufigsten besuchte Sehenswürdigkeit Österreichs außerhalb Wiens, wobei sie auch in der Bundeshauptstadt nur von Schloss Schönbrunn, Tiergarten Schönbrunn, Stephansdom und Wiener Prater übertroffen wird (Besucherstatistiken 2006 und 2007).</p>');

-- Linz: Pöstlingberg (mit leerer description)
INSERT INTO objectProperty (obj_id, name, value) VALUES (
(SELECT obj_id FROM geoObject WHERE uid='test_poestlingberg'),
'description', '');

COMMIT;
