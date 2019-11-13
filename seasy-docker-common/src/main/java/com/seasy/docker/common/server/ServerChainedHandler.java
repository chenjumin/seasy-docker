package com.seasy.docker.common.server;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seasy.docker.common.AbstractChainedHandler;
import com.seasy.docker.common.utils.StringUtil;

public class ServerChainedHandler extends AbstractChainedHandler {
	private static Logger logger = LoggerFactory.getLogger(ServerChainedHandler.class);
	private Map<String, IoSession> sessionMap = new ConcurrentHashMap<String, IoSession>();
	
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

	private void removeSession(IoSession session) {
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
