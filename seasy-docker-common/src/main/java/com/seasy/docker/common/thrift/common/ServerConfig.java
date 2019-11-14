package com.seasy.docker.common.thrift.common;

public class ServerConfig {
	private int port = 5566; //服务监听端口
	private int selectorThreads = 2; //selector线程数
	private int workerThreads = 10; //worker线程数
	private String serviceBasePackage; //服务实现类所在的根包路径，多个路径用分号(;)分隔
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public int getSelectorThreads() {
		return selectorThreads;
	}
	
	public void setSelectorThreads(int selectorThreads) {
		this.selectorThreads = selectorThreads;
	}
	
	public int getWorkerThreads() {
		return workerThreads;
	}
	
	public void setWorkerThreads(int workerThreads) {
		this.workerThreads = workerThreads;
	}
	
	public String getServiceBasePackage() {
		return serviceBasePackage;
	}
	
	public void setServiceBasePackage(String serviceBasePackage) {
		this.serviceBasePackage = serviceBasePackage;
	}
}
