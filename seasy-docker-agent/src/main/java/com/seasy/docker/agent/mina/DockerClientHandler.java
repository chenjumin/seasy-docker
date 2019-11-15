package com.seasy.docker.agent.mina;

import java.util.Arrays;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seasy.docker.common.mina.AbstractHandlerCommand;
import com.seasy.docker.common.mina.defaultimpl.DefaultMessage;

public class DockerClientHandler extends AbstractHandlerCommand {
	private static Logger logger = LoggerFactory.getLogger(DockerClientHandler.class);

	@Override
	public void doExecute(NextCommand next, IoSession session, Object object) throws Exception {
		DefaultMessage message = (DefaultMessage)object;
		logger.debug(Arrays.toString(message.getFullData()));
		next.execute(session, object);
	}
}
