package com.seasy.docker.common.mina;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seasy.docker.common.mina.config.ClientConfig;
import com.seasy.docker.common.mina.core.Client;
import com.seasy.docker.common.utils.StringUtil;

public abstract class AbstractClientChainedHandler extends AbstractChainedHandler {
	private static Logger logger = LoggerFactory.getLogger(AbstractClientChainedHandler.class);
	private Client client;
	
	public abstract void buildChain() throws Exception;
	
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		ClientConfig clientConfig = (ClientConfig)getConfig();
		if(clientConfig.getListener() != null){
			clientConfig.getListener().onSessionClosed(getClient(), session);
		}
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable ex) throws Exception {
		String errorMsg = StringUtil.trimToEmpty(ex.toString());
		if(errorMsg.matches(".+强迫关闭.+") || errorMsg.matches(".+Connection reset.+")){
			logger.error("Connection reset：" + ex.toString());
			session.closeNow();
		}else if(errorMsg.matches(".*SSL.*Exception.*")){
			logger.error("SSL Error", ex);
			session.closeNow();
		}else{
			logger.error("Client Error", ex);
		}
	}
	
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

}
