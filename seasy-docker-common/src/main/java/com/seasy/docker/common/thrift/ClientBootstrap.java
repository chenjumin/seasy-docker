package com.seasy.docker.common.thrift;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;

import com.seasy.docker.common.SeasyLoggerFactory;

public class ClientBootstrap{
	private static final Logger logger = SeasyLoggerFactory.getLogger(ClientBootstrap.class);
	private String host;
	private int port;
	private TTransport transport;
	
	public ClientBootstrap(String host, int port){
		this.host = host;
		this.port = port;
	}
	
	/**
	 * 获取ServiceClient实例对象
	 */
	@SuppressWarnings("unchecked")
	public <T> T getServiceClient(Class<T> serviceClientClass){
		try{
			String serviceClientClassName = serviceClientClass.getName();
			if(!serviceClientClassName.endsWith("$Client")){
				throw new IllegalArgumentException("serviceClientClass must be $Client class");
			}
			
			String serviceName = serviceClientClassName.replace("$Client", "");
			serviceName = serviceName.substring(serviceName.lastIndexOf(".")+1);
			
			TProtocol protocol = new TCompactProtocol(transport);
			TMultiplexedProtocol multiplexedProtocol = new TMultiplexedProtocol(protocol, serviceName);
	        
			Class[] classes = new Class[]{TProtocol.class};
	        Object serviceClientInstanceObject = serviceClientClass.getConstructor(classes).newInstance(multiplexedProtocol);
	        
	        return (T)serviceClientInstanceObject;
	        
		}catch(Exception ex){
			logger.error("Failed to get ServiceClient", ex);
		}
		return null;
	}
	
	public void open() throws Exception {
		transport = new TFramedTransport(new TSocket(host, port));
		transport.open();
	}
    
	public void destroy(){
    	if(transport != null){
    		transport.close();
    	}
    }
	
}
