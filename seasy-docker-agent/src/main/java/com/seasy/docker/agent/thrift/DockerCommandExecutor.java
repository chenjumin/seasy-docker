package com.seasy.docker.agent.thrift;

import org.apache.thrift.TException;

import com.seasy.docker.agent.CommandRunner;
import com.seasy.docker.common.thrift.api.CommandExecutor;
import com.seasy.docker.common.thrift.common.ServiceAnnotation;

@ServiceAnnotation(serviceClass=CommandExecutor.class)
public class DockerCommandExecutor implements CommandExecutor.Iface{
	@Override
	public String execute(String requestMethod, String requestParams, String command) throws TException {
		String commandResult = CommandRunner.execute(requestMethod, requestParams, command);
		return commandResult;
	}
}
