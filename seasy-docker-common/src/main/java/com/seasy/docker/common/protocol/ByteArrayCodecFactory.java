package com.seasy.docker.common.protocol;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class ByteArrayCodecFactory implements ProtocolCodecFactory {
	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
		return new ByteArrayEncoder();
	}
	
	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
		return new ByteArrayDecoder();
	}

}
