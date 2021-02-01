package havis.util.platform.impl;

import havis.util.platform.Platform;
import havis.util.platform.PlatformException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The LinuxPlatform is a Linux specific implementation for {@link Platform}. It
 * provides informations which are available via the native library
 * <code>liblinuxplatform.so</code> or the configuration file
 * <code>platform.properties</code>.
 */
public class LinuxPlatform implements Platform {

	private final static Logger log = Logger.getLogger(LinuxPlatform.class.getName());

	private final static String LIBRARY = "linuxplatform";

	private static boolean isLoaded;
	private PlatformProperties properties;
	private final String config;

	static {
		log.log(Level.FINE, "Loading native library {0}", LIBRARY);
		// load native library linuxplatform.dll
		// (Windows) or liblinuxplatform.so (Unix)
		System.loadLibrary(LIBRARY);
	}

	/**
	 * Loads the platform with the default configuration
	 * <code>havis-platform/platform.properties</code> in the class path.
	 */
	public LinuxPlatform() {
		this(DefaultPlatform.CONFIG);
	}

	/**
	 * @param config
	 *            The path to the configuration file. A relative path starts at
	 *            the classpath.
	 */
	public LinuxPlatform(String config) {
		this.config = config;
	}

	/**
	 * Opens the platform by loading <code>liblinuxplatform.so</code> and
	 * reading the properties file <code>platform.properties</code> from the
	 * class path.
	 */
	@Override
	public synchronized void open() throws PlatformException {
		properties = new PlatformProperties(config);
	}

	@Override
	public void close() throws PlatformException {
	}

	/**
	 * Returns the value of configuration property
	 * <code>havis.util.platform.hasUTCClock</code>. The Java API can be used to
	 * get the current time.
	 */
	@Override
	public synchronized boolean hasUTCClock() throws PlatformException {
		return properties.hasUTCClock();
	}

	/**
	 * Returns the Linux uptime in milliseconds.
	 */
	@Override
	public synchronized native long getUptime() throws PlatformException;
}