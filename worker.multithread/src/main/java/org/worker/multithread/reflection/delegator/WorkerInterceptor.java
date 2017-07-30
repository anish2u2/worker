package org.worker.multithread.reflection.delegator;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.logger.LoggerAPI;
import org.worker.multithread.contracts.Interceptor;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class WorkerInterceptor implements MethodInterceptor {

	private final List<Interceptor> interceptors = new LinkedList<Interceptor>();

	private static MethodInterceptor interceptor;

	private WorkerInterceptor() {
		LoggerAPI.logInfo("Creating Worker Interceptor.");
	}

	public static WorkerInterceptor getMethodInterceptorInstance() {
		if (interceptor == null) {
			interceptor = new WorkerInterceptor();
		}
		return (WorkerInterceptor) interceptor;
	}

	public Object intercept(Object arg0, Method method, Object[] arg2, MethodProxy arg3) throws Throwable {
		for (Interceptor interceptor : interceptors) {
			if (interceptor.isAnnotationPresent(method)) {
				return interceptor.intercept(arg0, method, arg2, arg3);
			}
		}

		return arg3.invokeSuper(arg0, arg2);
	}

	public void addInterceptor(Interceptor interceptor) {
		this.interceptors.add(interceptor);
		LoggerAPI.logInfo("Interceptor:" + interceptor + " registered.");
	}

	public List<Interceptor> getListOfRegisteredInterceptors() {
		return interceptors;
	}

}
