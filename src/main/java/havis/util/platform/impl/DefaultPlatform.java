package havis.util.platform.impl;

import havis.util.platform.Platform;
import havis.util.platform.PlatformException;

/**
 * The DefaultPlatform is a default implementation for {@link Platform}. It
 * provides only informations which are available via the JVM or the
 * configuration file <code>platform.properties</code>.
 */
public class DefaultPlatform implements Platform {

	public static final String CONFIG = "havis/util/platform/platform.properties";

	private PlatformProperties properties;
	private final String config;

	/**
	 * Loads the platform with the default configuration
	 * <code>havis-platform/platform.properties</code> in the class path.
	 */
	public DefaultPlatform() {
		this(CONFIG);
	}

	/**
	 * @param config
	 *            The path to the configuration file. A relative path starts at
	 *            the classpath.
	 */
	public DefaultPlatform(String config) {
		this.config = config;
	}

	/**
	 * Opens the platform by reading the properties file.
	 */
	@Override
	public synchronized void open() throws PlatformException {
		properties = new PlatformProperties(config);
	}

	@Override
	public void close() {
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
	 * Returns <code>-1</code> because the uptime of the operating platform is
	 * not provided by the JVM.
	 */
	@Override
	public long getUptime() {
		return -1;
	}
}