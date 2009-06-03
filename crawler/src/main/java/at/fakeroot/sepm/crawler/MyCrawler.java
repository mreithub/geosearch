package at.fakeroot.sepm.crawler;


public class MyCrawler {
  
  //private static ICrawler crawlMe = new ICrawler();
  //private static String url ;

  public static void main(String[] args) {
	  
	  new LocrCrawler();
	  /*
	  for(int i=0;i<26;i++){
	  BoundingBox curBound=crawlMe.crawlBox();
	  url="http://de.locr.com/api/get_photos_json.php" +
		"?latitudemin=" + curBound.getX1()+
		"&longitudemin=" + curBound.getY1() +
		"&latitudemax=" + curBound.getX2() +
		"&longitudemax=" + curBound.getY2() +
		"&category=popularity" +
		"&locr=true";

	  System.out.println(curBound);
	  //System.out.println(url);
	  //System.out.println(crawlMe.crawl(url));
	  }
	  */
	  
	  /*
	  while((curBound = crawlMe.crawlBox()) != null){
		  url="http://de.locr.com/api/get_photos_json.php" +
		  		"?latitudemin=" + curBound.getX1()+
		  		"&longitudemin=" + curBound.getY1() +
		  		"&latitudemax=" + curBound.getX2() +
		  		"&longitudemax=" + curBound.getY2() +
		  		"&category=popularity" +
		  		"&locr=true";
		  
		  System.out.println(url);
		  //System.out.println(crawlMe.crawl(url));
	  }
	  */
  }
}