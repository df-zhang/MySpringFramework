package df.learn.MySpringFramework.commons.utils;

public class SecondClock {
	private volatile long now = 0;// 当前时间
	private static final SecondClock clock = new SecondClock();

	public static SecondClock getInstance() {
		return clock;
	}

	public static long getNow() {
		return getInstance().now();
	}

	private SecondClock() {
		this.now = System.currentTimeMillis() ;
		start();
	}

	private void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					now = System.currentTimeMillis();
				}
			}
		}).start();
	}

	public long now() {
		return now;
	}
}
