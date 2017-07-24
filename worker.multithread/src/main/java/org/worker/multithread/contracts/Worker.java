package org.worker.multithread.contracts;

public interface Worker {

	public void startWorking(Work work);

	public void stopWork();

	public void takeBreak();

}
