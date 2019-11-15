package com.seasy.docker.common;

import org.apache.mina.core.session.IoSession;

import com.seasy.docker.common.mina.ClientImpl;
import com.seasy.docker.common.mina.DefaultClientListener;
import com.seasy.docker.common.mina.config.ClientConfig;
import com.seasy.docker.common.mina.config.SSLConfig;
import com.seasy.docker.common.mina.core.Client;
import com.seasy.docker.common.mina.defaultimpl.DefaultClientChainedHandler;
import com.seasy.docker.common.mina.defaultimpl.DefaultMessage;

public class MinaClientTest {
	public static void main(String[] args) {
		Client<DefaultMessage> client = null;
		try{
			final ClientConfig config = new ClientConfig.Builder()
					.setServerIp("127.0.0.1")
					.setPort(8000)
					.setHeartbeatEnabled(true) //心跳
					.setSslConfig(new SSLConfig.Builder().setEnabled(true).build()) //不启用SSL
					.setListener(new DefaultClientListener(){
						public void onConnectSuccess(IoSession session) {
							DefaultMessage message = new DefaultMessage(1000, "hello world".getBytes());
							session.write(message);
						}
						
						public void onSessionClosed(Client client, IoSession session) {
							client.reconnect();
						};
					})
					.build();
			
			client = new ClientImpl.Builder<DefaultMessage>()
					.setHandler(new DefaultClientChainedHandler())
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
