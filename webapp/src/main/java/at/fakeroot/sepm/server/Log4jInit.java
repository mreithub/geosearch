package at.fakeroot.sepm.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.log4j.PropertyConfigurator;

public class Log4jInit extends HttpServlet {
	/**
	 * default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	public void init(ServletConfig config) throws ServletException {
		Properties prop = new Properties();
		String logfile = getInitParameter("log4j-init-file");
		
		InputStream propStream = getClass().getResourceAsStream(logfile);
		if (propStream == null) {
			throw new ServletException ("Error: Couldn't open property file '"+logfile+"'");
		}
		try {
			prop.load(propStream);
		}
		catch (IOException e) {
			throw new ServletException("Couldn't load log4j property file", e);
		}

		PropertyConfigurator.configure(prop);
	}
}
