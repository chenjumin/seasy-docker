package com.seasy.docker.common.mina.core;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketConnector;

import com.seasy.docker.common.mina.config.Config;

public interface ClientListener {
	void onConnectSuccess(IoSession session);
	void onConnectFail(Throwable ex);
	void onReceived(Client client, Config config, Object msg);
	void onSessionClosed(Client client, IoSession session);
	void onDisconnect(SocketConnector connector, IoSession session);
}
