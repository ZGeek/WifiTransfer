package y.q.Transfer.Reciver;

/**
 * Created by Cfun on 2015/5/2.
 */

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import y.q.wifisend.Base.BaseApplication;
import y.q.wifisend.Base.BaseReciver;
import y.q.wifisend.Utils.LogUtil;

/**
 * 系统网络的连接状态发生改变时激发此Reciver
 */
public class ConnectivityChangeReciver extends BaseReciver
{
	ConnectivityManager manager;
	OnWifiConnectivityChangeListener onWifiConnectivityChangeListener;
	OnMobileConnectivityChangeListener onMobileConnectivityChangeListener;

	public ConnectivityChangeReciver()
	{
		super(ConnectivityManager.CONNECTIVITY_ACTION);
		manager = (ConnectivityManager) BaseApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction()))
		{
			NetworkInfo networkInfo = manager.getActiveNetworkInfo();
			LogUtil.i(this, "networkInfo" + networkInfo);
			if(networkInfo == null)
				return;
			if (networkInfo.getType() ==ConnectivityManager.TYPE_WIFI  && onWifiConnectivityChangeListener != null)
			{
				onWifiConnectivityChangeListener.onWifiConnectivityChange(networkInfo);
				return;
			}
			if (networkInfo.getType() ==ConnectivityManager.TYPE_MOBILE  && onMobileConnectivityChangeListener != null)
			{
				onMobileConnectivityChangeListener.onMobileConnectivityChange(networkInfo);
				return;
			}

		}
	}

	public void setOnWifiConnectivityChangeListener(OnWifiConnectivityChangeListener onWifiConnectivityChangeListener)
	{
		this.onWifiConnectivityChangeListener = onWifiConnectivityChangeListener;
	}

	public void setOnMobileConnectivityChangeListener(OnMobileConnectivityChangeListener onMobileConnectivityChangeListener)
	{
		this.onMobileConnectivityChangeListener = onMobileConnectivityChangeListener;
	}

	public interface OnWifiConnectivityChangeListener
	{
		void onWifiConnectivityChange(NetworkInfo info);
	}

	public interface OnMobileConnectivityChangeListener
	{
		void onMobileConnectivityChange(NetworkInfo info);
	}
}
