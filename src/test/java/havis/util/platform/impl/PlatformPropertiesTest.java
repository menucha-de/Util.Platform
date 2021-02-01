package havis.util.platform.impl;

import havis.util.platform.PlatformException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import mockit.Deencapsulation;
import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Assert;
import org.junit.Test;

public class PlatformPropertiesTest {

	private Path BASE_RESOURCE_PATH = Paths.get(PlatformPropertiesTest.class
			.getPackage().getName().replace('.', '/'));

	@Test
	public void constructor(final @Mocked Logger log) throws Exception {
		new NonStrictExpectations() {
			{
				log.isLoggable(Level.INFO);
				result = true;
			}
		};
		Logger origLog = Deencapsulation.getField(PlatformProperties.class,
				"log");
		Deencapsulation.setField(PlatformProperties.class, "log", log);

		// try to read a non-existing property file
		try {
			new PlatformProperties(BASE_RESOURCE_PATH.resolve("XXX").toString());
			Assert.fail();
		} catch (PlatformException e) {
			Assert.assertTrue(e.getMessage()
					.contains("Missing properties file"));
		}

		// try to read an invalid property file (missing entry)
		try {
			new PlatformProperties(BASE_RESOURCE_PATH.resolve(
					"platformEmpty.properties").toString());
			Assert.fail();
		} catch (PlatformException e) {
			Assert.assertTrue(e.getMessage().contains("Missing property"));
		}

		// try to read an invalid property file (invalid entry)
		try {
			new PlatformProperties(BASE_RESOURCE_PATH.resolve(
					"platformInvalid.properties").toString());
		} catch (PlatformException e) {
			Assert.assertTrue(e.getMessage().contains("Invalid property"));
		}

		Deencapsulation.setField(PlatformProperties.class, "log", origLog);
	}

	@Test
	public void hasUTCClock() throws Exception {
		// get the property
		PlatformProperties scProps = new PlatformProperties(BASE_RESOURCE_PATH
				.resolve("platformTrue.properties").toString());
		Assert.assertTrue(scProps.hasUTCClock());
		scProps = new PlatformProperties(BASE_RESOURCE_PATH.resolve(
				"platformFalse.properties").toString());
		Assert.assertFalse(scProps.hasUTCClock());
	}
}