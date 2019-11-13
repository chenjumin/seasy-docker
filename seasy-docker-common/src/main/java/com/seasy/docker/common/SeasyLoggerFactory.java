package com.seasy.docker.common;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;

public class SeasyLoggerFactory {
	public static final String LOG_CONFIG_FILE = "logback.xml";
	
	static{
        try {
        	doConfigure(LOG_CONFIG_FILE);
        } catch (Exception ex) {
			ex.printStackTrace();
        }
	}
	
	public static void doConfigure(String logConfigFile) throws Exception {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(loggerContext);
        loggerContext.reset();
        
        configurator.doConfigure(new File(logConfigFile));
	}
	
	public static Logger getLogger(Class<?> clazz){
		return LoggerFactory.getLogger(clazz);
	}
	
}
