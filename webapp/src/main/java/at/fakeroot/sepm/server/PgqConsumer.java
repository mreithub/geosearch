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
	PgqBatch pgqBatch = null;

	public class PgqBatch {
		// ResultSet: ev_id int8, ev_time timestamptz, ev_txid int8, ev_retry int4, ev_type text, ev_data text, ev_extra1, ev_extra2, ev_extra3, ev_extra4
		private Long batchId;
		private ResultSet rs;
		public int resultCount;
		
		private PgqBatch(Long batchId) throws SQLException {
			this.batchId = batchId;
			PreparedStatement getEventStmt = dbConn.prepareStatement("SELECT * FROM pgq.get_batch_events(?)");
			getEventStmt.setLong(1, batchId);
			rs = getEventStmt.executeQuery();
			rs.last();
			resultCount = rs.getRow();
			rs.beforeFirst();
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
	    public void finish() throws SQLException {
	    	if (batchId != null) {
	    		PreparedStatement finishBatchStmt = dbConn.prepareStatement("SELECT pgq.finish_batch(?)");
		    	finishBatchStmt.setLong(1, batchId);
		    	finishBatchStmt.executeQuery();
		    	batchId = null;
	    	}
	    }
	}
	
	public PgqConsumer(DBConnection db, String queueName, String consumerName) throws SQLException {
		this.queueName = queueName;
		this.consumerName = consumerName;
		dbConn = db;
		PreparedStatement pstmt = db.prepareStatement("SELECT pgq.register_consumer(?,?)");
		pstmt.setString(1, queueName);
		pstmt.setString(2, consumerName);
		pstmt.executeQuery();
	}
	
	public PgqConsumer(String queueName, String consumerName) throws FileNotFoundException, IOException, SQLException {
		this(new DBConnection(), queueName, consumerName);
	}
	
	protected void finalize() throws SQLException {
		PreparedStatement pstmt = dbConn.prepareStatement("SELECT pgq.unregister_consumer(?, ?)");
		pstmt.setString(1, queueName);
		pstmt.setString(2, consumerName);
		pstmt.executeQuery();
		
		dbConn.disconnect();
	}
	
	public PgqBatch nextBatch() throws SQLException {
		if (pgqBatch != null) { // release the last batch
			pgqBatch.finish();
			pgqBatch = null;
		}
		PreparedStatement nextBatchStmt = dbConn.prepareStatement("SELECT pgq.next_batch(?, ?)");
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
