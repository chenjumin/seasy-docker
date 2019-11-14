package com.seasy.docker.common.thrift;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Optional;

import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.ServerContext;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServerEventHandler;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;

import com.seasy.docker.common.SeasyLoggerFactory;
import com.seasy.docker.common.thrift.common.ServerConfig;
import com.seasy.docker.common.thrift.common.ServiceInformation;
import com.seasy.docker.common.thrift.loader.ServiceLoader;
import com.seasy.docker.common.utils.StringUtil;

public class ServerBootstrap {
	private static final Logger logger = SeasyLoggerFactory.getLogger(ServerBootstrap.class);
	
	private ServerConfig config = new ServerConfig();
	private List<ServiceInformation> serviceInfoList;
	
	private TMultiplexedProcessor multiplexedProcessor;
	private TNonblockingServerSocket serverSocket = null;
	private TServer tserver = null;
	
	public ServerBootstrap(ServerConfig config){
		this.config = config;
	}

	/**
	 * 非阻塞式的多线程服务模型
	 */
	public void start() throws Exception {
		logger.info("start ServerBootstrap...");
		loadBusinessService();
		buildMultiplexedProcessor();
		startServer();
	}
	
	/**
	 * 加载业务服务实现类
	 */
	private void loadBusinessService(){
		if(StringUtil.isEmpty(config.getServiceBasePackage())){
			throw new RuntimeException("ServiceBasePackage is null");
		}
		
		ServiceLoader loader = new ServiceLoader();
		loader.setBasePackages(config.getServiceBasePackage());
		serviceInfoList = loader.load();
	}
	
	/**
	 * 构造MultiplexedProcessor对象
	 */
	private void buildMultiplexedProcessor(){
		multiplexedProcessor = new TMultiplexedProcessor();
		
		Optional.ofNullable(serviceInfoList).get().forEach(info -> {
			TProcessor serviceProcessor = createServiceProcessor(info);
			
			if(serviceProcessor != null){
				multiplexedProcessor.registerProcessor(info.getServiceName(), serviceProcessor);
				logger.info("{} registered!", info.getServiceName());
			}
		});
	}
	
	/**
	 * 创建服务的TProcessor实例对象
	 */
    private TProcessor createServiceProcessor(ServiceInformation info){
    	try{
	        String processorClassName = info.getServiceInterfaceClass().getName() + "$Processor";
	        String ifaceClassName = info.getServiceInterfaceClass().getName() + "$Iface";
	        
	        Class<?> processorClass = Class.forName(processorClassName);
	        Class<?> ifaceClass = Class.forName(ifaceClassName);
	        
	        Constructor<?> constructor = processorClass.getDeclaredConstructor(new Class[]{ifaceClass});
	        TProcessor processor = (TProcessor) constructor.newInstance(new Object[]{info.getServiceInstance()});
	        
	        return processor;
	        
    	}catch(Exception ex){
    		logger.error("create service processor error", ex);
    	}
    	return null;
    }
    
    /**
     * 启动Thrift Server
     */
    private void startServer()throws Exception{
		serverSocket = new TNonblockingServerSocket(config.getPort());
		
		TThreadedSelectorServer.Args tArgs = new TThreadedSelectorServer.Args(serverSocket);
        tArgs.processor(multiplexedProcessor);
        tArgs.transportFactory(new TFramedTransport.Factory());
        tArgs.protocolFactory(new TCompactProtocol.Factory());
		tArgs.selectorThreads(config.getSelectorThreads());
		tArgs.workerThreads(config.getWorkerThreads());

        tserver = new TThreadedSelectorServer(tArgs);
        tserver.setServerEventHandler(new DefaultServerEventHandler());
        tserver.serve();
    }

	/**
	 * 停止thrift Server
	 */
	public void destroy(){
		if(serverSocket != null){
			serverSocket.close();
			serverSocket = null;
		}
		
		if(tserver != null){
			tserver.stop();
			tserver = null;
		}
	}
	
	class DefaultServerEventHandler implements TServerEventHandler{
		@Override
		public void preServe() {
	        logger.info("start server at port " + config.getPort());
		}
		
		@Override
		public ServerContext createContext(TProtocol input, TProtocol output) {
			logger.debug("创建客户端连接");
			return null;
		}
		
		@Override
		public void processContext(ServerContext serverContext, TTransport inputTransport, TTransport outputTransport) {
			//System.out.println("processContext");
		}
		
		@Override
		public void deleteContext(ServerContext serverContext, TProtocol input, TProtocol output) {
			logger.debug("删除客户端连接");
		}
	}
	
}
