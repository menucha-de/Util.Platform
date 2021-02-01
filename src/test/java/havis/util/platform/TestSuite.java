package havis.util.platform;

import havis.util.platform.impl.DefaultPlatformTest;
import havis.util.platform.impl.LinuxPlatformTest;
import havis.util.platform.impl.PlatformPropertiesTest;
import havis.util.platform.osgi.ActivatorTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DefaultPlatformTest.class, LinuxPlatformTest.class,
		PlatformPropertiesTest.class, ActivatorTest.class })
public class TestSuite {
}