package com.seasy.docker.common;

import com.seasy.docker.common.thrift.ServerBootstrap;
import com.seasy.docker.common.thrift.common.ServerConfig;

public class ThriftServerTest {
	public static void main(String[] args) {
		ServerBootstrap serverBootstrap = null;
		try{
			ServerConfig config = new ServerConfig();
			config.setServiceBasePackage("com.seasy.docker.common.thrift");
			
			serverBootstrap = new ServerBootstrap(config);
			serverBootstrap.start();
		}catch(Exception ex){
			ex.printStackTrace();
			
			if(serverBootstrap != null){
				serverBootstrap.destroy();
			}
		}
	}
}
