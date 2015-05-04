package y.q.wifisend.Base;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import y.q.Transfer.Reciver.ApStateBroadcastReciver;

/**
 * Created by Cfun on 2015/5/1.
 */
public abstract class BaseReciver extends BroadcastReceiver
{
	protected String INTENT_FILTER;
	private boolean hasRegister = false;

	public BaseReciver(final String INTENT_FILTER)
	{
		this.INTENT_FILTER = INTENT_FILTER;
	}

	public synchronized void registerSelf()
	{
		int i = 0;
		if (this instanceof ApStateBroadcastReciver)
		{
			i++;
		}
		if (!hasRegister)
			BaseApplication.getInstance().registerReceiver(this, new IntentFilter(INTENT_FILTER));
		hasRegister = true;
	}

	public synchronized void unRegisterSelf()
	{
		int i = 0;
		if (this instanceof ApStateBroadcastReciver)
		{
			i++;
		}
		if (hasRegister)
			BaseApplication.getInstance().unregisterReceiver(this);
		hasRegister = false;
	}

	@Override
	protected void finalize() throws Throwable
	{
		unRegisterSelf();
		super.finalize();
	}
}
