package org.worker.multithread.contracts;

import net.sf.cglib.proxy.MethodInterceptor;

public interface Interceptor extends MethodInterceptor {

	public Class<?> getAnnotatedClassToBeApplied();

	public void addInterceptor(Interceptor interceptor);
}
