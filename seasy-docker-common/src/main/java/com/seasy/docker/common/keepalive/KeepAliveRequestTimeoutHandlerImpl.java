package com.seasy.docker.common.keepalive;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;

public class KeepAliveRequestTimeoutHandlerImpl implements KeepAliveRequestTimeoutHandler {
	@Override
	public void keepAliveRequestTimedOut(KeepAliveFilter filter, IoSession session) throws Exception {
		//logger.debug("心跳包发送超时处理");
	}
}
