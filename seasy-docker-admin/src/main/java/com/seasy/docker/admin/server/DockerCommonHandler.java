package com.seasy.docker.admin.server;

import java.util.Arrays;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;

import com.seasy.docker.common.AbstractHandlerCommand;
import com.seasy.docker.common.CommonMessage;
import com.seasy.docker.common.MessageTypes;
import com.seasy.docker.common.SeasyLoggerFactory;

public class DockerCommonHandler extends AbstractHandlerCommand {
	private static Logger logger = SeasyLoggerFactory.getLogger(DockerCommonHandler.class);
	
	@Override
	public void doExecute(NextCommand next, IoSession session, Object object) throws Exception {
		CommonMessage message = (CommonMessage)object;
		logger.debug("收到客户端的数据包: " + Arrays.toString(message.getFullData()));
		
		if(MessageTypes.TEST == message.getType()){
			CommonMessage responseMessage = new CommonMessage(MessageTypes.TEST, "hello client".getBytes());
			session.write(responseMessage);
		}else if(MessageTypes.HEARTBEAT == message.getType()){
			//heartbeat
		}
		
		next.execute(session, message);
	}
	
}
