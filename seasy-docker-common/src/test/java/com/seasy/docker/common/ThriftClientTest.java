package com.seasy.docker.common;

import com.seasy.docker.common.thrift.ClientBootstrap;
import com.seasy.docker.common.thrift.api.CommandExecutor;

public class ThriftClientTest {
	private static ClientBootstrap clientBootstrap;
	private static CommandExecutor.Client client;
	
	public static void main(String[] args) {
		try{
			clientBootstrap = new ClientBootstrap("127.0.0.1", 5566);
			clientBootstrap.open();
			
			client = clientBootstrap.getServiceClient(CommandExecutor.Client.class);
			String result = client.execute("POST", "", "/images/json");
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
