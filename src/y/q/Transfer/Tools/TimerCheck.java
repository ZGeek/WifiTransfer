package y.q.Transfer.Tools;


public abstract class TimerCheck
{
	private int mCount = 0;
	private int mTimeOutCount = 1;
	private int mSleepTime = 1000; // 1s
	private boolean mExitFlag = false;
	private boolean isRuning = false;
	private Thread mThread = null;

	/**
	 * Do not process UI work in this.
	 */
	public abstract void doTimerCheckWork();

	public abstract void doTimeOutWork();

	public TimerCheck()
	{
		mThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				isRuning = true;
				while (!mExitFlag)
				{
					mCount++;
					if (mCount < mTimeOutCount)
					{
						doTimerCheckWork();
						try
						{
							mThread.sleep(mSleepTime);
						} catch (InterruptedException e)
						{
							e.printStackTrace();
							exit();
						}
					} else
					{
						doTimeOutWork();
					}
				}
				isRuning = false;
			}
		});
	}

	/**
	 * start
	 *
	 * @param timeOutCount     How many times will check?
	 * @param sleepTime ms, Every check sleep time.
	 */
	public void start(int timeOutCount, int sleepTime)
	{
		mTimeOutCount = timeOutCount;
		mSleepTime = sleepTime;

		mThread.start();
	}

	public void exit()
	{
		mExitFlag = true;
	}

	public boolean isRuning()
	{
		return isRuning;
	}

}
