package com.seasy.docker.common.config;

public class SSLConfig {
	private int sessionTimeoutSeconds = 0;
	
	private boolean enabled = true;
	private String protocol = "TLSv1.2";
	private String keystoreType = "PKCS12"; //PKCS12, JKS
	
	private String serverCer = "certificate/server.p12";
	private String serverCerPassword = "123456";
	
	private String trustCer = "certificate/ca.p12";
	private String trustCerPassword = "123456";
	
	private SSLConfig(){
		
	}
	
	public int getSessionTimeoutSeconds() {
		return sessionTimeoutSeconds;
	}
	
	public void setSessionTimeoutSeconds(int sessionTimeoutSeconds) {
		this.sessionTimeoutSeconds = sessionTimeoutSeconds;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public String getProtocol() {
		return protocol;
	}
	
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	public String getKeystoreType() {
		return keystoreType;
	}
	
	public void setKeystoreType(String keystoreType) {
		this.keystoreType = keystoreType;
	}
	
	public String getServerCer() {
		return serverCer;
	}
	
	public void setServerCer(String serverCer) {
		this.serverCer = serverCer;
	}
	
	public String getServerCerPassword() {
		return serverCerPassword;
	}
	
	public void setServerCerPassword(String serverCerPassword) {
		this.serverCerPassword = serverCerPassword;
	}
	
	public String getTrustCer() {
		return trustCer;
	}
	
	public void setTrustCer(String trustCer) {
		this.trustCer = trustCer;
	}
	
	public String getTrustCerPassword() {
		return trustCerPassword;
	}
	
	public void setTrustCerPassword(String trustCerPassword) {
		this.trustCerPassword = trustCerPassword;
	}
	
	public static class Builder{
		private SSLConfig config;
		
		public Builder(){
			config = new SSLConfig();
		}
		
		public SSLConfig build(){
			return config;
		}
		
		public Builder setSessionTimeoutSeconds(int sessionTimeoutSeconds) {
			config.setSessionTimeoutSeconds(sessionTimeoutSeconds);
			return this;
		}
		
		public Builder setEnabled(boolean enabled) {
			config.setEnabled(enabled);
			return this;
		}
		
		public Builder setProtocol(String protocol) {
			config.setProtocol(protocol);
			return this;
		}
		
		public Builder setKeystoreType(String keystoreType) {
			config.setKeystoreType(keystoreType);
			return this;
		}
		
		public Builder setServerCer(String serverCer) {
			config.setServerCer(serverCer);
			return this;
		}
		
		public Builder setServerCerPassword(String serverCerPassword) {
			config.setServerCerPassword(serverCerPassword);
			return this;
		}
		
		public Builder setTrustCer(String trustCer) {
			config.setTrustCer(trustCer);
			return this;
		}
		
		public Builder setTrustCerPassword(String trustCerPassword) {
			config.setTrustCerPassword(trustCerPassword);
			return this;
		}
	}
	
}
