package com.seasy.docker.common.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketConnector;

import com.seasy.docker.common.mina.config.Config;
import com.seasy.docker.common.mina.core.Client;
import com.seasy.docker.common.mina.core.ClientListener;

public class DefaultClientListener implements ClientListener {
	@Override
	public void onConnectSuccess(IoSession session) {
		
	}

	@Override
	public void onConnectFail(Throwable ex) {
		
	}

	@Override
	public void onReceived(Client client, Config config, Object msg) {
		
	}

	@Override
	public void onSessionClosed(Client client, IoSession session) {
		
	}

	@Override
	public void onDisconnect(SocketConnector connector, IoSession session) {
		
	}
}
