package com.seasy.docker.common.server;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;

import com.seasy.docker.common.SSLContextFactory;
import com.seasy.docker.common.SeasyLoggerFactory;
import com.seasy.docker.common.config.ServerConfig;
import com.seasy.docker.common.core.Server;
import com.seasy.docker.common.keepalive.KeepAliveMessageFactoryImpl;
import com.seasy.docker.common.keepalive.KeepAliveRequestTimeoutHandlerImpl;
import com.seasy.docker.common.protocol.ByteArrayCodecFactory;

public class ServerImpl implements Server{
	private static Logger logger = SeasyLoggerFactory.getLogger(ServerImpl.class);
	
	//最大Porcessor线程数
	private static final int PROCESSOR_COUNT = Runtime.getRuntime().availableProcessors();
	
	private SocketAcceptor socketAcceptor = null;
	private AbstractServerChainedHandler serverChainedHandler;
	private ServerConfig serverConfig;
	
	private ServerImpl(){
		
	}
	
	@Override
	public void start(){
        try {
        	//初始化指定数量的NioProcessor
    		socketAcceptor = new NioSocketAcceptor(PROCESSOR_COUNT);
    		
    		DefaultIoFilterChainBuilder filterChainBuilder = new DefaultIoFilterChainBuilder();

    		if(serverConfig.getSslConfig().isEnabled()){
				logger.info("server ssl enable");
    			SslFilter sslFilter = new SslFilter(SSLContextFactory.getServerSSLContext(serverConfig.getSslConfig()));
    			sslFilter.setNeedClientAuth(false);
    			filterChainBuilder.addLast("sslFilter", sslFilter);
    		}
    		
            filterChainBuilder.addLast("logger", new LoggingFilter());
    		filterChainBuilder.addLast("codec", new ProtocolCodecFilter(new ByteArrayCodecFactory()));
    		
    		//将IO线程和工作线程分开，业务逻辑处理使用单独的线程
    		//new ExecutorFilter() 缺省使用OrderedThreadPoolExecutor线程池，对同一session消息是顺序处理的
    		filterChainBuilder.addLast("threadPool", new ExecutorFilter());
    		
    		//心跳
    		if(serverConfig.isHeartbeatEnabled()){
    			logger.debug("server heartbeat enable");
    			
	    		KeepAliveMessageFactory heartBeatFactory = new KeepAliveMessageFactoryImpl(serverConfig);
	    		KeepAliveRequestTimeoutHandler heartBeatHandler = new KeepAliveRequestTimeoutHandlerImpl();
	            
	            KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory, IdleStatus.READER_IDLE, heartBeatHandler);
	            heartBeat.setForwardEvent(false); //是否回发
	            heartBeat.setRequestInterval(serverConfig.getRequestIntervalSeconds()); //发送频率
	            heartBeat.setRequestTimeout(serverConfig.getRequestTimeoutSeconds()); //发送超时
	            
	            filterChainBuilder.addLast("heartbeat", heartBeat);
    		}
    		
    		socketAcceptor.setFilterChainBuilder(filterChainBuilder);
            socketAcceptor.setHandler(serverChainedHandler);
            
			socketAcceptor.bind(new InetSocketAddress(serverConfig.getPort()));
			logger.info("Server has been started, listener port is " + serverConfig.getPort());
			
		} catch (Exception ex) {
			logger.error("start error", ex);
			stop();
		}
	}
	
	@Override
	public void stop() {
		serverChainedHandler.removeAllSession();
		
		if(socketAcceptor != null){
			socketAcceptor.dispose();
			socketAcceptor = null;
		}
	}
	
	@Override
	public void restart() {
		try{
			stop();
			
			logger.info("restart server[" + serverConfig.getPort() + "] after 10 seconds...");
			TimeUnit.SECONDS.sleep(10);
			
			start();
			
		}catch(Exception ex){
			logger.error("restart error", ex);
		}
	}
	
	public static class Builder{
		private ServerImpl server;
		
		public Builder(){
			server = new ServerImpl();
		}
		
		public Builder setHandler(AbstractServerChainedHandler serverChainedHandler){
			server.serverChainedHandler = serverChainedHandler;
			return this;
		}
		
		public Builder setConfig(ServerConfig config){
			server.serverConfig = config;
			return this;
		}
		
		public ServerImpl build(){
			server.serverChainedHandler.setConfig(server.serverConfig);
			return server;
		}
	}
	
}
