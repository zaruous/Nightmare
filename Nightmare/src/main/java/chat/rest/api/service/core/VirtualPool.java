package chat.rest.api.service.core;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

public final class VirtualPool extends Thread {

	private static VirtualPool pool;
	ThreadFactory factory = Thread.ofVirtual().factory();
	ExecutorService newVirtualThreadPerTaskExecutor;

	public synchronized static VirtualPool newInstance() {
		if (pool == null)
			pool = new VirtualPool();
		return pool;
	}

	private VirtualPool() {
		newVirtualThreadPerTaskExecutor = Executors.newThreadPerTaskExecutor(factory);
		Runtime.getRuntime().addShutdownHook(this);
	}

	/**
	 * 
	 */
	public void shutdown() {
		newVirtualThreadPerTaskExecutor.shutdown();
	}
	
	public void execute(Runnable r) {
		newVirtualThreadPerTaskExecutor.submit(r);
	}

	void execute(Thread r) {
		newVirtualThreadPerTaskExecutor.submit(r);
	}
	
	public Future<String> execute(Callable<String> c) {
		return newVirtualThreadPerTaskExecutor.submit(c);
	}
	public <T> List<Future<T>> invokeAll(Collection<Callable<T>> c) throws InterruptedException {
		return newVirtualThreadPerTaskExecutor.invokeAll(c);
	}
	
	@Override
	public void run() {
		if (newVirtualThreadPerTaskExecutor != null)
			newVirtualThreadPerTaskExecutor.shutdown();
	}

}
