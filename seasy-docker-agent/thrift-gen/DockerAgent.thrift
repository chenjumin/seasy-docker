namespace java com.seasy.docker.common.thrift.api

service CommandExecutor{
	string execute(1:string requestMethod, 2:string requestParams, 3:string command)
}
