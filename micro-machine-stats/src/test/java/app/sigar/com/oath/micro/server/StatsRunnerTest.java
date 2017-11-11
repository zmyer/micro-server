package app.sigar.com.oath.micro.server;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.concurrent.ExecutionException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oath.micro.server.MicroserverApp;
import com.oath.micro.server.config.Microserver;
import com.oath.micro.server.module.ConfigurableModule;
import com.oath.micro.server.testing.RestAgent;

@Microserver(properties = { "machine.stats.deploy.dir", "/tmp" })
public class StatsRunnerTest {

    RestAgent rest = new RestAgent();

    MicroserverApp server;

    @Before
    public void startServer() {
        new File(
                 "/tmp/sigar-lib").delete();
        server = new MicroserverApp(
                                    ConfigurableModule.builder()
                                                      .context("simple-app")
                                                      .build());

        server.start();

    }

    @After
    public void stopServer() {
        server.stop();
    }

    @Test
    public void runAppAndBasicTest() throws InterruptedException, ExecutionException {

        assertThat(rest.get("http://localhost:8080/simple-app/stats/machine"), containsString("cpu-stats"));
        assertTrue(new File(
                            "/tmp/sigar-lib").exists());
    }

}
