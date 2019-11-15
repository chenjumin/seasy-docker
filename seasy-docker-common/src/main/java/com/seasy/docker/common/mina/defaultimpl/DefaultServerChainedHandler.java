package com.seasy.docker.common.mina.defaultimpl;

import com.seasy.docker.common.mina.AbstractServerChainedHandler;

public class DefaultServerChainedHandler extends AbstractServerChainedHandler {
	@Override
	public void buildChain() throws Exception {
		if (!getChain().contains("DefaultServerHandler")) {
            getChain().addLast("DefaultServerHandler", new DefaultServerHandler());
        }
	}
}
