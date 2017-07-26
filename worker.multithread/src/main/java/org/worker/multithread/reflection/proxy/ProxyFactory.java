package org.worker.multithread.reflection.proxy;

import org.worker.multithread.contracts.Interceptor;
import org.worker.multithread.contracts.Proxy;
import org.worker.multithread.reflection.interceptors.WorkerInterceptor;

/*
 * 
 * @author Anish Singh 
 * 
 * This class will create proxy object using CGLIB and also be used for Intercept method invocation.
 * 
 */

public class ProxyFactory implements Proxy {

	private static Proxy proxy;

	public static Proxy getInstance() {
		if (proxy == null) {
			proxy = new ProxyFactory();
		}
		return proxy;
	}

	private ProxyFactory() {

	}

	public Object getBean(Class<?> clazz) {

		return CGlibProxyFactory.createProxyObject(clazz, WorkerInterceptor.getMethodInterceptorInstance());
	}

	public void addMethodInterceptor(Interceptor interceptor) {
		WorkerInterceptor.getMethodInterceptorInstance().addInterceptor(interceptor);
	}

}
