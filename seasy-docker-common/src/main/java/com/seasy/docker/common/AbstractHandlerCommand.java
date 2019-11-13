package com.seasy.docker.common;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.chain.IoHandlerCommand;

import com.seasy.docker.common.core.IMessage;
import com.seasy.docker.common.utils.StringUtil;

public abstract class AbstractHandlerCommand implements IoHandlerCommand {	
	@Override
	public void execute(NextCommand next, IoSession session, Object message) throws Exception {
		doExecute(next, session, message);
	}
	
	public abstract void doExecute(NextCommand next, IoSession session, Object message) throws Exception;
	
	protected String getAttributeValue(IoSession session, String key){
		Object obj = session.getAttribute(key);
		return (obj == null) ? "" : StringUtil.trimToEmpty((String)obj);
	}
	
	protected IoSession getSession(IoSession session, Long sessionId){
		return session.getService().getManagedSessions().get(sessionId);
	}
	
	protected void send(IoSession session, IMessage message) throws Exception {
		session.write(message);
	}
	
}
