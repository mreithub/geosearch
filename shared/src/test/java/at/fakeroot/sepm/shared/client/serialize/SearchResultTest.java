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
	
	public String getModulName()
	{
		return ("at.fakeroot.sepm.client.serialize.SearchResult");
	}
	
	@Test
	public void testSimple(){
		ClientGeoObject cgo = new ClientGeoObject(2,"bla", null, new String[]{"eins", "zwei", "drei"}, 14.987, 15.564);
		int hits = 7;
		int countLimit = 50;
		SearchResult testObject = new SearchResult(countLimit);
		testObject.addResultToList(cgo);
		testObject.setResultCount(hits);
		
		assertNotNull(testObject);
		assertEquals(hits, testObject.getResultCount());
		assertEquals(testObject.getResults().get(0).getId(), cgo.getId());
		assertEquals(testObject.getResults().get(0).getXPos(), cgo.getXPos(), delta);
		assertEquals(testObject.getResults().get(0).getTitle(), cgo.getTitle());
		assertEquals(testObject.getResults().get(0).getTags()[0], cgo.getTags()[0]);
	}
}
