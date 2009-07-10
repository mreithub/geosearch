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
		// only do this if we are on the testing database
		if (db.isTesting()) {
			//Insert
			String testName="testWriteRead2";
			String testImg="/img/testWriteRead2.png";
			
			Statement s = db.createStatement();
			
			s.executeUpdate("INSERT INTO serviceType (name, thumbnail) VALUES ('testWriteRead', '/img/testWriteRead1.png')");
			s.executeUpdate("INSERT INTO serviceType (name, thumbnail) VALUES ('"+testName+"', '"+testImg+"')");

			ResultSet r = s.executeQuery("SELECT * FROM serviceType WHERE name='"+testName+"'");
			
			r.next();
			String testNameBack=r.getString("name");
			String testImgBack=r.getString("thumbnail");

			
			//Update
			String testTestImg="/img/testWriteRead3.png";
			

			s.executeUpdate("UPDATE serviceType SET thumbnail='"+testTestImg+"' WHERE name='"+testName+"'");
			r = s.executeQuery("SELECT * FROM serviceType WHERE name='"+testName+"'");
			
			r.next();
			String testTestNameBack=r.getString("name");
			String testTestImgBack=r.getString("thumbnail");
			
			
			// make sure that the DB transaction is rolled back before we run an assert 
			db.disconnect();
			
			
			assertEquals(testNameBack, testName);
			assertEquals(testImgBack, testImg);
			
			assertEquals(testNameBack, testTestNameBack);
			assertEquals(testTestImg, testTestImgBack);
		}else{
			fail("Not using the testing database");
		}
	}
	
	@Test
	public void testWriteRead() throws SQLException {
		// only do this if we are on the testing database
		if (db.isTesting()) {
			String testName="testWriteRead2";
			String testImg="/img/testWriteRead2.png";
			
			Statement s = db.createStatement();
			s.executeUpdate("INSERT INTO serviceType (name, thumbnail) VALUES ('testWriteRead', '/img/testWriteRead1.png')");
			s.executeUpdate("INSERT INTO serviceType (name, thumbnail) VALUES ('"+testName+"', '"+testImg+"')");

			ResultSet r = s.executeQuery("SELECT * FROM serviceType WHERE name='testWriteRead2'");
			
			/*
			while(r.next()){
				System.out.println("rowCount: "+r.getRow());
				System.out.println("n:"+r.getString("name"));
				System.out.println("i:"+r.getString("thumbnail"));
			}
			*/
			r.next();
			String testNameBack=r.getString("name");
			String testImgBack=r.getString("thumbnail");

			// make sure that the DB transaction is rolled back before we run an assert 
			db.disconnect();
			
			
			assertEquals(testNameBack, testName);
			assertEquals(testImgBack, testImg);
		}else{
			fail("Not using the testing database!");
		}
	}
	
	@Test
	public void testWrite() throws SQLException {
		// only do this if we are on the testing database
		if (db.isTesting()) {
			Statement s = db.createStatement();
			s.executeUpdate("INSERT INTO serviceType (name, thumbnail) VALUES ('testtype', '/img/test1.png')");
			s.executeUpdate("INSERT INTO serviceType (name, thumbnail) VALUES ('testtype2', '/img/test2.png')");

			ResultSet r = s.executeQuery("SELECT name FROM serviceType");
			

			// check if rowcount = 5
			r.last();
			int rowcount = r.getRow();

			// make sure that the DB transaction is rolled back before we run an assert 
			db.disconnect();
		
			assertEquals(3, rowcount);
 		}else{
			fail("Not using the testing database!");
		}
	}

	@Before
	public void setUp() throws FileNotFoundException, IOException {
		db = new DBConnection();
		
	}
	
	@After
	public void tearDown() {
		
	}
}
