package com.seasy.docker.common.mina.defaultimpl;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;

public class DefaultKeepAliveRequestTimeoutHandler implements KeepAliveRequestTimeoutHandler {
	@Override
	public void keepAliveRequestTimedOut(KeepAliveFilter filter, IoSession session) throws Exception {
		//logger.debug("心跳包发送超时处理");
	}
}
