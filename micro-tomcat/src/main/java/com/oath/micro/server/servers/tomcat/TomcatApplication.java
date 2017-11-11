package com.oath.micro.server.servers.tomcat;

import java.io.File;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestListener;

import com.oath.cyclops.types.persistent.PersistentList;
import com.oath.cyclops.util.ExceptionSoftener;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.valves.AccessLogValve;
import org.apache.catalina.valves.Constants;
import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.http11.AbstractHttp11JsseProtocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.oath.micro.server.InternalErrorCode;
import com.oath.micro.server.config.SSLProperties;
import com.oath.micro.server.module.WebServerProvider;
import com.oath.micro.server.servers.AccessLogLocationBean;
import com.oath.micro.server.servers.JaxRsServletConfigurer;
import com.oath.micro.server.servers.ServerApplication;
import com.oath.micro.server.servers.model.AllData;
import com.oath.micro.server.servers.model.FilterData;
import com.oath.micro.server.servers.model.ServerData;
import com.oath.micro.server.servers.model.ServletData;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TomcatApplication implements ServerApplication {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Getter
	private final ServerData serverData;

	private final PersistentList<FilterData> filterData;
	private final PersistentList<ServletData> servletData;
	private final PersistentList<ServletContextListener> servletContextListenerData;
	private final PersistentList<ServletRequestListener> servletRequestListenerData;

	public TomcatApplication(AllData serverData) {
		this.serverData = serverData.getServerData();
		this.filterData = serverData.getFilterDataList();
		this.servletData = serverData.getServletDataList();
		this.servletContextListenerData = serverData.getServletContextListeners();
		this.servletRequestListenerData = serverData.getServletRequestListeners();
	}

	public void run(CompletableFuture start,  JaxRsServletConfigurer jaxRsConfigurer, CompletableFuture end) {
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(serverData.getPort());
		tomcat.getHost().setAutoDeploy(false);
		tomcat.getEngine().setBackgroundProcessorDelay(-1);
		 File docBase = new File(".");
		 StandardContext context =(StandardContext)tomcat.addContext("", docBase.getAbsolutePath());
		context.addServletContainerInitializer(new TomcatListener(jaxRsConfigurer, serverData, filterData, servletData, servletContextListenerData, servletRequestListenerData),
				new HashSet<>());
		addAccessLog(tomcat,context);

		serverData.getModule().getServerConfigManager().accept(new WebServerProvider(tomcat));

		addSSL(tomcat.getConnector());

		startServer(tomcat, start, end);
	}

	private void addSSL(Connector connector) {
		SSLProperties sslProperties = serverData.getRootContext().getBean(SSLProperties.class);
		ProtocolHandler handler = connector.getProtocolHandler();
		if(sslProperties!= null && handler instanceof AbstractHttp11JsseProtocol){
			new SSLConfigurationBuilder().build((AbstractHttp11JsseProtocol)handler,sslProperties);
			connector.setScheme("https");
			connector.setSecure(true);
		}
	}

	private void startServer( Tomcat httpServer, CompletableFuture start, CompletableFuture end) {

		try {
			logger.info("Starting application {} on port {}", serverData.getModule().getContext(), serverData.getPort());
			logger.info("Browse to http://localhost:{}/{}/application.wadl", serverData.getPort(), serverData.getModule().getContext());
			logger.info("Configured resource classes :-");
			serverData.extractResources().forEach(
					t -> logger.info(t._1() + " : " + "http://localhost:" + serverData.getPort() + "/" + serverData.getModule().getContext() + t._2()));
			;

				httpServer.start();

			start.complete(true);
			end.get();

		}catch (LifecycleException e) {
			 throw ExceptionSoftener.throwSoftenedException(e);
		} catch (ExecutionException e) {
			throw ExceptionSoftener.throwSoftenedException(e);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw ExceptionSoftener.throwSoftenedException(e);
		} finally {
			try {
				httpServer.stop();
				httpServer.getConnector().destroy();
				httpServer.getEngine().destroy();
				httpServer.destroy();



			} catch (LifecycleException e) {
			}
				try{
					Thread.sleep(5_000);
				}
			catch (InterruptedException e) {

			}

		}
	}

	private void addAccessLog(Tomcat httpServer, StandardContext context) {
		try {

			String accessLogLocation = serverData.getRootContext().getBean(AccessLogLocationBean.class).getAccessLogLocation();

			accessLogLocation = accessLogLocation + "/" + replaceSlash(serverData.getModule().getContext()) + "-access.log";

			AccessLogValve accessLogValve = new AccessLogValve();
            accessLogValve.setDirectory(accessLogLocation);
            accessLogValve.setPattern(Constants.AccessLog.COMMON_ALIAS);
            accessLogValve.setSuffix(".log");
            accessLogValve.setRotatable(true);
            context.getPipeline().addValve(accessLogValve);

		} catch (Exception e) {

			logger.error(InternalErrorCode.SERVER_STARTUP_FAILED_TO_CREATE_ACCESS_LOG.toString() + ": " + e.getMessage());
			if (e.getCause() != null)
				logger.error("CAUSED BY: " + InternalErrorCode.SERVER_STARTUP_FAILED_TO_CREATE_ACCESS_LOG.toString() + ": " + e.getCause().getMessage());

		}

	}



	private String replaceSlash(String context) {
		if (context != null && context.contains("/")) {
			return context.substring(0, context.indexOf("/"));
		}
		return context;
	}

}
