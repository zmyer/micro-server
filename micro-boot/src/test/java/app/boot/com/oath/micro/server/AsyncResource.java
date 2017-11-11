package app.boot.com.oath.micro.server;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

import cyclops.async.SimpleReact;
import org.springframework.stereotype.Component;


import com.oath.micro.server.auto.discovery.RestResource;
import com.oath.micro.server.rest.client.nio.AsyncRestClient;
import com.google.common.collect.ImmutableList;

@Path("/async")
@Component
public class AsyncResource implements RestResource{

	private final SimpleReact simpleReact =new SimpleReact();
	private final ImmutableList<String> urls = ImmutableList.of("http://localhost:8080/async-app/async/ping2",
			"http://localhost:8080/async-app/async/ping",
			"http://localhost:8080/async-app/async/ping",
			"http://localhost:8080/async-app/async/ping");
    
    	private final AsyncRestClient client = new AsyncRestClient(1000,1000).withAccept("text/plain");
    	
        @GET
        @Path("/expensive")
        @Produces("text/plain")
        public void expensive(@Suspended AsyncResponse asyncResponse){
        	
        	simpleReact.fromStream(urls.stream()
					.<CompletableFuture<String>>map(it ->  client.get(it)))
					.onFail(it -> "")
					.peek(it -> 
					System.out.println(it))
					.allOf(data -> {
						System.out.println(data);
							return asyncResponse.resume(String.join(";", (List<String>)data)); });
        	
        }
        
        @GET
    	@Produces("text/plain")
    	@Path("/ping")
    	public String ping() {
    		return "test!";
    	}
    	
	
}
