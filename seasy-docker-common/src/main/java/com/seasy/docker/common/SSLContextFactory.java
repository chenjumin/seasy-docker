package com.seasy.docker.common;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import com.seasy.docker.common.config.SSLConfig;

public class SSLContextFactory {
	/**
	 * 服务端使用的SSLContext
	 */
	public static SSLContext getServerSSLContext(SSLConfig config) throws Exception {
		InputStream inputStream = new FileInputStream(config.getServerCer());
		
		KeyStore ks = KeyStore.getInstance(config.getKeystoreType());  
		ks.load(inputStream, config.getServerCerPassword().toCharArray());
		
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509"); //SunX509
		keyManagerFactory.init(ks, config.getServerCerPassword().toCharArray());
		
		KeyManager[] kms = keyManagerFactory.getKeyManagers();
		
		SSLContext sslContext = SSLContext.getInstance(config.getProtocol());
		sslContext.init(kms, null, null);
		sslContext.getServerSessionContext().setSessionTimeout(config.getSessionTimeoutSeconds());
		
		return sslContext;
	}
	
	/**
	 * 客户端使用的SSLContext
	 */
	public static SSLContext getClientSSLContext(SSLConfig config) throws Exception {
		InputStream inputStream = new FileInputStream(config.getTrustCer());
		
        KeyStore tks = KeyStore.getInstance(config.getKeystoreType());
        tks.load(inputStream, config.getTrustCerPassword().toCharArray());

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509"); //SunX509
        trustManagerFactory.init(tks);
        TrustManager[] tms = trustManagerFactory.getTrustManagers(); 
        
		SSLContext sslContext = SSLContext.getInstance(config.getProtocol());
        sslContext.init(null, tms, null);
        sslContext.getClientSessionContext().setSessionTimeout(config.getSessionTimeoutSeconds());
        
        return sslContext;
	}
	
}
