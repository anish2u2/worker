package org.worker.multithread.reflection.interceptors;

import java.lang.reflect.Method;

import org.worker.multithread.annotation.Task;
import org.worker.multithread.annotation.Task.Type;
import org.worker.multithread.contracts.Interceptor;
import org.worker.multithread.contracts.ThreadUtility;
import org.worker.multithread.contracts.Work;
import org.worker.multithread.thread.WorkerThread;
import org.worker.multithread.thread.utility.ThreadUtilityFactory;

import net.sf.cglib.proxy.MethodProxy;

/*
 * @author Anish Singh
 * 
 * This class is responsible for handling task execution in another thread.
 * It will invoke the method which is used by @Task annotation in separate thread. 
 * 
 */

public class TaskInvocationInterceptor implements Interceptor {

	private final static String TASK_NAME = "TASK-NAME";
	private final static String IS_SYNCY_TASK = "IS_SYNCY_TASK";

	public Object intercept(final Object object, Method method, final Object[] arguments, final MethodProxy proxyMethod)
			throws Throwable {
		final ThreadUtility threadUtility = ThreadUtilityFactory.getInstance();
		readAnnotationProps(method);
		(!threadUtility.get(TASK_NAME).equals("NULL") ? WorkerThread.getWorker((String) threadUtility.get(TASK_NAME))
				: WorkerThread.getWorker()).startWorking(new Work() {

					public void doWork() throws Throwable {
						Object result = null;
						if ((Boolean) threadUtility.get(IS_SYNCY_TASK)) {
							synchronized (object) {
								result = proxyMethod.invokeSuper(object, arguments);
								object.notifyAll();
							}
						} else {
							result = proxyMethod.invokeSuper(object, arguments);
						}
						threadUtility.add("EXE-RESULT", result);
					}
				});
		return threadUtility.get("EXE-RESULT");
	}

	/* this method will return annotation type of class */
	public Class<?> getAnnotatedClassToBeApplied() {
		return Task.class;
	}

	private void readAnnotationProps(Method method) {
		Task taskAnnotation = method.getAnnotation(Task.class);
		if (taskAnnotation != null) {
			ThreadUtility threadUtility = ThreadUtilityFactory.getInstance();
			threadUtility.add(TASK_NAME, taskAnnotation.taskName());
			threadUtility.add(IS_SYNCY_TASK, taskAnnotation.type().equals(Type.SYNCHRONOUS));
		}

	}

	public boolean isAnnotationPresent(Method method) {
		return method.isAnnotationPresent(Task.class);
	}
}
