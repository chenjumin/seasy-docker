package com.seasy.docker.common.utils;

import java.util.UUID;

public class StringUtil {
	public static String trimToEmpty(String text){
		if(text == null){
			return "";
		}else{
			return text.trim();
		}
	}
	
	public static boolean isEmpty(String text){
		return (text == null || text.length() == 0);
	}
	
	public static boolean isNotEmpty(String text){
		return (text != null && text.length() > 0);
	}
	
	public static String getUUIDString(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * 将字节数组转换成16进制字符串
	 * @param buf 字节数组
	 * @return 16进制字符串
	 */
	public static String byte2HexStr(byte buf[]) {  
	    StringBuffer sb = new StringBuffer();  
	    for (int i = 0; i < buf.length; i++) {  
	            String hex = Integer.toHexString(buf[i] & 0xFF);  
	            if (hex.length() == 1) {  
	                    hex = '0' + hex;  
	            }  
	            sb.append(hex.toUpperCase());  
	    }  
	    return sb.toString();  
	}

	/**
	 * 将16进制字符串转换为字节数组 
	 * @param hexStr 16进制字符串
	 * @return 字节数组
	 */
	public static byte[] hexStr2Byte(String hexStr) {  
	    if (hexStr.length() < 1)  
	            return null;  
	    byte[] result = new byte[hexStr.length()/2];  
	    for (int i = 0;i< hexStr.length()/2; i++) {  
	            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);  
	            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);  
	            result[i] = (byte) (high * 16 + low);  
	    }  
	    return result;
	}
	
}
