package y.q.Transfer.Services.send;

import android.app.Service;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;
import y.q.Transfer.Reciver.ApStateBroadcastReciver;
import y.q.Transfer.Reciver.ConnectivityChangeReciver;
import y.q.Transfer.Reciver.ScanResultAviableReciver;
import y.q.Transfer.Reciver.WifiStateBroadcastReciver;
import y.q.Transfer.Tools.MobileDataHelper;
import y.q.Transfer.Tools.WifiApHelper;
import y.q.Transfer.Tools.WifiHelper;
import y.q.wifisend.Base.BaseApplication;
import y.q.wifisend.Entry.ApNameInfo;
import y.q.wifisend.Entry.SendFileInfo;
import y.q.wifisend.Reciver.ConnectToTargetWifiReciver;
import y.q.wifisend.Reciver.ScanReciverResultReciver;
import y.q.wifisend.Utils.ApNameUtil;
import y.q.wifisend.Utils.LogUtil;


import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Cfun on 2015/4/21.
 */
public class SendService extends Service implements ScanResultAviableReciver.OnScanResultAviableListener, ConnectivityChangeReciver.OnWifiConnectivityChangeListener
{
	private SendActionBinder bind = null;
	private WifiApHelper apHelper;
	private WifiHelper wifiHelper;
	private String targetSSID;
	private SendTask sendTask;

	private ApStateBroadcastReciver apStateBroadcastReciver;
	private WifiStateBroadcastReciver wifiStateBroadcastReciver;
	private ScanResultAviableReciver scanResultAviableReciver;
	private ConnectivityChangeReciver connectivityChangeReciver;

	@Override
	public void onCreate()
	{
		super.onCreate();

		wifiHelper = new WifiHelper();
		apHelper = new WifiApHelper(wifiHelper);
		bind = new SendActionBinder();
		apStateBroadcastReciver = new ApStateBroadcastReciver();
		wifiStateBroadcastReciver = new WifiStateBroadcastReciver();
		scanResultAviableReciver = new ScanResultAviableReciver();
		connectivityChangeReciver = new ConnectivityChangeReciver();
		apStateBroadcastReciver.setApStateChangListener(new ApStateBroadcastReciver.OnApStateChangListener()
		{
			@Override
			public void onApClosing()
			{

			}

			@Override
			public void onApOpening()
			{

			}

			@Override
			public void onApOpened()
			{

			}

			@Override
			public void onApClosed()
			{
				apStateBroadcastReciver.unRegisterSelf();
				step2WifiOpen();
			}
		});
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return bind;
	}

	@Override
	public void onScanResultAviable()
	{
		List<ScanResult> results = wifiHelper.getScanResult();
		ScanReciverResultReciver.sendBroadcast(usefulApFilter(results));
//		wifiHelper.scanApList();
	}

	public ArrayList<ApNameInfo> usefulApFilter(List<ScanResult> results)
	{
		ArrayList<ApNameInfo> infos = new ArrayList<>();
		if (results == null || results.size() == 0)
			return infos;
		//去重
		HashSet<String> ssids = new HashSet<>();
		for (int i = 0; i < results.size(); i++)
		{
			ssids.add(results.get(i).SSID);
		}


		Iterator<String> iterator = ssids.iterator();
		while (iterator.hasNext())
		{
			String result = iterator.next();
			ApNameInfo ap = ApNameUtil.decodeApName(result);
			if (ap != null)
				infos.add(ap);
		}
		return infos;
	}

	protected void step1ApClose()
	{
		if (apHelper.isApEnabled())
		{
			apStateBroadcastReciver.registerSelf();
			apHelper.closeWifiAp(null);
		} else
		{
			step2WifiOpen();
		}
	}

