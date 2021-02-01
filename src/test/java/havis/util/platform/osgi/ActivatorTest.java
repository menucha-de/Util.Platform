package havis.util.platform.osgi;

import havis.util.platform.Platform;
import havis.util.platform.impl.DefaultPlatform;
import havis.util.platform.impl.LinuxPlatform;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Dictionary;
import java.util.logging.Level;
import java.util.logging.Logger;

import mockit.Deencapsulation;
import mockit.Delegate;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;

import org.junit.Assert;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class ActivatorTest {

	private Path BASE_RESOURCE_PATH = Paths.get(ActivatorTest.class
			.getPackage().getName().replace('.', '/'));

	@Test
	public void startStopJava(@Mocked final BundleContext bundleContext,
			@Mocked final ServiceRegistration<Platform> srvReg,
			@Mocked final Logger log) throws Exception {
		startStop(bundleContext, srvReg, log, "bundle.properties",
				DefaultPlatform.class);
	}

	@Test
	public void startStopLinux(@Mocked final BundleContext bundleContext,
			@Mocked final ServiceRegistration<Platform> srvReg,
			@Mocked final Logger log) throws Exception {
		startStop(bundleContext, srvReg, log, "bundleLinux.properties",
				LinuxPlatform.class);
	}

	@SuppressWarnings("unchecked")
	private <T extends Platform> void startStop(
			final BundleContext bundleContext,
			final ServiceRegistration<Platform> registration, final Logger log,
			final String fileName, final Class<T> platformClass)
			throws Exception {
		new NonStrictExpectations() {
			{
				bundleContext.getBundle().getResource("bundle.properties");
				result = getClass().getClassLoader().getResource(
						BASE_RESOURCE_PATH.resolve(fileName).toString());

				bundleContext.registerService(Platform.class,
						withInstanceOf(Platform.class),
						withInstanceOf(Dictionary.class));
				result = registration;

				log.isLoggable(Level.INFO);
				result = true;
			}
		};
		Deencapsulation.setField(Activator.class, "log", log);

		// start and stop the bundle
		Activator activator = new Activator();
		activator.start(bundleContext);
		activator.stop(bundleContext);

		new Verifications() {
			{
				// a DefaultPlatform instance is registered as service
				ServiceRegistration<Platform> reg = bundleContext
						.registerService(Platform.class,
								withInstanceOf(platformClass), null);
				times = 1;

				// the instance is unregistered
				reg.unregister();
				times = 1;
			}
		};
	}

	@Test
	public void startError1(@Mocked final BundleContext bundleContext)
			throws Exception {
		class Data {
			URL url;
		}
		final Data data = new Data();
		new NonStrictExpectations() {
			{
				bundleContext.getBundle().getResource("bundle.properties");
				result = new Delegate<Bundle>() {
					@SuppressWarnings("unused")
					URL getResource(String name) {
						return data.url;
					}
				};
			}
		};

		Activator activator = new Activator();

		// start the bundle with an invalid platform property
		data.url = getClass().getClassLoader().getResource(
				BASE_RESOURCE_PATH.resolve("bundleInvalidPlatform.properties")
						.toString());
		try {
			activator.start(bundleContext);
			Assert.fail();
		} catch (InvalidPropertyException e) {
			Assert.assertTrue(e.getMessage().contains("huhu"));
		}
	}
}