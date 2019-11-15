package com.seasy.docker.admin.test;


import org.slf4j.Logger;

import com.seasy.docker.admin.server.DockerChainedHandler;
import com.seasy.docker.common.SeasyLoggerFactory;
import com.seasy.docker.common.mina.ServerImpl;
import com.seasy.docker.common.mina.config.SSLConfig;
import com.seasy.docker.common.mina.config.ServerConfig;
import com.seasy.docker.common.mina.core.Server;

public class ServerTest {
	private static Logger logger = SeasyLoggerFactory.getLogger(ServerTest.class);
	
	public static void main(String[] args) {
		Server server = null;
		try{
			ServerConfig config = new ServerConfig.Builder()
					.setHeartbeatEnabled(true)
					.setSslConfig(new SSLConfig.Builder().setEnabled(true).build())
					.build();
			
			server = new ServerImpl.Builder()
				.setHandler(new DockerChainedHandler())
				.setConfig(config)
				.build();
			
			server.start();
			System.out.println("start server...");
			
		}catch(Exception ex){
			logger.error("start server error", ex);
			if(server != null){
				server.stop();
			}
		}
	}
	
}
