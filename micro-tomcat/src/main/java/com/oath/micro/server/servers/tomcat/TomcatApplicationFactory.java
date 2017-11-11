package com.oath.micro.server.servers.tomcat;

import java.util.List;

import com.oath.cyclops.types.persistent.PersistentList;
import lombok.AllArgsConstructor;


import org.springframework.context.ApplicationContext;

import com.oath.micro.server.module.Environment;
import com.oath.micro.server.module.Module;
import com.oath.micro.server.module.ModuleDataExtractor;
import com.oath.micro.server.servers.ServerApplication;
import com.oath.micro.server.servers.ServerApplicationFactory;
import com.oath.micro.server.servers.model.AllData;
import com.oath.micro.server.servers.model.FilterData;
import com.oath.micro.server.servers.model.ServerData;
import com.oath.micro.server.servers.model.ServletData;

@AllArgsConstructor
public class TomcatApplicationFactory implements ServerApplicationFactory {

	
	
	
	public ServerApplication createApp(final  Module module, final ApplicationContext rootContext) {
		 ModuleDataExtractor extractor = new ModuleDataExtractor(module);
		PersistentList resources = extractor.getRestResources(rootContext);

		Environment environment = rootContext.getBean(Environment.class);

		environment.assureModule(module);
		String fullRestResource = "/" + module.getContext() + "/*";

		ServerData serverData = new ServerData(environment.getModuleBean(module).getPort(), 
				resources,
				rootContext, fullRestResource, module);
		List<FilterData> filterDataList = extractor.createFilteredDataList(serverData);
		List<ServletData> servletDataList = extractor.createServletDataList(serverData);

		TomcatApplication app = new TomcatApplication(
				new AllData(serverData,
							filterDataList,
							servletDataList,
							module.getListeners(serverData),
							module.getRequestListeners(serverData)));
		return app;
	}
}
