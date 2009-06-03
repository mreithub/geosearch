package at.fakeroot.sepm.server.crawler;

public class LocrCrawler extends ICrawler {

	public LocrCrawler() {
		ICrawler(new BoundingBox(-1.0,-1.0,1.0,1.0),0.5);
		beginCrawlin();
	}

	private void beginCrawlin() {
		//while(true){
		for (int i = 0; i < 20; i++) {
			crawlBox();
			String url = "http://de.locr.com/api/get_photos_json.php"
					+ "?latitudemin=" + curBox.getX1() + 
					"&longitudemin=" + curBox.getY1() + 
					"&latitudemax=" + curBox.getX2()
					+ "&longitudemax=" + curBox.getY2()
					+ "&category=popularity" + "&locr=true";

			//System.out.println(curBox);
			System.out.println(url);
			//System.out.println(crawl(url));
		}
	}
}
