package y.q.Transfer.Tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import y.q.Transfer.Tools.admin.WifiApAdmin;
import y.q.wifisend.Base.BaseApplication;
import y.q.wifisend.Utils.LogUtil;

import java.util.List;

/**
 * Created by Administrator on 2014/11/25.
 */
public class WifiHelper
{
	public static final int TYPE_NO_PASSWD = 0x11;
	public static final int TYPE_WEP = 0x12;
	public static final int TYPE_WPA = 0x13;

	public static final int WIFI_CONNECTED = 0x01;
	public static final int WIFI_CONNECT_FAILED = 0x02;
	public static final int WIFI_CONNECTING = 0x03;

	private WifiManager wifiManager = null;
	private WifiManager.WifiLock mWifiLock;

	public WifiHelper()
	{
		wifiManager = (WifiManager) BaseApplication.getInstance().getSystemService(Context.WIFI_SERVICE);

	}

//	public boolean disableCurrent()
//	{
//		LogUtil.i(this, "disableCurrent");
//		return wifiManager.disconnect();
//	}

	/**
	 * 设置WIFI的状态，WIFI状态被设置后不等待直接返回
	 *
	 * @param statu 将要设置的WIFI的状态
	 */
	public void setWifiEnabled(boolean statu)
	{
		LogUtil.i(this, "setWifiEnabled " + statu);
		if (wifiManager.isWifiEnabled() != statu)
			wifiManager.setWifiEnabled(statu);
	}

	/**
	 * 设置WIFI的状态，但等待WIFI状态的真正改变后才返回
	 *
	 * @param statu 要设置的wifi的状态
	 * @param sync  是否同步执行
	 */
	public void setAndWaitWifiEnabled(boolean statu, boolean sync)
	{
		setWifiEnabled(statu);
		if (sync)
			while (true)
			{
				if (wifiManager.isWifiEnabled() == statu)
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

	public String getCurrentConnectedSSID()
	{
		LogUtil.i(this, "getCurrentConnectedSSID");
		WifiInfo info = wifiManager.getConnectionInfo();
		if (info != null)
			return  info.getSSID();
		return null;
	}

	public boolean scanApList()
	{
		LogUtil.i(this, "scanApList");
		return wifiManager.startScan();
	}

	public void addNetwork(String ssid, String passwd, int type)
	{
		if (ssid == null || passwd == null || ssid.equals(""))
		{
			return;
		}

		addNetwork(createWifiInfo(ssid, passwd, type));
	}

	// 添加一个网络并连接
	public void addNetwork(WifiConfiguration wcg)
	{
		LogUtil.i(this, "addNetwork");
		int wcgID = wifiManager.addNetwork(wcg);
		if (wcgID != -1)
		{
			boolean b = wifiManager.enableNetwork(wcgID, true);
		}
		else
		{
			LogUtil.e(this, "addNetwork Fail");
		}

	}

	public WifiConfiguration createWifiInfo(String SSID, String password, int type)
	{
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";

		WifiConfiguration tempConfig = this.IsExsits(SSID);
		if (tempConfig != null)
		{
			wifiManager.removeNetwork(tempConfig.networkId);
		}

		// 分为三种情况：1没有密码2用wep加密3用wpa加密
		if (type == TYPE_NO_PASSWD)
		{// WIFICIPHER_NOPASS
//			config.wepKeys[0] = "";
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//			config.wepTxKeyIndex = 0;

		} else if (type == TYPE_WEP)
		{  //  WIFICIPHER_WEP
			config.hiddenSSID = true;
			config.wepKeys[0] = "\"" + password + "\"";
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			config.allowedGroupCiphers
					.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		} else if (type == TYPE_WPA)
		{   // WIFICIPHER_WPA
			config.preSharedKey = "\"" + password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			// config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.status = WifiConfiguration.Status.ENABLED;
		}

		return config;
	}

	private WifiConfiguration IsExsits(String SSID)
	{
		List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
		for (WifiConfiguration existingConfig : existingConfigs)
		{
			if (existingConfig.SSID.equals("\"" + SSID + "\"") /*&& existingConfig.preSharedKey.equals("\"" + password + "\"")*/)
			{
				return existingConfig;
			}
		}
		return null;
	}

	/**
	 * 判断wifi是否连接成功,不是network
	 *
	 * @return
	 */
	public int isWifiContected()
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) BaseApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


		if (wifiNetworkInfo.getDetailedState() == NetworkInfo.DetailedState.OBTAINING_IPADDR
				|| wifiNetworkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTING)
		{
			return WIFI_CONNECTING;
		} else if (wifiNetworkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED)
		{
			return WIFI_CONNECTED;
		} else
		{
			return WIFI_CONNECT_FAILED;
		}
	}

	public List<ScanResult> getScanResult()
	{
		return wifiManager.getScanResults();
	}

	public boolean isWifiEnabled()
	{
		return wifiManager.isWifiEnabled();
	}

	// 断开指定ID的网络
	public void disconnectWifi(int netId)
	{
		wifiManager.disableNetwork(netId);
		wifiManager.disconnect();
	}

	// 锁定WifiLock
	public void acquireWifiLock()
	{
		mWifiLock.acquire();
	}

	// 解锁WifiLock
	public void releaseWifiLock()
	{
		// 判断时候锁定
		if (mWifiLock.isHeld())
		{
			mWifiLock.acquire();
		}
	}

	// 创建一个WifiLock
	public void creatWifiLock()
	{
		mWifiLock = wifiManager.createWifiLock("WIFISEND");
	}
}
