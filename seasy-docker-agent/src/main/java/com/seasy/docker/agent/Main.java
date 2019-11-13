package com.seasy.docker.agent;

import org.apache.mina.core.session.IoSession;

import com.seasy.docker.common.CommonMessage;
import com.seasy.docker.common.MessageTypes;
import com.seasy.docker.common.client.ClientChainedHandler;
import com.seasy.docker.common.client.ClientImpl;
import com.seasy.docker.common.config.ClientConfig;
import com.seasy.docker.common.config.SSLConfig;
import com.seasy.docker.common.core.Client;
import com.seasy.docker.common.core.DefaultClientListener;

public class Main {
	public static void main(String[] args) {
		Client<CommonMessage> client = null;
		try{
			final ClientConfig config = new ClientConfig.Builder()
					.setServerIp("127.0.0.1")
					.setHeartbeatEnabled(true) //心跳
					.setSslConfig(new SSLConfig.Builder().setEnabled(true).build()) //不启用SSL
					.setListener(new DefaultClientListener(){
						public void onConnectSuccess(IoSession session) {
							CommonMessage message = new CommonMessage(MessageTypes.TEST, "hello server".getBytes());
							session.write(message);
						}
						
						public void onSessionClosed(Client client, IoSession session) {
							client.reconnect();
						};
					})
					.build();
			
			client = new ClientImpl.Builder<CommonMessage>()
					.setHandler(new ClientChainedHandler())
					.setConfig(config)
					.build();
			
			client.connect();
			
		}catch(Exception ex){
			ex.printStackTrace();
			
			if(client != null){
				client.disconnect();
			}
		}
	}
	
}
