package org.worker.multithread.reflection.interceptors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.worker.multithread.contracts.Interceptor;

import net.sf.cglib.proxy.MethodProxy;

public class WorkerInterceptor implements Interceptor {

	private final List<Interceptor> interceptors = new LinkedList<Interceptor>();

	private static WorkerInterceptor interceptor;

	private WorkerInterceptor() {

	}

	public static Interceptor getMethodInterceptorInstance() {
		if (interceptor == null) {
			interceptor = new WorkerInterceptor();
		}
		return interceptor;
	}

	public Object intercept(Object arg0, Method method, Object[] arg2, MethodProxy arg3) throws Throwable {
		Annotation[] annotations = method.getDeclaredAnnotations();
		for (Annotation annotation : annotations) {
			for (Interceptor interceptor : interceptors) {
				if (annotation.annotationType().isAssignableFrom(interceptor.getAnnotatedClassToBeApplied())) {
					return interceptor.intercept(arg0, method, arg2, arg3);
				}
			}
		}
		return arg3.invokeSuper(arg0, arg2);
	}

	public Class<?> getAnnotatedClassToBeApplied() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addInterceptor(Interceptor interceptor) {
		this.interceptors.add(interceptor);
	}

}
