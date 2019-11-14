package com.seasy.docker.common.thrift.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;

import com.seasy.docker.common.SeasyLoggerFactory;
import com.seasy.docker.common.thrift.common.ServiceAnnotation;
import com.seasy.docker.common.thrift.common.ServiceInformation;
import com.seasy.docker.common.utils.StringUtil;

/**
 * 加载thrift服务类
 */
public class ServiceLoader extends AbstractLoader<List<ServiceInformation>> {
    private static final Logger logger = SeasyLoggerFactory.getLogger(ServiceLoader.class);

    @Override
    public List<ServiceInformation> load(){
    	List<ServiceInformation> serviceList = new ArrayList<>();
    	
        if(StringUtil.isNotEmpty(getBasePackages())){
            try {
                String[] packagesArray = getBasePackages().split(";");
                for(String packagePath : packagesArray){
                	serviceList.addAll(loadService(packagePath));
                }
            }catch (Exception ex){
            	serviceList.clear();
                logger.error("load service error", ex);
            }
        }

        return serviceList;
    }
    
    /**
     * 根据包路径加载包含指定注解类的thrift服务类
     * @param packagePath 服务类所在的包路径
     */
    private List<ServiceInformation> loadService(String packagePath) throws Exception {
    	List<ServiceInformation> serviceList = new ArrayList<>();
    	
    	Reflections reflections = new Reflections(packagePath);
    	
    	//查找有指定注解类的服务类
		Set<Class<?>> serviceImplementClassSet = reflections.getTypesAnnotatedWith(ServiceAnnotation.class);
		for(Class<?> serviceImplementClass : serviceImplementClassSet){			
			ServiceAnnotation serviceAnnotation = serviceImplementClass.getAnnotation(ServiceAnnotation.class);

			ServiceInformation info = new ServiceInformation();
			info.setServiceName(serviceAnnotation.serviceClass().getSimpleName());
			info.setServiceInterfaceClass(serviceAnnotation.serviceClass());
			info.setServiceInstance(serviceImplementClass.newInstance());

			serviceList.add(info);
			logger.debug("Service class [" + serviceAnnotation.serviceClass().getName() + "] loaded!");
		}
		
		return serviceList;
    }

}
