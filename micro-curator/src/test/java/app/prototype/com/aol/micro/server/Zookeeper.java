package app.prototype.com.aol.micro.server;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.admin.AdminServer.AdminServerException;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.springframework.stereotype.Component;


public class Zookeeper {
	private ZooKeeperServerMain zooKeeperServer;
	
	
	public void init(){
		
			
			Properties startupProperties = new Properties();
			
			startupProperties.put("dataDir", "/tmp/zookeeper/pl");
			startupProperties.put("clientPort", "12181");

			
			QuorumPeerConfig quorumConfiguration = new QuorumPeerConfig();
			try {
			    quorumConfiguration.parseProperties(startupProperties);
			} catch(Exception e) {
			    throw new RuntimeException(e);
			}

			zooKeeperServer = new ZooKeeperServerMain();
			final ServerConfig configuration = new ServerConfig();
			configuration.readFrom(quorumConfiguration);

			new Thread() {
			    public void run() {
			        try {
			            zooKeeperServer.runFromConfig(configuration);
			        } catch (IOException e) {
			        	e.printStackTrace();
			        } catch (AdminServerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
			}.start();
			
			
			
		
	}
}
