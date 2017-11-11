package app.boot.com.oath.micro.server;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.concurrent.ExecutionException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oath.micro.server.MicroserverApp;
import com.oath.micro.server.boot.config.Microboot;
import com.oath.micro.server.config.Microserver;
import com.oath.micro.server.rest.client.nio.AsyncRestClient;

@Microserver @Microboot
public class AsyncAppRunner {


	AsyncRestClient rest = new AsyncRestClient(1000,1000).withAccept("text/plain");
	
	MicroserverApp server;
	@Before
	public void startServer(){
		
		server = new MicroserverApp( ()-> "async-app");
		server.start();
		

	}
	
	@After
	public void stopServer(){
		server.stop();
	}
	
	@Test
	public void runAppAndBasicTest() throws InterruptedException, ExecutionException{
		
		Thread.sleep(2000);
		
		assertThat(rest.get("http://localhost:8080/async-app/async/expensive").get(),is(";test!;test!;test!"));
	
	}
	
	
	
}