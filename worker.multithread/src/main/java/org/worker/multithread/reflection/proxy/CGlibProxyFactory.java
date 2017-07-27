package org.worker.multithread.reflection.proxy;

import org.worker.multithread.reflection.delegator.WorkerInterceptor;

import net.sf.cglib.proxy.Enhancer;

public class CGlibProxyFactory {

	public static Object createProxyObject(Class<?> clazz, WorkerInterceptor interceptor) {
		return Enhancer.create(clazz, interceptor);
	}

}
