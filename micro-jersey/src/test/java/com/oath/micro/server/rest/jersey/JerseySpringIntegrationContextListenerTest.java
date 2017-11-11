package com.oath.micro.server.rest.jersey;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import cyclops.collections.immutable.LinkedListX;
import org.junit.Before;
import org.junit.Test;

import com.oath.micro.server.servers.model.ServerData;

public class JerseySpringIntegrationContextListenerTest {
	
	JerseySpringIntegrationContextListener listener;
	ServerData serverData;
	@Before
	public void setup (){
		serverData = ServerData.builder().module(()->"hello").resources(LinkedListX.of()).build();
		listener = new JerseySpringIntegrationContextListener(serverData);
	}
	

	@Test
	public void testContextInitialized() {
		listener.contextInitialized(null);
		
		assertThat(JerseyRestApplication.getResourcesMap().get(serverData.getModule().getContext())
				, equalTo(Arrays.asList()));
		assertThat(JerseyRestApplication.getPackages().get(serverData.getModule().getContext()),is( serverData.getModule().getDefaultJaxRsPackages()));
		assertThat(JerseyRestApplication.getResourcesClasses().get(serverData.getModule().getContext()), 
				is(serverData.getModule().getDefaultResources()));
	}

}
