package at.fakeroot.sepm.shared.client.serialize;
import junit.framework.TestCase;
/**
 * SearchResult JUnitTest
 * @author RK
 */

public class SearchResultTest extends TestCase
{
	public String getModulName()
	{
		return ("at.fakeroot.sepm.client.serialize.SearchResult");
	}
	
	public void testSimple(){
		ClientGeoObject cgo = new ClientGeoObject(2,"bla", null, new String[]{"eins", "zwei", "drei"}, 14.987, 15.564);
		int hits = 7;
		SearchResult testObject = new SearchResult();
		testObject.addResultToList(cgo);
		testObject.setResultCount(hits);
		
		assertNotNull(testObject);
		assertEquals(hits, testObject.getResultCount());
		assertEquals(testObject.getResults().get(0).getId(), cgo.getId());
		assertEquals(testObject.getResults().get(0).getXPos(), cgo.getXPos());
		assertEquals(testObject.getResults().get(0).getTitel(), cgo.getTitel());
		assertEquals(testObject.getResults().get(0).getTags()[0], cgo.getTags()[0]);
	}
}
