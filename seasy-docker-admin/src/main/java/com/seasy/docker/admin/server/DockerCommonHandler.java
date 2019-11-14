package com.seasy.docker.admin.server;

import java.util.Arrays;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;

import com.seasy.docker.admin.utils.JsonUtil;
import com.seasy.docker.common.AbstractHandlerCommand;
import com.seasy.docker.common.CommonMessage;
import com.seasy.docker.common.SeasyLoggerFactory;
import com.seasy.docker.common.core.MessageTypes;

public class DockerCommonHandler extends AbstractHandlerCommand {
	private static Logger logger = SeasyLoggerFactory.getLogger(DockerCommonHandler.class);
	
	@Override
	public void doExecute(NextCommand next, IoSession session, Object object) throws Exception {
		CommonMessage message = (CommonMessage)object;
		logger.debug("收到客户端的数据包: " + Arrays.toString(message.getFullData()));
		
		if(MessageTypes.TEST == message.getType()){
			String data = new String(message.getData());
			System.out.println(data);
			
			if(data.startsWith("{")){
				System.out.println(JsonUtil.object2string(JsonUtil.string2object(data)));
			}else if(data.startsWith("[")){
				System.out.println(JsonUtil.array2string(JsonUtil.string2array(data)));
			}
			
			CommonMessage responseMessage = new CommonMessage(MessageTypes.TEST, "hello client".getBytes());
			session.write(responseMessage);
		}else if(MessageTypes.HEARTBEAT == message.getType()){
			//heartbeat
		}else{
			next.execute(session, message);
		}
	}
	
}
