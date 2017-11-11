package app.custom.binder.resource.objects;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ExecutionException;

import cyclops.collections.mutable.SetX;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oath.micro.server.MicroserverApp;
import com.oath.micro.server.module.ConfigurableModule;
import com.oath.micro.server.spring.boot.MicroSpringBoot;
import com.oath.micro.server.testing.RestAgent;

@MicroSpringBoot
public class BinderTest {
	RestAgent rest = new RestAgent();
	MicroserverApp server;
	@Before
	public void startServer(){
		
		
		server = new MicroserverApp(ConfigurableModule.builder().context("binder").jaxRsResourceObjects(SetX.of(new CustomBinder3())).build());
	
	

	}
	
	@After
	public void stopServer(){
		server.stop();
	}
	
	@Test
	public void runAppAndBasicTest() throws InterruptedException, ExecutionException{
		
		
		MyIncovationHandler.captured=false;
		assertThat(rest.get("http://localhost:8080/binder/test"),is("hello world!"));
		assertTrue(MyIncovationHandler.captured);
		
		
	}
}
