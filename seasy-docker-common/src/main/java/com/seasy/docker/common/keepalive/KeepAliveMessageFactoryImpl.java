package com.seasy.docker.common.keepalive;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

import com.seasy.docker.common.CommonMessage;
import com.seasy.docker.common.config.AbstractConfig;
import com.seasy.docker.common.config.Config;
import com.seasy.docker.common.core.MessageTypes;

public class KeepAliveMessageFactoryImpl implements KeepAliveMessageFactory {
	private Config config;
	
	public KeepAliveMessageFactoryImpl(Config config){
		this.config = config;
	}
	
	/**
	 * 判断是否是客户端发送来的心跳包，此判断影响 KeepAliveRequestTimeoutHandler实现类
	 */
	@Override
	public boolean isRequest(IoSession session, Object object) {
		CommonMessage message = (CommonMessage)object;
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
		return new CommonMessage(MessageTypes.HEARTBEAT, ((AbstractConfig)config).getHeartbeatMessage().getBytes());
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
