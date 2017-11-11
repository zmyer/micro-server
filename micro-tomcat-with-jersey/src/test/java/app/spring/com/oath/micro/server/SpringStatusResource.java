package app.spring.com.oath.micro.server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oath.micro.server.auto.discovery.RestResource;

@Component
@Path("/spring")
public class SpringStatusResource implements RestResource {

	@Autowired
	private MyBean mybean;
	
	@GET
	@Produces("text/plain")
	@Path("/ping")
	public String ping() {
		return mybean.getInjected().getData();
	}
	@GET
	@Produces("text/plain")
	@Path("/ping2")
	public String ping2() {
		return "ok";
	}

}