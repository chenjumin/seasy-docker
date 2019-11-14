package com.seasy.docker.agent.thrift;

import org.slf4j.Logger;

import com.seasy.docker.agent.CommandRunner;
import com.seasy.docker.common.ParamKeys;
import com.seasy.docker.common.SeasyLoggerFactory;
import com.seasy.docker.common.thrift.ServerBootstrap;
import com.seasy.docker.common.thrift.common.ServerConfig;
import com.seasy.docker.common.utils.PropertiesUtil;

public class ThriftServer {
	private static Logger logger = SeasyLoggerFactory.getLogger(CommandRunner.class);
	
	public static void start() {
		ServerBootstrap serverBootstrap = null;
		try{
			String listenPort = PropertiesUtil.getInstance().getProperty(ParamKeys.Server.LISTEN_PORT.name());
			String selectorThreads = PropertiesUtil.getInstance().getProperty(ParamKeys.Server.SELECTOR_THREADS.name());
			String workerThreads = PropertiesUtil.getInstance().getProperty(ParamKeys.Server.WORKER_THREADS.name());
			
			ServerConfig config = new ServerConfig();
			config.setPort(Integer.parseInt(listenPort));
			config.setSelectorThreads(Integer.parseInt(selectorThreads));
			config.setWorkerThreads(Integer.parseInt(workerThreads));
			config.setServiceBasePackage("com.seasy.docker.agent.thrift");
			
			serverBootstrap = new ServerBootstrap(config);
			serverBootstrap.start();
			
		}catch(Exception ex){
			logger.error("start ServerBootstrap error", ex);
			
			if(serverBootstrap != null){
				serverBootstrap.destroy();
			}
		}
	}
	
}
