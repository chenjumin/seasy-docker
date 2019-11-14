package com.seasy.docker.common.mina.protocol;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.seasy.docker.common.mina.CommonMessage;

public class ByteArrayEncoder extends ProtocolEncoderAdapter {
	/**
	 * 发送消息前，通过编码器将字节数组转成IoBuffer
	 */
	public void encode(IoSession session, Object obj, ProtocolEncoderOutput out) throws Exception {
		CommonMessage message = (CommonMessage)obj;
		
		IoBuffer buffer = IoBuffer.allocate(message.getFullData().length).setAutoExpand(true);
		buffer.put(message.getFullData());
		
        //写之前要调用flip方法
        //设置limit为positon处，设置positon为0
        buffer.flip();

        //将positon到limit之间的数据写到Output
		out.write(buffer);
	    out.flush();
	    
	    buffer.free();
	}
	
}