	protected void step2WifiOpen()
	{
		if (wifiHelper.isWifiEnabled())
			step3WifiDisConnected();
		else
		{
			wifiStateBroadcastReciver.registerSelf();
			wifiStateBroadcastReciver.setOnWIfiStateChangedListener(new WifiStateBroadcastReciver.OnWIfiStateChangedListener()
			{

				@Override
				public void onWifiStateChanged(int wifiSatate)
				{
					if (wifiSatate == WifiManager.WIFI_STATE_ENABLED)
					{
						step3WifiDisConnected();
						wifiStateBroadcastReciver.unRegisterSelf();
					}
				}
			});
			wifiHelper.setWifiEnabled(true);
		}
	}

	protected void step3WifiDisConnected()
	{
//		int state = wifiHelper.isWifiContected();
//		if (state == WifiHelper.WIFI_CONNECTING || state == WifiHelper.WIFI_CONNECTED)
//		{
//			wifiHelper.disableCurrent();
//		}
		step4ScanApConnected();
	}

	protected void step4ScanApConnected()
	{
		scanResultAviableReciver.setOnScanResultAviableListener(this);
		scanResultAviableReciver.registerSelf();
		wifiHelper.scanApList();
	}

	/**
	 * 当WIFI的连接状态发生改变时调用
	 *
	 * @param info
	 */
	@Override
	public void onWifiConnectivityChange(NetworkInfo info)
	{
		if (info.isConnected())
		{
			if (info.isAvailable())
			{
				if (info.getExtraInfo().equals("\"" + targetSSID + "\""))
				{
					connectivityChangeReciver.setOnWifiConnectivityChangeListener(null);
					connectivityChangeReciver.unRegisterSelf();
					ConnectToTargetWifiReciver.sendBroadcast(targetSSID);
				} else
				{
//					wifiHelper.disableCurrent();
					wifiHelper.addNetwork(targetSSID, "", WifiHelper.TYPE_NO_PASSWD);
				}
			}

		} else
		{
//			bind.connectionSSID(targetSSID);
			wifiHelper.addNetwork(targetSSID, "", WifiHelper.TYPE_NO_PASSWD);
		}
	}

	public class SendActionBinder extends Binder
	{
		/**
		 * Activity<--->Service
		 * 1:ACtivity通知Service为传书做为准备 --->
		 * 2:Service通知ActivityWIFI是否已经就绪（失败，或正在打开）<---
		 * 3:Service通知Activitywifi扫描已经就绪，通知Activity通过Service获取扫描列表<---
		 * 4：ACtivity得到列表后选择合适的热点通知Service进行连接--->
		 * 5：Service通知Activity连接客户端的状态<---
		 * 6：若成功，则Service持续报告传输状态<---
		 * 7：在传输期间ACtivity可以通知Service取消本次发送--->
		 */

		public void preparedTranSend()
		{
			if (MobileDataHelper.getMobileDataState())
				MobileDataHelper.setMobileData(false);
			step1ApClose();
		}

		public void scanAP()
		{
			wifiHelper.scanApList();
		}

		public void connectionSSID(final String ssid)
		{
			targetSSID = ssid;
			if (("\"" + ssid + "\"").equals(wifiHelper.getCurrentConnectedSSID()))
			{
				ConnectToTargetWifiReciver.sendBroadcast(targetSSID);
				return;
			}
			connectivityChangeReciver.setOnWifiConnectivityChangeListener(SendService.this);
			connectivityChangeReciver.registerSelf();

			int state = wifiHelper.isWifiContected();
//			if (state == WifiHelper.WIFI_CONNECTING || state == WifiHelper.WIFI_CONNECTED)
//			{
//				wifiHelper.disableCurrent();
//			} else
//			{
			wifiHelper.addNetwork(ssid, "", WifiHelper.TYPE_NO_PASSWD);
//			}

		}

		public void tranFiles(InetAddress address, int port, List<SendFileInfo> tasks)
		{
			sendTask = new SendTask(address, port, tasks);
			sendTask.setName("发送进程");
			sendTask.start();
		}

		public void cancleTran()
		{
			if (sendTask != null)
				sendTask.stopTran();
		}
	}

}
