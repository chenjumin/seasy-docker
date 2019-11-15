package com.seasy.docker.admin.server;

import com.seasy.docker.common.mina.AbstractServerChainedHandler;

public class DockerChainedHandler extends AbstractServerChainedHandler {
	@Override
	public void buildChain() throws Exception {
		if (!getChain().contains("DockerServerHandler")) {
            getChain().addLast("DockerServerHandler", new DockerServerHandler());
        }
	}
}
