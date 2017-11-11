package app.custom.com.oath.micro.server.copy;

import javax.ws.rs.GET;
import javax.ws.rs.Path;


import com.oath.cyclops.util.ExceptionSoftener;
import com.oath.micro.server.auto.discovery.Rest;

@Rest
@Path("/status")
public class CustomStatusResource {

	@GET
	@Path("/ping")
	public String ping() {
		throw ExceptionSoftener.throwSoftenedException( new MyException());
		
	}

	
}