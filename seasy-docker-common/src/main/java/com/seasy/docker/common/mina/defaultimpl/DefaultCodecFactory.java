package com.seasy.docker.common.mina.defaultimpl;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;

public class DefaultCodecFactory implements ProtocolCodecFactory {
	private ProtocolEncoderAdapter encoder = new DefaultEncoder();
	private CumulativeProtocolDecoder decoder = new DefaultDecoder();
	
	public DefaultCodecFactory(){
		
	}
	
	public DefaultCodecFactory(ProtocolEncoderAdapter encoder, CumulativeProtocolDecoder decoder){
		this.encoder = encoder;
		this.decoder = decoder;
	}
	
	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
		return encoder;
	}
	
	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
		return decoder;
	}
}
