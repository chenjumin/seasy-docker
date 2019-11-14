package com.seasy.docker.admin.server;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;

import com.seasy.docker.common.SeasyLoggerFactory;
import com.seasy.docker.common.server.AbstractServerChainedHandler;
import com.seasy.docker.common.utils.StringUtil;

public class DockerChainedHandler extends AbstractServerChainedHandler {
	private static Logger logger = SeasyLoggerFactory.getLogger(DockerChainedHandler.class);
	
	@Override
	public void buildChain() throws Exception {
		if (!getChain().contains("DockerCommonHandler")) {
            getChain().addLast("DockerCommonHandler", new DockerCommonHandler());
        }
	}
	
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		sessionMap.put(String.valueOf(session.getId()), session);
		logger.info("add session, id is " + session.getId() + ", client address is " + session.getRemoteAddress().toString());
	}
	
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		removeSession(session);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable ex) throws Exception {
		removeSession(session);
		
		String errorMsg = StringUtil.trimToEmpty(ex.toString());
		if(errorMsg.matches(".+强迫关闭.+") || errorMsg.matches(".+Connection reset.+")){
			logger.error("Connection reset：" + ex.toString());
			session.closeNow();
		}else if(errorMsg.matches(".*SSL.*Exception.*")){
			logger.error("SSL Error", ex);
			session.closeNow();
		}else if(errorMsg.matches(".*NegativeArraySizeException.*")){
			logger.error("Protocol Codec Error", ex);
			session.closeNow();
		}else{
			logger.error("Server Error", ex);
		}
	}	
}
