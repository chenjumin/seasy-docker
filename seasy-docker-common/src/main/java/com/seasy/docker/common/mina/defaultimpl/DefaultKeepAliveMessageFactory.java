package com.seasy.docker.common.mina.defaultimpl;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

import com.seasy.docker.common.mina.MessageTypes;

public class DefaultKeepAliveMessageFactory implements KeepAliveMessageFactory {
	/**
	 * 判断是否是客户端发送来的心跳包，此判断影响 KeepAliveRequestTimeoutHandler实现类
	 */
	@Override
	public boolean isRequest(IoSession session, Object object) {
		DefaultMessage message = (DefaultMessage)object;
		if(MessageTypes.HEARTBEAT == message.getType()){
			return true;
		}
		return false;
	}

	/**
	 * 获取心跳包对象
	 */
	@Override
	public Object getRequest(IoSession session) {
		return new DefaultMessage(MessageTypes.HEARTBEAT, "HB".getBytes());
	}

	/**
	 * 判断发送回复心跳包，此判断影响 KeepAliveRequestTimeoutHandler实现类
	 */
	@Override
	public boolean isResponse(IoSession session, Object object) {
		return false;
	}

	/**
	 * 获取回复心跳包
	 */
	@Override
	public Object getResponse(IoSession session, Object request) {
		return null;
	}

}
