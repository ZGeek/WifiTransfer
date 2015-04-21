package y.q.wifisend.Tools.wifi.Helper;

import android.content.Context;
import android.net.wifi.WifiManager;

/**
 * Created by Administrator on 2014/11/25.
 */
public class WifiHelper
{
	private WifiManager wifiManager = null;

	public WifiHelper(Context context)
	{
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	}

	/**
	 * 设置WIFI的状态，WIFI状态被设置后不等待直接返回
	 *
	 * @param statu 将要设置的WIFI的状态
	 */
	public void setWifiEnabled(boolean statu)
	{
		if (wifiManager.isWifiEnabled() != statu)
			wifiManager.setWifiEnabled(statu);
	}

	/**
	 * 设置WIFI的状态，但等待WIFI状态的真正改变后才返回
	 *
	 * @param statu 要设置的wifi的状态
	 * @param  sync 是否同步执行
	 */
	public void waitWifiEnabled(boolean statu, boolean sync)
	{
		setWifiEnabled(statu);
		if(sync)
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

	/**
	 * 得到WIFI网卡的MAC地址
	 *
	 * @return 得到的MAC地址
	 */
	public String getWifiMAC()
	{
		//开启wifi
		waitWifiEnabled(true, true);
		return wifiManager.getConnectionInfo().getMacAddress();
	}
}
