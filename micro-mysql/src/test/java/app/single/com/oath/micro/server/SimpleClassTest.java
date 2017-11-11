package app.single.com.oath.micro.server;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.concurrent.ExecutionException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oath.micro.server.MicroserverApp;
import com.oath.micro.server.config.Microserver;
import com.oath.micro.server.testing.RestAgent;

@Microserver(properties={"db.connection.driver","org.hsqldb.jdbcDriver",
	    "db.connection.url","jdbc:hsqldb:mem:aname",
	    "db.connection.username", "sa",
	    "db.connection.password", "",
	    "db.connection.dialect","org.hibernate.dialect.HSQLDialect",
	    "db.connection.ddl.auto","create-drop"   })
public class SimpleClassTest {

	RestAgent rest = new RestAgent();
	
	MicroserverApp server;
	
	@Before
	public void startServer(){
		
		server = new MicroserverApp(()-> "simple-app");
		server.start();

	}
	
	@After
	public void stopServer(){
		server.stop();
	}
	
	@Test
	public void runAppAndBasicTest() throws InterruptedException, ExecutionException{
		
		assertThat(rest.get("http://localhost:8080/simple-app/status/ping"),is("true"));
	
	}

	
	
	
}