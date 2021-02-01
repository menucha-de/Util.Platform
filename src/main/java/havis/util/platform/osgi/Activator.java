package havis.util.platform.osgi;

import havis.util.platform.Platform;
import havis.util.platform.impl.DefaultPlatform;
import havis.util.platform.impl.LinuxPlatform;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * The OSGi activator registers an {@link Platform} instance as OSGi service.
 * The registered service is <em>not</em> opened via {@link Platform#open()}.
 * The service properties are configured in <code>bundle.properties</code> file.
 * <p>
 * The following properties must exist:
 * <ul>
 * <li><code>havis.util.platform.native.osname</code>: the native osname (
 * <code>default</code>, <code>linux</code>)</li>
 * <li><code>havis.util.platform.config.base.path</code>: the path to the
 * configuration file <code>platform.properties</code></li>
 * </ul>
 * For osname <code>linux</code> a native library liblinuxplatform.so must be
 * available for the JVM.
 * </p>
 * <p>
 * If the file <code>bundle.properties</code> does not exist then the bundle
 * properties provided by the OSGi container are used.
 */
public class Activator implements BundleActivator {

	private static final Logger log = Logger.getLogger(Activator.class.getName());

	private static final String BUNDLE_FILE = "bundle.properties";
	private static final String PREFIX = "havis.util.platform.";
	private static final String OSNAME = "osname";

	private ServiceRegistration<Platform> registration = null;

	@Override
	public void start(BundleContext context) throws Exception {

		// load bundle properties file
		Properties properties = null;
		URL url = context.getBundle().getResource(BUNDLE_FILE);
		if (url != null) {
			properties = new Properties();
			try (InputStream stream = url.openStream()) {
				properties.load(stream);
				log.log(Level.FINE, "Loaded bundle properties from file {0}", url);
			}
		}
		// get property
		String osname = getBundleProperty(properties, context, OSNAME);
		// create platform
		Platform platform;
		switch (osname.trim()) {
		case "default":
			platform = new DefaultPlatform();
			break;
		case "linux":
			platform = new LinuxPlatform();
			break;
		default:
			throw new InvalidPropertyException("Unsupported native os name '" + osname + "'");
		}
		// register platform as service
		registration = context.registerService(Platform.class, platform, null);
		log.log(Level.FINE, "Registered platform for os name {0} as service.", osname);
	}

	@Override
	public void stop(BundleContext ctx) throws Exception {
		if (registration != null) {
			registration.unregister();
			registration = null;
			log.log(Level.FINE, "Unregistered platform service with filter.");
		}
	}

	private String getBundleProperty(Properties properties, BundleContext bundleContext, String key) throws MissingPropertyException {
		String value = null;
		if (properties != null) {
			value = properties.getProperty(PREFIX + key);
		}
		if (value == null || value.trim().length() == 0) {
			value = bundleContext.getProperty(PREFIX + key);
		}
		if (value == null || value.trim().length() == 0) {
			throw new MissingPropertyException("Missing bundle property '" + PREFIX + key + "'");
		}
		return value.trim();
	}
}