package app.sigar.com.oath.micro.server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.oath.micro.server.MicroserverApp;
import com.oath.micro.server.auto.discovery.Rest;

@Rest
@Path("/status")
public class SingleClassApp {

	public static void main(String[] args){
		new MicroserverApp(()-> "simple-app").run();
	}
	@GET
	@Produces("text/plain")
	@Path("/ping")
	public String ping() {
		return "ok";
	}
	
}