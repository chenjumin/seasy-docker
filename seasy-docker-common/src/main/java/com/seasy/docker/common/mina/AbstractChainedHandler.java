package com.seasy.docker.common.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.chain.ChainedIoHandler;
import org.apache.mina.transport.socket.SocketSessionConfig;

import com.seasy.docker.common.mina.config.AbstractConfig;
import com.seasy.docker.common.mina.config.Config;

public abstract class AbstractChainedHandler extends ChainedIoHandler {
	private Config config;
	
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		AbstractConfig abstractConfig = (AbstractConfig)config;
		
		SocketSessionConfig sessionConfig = (SocketSessionConfig) session.getConfig();
        sessionConfig.setReuseAddress(abstractConfig.isReuseaddr());
        sessionConfig.setKeepAlive(abstractConfig.isKeepalive());
        sessionConfig.setTcpNoDelay(abstractConfig.isNodelay());
        sessionConfig.setSoLinger(abstractConfig.getLinger());
        sessionConfig.setReceiveBufferSize(abstractConfig.getReceiveBuf());
        sessionConfig.setSendBufferSize(abstractConfig.getSendBuf());
        sessionConfig.setReadBufferSize(abstractConfig.getReadBuf());
        
        buildChain();
	}
	
	public abstract void buildChain() throws Exception;
	
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		getChain().execute(null, session, message);
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}
	
}
