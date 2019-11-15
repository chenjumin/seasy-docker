package com.seasy.docker.common;


import org.slf4j.Logger;

import com.seasy.docker.common.mina.ServerImpl;
import com.seasy.docker.common.mina.config.SSLConfig;
import com.seasy.docker.common.mina.config.ServerConfig;
import com.seasy.docker.common.mina.core.Server;
import com.seasy.docker.common.mina.defaultimpl.DefaultServerChainedHandler;

public class MinaServerTest {
	private static Logger logger = SeasyLoggerFactory.getLogger(MinaServerTest.class);
	
	public static void main(String[] args) {
		Server server = null;
		try{
			ServerConfig config = new ServerConfig.Builder()
					.setHeartbeatEnabled(true)
					.setSslConfig(new SSLConfig.Builder().setEnabled(true).build())
					.build();
			
			server = new ServerImpl.Builder()
				.setHandler(new DefaultServerChainedHandler())
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
