package at.fakeroot.sepm.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * thin wrapper around PG/Q's consumer sql functions
 * as descriped in http://skytools.projects.postgresql.org/doc/pgq-sql.html
 * @author Manuel Reithuber
 */
public class PgqConsumer {
	DBConnection dbConn;
	String queueName, consumerName;
	PreparedStatement nextBatchStmt, finishBatchStmt, getEventStmt;
	PgqBatch pgqBatch = null;

	public class PgqBatch {
		// resultset: ev_id int8, ev_time timestamptz, ev_txid int8, ev_retry int4, ev_type text, ev_data text, ev_extra1, ev_extra2, ev_extra3, ev_extra4
		Long batchId;
		ResultSet rs;
		private PgqBatch(Long batchId) throws SQLException {
			this.batchId = batchId;
			getEventStmt.setLong(1, batchId);
			rs = getEventStmt.executeQuery();
		}
		
		public boolean nextEvent() throws SQLException {
			return rs.next();
		}
		
		public long getEventId() throws SQLException {
			return rs.getLong("ev_id");
		}
		
		public Timestamp getEventTime() throws SQLException {
			return rs.getTimestamp("ev_time");
		}
		
		public String getEventType() throws SQLException {
			return rs.getString("ev_type");
		}
		
		public String getEventData() throws SQLException {
			return rs.getString("ev_data");
		}
		
		/**
		 * release the batch. called by PgqConsumer.nextBatch()
		 * @throws SQLException
		 */
	    private void finish() throws SQLException {
	    	finishBatchStmt.setLong(1, batchId);
	    	finishBatchStmt.executeUpdate();
	    	batchId = null;
	    }
	}
	
	public PgqConsumer(String queueName, String consumerName) throws FileNotFoundException, IOException, SQLException {
		this.queueName = queueName;
		dbConn = new DBConnection();
		nextBatchStmt = dbConn.prepareStatement("SELECT * FROM pgq.next_batch('queueName', 'consumerName')");
		finishBatchStmt = dbConn.prepareStatement("SELECT * FROM pgq.finish_batch(?)");
		getEventStmt = dbConn.prepareStatement("SELECT * FROM pgq.get_batch_events(?)");
	}
	
	protected void finalize() throws SQLException {
		dbConn.disconnect();
	}
	
	public PgqBatch nextBatch() throws SQLException {
		if (pgqBatch != null) { // release the last batch
			pgqBatch.finish();
			pgqBatch = null;
		}
		nextBatchStmt.setString(1, queueName);
		nextBatchStmt.setString(2, consumerName);
		ResultSet rs = nextBatchStmt.executeQuery();
		rs.next();
		/// TODO check if column 1 is null
		if (rs.getString(1) != null) {
			pgqBatch = new PgqBatch(rs.getLong(1));
		}

		return pgqBatch;
	}
 
}
