package com.seasy.docker.common;

public final class ParamKeys {
	public enum Client{
		SERVER_IP,
		PORT
	}
	
	public enum Server{
		LISTEN_PORT,
		SELECTOR_THREADS,
		WORKER_THREADS
	}
}
