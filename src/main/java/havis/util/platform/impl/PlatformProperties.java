package havis.util.platform.impl;

import havis.util.platform.Platform;
import havis.util.platform.PlatformException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides the properties for a {@link Platform} from
 * <code>platform.properties</code>.
 */
class PlatformProperties extends Properties {

	private static final Logger log = Logger.getLogger(PlatformProperties.class.getName());

	private static final long serialVersionUID = 5103529318439853825L;

	private static final String HAS_UTC_CLOCK = "havis.util.platform.hasUTCClock";
	private boolean hasUTCClock = false;

	/**
	 * Reads a given properties file from the classpath.
	 * 
	 * @param propertiesFilePath
	 *            The path to the properties file. A relative path starts at the
	 *            classpath.
	 * @param rfcServiceFactory
	 * @throws Exception
	 */
	public PlatformProperties(String propertiesFilePath) throws PlatformException {
		InputStream is = null;
		try {
			log.log(Level.FINE, "Loading properties file {0}", propertiesFilePath);
			if (Paths.get(propertiesFilePath).isAbsolute()) {
				is = new FileInputStream(propertiesFilePath);
			} else {
				is = getClass().getClassLoader().getResourceAsStream(propertiesFilePath);
			}
			if (is == null) {
				throw new PlatformException("Missing properties file " + propertiesFilePath);
			}
			load(is);
			hasUTCClock = getBooleanProperty(HAS_UTC_CLOCK);
		} catch (IOException e) {
			throw new PlatformException("Cannot load properties from " + propertiesFilePath, e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					throw new PlatformException("Cannot close input stream from properties file " + propertiesFilePath, e);
				}
			}
		}
	}

	/**
	 * Returns the property <code>havis.util.platform.hasUTCClock</code>.
	 * 
	 * @return
	 * @throws PlatformException
	 */
	public boolean hasUTCClock() throws PlatformException {
		return hasUTCClock;
	}

	/**
	 * Converts a string property to a boolean. Valid values: <code>false</code>
	 * , <code>true</code>, <code>0</code>, <code>1</code>
	 * 
	 * @param propertyName
	 * @return
	 * @throws PlatformException
	 *             The property does not exist or is invalid.
	 */
	private boolean getBooleanProperty(String propertyName) throws PlatformException {
		String value = getProperty(propertyName);
		if (value == null) {
			throw new PlatformException("Missing property " + propertyName);
		}
		if (value.toLowerCase().equals("true") || value.equals("1")) {
			return true;
		} else if (!value.toLowerCase().equals("false") && !value.equals("0")) {
			throw new PlatformException("Invalid property " + propertyName + ": " + value);
		}
		return false;
	}
}