package com.seasy.docker.agent;

import com.seasy.docker.agent.mina.MinaClient;
import com.seasy.docker.agent.thrift.ThriftServer;

public class Main {
	/**
	 * server	作为服务端模式
	 * client	作为客户端模式
	 */
	private static String mode = "server";
	
	public static void main(String[] args) {
		if(args != null && args.length > 0){
			mode = args[0];
		}
		
		if("server".equalsIgnoreCase(mode)){
			ThriftServer.start();
		}else{
			MinaClient.start();
		}
	}
}
