package com.seasy.docker.common.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
	private static final String DEFAULT_PROPERTIES = "agent.properties";
	private Properties properties;
	private static PropertiesUtil instance = new PropertiesUtil();
	
	public PropertiesUtil(){
		String filePath = System.getProperty("user.dir") + File.separator + DEFAULT_PROPERTIES;
		System.out.println("Config file path: " + filePath);
		
		initProperties(filePath);
	}
	
	public PropertiesUtil(String propertiesFileName){
		initProperties(propertiesFileName);
	}
	
	private void initProperties(String propertiesFileName){
    	InputStream inputStream = null;
		try {
			properties = new Properties();
			inputStream = new BufferedInputStream(new FileInputStream(propertiesFileName));
			properties.load(inputStream);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if(inputStream != null){
				try {
					inputStream.close();
					inputStream = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static PropertiesUtil getInstance(){
	    return instance;
	}
	
	public static PropertiesUtil getInstance(String propertyFileName){
		return new PropertiesUtil(propertyFileName);
	}

	public String getProperty(String key){
		return getProperty(key, null);
	}
	
	public String getProperty(String key, String defaultValue){
		String value = properties.getProperty(key);
		return (value != null) ? value.trim() : defaultValue;
	}
	
}
