package y.q.Transfer.Reciver;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import y.q.wifisend.Base.BaseReciver;

/**
 * Created by CFun on 2015/4/22.
 */

/***
 * WIFI状态改变时系统发出此广播，wiif状态包括以下几种
 * WIFI_STATE_DISABLING
 * WIFI_STATE_DISABLED
 * WIFI_STATE_ENABLING
 * WIFI_STATE_ENABLED
 * WIFI_STATE_UNKNOWN
 */
public class WifiStateBroadcastReciver extends BaseReciver
{
	private OnWIfiStateChangedListener onWIfiStateChangedListener;

	public WifiStateBroadcastReciver()
	{
		super(WifiManager.WIFI_STATE_CHANGED_ACTION);
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		if(WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction()))
		{
			if(onWIfiStateChangedListener != null)
			{
				int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
				onWIfiStateChangedListener.onWifiStateChanged(wifiState);
			}
		}
	}


	public void setOnWIfiStateChangedListener(OnWIfiStateChangedListener onWIfiStateChangedListener)
	{
		this.onWIfiStateChangedListener = onWIfiStateChangedListener;
	}

	public interface OnWIfiStateChangedListener
	{
		void onWifiStateChanged(int wifiSatate);
	}
}
