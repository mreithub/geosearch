/**
 * 
 */
package at.fakeroot.sepm.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * @author Manuel Reithuber
 *
 */
public class DBConnectionTest {
	IDBConnection db = null;

	@Test
	public void testWriteUpdateRead() throws SQLException {
		//Insert
		String testName="testWriteRead2";
		String testImg="/img/testWriteRead2.png";
		
		Statement s = db.createStatement();
		
		s.executeUpdate("INSERT INTO serviceType (name, thumbnail) VALUES ('testWriteRead', '/img/testWriteRead1.png')");
		s.executeUpdate("INSERT INTO serviceType (name, thumbnail) VALUES ('"+testName+"', '"+testImg+"')");

		ResultSet r = s.executeQuery("SELECT * FROM serviceType WHERE name='"+testName+"'");
		
		r.next();
		assertEquals(testName, r.getString("name"));
		assertEquals(testImg, r.getString("thumbnail"));

		
		//Update
		String testTestImg="/img/testWriteRead3.png";
		

		s.executeUpdate("UPDATE serviceType SET thumbnail='"+testTestImg+"' WHERE name='"+testName+"'");
		r = s.executeQuery("SELECT * FROM serviceType WHERE name='"+testName+"'");
		
		r.next();
		assertEquals(testName, r.getString("name"));
		assertEquals(testTestImg, r.getString("thumbnail"));
	}
	
	@Test
	public void testWriteRead() throws SQLException {
		String testName="testWriteRead2";
		String testImg="/img/testWriteRead2.png";
		
		Statement s = db.createStatement();
		s.executeUpdate("INSERT INTO serviceType (name, thumbnail) VALUES ('testWriteRead', '/img/testWriteRead1.png')");
		s.executeUpdate("INSERT INTO serviceType (name, thumbnail) VALUES ('"+testName+"', '"+testImg+"')");

		ResultSet r = s.executeQuery("SELECT * FROM serviceType WHERE name='testWriteRead2'");

		r.next();
		assertEquals(testName, r.getString("name"));
		assertEquals(testImg, r.getString("thumbnail"));
	}
	
	@Test
	public void testWrite() throws SQLException {
		Statement s = db.createStatement();
		s.executeUpdate("INSERT INTO serviceType (name, thumbnail) VALUES ('testtype', '/img/test1.png')");
		s.executeUpdate("INSERT INTO serviceType (name, thumbnail) VALUES ('testtype2', '/img/test2.png')");

		ResultSet r = s.executeQuery("SELECT name FROM serviceType");

		// check for the correct number of rows
		// (one permanent test serviceType and the two we've just created) 
		r.last();
	
		assertEquals(3, r.getRow());
	}

	@Before
	public void setUp() throws FileNotFoundException, IOException {
		db = new DBConnection();
		// only test if we are on the testing database
		// (which should always be true because of a specific jdbc.properties file in src/test/resources)
		assertTrue("Not using the  testing database!", db.isTesting());
	}
	
	@After
	public void tearDown() throws SQLException {
		db.disconnect();
	}
}
