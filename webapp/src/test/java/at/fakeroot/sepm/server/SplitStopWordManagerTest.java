package at.fakeroot.sepm.server;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
			assertNotNull(splitChars);
			assertTrue(splitChars.length() > 0);
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
			assertNotNull(stopWords);
			assertTrue(stopWords.length > 0);
		}
	}
}
