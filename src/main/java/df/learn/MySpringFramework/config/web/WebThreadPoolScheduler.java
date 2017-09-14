package df.learn.MySpringFramework.config.web;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class WebThreadPoolScheduler {

	private static WebThreadPoolScheduler manager = new WebThreadPoolScheduler();
	private ThreadPoolExecutor pool;

	private WebThreadPoolScheduler() {
		pool = new ThreadPoolExecutor(100, 160, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100), new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public static boolean addTask(Runnable run) {
		manager.pool.execute(run);
		return true;
	}
}