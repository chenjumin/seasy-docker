package com.seasy.docker.common.client;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.chain.ChainedIoHandler;
import org.apache.mina.transport.socket.SocketSessionConfig;

import com.seasy.docker.common.config.AbstractConfig;
import com.seasy.docker.common.config.Config;
import com.seasy.docker.common.core.Client;

public abstract class AbstractClientChainedHandler extends ChainedIoHandler {
	private Client client;
	private Config config;
	protected Map<String, IoSession> sessionMap = new ConcurrentHashMap<String, IoSession>();
	
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

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}
	
	public void removeSession(IoSession session) {
		if(session != null && sessionMap.containsKey(String.valueOf(session.getId()))){
			sessionMap.remove(String.valueOf(session.getId()));
		}
	}
	
	public void removeAllSession() {
		for(Iterator<IoSession> it=sessionMap.values().iterator(); it.hasNext();){
			it.next().closeNow();
		}
		sessionMap.clear();
	}
}
