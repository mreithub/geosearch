package at.fakeroot.sepm.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DBTestServlet extends HttpServlet {
	/// default serial version id
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	    resp.setContentType("text/html; charset=utf-8");
	    
	    PrintWriter out = resp.getWriter();
		out.print("Hallo");
		String sql = "";
		
		printForm(out, sql);

		out.close();
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		GeoObjectManager g;
		IDBConnection db;
		Statement stmt;
		try {
			g = (GeoObjectManager) GeoObjectManager.getInstance();
			db = g.getDbRead();
			stmt = db.createStatement();
		} catch (SQLException e) {
			throw new ServletException("SQL exception: "+e.getMessage(), e);
		}

		response.setContentType("text/html; charset=UTF-8");

	    PrintWriter out = response.getWriter();
	    String sql = request.getParameter("sql");
	    
	    out.write("<html><head><title>SQL test</title><style>pre{margin:0} table,tr,td{border: 1px solid black; border-collapse: collapse;}</style></head><body>");
	    
	    if (sql.matches("/(?i:COMMIT)")) {
	    	out.write("statement '"+sql+"' isn't allowed!!!");
	    }
	    else if (sql.length() > 0) {
	    	try {
				stmt.executeUpdate("BEGIN");
				ResultSet rs = stmt.executeQuery(sql);
				ResultSetMetaData meta = rs.getMetaData();
				out.write("<table>\n");
				if (meta.getColumnCount() > 0) {
					out.write("<tr>\n");
					for (int i = 1; i <= meta.getColumnCount(); i++) {
						out.write("<th>"+meta.getColumnLabel(i)+"</th>\n");
					}
					out.write("</tr>\n");
				}
				while (rs.next()) {
					out.write("<tr>\n");
					for (int i = 1; i <= meta.getColumnCount(); i++) {
						out.write("<td><pre>"+rs.getString(i)+"</pre></td>\n");
					}
					out.write("</tr>\n");
				}
				rs.close();
				
			} catch (SQLException e) {
				out.write("SQL error: "+e.getMessage());
			}
			try {
				stmt.executeUpdate("ROLLBACK");
			}
			catch (SQLException e) {
				throw new ServletException("SQLException: "+e.getMessage(), e);
			}
	    }
	    
	    printForm(out, sql);
	    
	    out.write("</body>\n</html>\n");
		out.close();
	}
	
	private void printForm(PrintWriter out, String sql) {
		out.write("<form method=\"post\"><input type=\"text\" name=\"sql\" value=\""+sql+"\" /><input type=\"submit\" /></form>");
	}
}
