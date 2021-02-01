package havis.util.platform.impl;

import havis.util.platform.Platform;
import havis.util.platform.PlatformException;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

public class DefaultPlatformTest {

	private static Path BASE_RESOURCE_PATH = Paths
			.get(DefaultPlatformTest.class.getPackage().getName()
					.replace('.', '/'));

	@Test
	public void hasUTCClock() throws PlatformException {
		// open platform and get flag for UTC clock from properties file
		Platform sc = new DefaultPlatform(BASE_RESOURCE_PATH.resolve(
				"platformTrue.properties").toString());
		sc.open();
		Assert.assertTrue(sc.hasUTCClock());
		sc.close();
	}

	@Test
	public void getUptime() throws PlatformException {
		// open platform and get uptime
		Platform sc = new DefaultPlatform(BASE_RESOURCE_PATH.resolve(
				"platformTrue.properties").toString());
		sc.open();
		Assert.assertEquals(sc.getUptime(), -1);
		sc.close();
	}
}