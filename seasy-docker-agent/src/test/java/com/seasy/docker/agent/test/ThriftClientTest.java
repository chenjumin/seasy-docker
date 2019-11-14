package com.seasy.docker.agent.test;

import com.seasy.docker.common.thrift.ClientBootstrap;
import com.seasy.docker.common.thrift.api.CommandExecutor;

public class ThriftClientTest {
	private static ClientBootstrap clientBootstrap;
	private static CommandExecutor.Client client;
	
	public static void main(String[] args) {
		try{
			clientBootstrap = new ClientBootstrap("192.168.134.138", 5566);
			clientBootstrap.open();
			
			client = clientBootstrap.getServiceClient(CommandExecutor.Client.class);
			String result = client.execute("GET", "", "/images/json");
			System.out.println(result);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if(clientBootstrap != null){
				clientBootstrap.destroy();
			}
		}
	}
	
}
