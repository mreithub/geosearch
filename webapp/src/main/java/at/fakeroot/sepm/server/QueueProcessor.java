package at.fakeroot.sepm.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import at.fakeroot.sepm.server.PgqConsumer.PgqBatch;

/**
 * process the counting queues in the database if explicitly enabled 
 * 
 * Note: Only one one QueueProcessor should run for the whole application
 * @author Manuel Reithuber
 */
public class QueueProcessor extends HttpServlet {
	/// default serial version id
	private static final long serialVersionUID = 1L;
	private PgqConsumer pgq;
	private DBConnection dbConn;

	public QueueProcessor() throws FileNotFoundException, IOException, SQLException {
		String consumerName = "QueueProcessor";
		dbConn = new DBConnection();
		pgq = new PgqConsumer(dbConn, "countStatistics", consumerName);
	}

    private void batchProcess() throws InterruptedException, SQLException {
		/// TODO: check if the QueueProcessor is explicitely enabled
		while (true) {
			PgqBatch batch = pgq.nextBatch();
			if (batch == null) {
				Thread.sleep(250);
			}
			else {
				while (batch.nextEvent()) {
					String type = batch.getEventType();
					if (type.equals("insertService")) {
						_updateService(Integer.parseInt(batch.getEventData()), 1);
					}
					else if (type.equals("deleteService")) {
						_updateService(Integer.parseInt(batch.getEventData()), -1);
					}
					else if (type.equals("insertTag")) {
						_updateTag(batch.getEventData(), 1);
					}
					else if (type.equals("deleteTag")) {
						_updateTag(batch.getEventData(), -1);
					}
				}
				batch.finish();
			}
		}
    }

    private void _updateService(int svcId, int delta) throws SQLException {
    	// update the objectCount for a service
		PreparedStatement pstmt = dbConn.prepareStatement("UPDATE service SET objectCount = objectCount + ? WHERE svc_id = ?");
    	pstmt.setInt(1, delta);
    	pstmt.setInt(2, svcId);
    	pstmt.executeUpdate();
    	
    	
    	// update the count for all the serviceTags
    	pstmt = dbConn.prepareStatement("UPDATE tag SET objectCount = objectCount +  ? WHERE tag IN (SELECT tag FROM serviceTag WHERE svc_id = ?)");
    	pstmt.setInt(1, delta);
    	pstmt.setInt(2, svcId);
    	pstmt.executeUpdate();
    }
    
    private void _updateTag(String tag, int delta) throws SQLException {
    	// update the objectCount for a tag
    	PreparedStatement pstmt = dbConn.prepareStatement("UPDATE tag SET objectCount = objectCount + ? WHERE tag = ?");
		pstmt.setInt(1, delta);
		pstmt.setString(2, tag);
		pstmt.executeUpdate();
	}
    
	public void init() throws ServletException {
		// TODO load queueName + consumerName from .properties file
		try {
			batchProcess();
		} catch (InterruptedException e) {
			throw new ServletException("Interrupted", e);
		} catch (SQLException e) {
			throw new ServletException("SQL error: "+e.getMessage(), e);
		}
	}
}
