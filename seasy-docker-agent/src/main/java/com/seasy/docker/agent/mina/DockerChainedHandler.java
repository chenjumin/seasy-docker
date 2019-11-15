package com.seasy.docker.agent.mina;

import com.seasy.docker.common.mina.AbstractClientChainedHandler;

public class DockerChainedHandler extends AbstractClientChainedHandler {
	@Override
	public void buildChain() throws Exception {
		if (!getChain().contains("DockerClientHandler")) {
            getChain().addLast("DockerClientHandler", new DockerClientHandler());
        }
	}
}
