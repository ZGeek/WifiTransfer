package y.q.Transfer.Tools;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import y.q.Transfer.Broadcast.ApStateBroadcastReciver;
import y.q.wifisend.Base.BaseApplication;


import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Administrator on 2014/11/25.
 */
public class WifiApHelper
{
	private WifiManager wifiManager = null;
	private WifiHelper wifihelper = null;
	private ApClosedCheckTimer closedCheckTimer;
	private ApOpenedCheckTimer openedCheckTimer;

	public WifiApHelper()
	{
		wifiManager = (WifiManager) BaseApplication.getInstance().getSystemService(Context.WIFI_SERVICE);
		wifihelper = new WifiHelper();
	}
	public WifiApHelper(WifiHelper wifihelper)
	{
		wifiManager = (WifiManager) BaseApplication.getInstance().getSystemService(Context.WIFI_SERVICE);
		this.wifihelper = wifihelper;
	}

	protected WifiConfiguration createWifiInfo(String SSID)
	{
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";


		WifiConfiguration tempConfig = this.isExsits(SSID);
		if (tempConfig != null)
		{
			wifiManager.removeNetwork(tempConfig.networkId);
		}

		config.wepKeys[0] = "";
		config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		config.wepTxKeyIndex = 0;
		return config;
	}

	protected WifiConfiguration isExsits(String SSID)
	{
		List<WifiConfiguration> lc = wifiManager.getConfiguredNetworks();
		if (lc != null)
			for (WifiConfiguration w : lc)
			{
				if (w.SSID.equals(SSID))
					return w;
			}
		return null;
	}

	public void openWifiAp(String SSID)
	{
		ApStateBroadcastReciver.sendBroadcast(ApStateBroadcastReciver.WIFI_AP_OPENING);
		try
		{
			wifiManager.getClass().getDeclaredMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class)
					.invoke(wifiManager, createWifiInfo(SSID), true);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		if(openedCheckTimer == null)
			openedCheckTimer = new ApOpenedCheckTimer();
		if(!openedCheckTimer.isRuning())
			openedCheckTimer.start(20, 500);
	}


	public void closeWifiAp()
	{
		if (isApEnabled())
		{
			ApStateBroadcastReciver.sendBroadcast(ApStateBroadcastReciver.WIFI_AP_CLOSING);
			try
			{
				Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
				method.setAccessible(true);

				WifiConfiguration config = (WifiConfiguration) method.invoke(wifiManager);

				Method method2 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
				method2.invoke(wifiManager, config, false);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			if(closedCheckTimer == null)
				closedCheckTimer = new ApClosedCheckTimer();
			if(!closedCheckTimer.isRuning())
				closedCheckTimer.start(20, 500);
		}
	}

	public boolean isApEnabled()
	{
		try
		{
			Method method = wifiManager.getClass().getMethod("isWifiApEnabled");
			method.setAccessible(true);
			return (Boolean) method.invoke(wifiManager);

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}


	private class ApClosedCheckTimer extends TimerCheck
	{
		@Override
		public void doTimerCheckWork()
		{
			if(!isApEnabled())
			{
				ApStateBroadcastReciver.sendBroadcast(ApStateBroadcastReciver.WIFI_AP_CLOSED);
				exit();
			}
		}

		@Override
		public void doTimeOutWork()
		{
			ApStateBroadcastReciver.sendBroadcast(ApStateBroadcastReciver.WIFI_AP_Operation_TimeOut);
			exit();
		}
	}

	private class ApOpenedCheckTimer extends TimerCheck
	{
		@Override
		public void doTimerCheckWork()
		{
			if(isApEnabled())
			{
				ApStateBroadcastReciver.sendBroadcast(ApStateBroadcastReciver.WIFI_AP_OPENED);
				exit();
			}
		}

		@Override
		public void doTimeOutWork()
		{
			ApStateBroadcastReciver.sendBroadcast(ApStateBroadcastReciver.WIFI_AP_Operation_TimeOut);
			exit();
		}
	}

}
