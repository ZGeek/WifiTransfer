package y.q.wifisend.Reciver;

import android.content.Context;
import android.content.Intent;
import y.q.wifisend.Base.BaseApplication;
import y.q.wifisend.Base.BaseReciver;

/**
 * Created by Cfun on 2015/5/3.
 */
public class RecvingPrepareStateChangReciver extends BaseReciver
{
	private static final String INTENT_FILTER = RecvingPrepareStateChangReciver.class.getName();
	private OnRecivePrepareStateChangedListener onRecivePrepareStateChangedListener;
	public static final int STATE_PREPARING =0;
	public static final int STATE_FINISH =1;
	public static final int STATE_ERROR = -1;

	public RecvingPrepareStateChangReciver()
	{
		super(INTENT_FILTER);
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		if(INTENT_FILTER.equals(intent.getAction()))
		{
			if(onRecivePrepareStateChangedListener != null)
			{
				int state = intent.getIntExtra("state", 0);
				onRecivePrepareStateChangedListener.onRecivePrepareStateChanged(state);
			}

		}
	}
	public static void sendBroadcast(int state)
	{
		Intent intent = new Intent(INTENT_FILTER);
		intent.putExtra("state", state);
		BaseApplication.getInstance().sendBroadcast(intent);
	}

	public void setOnRecivePrepareStateChangedListener(OnRecivePrepareStateChangedListener onRecivePrepareStateChangedListener)
	{
		this.onRecivePrepareStateChangedListener = onRecivePrepareStateChangedListener;
	}

	public interface OnRecivePrepareStateChangedListener
	{
		void onRecivePrepareStateChanged(int state);
	}
}
