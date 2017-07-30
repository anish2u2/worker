package org.worker.multithread.reflection.proxy;

import java.lang.reflect.Method;

import org.factory.design.contracts.InitFactory;
import org.worker.multithread.contracts.Interceptor;
import org.worker.multithread.contracts.Proxy;
import org.worker.multithread.reflection.delegator.WorkerInterceptor;
import org.worker.multithread.reflection.interceptors.TaskInvocationInterceptor;

/*
 * 
 * @author Anish Singh 
 * 
 * This class will create proxy object using CGLIB and also be used for Intercept method invocation.
 * 
 */

public class ProxyFactory implements Proxy, InitFactory {

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
		System.out.println("Checking in Proxy factory..");
		if (!checkIsThisFactoryApplicable(clazz))
			return null;
		System.out.println("Creating Object..");
		return CGlibProxyFactory.createProxyObject(clazz, WorkerInterceptor.getMethodInterceptorInstance());
	}

	private boolean checkIsThisFactoryApplicable(Class<?> clazz) {
		for (Method method : clazz.getMethods()) {
			for (Interceptor interceptor : WorkerInterceptor.getMethodInterceptorInstance()
					.getListOfRegisteredInterceptors()) {
				if (interceptor.isAnnotationPresent(method))
					return true;
			}
		}
		return false;
	}

	public void addMethodInterceptor(Interceptor interceptor) {
		WorkerInterceptor.getMethodInterceptorInstance().addInterceptor(interceptor);
	}

	public org.factory.design.contracts.patterns.creational.Proxy init() {
		final ProxyFactory proxyFactory = (ProxyFactory) ProxyFactory.getInstance();
		proxyFactory.addMethodInterceptor(new TaskInvocationInterceptor());
		return new org.factory.design.contracts.patterns.creational.Proxy() {

			public Object getProxyBean(Class<?> clazz) {

				return proxyFactory.getBean(clazz);
			}
		};
	}

	public void destroy() {
		proxy = null;

	}

}
