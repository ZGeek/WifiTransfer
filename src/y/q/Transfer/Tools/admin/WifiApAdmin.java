package y.q.Transfer.Tools.admin;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;
import y.q.Transfer.Tools.TimerCheck;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 创建热点
 */
public class WifiApAdmin
{
	public static final String TAG = "WifiApAdmin";

	public static void closeWifiAp(Context context)
	{
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		closeWifiAp(wifiManager);
	}

	private WifiManager mWifiManager = null;

	private Context mContext = null;

	public WifiApAdmin(Context context)
	{
		mContext = context;

		mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);

		closeWifiAp(mWifiManager);
	}

	private String mSSID = "";
	private String mPasswd = "";

	public void startWifiAp(String ssid, String passwd)
	{
		mSSID = ssid;
		mPasswd = passwd;

		if (mWifiManager.isWifiEnabled())
		{
			mWifiManager.setWifiEnabled(false);
		}

		stratWifiAp();

		TimerCheck timerCheck = new TimerCheck()
		{

			@Override
			public void doTimerCheckWork()
			{
				// TODO Auto-generated method stub

				if (isWifiApEnabled(mWifiManager))
				{
					Log.v(TAG, "Wifi enabled success!");
					this.exit();
				} else
				{
					Log.v(TAG, "Wifi enabled failed!");
				}
			}

			@Override
			public void doTimeOutWork()
			{
				// TODO Auto-generated method stub
				this.exit();
			}
		};
		timerCheck.start(15, 1000);

	}

	public void stratWifiAp()
	{
		Method method1 = null;
		try
		{
			method1 = mWifiManager.getClass().getMethod("setWifiApEnabled",
					WifiConfiguration.class, boolean.class);
			WifiConfiguration netConfig = new WifiConfiguration();

			netConfig.SSID = mSSID;
			netConfig.preSharedKey = mPasswd;

			netConfig.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.OPEN);
			netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
			netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			netConfig.allowedKeyManagement
					.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			netConfig.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			netConfig.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			netConfig.allowedGroupCiphers
					.set(WifiConfiguration.GroupCipher.CCMP);
			netConfig.allowedGroupCiphers
					.set(WifiConfiguration.GroupCipher.TKIP);

			method1.invoke(mWifiManager, netConfig, true);

		} catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void closeWifiAp(WifiManager wifiManager)
	{
		if (isWifiApEnabled(wifiManager))
		{
			try
			{
				Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
				method.setAccessible(true);

				WifiConfiguration config = (WifiConfiguration) method.invoke(wifiManager);

				Method method2 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
				method2.invoke(wifiManager, config, false);
			} catch (NoSuchMethodException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static boolean isWifiApEnabled(WifiManager wifiManager)
	{
		try
		{
			Method method = wifiManager.getClass().getMethod("isWifiApEnabled");
			method.setAccessible(true);
			return (Boolean) method.invoke(wifiManager);

		} catch (NoSuchMethodException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return false;
	}

}
