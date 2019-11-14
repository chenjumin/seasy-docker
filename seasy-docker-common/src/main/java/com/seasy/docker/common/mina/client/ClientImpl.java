package com.seasy.docker.common.mina.client;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;

import com.seasy.docker.common.SeasyLoggerFactory;
import com.seasy.docker.common.mina.SSLContextFactory;
import com.seasy.docker.common.mina.config.ClientConfig;
import com.seasy.docker.common.mina.core.Client;
import com.seasy.docker.common.mina.keepalive.KeepAliveMessageFactoryImpl;
import com.seasy.docker.common.mina.keepalive.KeepAliveRequestTimeoutHandlerImpl;
import com.seasy.docker.common.mina.protocol.ByteArrayCodecFactory;

public class ClientImpl<T> implements Client<T> {
	private static Logger logger = SeasyLoggerFactory.getLogger(ClientImpl.class);
	private SocketConnector socketConnector;
	private IoSession session = null;
	private int reconnectedCount = 0; //已重连的次数
	
	private AbstractClientChainedHandler clientChainHandler;
	private ClientConfig clientConfig;
	
	private ClientImpl(){
		
	}
	
	@Override
	public void connect() {
		try{
			socketConnector = new NioSocketConnector();
	        socketConnector.setConnectTimeoutMillis(clientConfig.getConnectTimeoutMills());
	
			DefaultIoFilterChainBuilder filterChainBuilder = new DefaultIoFilterChainBuilder();
			
			//SSL
			if(clientConfig.getSslConfig().isEnabled()){
				logger.info("client ssl enable");
				SSLContext sslContext = SSLContextFactory.getClientSSLContext(clientConfig.getSslConfig());
				
				SslFilter sslFilter = new SslFilter(sslContext);
				sslFilter.setEnabledCipherSuites(sslContext.getSupportedSSLParameters().getCipherSuites());
				sslFilter.setUseClientMode(true);
				filterChainBuilder.addLast("sslFilter", sslFilter);
			}
			
	        filterChainBuilder.addLast("logger", new LoggingFilter());
			filterChainBuilder.addLast("codec", new ProtocolCodecFilter(new ByteArrayCodecFactory()));

    		//心跳
    		if(clientConfig.isHeartbeatEnabled()){
    			logger.debug("client heartbeat enable");
    			
	    		KeepAliveMessageFactory heartBeatFactory = new KeepAliveMessageFactoryImpl(clientConfig);
	            KeepAliveRequestTimeoutHandlerImpl heartBeatHandler = new KeepAliveRequestTimeoutHandlerImpl();
	            
	            KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory, IdleStatus.READER_IDLE, heartBeatHandler);
	            heartBeat.setForwardEvent(false); //是否回发
	            heartBeat.setRequestInterval(clientConfig.getRequestIntervalSeconds()); //发送频率
	            heartBeat.setRequestTimeout(clientConfig.getRequestTimeoutSeconds()); //发送超时
	            
	            filterChainBuilder.addLast("heartbeat", heartBeat);
    		}
	
			socketConnector.setFilterChainBuilder(filterChainBuilder);
	        socketConnector.setHandler(clientChainHandler);
	        
	        doConnect();
	        
		}catch(Exception ex){
			logger.error("connect error", ex);
			disconnect();
		}
	}

	private void doConnect() throws Exception {
		//Connect
		InetSocketAddress addr = new InetSocketAddress(clientConfig.getServerIp(), clientConfig.getPort());
		ConnectFuture future = socketConnector.connect(addr);
		future.awaitUninterruptibly(5000, TimeUnit.MILLISECONDS);
		
		if(future.isConnected()){
			reconnectedCount = 0;
			session = future.getSession();
			logger.info("success connect to: " + session.getRemoteAddress().toString());
			
			if(clientConfig.getListener() != null){
				clientConfig.getListener().onConnectSuccess(session);
			}
			
		}else{
			if(clientConfig.getListener() != null){
				clientConfig.getListener().onConnectFail(future.getException());
			}
			
			reconnect();			
		}
	}
	
	@Override
	public void reconnect(){
		try{
			if(clientConfig.getReconnectCount() == -1 || (clientConfig.getReconnectCount() > 0 && reconnectedCount < clientConfig.getReconnectCount())){
				++reconnectedCount;
	
				TimeUnit.SECONDS.sleep(clientConfig.getReconnectIntervalSeconds());
				
				logger.debug("第" + reconnectedCount + "次尝试重新建立连接...");
				doConnect();
				
			}else{
				disconnect();
			}
			
		}catch(Exception ex){
			logger.error("reconnect error", ex);
			disconnect();
		}
	}
	
	@Override
	public void disconnect() {
		logger.debug("断开连接...");
		
		if(clientConfig.getListener() != null){
			clientConfig.getListener().onDisconnect(socketConnector, session);
		}
		
		if(session != null){
			session.closeNow();
			session = null;
		}
		
		if(socketConnector != null){
			socketConnector.dispose();
			socketConnector = null;
		}
	}
	
	@Override
	public boolean isConnected() {
		return session != null && session.isConnected();
	}
	
	@Override
	public void sendMessage(T message) {
		session.write(message);
	}
	
	public static class Builder<T>{
		private ClientImpl<T> client;
		
		public Builder(){
			client = new ClientImpl<T>();
		}
		
		public Client<T> build(){
			client.clientChainHandler.setConfig(client.clientConfig);
			client.clientChainHandler.setClient(client);
			return client;
		}
		
		public Builder<T> setHandler(AbstractClientChainedHandler handler){
			client.clientChainHandler = handler;
			return this;
		}
		
		public Builder<T> setConfig(ClientConfig config){
			client.clientConfig = config;
			return this;
		}
	}
	
}
