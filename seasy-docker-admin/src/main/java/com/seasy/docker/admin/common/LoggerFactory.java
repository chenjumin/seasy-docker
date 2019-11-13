package com.seasy.docker.admin.common;

import org.slf4j.Logger;

/**
 * 日志工厂类
 */
public class LoggerFactory {
	public static Logger getLogger(Class<?> clazz){
		return org.slf4j.LoggerFactory.getLogger(clazz);
	}
}
