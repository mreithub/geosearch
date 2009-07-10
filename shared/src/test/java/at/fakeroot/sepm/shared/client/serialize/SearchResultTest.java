package at.fakeroot.sepm.shared.client.serialize;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * SearchResult JUnitTest
 * @author RK
 */

public class SearchResultTest
{
	private double delta = 0.0001;
	
	@Test
	public void testGettersAndSetters(){
		ClientGeoObject cgo = new ClientGeoObject(2,"bla", null, new String[]{"eins", "zwei", "drei"}, 14.987, 15.564);
		int hits = 7;
		int countLimit = 50;
		SearchResult testObject = new SearchResult(countLimit);
		testObject.addResultToList(cgo);
		testObject.setResultCount(hits);
		
		assertEquals(hits, testObject.getResultCount());
		assertEquals(testObject.getResults().get(0).getId(), cgo.getId());
		assertEquals(testObject.getResults().get(0).getXPos(), cgo.getXPos(), delta);
		assertEquals(testObject.getResults().get(0).getTitle(), cgo.getTitle());
		assertArrayEquals(testObject.getResults().get(0).getTags(), cgo.getTags());
	}
}
