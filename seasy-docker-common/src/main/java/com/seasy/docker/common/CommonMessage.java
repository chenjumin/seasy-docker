package com.seasy.docker.common;

import org.apache.commons.lang.ArrayUtils;

import com.seasy.docker.common.core.IMessage;
import com.seasy.docker.common.utils.NumberUtil;

/**
 * 消息对象。格式如下：
 * 	 	实际数据的字节长度[4字节] + 报文类型[4字节] + 实际数据的字节数组
 */
public class CommonMessage implements IMessage{
	private int length;
	private int type;
	private byte[] dataArr;
	private byte[] fullData; //完整报文的字节数组
	
	public CommonMessage(int type, byte[] dataArr){
		this.length = dataArr.length;
		this.type = type;
		this.dataArr = dataArr;
		
		setFullData();
	}
	
	public int getLength() {
		return length;
	}
	
	public int getType() {
		return type;
	}
	
	public byte[] getData() {
		return dataArr;
	}

	public byte[] getFullData() {
		return this.fullData;
	}
	
	private void setFullData(){
		byte[] lenArr = NumberUtil.int4ToByteArray(length);
		byte[] typeArr = NumberUtil.int4ToByteArray(type);
		
		byte[] fullArr = new byte[lenArr.length + typeArr.length + dataArr.length];
		fullArr = ArrayUtils.addAll(lenArr, typeArr);
		fullArr = ArrayUtils.addAll(fullArr, dataArr);
		
		this.fullData = fullArr;
	}
	
}
