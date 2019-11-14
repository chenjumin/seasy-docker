package com.seasy.docker.common.mina.config;

import com.seasy.docker.common.mina.core.ClientListener;

public class ClientConfig extends AbstractConfig {
	private String serverIp = "127.0.0.1"; //服务端IP地址
	
	private int connectTimeoutMills = 10000; //连接超时毫秒数
	
	private int reconnectCount = -1; //尝试重连次数，-1表示无限次重连
	private int reconnectIntervalSeconds = 10; //重连间隔秒数

	private ClientListener listener;
	
	private ClientConfig(){
		
	}
	
	public String getServerIp() {
		return serverIp;
	}
	
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	
	public int getConnectTimeoutMills() {
		return connectTimeoutMills;
	}
	
	public void setConnectTimeoutMills(int connectTimeoutMills) {
		this.connectTimeoutMills = connectTimeoutMills;
	}
	
	public int getReconnectCount() {
		return reconnectCount;
	}

	public void setReconnectCount(int reconnectCount) {
		this.reconnectCount = reconnectCount;
	}

	public int getReconnectIntervalSeconds() {
		return reconnectIntervalSeconds;
	}
	
	public void setReconnectIntervalSeconds(int reconnectIntervalSeconds) {
		this.reconnectIntervalSeconds = reconnectIntervalSeconds;
	}

	public ClientListener getListener() {
		return listener;
	}

	public void setListener(ClientListener listener) {
		this.listener = listener;
	}

	public static class Builder{
		private ClientConfig config;
		
		public Builder(){
			config = new ClientConfig();
		}
		
		public ClientConfig build(){
			return config;
		}
		
		//Config
		public Builder setPort(int port) {
			config.setPort(port);
			return this;
		}
		
		public Builder setReuseaddr(boolean reuseaddr) {
			config.setReuseaddr(reuseaddr);
			return this;
		}
		
		public Builder setKeepalive(boolean keepalive) {
			config.setKeepalive(keepalive);
			return this;
		}
		
		public Builder setNodelay(boolean nodelay) {
			config.setNodelay(nodelay);
			return this;
		}
		
		public Builder setLinger(int linger) {
			config.setLinger(linger);
			return this;
		}

		public Builder setHeartbeatEnabled(boolean heartbeatEnabled) {
			config.setHeartbeatEnabled(heartbeatEnabled);
			return this;
		}

		public Builder setHeartbeatMessage(String heartbeatMessage) {
			config.setHeartbeatMessage(heartbeatMessage);
			return this;
		}

		public Builder setRequestIntervalSeconds(int requestIntervalSeconds) {
			config.setRequestIntervalSeconds(requestIntervalSeconds);
			return this;
		}

		public Builder setRequestTimeoutSeconds(int requestTimeoutSeconds) {
			config.setRequestTimeoutSeconds(requestTimeoutSeconds);
			return this;
		}

		public Builder setSslConfig(SSLConfig sslConfig) {
			config.setSslConfig(sslConfig);
			return this;
		}
		
		public Builder setReceiveBuf(int receiveBuf) {
			config.setReceiveBuf(receiveBuf);
			return this;
		}
		
		public Builder setSendBuf(int sendBuf) {
			config.setSendBuf(sendBuf);
			return this;
		}
		
		public Builder setReadBuf(int readBuf) {
			config.setReadBuf(readBuf);
			return this;
		}
		
		
		//ClientConfig		
		public Builder setServerIp(String serverIp) {
			config.setServerIp(serverIp);
			return this;
		}
		
		public Builder setConnectTimeoutMills(int connectTimeoutMills) {
			config.setConnectTimeoutMills(connectTimeoutMills);
			return this;
		}

		public Builder setReconnectCounts(int reconnectCounts) {
			config.setReconnectCount(reconnectCounts); 
			return this;
		}
		
		public Builder setReconnectIntervalSeconds(int reconnectIntervalSeconds) {
			config.setReconnectIntervalSeconds(reconnectIntervalSeconds);
			return this;
		}

		public Builder setListener(ClientListener listener) {
			config.setListener(listener);
			return this;
		}
	}
	
}
