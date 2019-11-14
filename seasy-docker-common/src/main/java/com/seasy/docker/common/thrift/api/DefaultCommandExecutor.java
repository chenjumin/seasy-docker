package com.seasy.docker.common.thrift.api;

import org.apache.thrift.TException;

import com.seasy.docker.common.thrift.common.ServiceAnnotation;

@ServiceAnnotation(serviceClass=CommandExecutor.class)
public class DefaultCommandExecutor implements CommandExecutor.Iface{
	@Override
	public String execute(String requestMethod, String requestParams, String command) throws TException {
		StringBuffer sb = new StringBuffer();
		sb.append("requestMethod=" +requestMethod);
		sb.append(", requestParams=" + requestParams);
		sb.append(", command=" + command);
		return sb.toString();
	}
}
