package com.seasy.docker.agent.mina;

import org.apache.mina.core.session.IoSession;

import com.seasy.docker.agent.CommandRunner;
import com.seasy.docker.common.ParamKeys;
import com.seasy.docker.common.mina.CommonMessage;
import com.seasy.docker.common.mina.client.ClientImpl;
import com.seasy.docker.common.mina.config.ClientConfig;
import com.seasy.docker.common.mina.config.SSLConfig;
import com.seasy.docker.common.mina.core.Client;
import com.seasy.docker.common.mina.core.DefaultClientListener;
import com.seasy.docker.common.mina.core.MessageTypes;
import com.seasy.docker.common.utils.PropertiesUtil;

public class MinaClient {
	public static void start() {
		Client<CommonMessage> client = null;
		try{
			String serverIp = PropertiesUtil.getInstance().getProperty(ParamKeys.Client.SERVER_IP.name());
			String port = PropertiesUtil.getInstance().getProperty(ParamKeys.Client.PORT.name());
			
			final ClientConfig config = new ClientConfig.Builder()
					.setServerIp(serverIp)
					.setPort(Integer.parseInt(port))
					.setHeartbeatEnabled(true) //心跳
					.setSslConfig(new SSLConfig.Builder().setEnabled(true).build()) //不启用SSL
					.setListener(new DefaultClientListener(){
						public void onConnectSuccess(IoSession session) {
							String commandResult = CommandRunner.execute("/images/json");
							CommonMessage message = new CommonMessage(MessageTypes.TEST, commandResult.getBytes());
							session.write(message);
						}
						
						public void onSessionClosed(Client client, IoSession session) {
							client.reconnect();
						};
					})
					.build();
			
			client = new ClientImpl.Builder<CommonMessage>()
					.setHandler(new DockerChainedHandler())
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
