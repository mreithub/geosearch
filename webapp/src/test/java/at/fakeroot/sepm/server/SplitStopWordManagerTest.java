package at.fakeroot.sepm.server;


import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;


import org.junit.Before;
import org.junit.Test;

public class SplitStopWordManagerTest
{
	SplitStopWordManager swManager;
	
	@Before
	public void setUp() throws Exception
	{
		swManager = new SplitStopWordManager();
	}
	
	
	@Test
	/**
	 * Tests if the split chars can be read from the database.
	 */
	public void testSplitChars() throws IOException, SQLException
	{
		if (DBConnection.staticIsTesting())
		{
			String splitChars = swManager.getSplitChars();
			// test for the 5 SplitChars in the test database
			assertEquals(5, splitChars.length());
			assertTrue(splitChars.contains(" "));
			assertTrue(splitChars.contains("."));
			assertTrue(splitChars.contains(","));
			assertTrue(splitChars.contains("!"));
			assertTrue(splitChars.contains("?"));
		}
	}
	
	@Test
	/**
	 * Tests if the stop words can be read from the database.
	 */
	public void testStopWords() throws IOException, SQLException
	{
		if (DBConnection.staticIsTesting())
		{
			String[] stopWords = swManager.getStopWords();
			// test for the 5 stopWords in the test database
			assertEquals(stopWords.length, 5);
			assertArrayEquals(new String[] {"a", "e", "i", "o", "u"}, stopWords);
		}
	}
}
