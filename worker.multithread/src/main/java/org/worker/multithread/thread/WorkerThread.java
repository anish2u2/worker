package org.worker.multithread.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.logger.LoggerAPI;
import org.worker.multithread.contracts.ThreadUtility;
import org.worker.multithread.contracts.Work;
import org.worker.multithread.contracts.Worker;

public class WorkerThread extends Thread implements Worker {

	private static final Map<Object, Object> THREAD_RESOURCES = new WeakHashMap<Object, Object>();
	private static int THREAD_COUNT;
	private int threadNumber;
	private Work work;
	private boolean threadWorkCompleted = false;
	private boolean isStopCommanExecuted;

	private WorkerThread() {
		THREAD_COUNT++;
		threadNumber = THREAD_COUNT;
		THREAD_RESOURCES.put(THREAD_COUNT, this);
		LoggerAPI.logInfo("Numer of Thread:" + THREAD_RESOURCES.size());
		startDeamonThreadForMonitoringThreads();
	}

	public static Worker getWorker() {
		Worker worker = findFreeWorkerFromThreadResources();
		if (worker == null) {
			worker = new WorkerThread();
		}
		return worker;
	}

	private static Worker findFreeWorkerFromThreadResources() {
		for (Object key : THREAD_RESOURCES.keySet()) {
			if (((WorkerThread) THREAD_RESOURCES.get(key)).isCurretWorkCompletedByThisWorker()) {
				return (WorkerThread) THREAD_RESOURCES.get(key);
			}
		}
		return null;
	}

	public void startWorking(Work work) {
		this.work = work;
		this.start();
	}

	public void stopWork() {
		isStopCommanExecuted = true;
		releaseResources();
	}

	public void takeBreak() {
		try {
			Thread.sleep(3000);
		} catch (Exception ex) {
			LoggerAPI.logError(ex);
		}
	}

	public boolean isCurretWorkCompletedByThisWorker() {
		ThreadUtility threadUtility = ThreadUtilityFactory.getInstance();
		if (threadUtility.get("isCurrentWorkDone") == null) {
			return false;
		}
		return (Boolean) threadUtility.get("isCurrentWorkDone");
	}

	@Override
	public void run() {
		if (work != null && !isStopCommanExecuted) {
			synchronized (this) {
				work.doWork();
				this.notify();
				threadWorkCompleted = true;
			}
			work = null;
		}
		if (isStopCommanExecuted)
			return;
	}

	private void releaseResources() {
		work = null;
		THREAD_RESOURCES.remove(threadNumber);
		THREAD_RESOURCES.clear();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		for (Object key : THREAD_RESOURCES.keySet()) {
			((Worker) THREAD_RESOURCES.get(key)).stopWork();
		}
		THREAD_RESOURCES.clear();
	}

	private void startDeamonThreadForMonitoringThreads() {
		Thread deamon = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						List<Object> keysToBeremovedFromThreadResources = new ArrayList<Object>();
						for (Object key : THREAD_RESOURCES.keySet()) {
							if (((WorkerThread) THREAD_RESOURCES.get(key)).isThreadFinishedItsWork()) {
								keysToBeremovedFromThreadResources.add(key);
							}
						}
						for (Object key : keysToBeremovedFromThreadResources) {
							THREAD_RESOURCES.remove(key);
						}

						Thread.sleep(10000);
					} catch (Exception ex) {
						LoggerAPI.logError(ex);
					}
				}
			}
		});
		deamon.setDaemon(true);
		deamon.start();
	}

	public boolean isThreadFinishedItsWork() {
		return threadWorkCompleted;
	}
}
