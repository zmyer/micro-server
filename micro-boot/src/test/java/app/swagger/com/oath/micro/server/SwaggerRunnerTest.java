package app.swagger.com.oath.micro.server;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import java.util.concurrent.ExecutionException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.oath.micro.server.MicroserverApp;
import com.oath.micro.server.testing.RestAgent;

@Configuration
@ComponentScan(basePackages = { "app.swagger.com.oath.micro.server" })
public class SwaggerRunnerTest {


	RestAgent rest = new RestAgent();
	
	MicroserverApp server;
	@Before
	public void startServer(){
		
		server = new MicroserverApp( SwaggerRunnerTest.class, ()-> "swagger-app");
		server.start();

	}
	
	@After
	public void stopServer(){
		server.stop();
	}
	
	@Test
	public void runAppAndBasicTest() throws InterruptedException, ExecutionException{
		
		
		
		assertThat(rest.getJson("http://localhost:8080/api-docs/stats"),containsString("Make a ping call"));
	
	}
	
	
	
}
