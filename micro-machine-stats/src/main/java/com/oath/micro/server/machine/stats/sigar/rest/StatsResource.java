package com.oath.micro.server.machine.stats.sigar.rest;



import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.hyperic.sigar.Sigar;
import org.springframework.beans.factory.annotation.Autowired;

import com.oath.micro.server.auto.discovery.SingletonRestResource;
import com.oath.micro.server.machine.stats.sigar.MachineStats;
import com.oath.micro.server.machine.stats.sigar.MachineStatsChecker;

@Path("/stats")
public class StatsResource implements SingletonRestResource {

	
	private final MachineStatsChecker machineStatsChecker;
	
	@Autowired
	public StatsResource(MachineStatsChecker machineStatsChecker){
		this.machineStatsChecker = machineStatsChecker;
	}

	@GET
	@Path("/machine")
	@Produces("application/json")
	public MachineStats getMachineStats() {
		return machineStatsChecker.getStats(new Sigar());
	}
}
