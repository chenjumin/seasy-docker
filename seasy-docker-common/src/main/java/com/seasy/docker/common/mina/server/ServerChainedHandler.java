package com.seasy.docker.common.mina.server;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seasy.docker.common.utils.StringUtil;

public class ServerChainedHandler extends AbstractServerChainedHandler {
	private static Logger logger = LoggerFactory.getLogger(ServerChainedHandler.class);
	
	@Override
	public void buildChain() throws Exception {
		if (!getChain().contains("ServerCommonHandler")) {
            getChain().addLast("ServerCommonHandler", new ServerCommonHandler());
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
