package com.seasy.docker.admin.server;

import java.util.Arrays;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;

import com.seasy.docker.common.SeasyLoggerFactory;
import com.seasy.docker.common.mina.AbstractHandlerCommand;
import com.seasy.docker.common.mina.MessageTypes;
import com.seasy.docker.common.mina.defaultimpl.DefaultMessage;
import com.seasy.docker.common.utils.JsonUtil;

public class DockerServerHandler extends AbstractHandlerCommand {
	private static Logger logger = SeasyLoggerFactory.getLogger(DockerServerHandler.class);
	
	@Override
	public void doExecute(NextCommand next, IoSession session, Object object) throws Exception {
		DefaultMessage message = (DefaultMessage)object;
		logger.debug("收到客户端的数据包: " + Arrays.toString(message.getFullData()));
		
		if(MessageTypes.TEST == message.getType()){
			String data = new String(message.getData());
			System.out.println(data);
			
			if(data.startsWith("{")){
				System.out.println(JsonUtil.object2string(JsonUtil.string2object(data)));
			}else if(data.startsWith("[")){
				System.out.println(JsonUtil.array2string(JsonUtil.string2array(data)));
			}
			
			DefaultMessage responseMessage = new DefaultMessage(MessageTypes.TEST, "hello client".getBytes());
			session.write(responseMessage);
		}else if(MessageTypes.HEARTBEAT == message.getType()){
			//heartbeat
		}else{
			next.execute(session, message);
		}
	}
	
}
