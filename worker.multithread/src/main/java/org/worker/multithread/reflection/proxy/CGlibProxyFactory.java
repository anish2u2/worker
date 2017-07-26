package org.worker.multithread.reflection.proxy;

import org.worker.multithread.contracts.Interceptor;

import net.sf.cglib.proxy.Enhancer;

public class CGlibProxyFactory {

	public static Object createProxyObject(Class<?> clazz, Interceptor interceptor) {
		return Enhancer.create(clazz, interceptor);
	}

}
