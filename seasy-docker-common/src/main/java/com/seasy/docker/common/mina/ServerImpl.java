package com.seasy.docker.common.mina;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
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

import com.seasy.docker.common.SeasyLoggerFactory;
import com.seasy.docker.common.mina.config.ServerConfig;
import com.seasy.docker.common.mina.core.Server;
import com.seasy.docker.common.mina.defaultimpl.DefaultCodecFactory;
import com.seasy.docker.common.mina.defaultimpl.DefaultKeepAliveMessageFactory;
import com.seasy.docker.common.mina.defaultimpl.DefaultKeepAliveRequestTimeoutHandler;
import com.seasy.docker.common.mina.defaultimpl.DefaultServerChainedHandler;

public class ServerImpl implements Server{
	private static Logger logger = SeasyLoggerFactory.getLogger(ServerImpl.class);
	
	private SocketAcceptor socketAcceptor = null;

	private ProtocolCodecFactory protocolCodecFactory;
	private AbstractServerChainedHandler serverChainedHandler;
	private ServerConfig serverConfig;
	private KeepAliveMessageFactory keepAliveMessageFactory;
	
	private ServerImpl(){
		
	}
	
	@Override
	public void start(){
        try {
        	//初始化指定数量的NioProcessor
    		socketAcceptor = new NioSocketAcceptor(serverConfig.getProcessorCount());
    		
    		DefaultIoFilterChainBuilder filterChainBuilder = new DefaultIoFilterChainBuilder();

    		if(serverConfig.getSslConfig().isEnabled()){
				logger.info("server ssl enable");
				SSLContext sslContext = SSLContextFactory.getServerSSLContext(serverConfig.getSslConfig());
				
    			SslFilter sslFilter = new SslFilter(sslContext);
				sslFilter.setEnabledCipherSuites(sslContext.getSupportedSSLParameters().getCipherSuites());
    			sslFilter.setNeedClientAuth(false);
    			filterChainBuilder.addLast("sslFilter", sslFilter);
    		}
    		
            filterChainBuilder.addLast("logger", new LoggingFilter());
    		filterChainBuilder.addLast("codec", new ProtocolCodecFilter(protocolCodecFactory));
    		
    		//将IO线程和工作线程分开，业务逻辑处理使用单独的线程
    		//new ExecutorFilter() 缺省使用OrderedThreadPoolExecutor线程池，对同一session消息是顺序处理的
    		filterChainBuilder.addLast("threadPool", new ExecutorFilter());
    		
    		//心跳
    		if(serverConfig.isHeartbeatEnabled()){
    			logger.debug("server heartbeat enable");
	    		KeepAliveRequestTimeoutHandler heartBeatHandler = new DefaultKeepAliveRequestTimeoutHandler();
	            
	            KeepAliveFilter heartBeat = new KeepAliveFilter(keepAliveMessageFactory, IdleStatus.BOTH_IDLE, heartBeatHandler);
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
		
		public ServerImpl build(){
			if(server.serverConfig == null){
				throw new RuntimeException("Config must be set");
			}
			
			server.serverChainedHandler.setConfig(server.serverConfig);

			if(server.protocolCodecFactory == null){
				server.protocolCodecFactory = new DefaultCodecFactory();
			}
			
			if(server.serverChainedHandler == null){
				server.serverChainedHandler = new DefaultServerChainedHandler();
			}
			
			if(server.serverConfig.isHeartbeatEnabled()){
				if(server.keepAliveMessageFactory == null){
					server.keepAliveMessageFactory = new DefaultKeepAliveMessageFactory();
				}
			}
			
			return server;
		}
		
		//通讯协议工厂
		public Builder setProtocolCodecFactory(ProtocolCodecFactory protocolCodecFactory){
			server.protocolCodecFactory = protocolCodecFactory;
			return this;
		}

		//Handler链
		public Builder setHandler(AbstractServerChainedHandler serverChainedHandler){
			server.serverChainedHandler = serverChainedHandler;
			return this;
		}

		//配置对象
		public Builder setConfig(ServerConfig config){
			server.serverConfig = config;
			return this;
		}
		
		//心跳消息工厂
		public Builder setKeepAliveMessageFactory(KeepAliveMessageFactory keepAliveMessageFactory){
			server.keepAliveMessageFactory = keepAliveMessageFactory;
			return this;
		}
	}
	
}
