package y.q.wifisend.Tools.wifi.Helper;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;


import java.io.*;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Administrator on 2014/11/25.
 */
public class WifiApHelper
{
	private WifiManager wifiManager = null;
	private WifiHelper wifihelper = null;

	public WifiApHelper(Context context)
	{
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		wifihelper = new WifiHelper(context);
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

	public void setApEnabled(boolean statu, String SSID)
	{
		wifihelper.waitWifiEnabled(false, true);
		try
		{
			wifiManager.getClass().getDeclaredMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class)
					.invoke(wifiManager, createWifiInfo(SSID), statu);
		} catch (Exception e)
		{
		}
	}

	public void waitApEnabled(boolean statu, String SSID)
	{
		setApEnabled(statu, SSID);
		while (true)
		{
			if (isApEnabled() == statu)
				break;
			else
				try
				{
					Thread.sleep(500);
				} catch (InterruptedException e)
				{
				}
		}
	}

	private boolean isApEnabled()
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
}
