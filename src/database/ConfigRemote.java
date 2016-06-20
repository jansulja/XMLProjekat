package database;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.marklogic.client.DatabaseClientFactory.Authentication;

import services.AktService;

public class ConfigRemote {

	private static Properties props = loadProperties();
	
	public static String host = props.getProperty("conn.host");
	
	public static int port = Integer.parseInt(props.getProperty("conn.port"));
	
	public static String user = props.getProperty("conn.user");
	
	public static String password = props.getProperty("conn.pass");
	
	public static String database = props.getProperty("conn.database");
	
	public static Authentication authType = Authentication.valueOf(
				props.getProperty("conn.authentication_type").toUpperCase()
				);

	// get the configuration for the example
	private static Properties loadProperties() {		
	    try {
			String propsName = "Config.properties";
			InputStream propsStream =
				ConfigRemote.class.getClassLoader().getResourceAsStream(propsName);
			if (propsStream == null)
				throw new IOException("Could not read config properties");

			Properties props = new Properties();
			props.load(propsStream);

			return props;

	    } catch (final IOException exc) {
	        throw new Error(exc);
	    }  
	}
}
