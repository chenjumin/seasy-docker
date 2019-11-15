package com.seasy.docker.common.mina.defaultimpl;

import com.seasy.docker.common.mina.AbstractClientChainedHandler;

public class DefaultClientChainedHandler extends AbstractClientChainedHandler {
	@Override
	public void buildChain() throws Exception {
		if (!getChain().contains("DefaultClientHandler")) {
			getChain().addLast("DefaultClientHandler", new DefaultClientHandler());
		}
	}
}
