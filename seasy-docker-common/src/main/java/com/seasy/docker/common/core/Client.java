package com.seasy.docker.common.core;

public interface Client<T> {
	void connect();
	boolean isConnected();
	void reconnect();
	void disconnect();
	void sendMessage(T message);
}