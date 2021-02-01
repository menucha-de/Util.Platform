package havis.util.platform;

/**
 * The platform is an interface for the communication with services
 * which are provided directly by the platform.
 * <p>
 * An implementation must be thread safe.
 * </p>
 */
public interface Platform {
	/**
	 * Opens the platform.
	 * 
	 * @throws PlatformException
	 */
	void open() throws PlatformException;

	/**
	 * Closes the platform.
	 */
	void close() throws PlatformException;

	/**
	 * Whether the platform provides a hardware clock.
	 * 
	 * @return
	 */
	boolean hasUTCClock() throws PlatformException;

	/**
	 * Returns the uptime of the platform in milliseconds. If the platform
	 * does not provide the uptime then a negative value is returned.
	 */
	long getUptime() throws PlatformException;
}