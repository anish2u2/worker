package org.worker.multithread.contracts;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodProxy;

public interface Interceptor {

	public Class<?> getAnnotatedClassToBeApplied();

	/*
	 * This method is used for intercepting method calls based on annotation and interceptors registered in the frameworks.
	 * 
	 */

	public Object intercept(Object object, Method method, Object[] arguments, MethodProxy proxyMethod) throws Throwable;

	public boolean isAnnotationPresent(Method method);
}
