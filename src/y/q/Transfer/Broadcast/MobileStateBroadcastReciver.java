package y.q.Transfer.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import y.q.wifisend.Base.BaseApplication;

/**
 * Created by CFun on 2015/4/22.
 */
public class MobileStateBroadcastReciver extends BroadcastReceiver
{
	public static String ACTION = ApStateBroadcastReciver.class.getName();
	public static final int MOBILE_STARTING = 4;
	public static final int MOBILE_STRATED = 5;
	public static final int MOBILE_CLOSING = 6;
	public static final int MOBILE_CLOSED = 7;

	@Override
	public void onReceive(Context context, Intent intent)
	{

	}
	public static void sendBroadcast(int state)
	{
		Intent intent = new Intent(ACTION);
		intent.putExtra("statu", state);
		BaseApplication.getInstance().sendBroadcast(intent);
	}
}