package com.seasy.docker.common.mina.defaultimpl;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seasy.docker.common.mina.core.IMessage;
import com.seasy.docker.common.utils.NumberUtil;

public class DefaultDecoder extends CumulativeProtocolDecoder {
	private static final Logger logger = LoggerFactory.getLogger(DefaultDecoder.class);
	
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		if(in.remaining() < 8){
			return false;
		}
		
		in.mark();
		
		//length
		byte[] lengthArr = new byte[4];
		in.get(lengthArr, 0, 4);
		int length = NumberUtil.byteArrayToInt4(lengthArr);
		//logger.debug("length=" + length);
		
		//type
		byte[] typeArr = new byte[4];
		in.get(typeArr, 0, 4);
		int type = NumberUtil.byteArrayToInt4(typeArr);
		//logger.debug("type=" + type);
		
		if(in.remaining() < length){
			in.reset();
			return false;
		}
		
		//data
		byte[] dataArr = new byte[length];
		in.get(dataArr, 0, length);
		logger.debug("length=" + length + ", type=" + type + ", data=" + new String(dataArr));

        IMessage message = new DefaultMessage(type, dataArr);
		out.write(message);
		
		//继续做拆包处理：让父类把剩下的数据再给解析一次
		if(in.remaining() > 0){
			return true;
		}
		
		return false;
	}

}
