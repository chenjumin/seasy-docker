package com.seasy.docker.common.server;

import java.util.Arrays;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seasy.docker.common.AbstractHandlerCommand;
import com.seasy.docker.common.CommonMessage;
import com.seasy.docker.common.core.MessageTypes;

public class ServerCommonHandler extends AbstractHandlerCommand {
	private static Logger logger = LoggerFactory.getLogger(ServerCommonHandler.class);
	
	@Override
	public void doExecute(NextCommand next, IoSession session, Object object) throws Exception {
		CommonMessage message = (CommonMessage)object;
		logger.debug("receive from client: " + Arrays.toString(message.getFullData()));
		
		if(MessageTypes.TEST == message.getType()){
			CommonMessage responseMessage = new CommonMessage(MessageTypes.TEST, "hello client".getBytes());
			session.write(responseMessage);
		}else if(MessageTypes.HEARTBEAT == message.getType()){
			//heartbeat
		}
		
		next.execute(session, message);
	}
	
}
