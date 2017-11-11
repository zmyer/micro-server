package app.micro.server.servers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oath.micro.server.MicroserverApp;
import com.oath.micro.server.config.Microserver;

@Microserver(properties={"access.log.output", "${user.home}"})
public class AccessLogConfigTest {

	MicroserverApp server;

	File logFile;

	@Before
	public void startServer() {

		logFile = new File(System.getProperty("user.home") + "/access-log-app-access.log");
		logFile.delete();

		assertThat(logFile.exists(), is(false));

		server = new MicroserverApp(() -> "access-log-app");
		server.start();

	}

	@After
	public void stopServer() {
		server.stop();
	}

	@Test
	public void runAppAndBasicTest() {

		assertThat(logFile.exists(), is(true));

	}
}
