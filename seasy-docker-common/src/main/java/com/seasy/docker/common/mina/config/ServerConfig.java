package com.seasy.docker.common.mina.config;

public class ServerConfig extends AbstractConfig {
	/**
	 * 最大Porcessor线程数
	 */
	private int processorCount = Runtime.getRuntime().availableProcessors();
	
	private ServerConfig(){
		
	}
	
	public int getProcessorCount() {
		return processorCount;
	}

	public void setProcessorCount(int processorCount) {
		this.processorCount = processorCount;
	}

	public static class Builder{
		private ServerConfig config;

		public Builder(){
			config = new ServerConfig();
		}
		
		public ServerConfig build(){
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
		
		//ServerConfig
		public Builder setProcessorCount(int processorCount) {
			config.setProcessorCount(processorCount);
			return this;
		}
	}
	
}
