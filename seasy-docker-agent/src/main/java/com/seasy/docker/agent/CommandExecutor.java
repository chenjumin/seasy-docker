package com.seasy.docker.agent;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.slf4j.Logger;

import com.seasy.docker.common.SeasyLoggerFactory;
import com.seasy.docker.common.utils.StringUtil;

/**
 * 启动容器的命令：
 *     docker run -d --rm --name seasy-docker-agent -v /var/run/docker.sock:/var/run/docker.sock 
 *         192.168.134.139/seasy/seasy-docker-agent:0.0.1
 */
public class CommandExecutor {
	private static Logger logger = SeasyLoggerFactory.getLogger(CommandExecutor.class);
	
	private static final String CMD_BASE = "curl -s --unix-socket /var/run/docker.sock";
	private static final String SERVER_URL_PREFIX = " http://localhost";
	private static final String METHOD_GET = " GET";

	public static String execute(String commandURL){
		return execute(METHOD_GET, "", commandURL);
	}

	public static String execute(String requestParams, String commandURL){
		return execute(METHOD_GET, requestParams, commandURL);
	}
	
	public static String execute(String requestMethod, String requestParams, String commandURL){
		StringBuilder sb = new StringBuilder();
		try{
			if(StringUtil.isEmpty(requestMethod)){
				requestMethod = METHOD_GET;
			}
			
			requestParams = StringUtil.trimToEmpty(requestParams);
			
			String cmd = CMD_BASE + " -X " + requestMethod + requestParams + SERVER_URL_PREFIX + commandURL;
			logger.info(cmd);
			
			String[] cmdArray = new String[]{"/bin/sh", "-c", cmd};
			
			Process p = Runtime.getRuntime().exec(cmdArray);
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			String line = null;
			while((line=reader.readLine()) != null){
				sb.append(line + "\n");
			}
			
			if(logger.isDebugEnabled()){
				logger.debug(sb.toString());
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return sb.toString();
	}
}
