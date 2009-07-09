package at.fakeroot.sepm.server;


import java.io.IOException;
import java.sql.SQLException;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SplitStopWordManagerTest {
	SplitStopWordManager swManager;
	
	// TODO restliche Funktionalität testen!
	
	@Test
	public void testSplitChars_shouldSucceed() throws IOException, SQLException {
		String splitChars = swManager.getSplitChars();
		if (DBConnection.staticIsTesting()) {
			assertEquals(5, splitChars.length());
			assertTrue(splitChars.contains(" "));
			assertTrue(splitChars.contains("."));
			assertTrue(splitChars.contains(","));
			assertTrue(splitChars.contains("!"));
			assertTrue(splitChars.contains("?"));
		}
	}
	
	@Before
	public void setUp() throws Exception {
		swManager = new SplitStopWordManager();
	}

	@After
	public void tearDown() throws Exception {
	}

}
