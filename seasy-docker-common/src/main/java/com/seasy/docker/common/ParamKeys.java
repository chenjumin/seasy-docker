package com.seasy.docker.common;

public final class ParamKeys {
	public enum Client{
		SERVER_IP,
		PORT
	}
	
	public enum Server{
		S_LISTEN_PORT,
		S_SELECTOR_THREADS,
		S_WORKER_THREADS
	}
}
