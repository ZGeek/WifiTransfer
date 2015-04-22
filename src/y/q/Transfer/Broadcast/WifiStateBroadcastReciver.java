package y.q.Transfer.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import y.q.wifisend.Base.BaseApplication;

/**
 * Created by CFun on 2015/4/22.
 */
public class WifiStateBroadcastReciver extends BroadcastReceiver
{
	public static String ACTION = ApStateBroadcastReciver.class.getName();
	public static final int WIFI_STARTING = 0;
	public static final int WIFI_STARTED = 1;
	public static final int WIFI_CLOSING = 2;
	public static final int WIFI_CLOSED = 3;

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
