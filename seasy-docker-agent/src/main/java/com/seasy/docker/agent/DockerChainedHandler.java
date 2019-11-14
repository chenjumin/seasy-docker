package com.seasy.docker.agent;

import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seasy.docker.common.client.AbstractClientChainedHandler;
import com.seasy.docker.common.config.ClientConfig;

public class DockerChainedHandler extends AbstractClientChainedHandler {
	private static Logger logger = LoggerFactory.getLogger(DockerChainedHandler.class);
	
	@Override
	public void buildChain() throws Exception {
		if (!getChain().contains("DockerCommonHandler")) {
            getChain().addLast("DockerCommonHandler", new DockerCommonHandler());
        }
	}
	
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		ClientConfig clientConfig = (ClientConfig)getConfig();
		if(clientConfig.getListener() != null){
			clientConfig.getListener().onSessionClosed(getClient(), session);
		}
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable ex) throws Exception {
		String errorMsg = StringUtils.trimToEmpty(ex.toString());
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
	
}
