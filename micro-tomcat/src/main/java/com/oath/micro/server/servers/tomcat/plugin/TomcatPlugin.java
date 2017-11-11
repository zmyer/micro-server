package com.oath.micro.server.servers.tomcat.plugin;

import java.util.Optional;

import com.oath.micro.server.Plugin;
import com.oath.micro.server.servers.ServerApplicationFactory;
import com.oath.micro.server.servers.tomcat.TomcatApplicationFactory;


public class TomcatPlugin implements Plugin{
	
	@Override
	public Optional<ServerApplicationFactory> serverApplicationFactory(){
		return Optional.of(new TomcatApplicationFactory());
	}
	

}
