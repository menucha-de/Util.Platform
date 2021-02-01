package havis.util.platform.impl;

import havis.util.platform.Platform;
import havis.util.platform.PlatformException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;

import mockit.Deencapsulation;
import mockit.Mocked;
import mockit.NonStrictExpectations;

public class LinuxPlatformTest {

	private static Path BASE_RESOURCE_PATH = Paths.get(LinuxPlatformTest.class
			.getPackage().getName().replace('.', '/'));

	@Test
	public void hasUTCClock() throws PlatformException {
		// open platform without loading a native library and get flag for UTC
		// clock from properties file
		Deencapsulation.setField(LinuxPlatform.class, "isLoaded", true);
		Platform sc = new LinuxPlatform(BASE_RESOURCE_PATH.resolve(
				"platformTrue.properties").toString());
		sc.open();
		Assert.assertTrue(sc.hasUTCClock());
		sc.close();

		Deencapsulation.setField(LinuxPlatform.class, "isLoaded", false);
	}

	@Test
	public void getUptime(@Mocked final Logger log) throws PlatformException {
		new NonStrictExpectations() {
			{
				log.isLoggable(Level.INFO);
				result = true;
			}
		};
		Logger origLog = Deencapsulation.getField(PlatformProperties.class,
				"log");
		Deencapsulation.setField(LinuxPlatform.class, "log", log);
		LinuxPlatform sc = new LinuxPlatform(BASE_RESOURCE_PATH.resolve(
				"platformTrue.properties").toString());
		sc.open();
		long start = System.nanoTime();
		long uptime = sc.getUptime();
		double duration = (System.nanoTime() - start) / 1_000_000.0;
		sc.close();
		System.out.println("Get Linux uptime " + uptime + " ms within "
				+ duration + " ms");
		Assert.assertTrue(uptime > 0);

		Deencapsulation.setField(LinuxPlatform.class, "log", origLog);
	}
}