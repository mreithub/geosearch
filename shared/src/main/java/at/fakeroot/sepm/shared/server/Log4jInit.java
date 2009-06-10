/**
 * 
 */
package at.fakeroot.sepm.shared.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServlet;

import org.apache.log4j.PropertyConfigurator;

/**
 * @author Manuel Reithuber
 *
 */
public class Log4jInit extends HttpServlet {
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	public void init() {
		Properties prop = new Properties();
		
		InputStream propStream = getClass().getResourceAsStream(getInitParameter("log4j-init-file"));
		try {
			if (propStream != null) {
				prop.load(propStream);
				PropertyConfigurator.configure(prop);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
