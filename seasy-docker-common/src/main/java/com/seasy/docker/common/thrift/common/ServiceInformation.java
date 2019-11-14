package com.seasy.docker.common.thrift.common;

/**
 * 服务信息
 */
public class ServiceInformation {
	private String serviceName;
	private Class<?> serviceInterfaceClass;
	private Object serviceInstance;
	
	public String getServiceName() {
		return serviceName;
	}
	
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public Class<?> getServiceInterfaceClass() {
		return serviceInterfaceClass;
	}
	
	public void setServiceInterfaceClass(Class<?> serviceInterfaceClass) {
		this.serviceInterfaceClass = serviceInterfaceClass;
	}
	
	public Object getServiceInstance() {
		return serviceInstance;
	}
	
	public void setServiceInstance(Object serviceInstance) {
		this.serviceInstance = serviceInstance;
	}
	
}
