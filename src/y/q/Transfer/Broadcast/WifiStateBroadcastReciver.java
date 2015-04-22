package y.q.Transfer.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import y.q.wifisend.Base.BaseApplication;

/**
 * Created by CFun on 2015/4/22.
 */
public class WifiStateBroadcastReciver extends BroadcastReceiver
{
	private OnWIfiStateChangedListener onWIfiStateChangedListener;

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
		if (null != parcelableExtra) {
			NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
			if(networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI && onWIfiStateChangedListener!= null)
				onWIfiStateChangedListener.onWifiStateChanged(networkInfo);
		}
	}
	public  void resignBroadcastSelf()
	{
		BaseApplication.getInstance().registerReceiver(this, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
	}

	public void setOnWIfiStateChangedListener(OnWIfiStateChangedListener onWIfiStateChangedListener)
	{
		this.onWIfiStateChangedListener = onWIfiStateChangedListener;
	}

	public interface OnWIfiStateChangedListener
	{
		void onWifiStateChanged(NetworkInfo info);
	}
}
