package com.seasy.docker.common.config;

public abstract class AbstractConfig implements Config{
	private int port = 8000; //通信端口号
	
	private boolean reuseaddr = true;
	private boolean keepalive = true;
	private boolean nodelay = true; //true表示立即发送数据
	private int linger = 0; //延迟n秒关闭Socket底层连接
	
	private boolean heartbeatEnabled = true; //是否启用心跳功能
	private String heartbeatMessage = "HEARTBEAT"; //心跳报文内容
	
	//两个心跳报文之间的总间隔时间 = requestIntervalSeconds + requestTimeoutSeconds
	private int requestIntervalSeconds = 5; //心跳请求包发送的频率，单位为秒
	private int requestTimeoutSeconds = 5; //心跳请求包超时时间，单位为秒
	
	private SSLConfig sslConfig = new SSLConfig.Builder().build();

	private int receiveBuf = 2 * 1024; //接收数据的缓冲大小
	private int sendBuf = 2 * 1024; //发送数据的缓冲大小
	private int readBuf = 2 * 1024; //读数据的缓冲大小
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public boolean isReuseaddr() {
		return reuseaddr;
	}
	
	public void setReuseaddr(boolean reuseaddr) {
		this.reuseaddr = reuseaddr;
	}
	
	public boolean isKeepalive() {
		return keepalive;
	}
	
	public void setKeepalive(boolean keepalive) {
		this.keepalive = keepalive;
	}
	
	public boolean isNodelay() {
		return nodelay;
	}
	
	public void setNodelay(boolean nodelay) {
		this.nodelay = nodelay;
	}
	
	public int getLinger() {
		return linger;
	}
	
	public void setLinger(int linger) {
		this.linger = linger;
	}

	public boolean isHeartbeatEnabled() {
		return heartbeatEnabled;
	}

	public void setHeartbeatEnabled(boolean heartbeatEnabled) {
		this.heartbeatEnabled = heartbeatEnabled;
	}

	public String getHeartbeatMessage() {
		return heartbeatMessage;
	}

	public void setHeartbeatMessage(String heartbeatMessage) {
		this.heartbeatMessage = heartbeatMessage;
	}

	public int getRequestIntervalSeconds() {
		return requestIntervalSeconds;
	}

	public void setRequestIntervalSeconds(int requestIntervalSeconds) {
		this.requestIntervalSeconds = requestIntervalSeconds;
	}

	public int getRequestTimeoutSeconds() {
		return requestTimeoutSeconds;
	}

	public void setRequestTimeoutSeconds(int requestTimeoutSeconds) {
		this.requestTimeoutSeconds = requestTimeoutSeconds;
	}

	public SSLConfig getSslConfig() {
		return sslConfig;
	}

	public void setSslConfig(SSLConfig sslConfig) {
		this.sslConfig = sslConfig;
	}

	public int getReceiveBuf() {
		return receiveBuf;
	}

	public void setReceiveBuf(int receiveBuf) {
		this.receiveBuf = receiveBuf;
	}

	public int getSendBuf() {
		return sendBuf;
	}

	public void setSendBuf(int sendBuf) {
		this.sendBuf = sendBuf;
	}

	public int getReadBuf() {
		return readBuf;
	}

	public void setReadBuf(int readBuf) {
		this.readBuf = readBuf;
	}

}
