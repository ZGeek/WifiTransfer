package y.q.Transfer.Services.receive;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import y.q.AppConfig;
import y.q.Transfer.Reciver.ApStateBroadcastReciver;
import y.q.Transfer.Reciver.ScanResultAviableReciver;
import y.q.Transfer.Reciver.WifiStateBroadcastReciver;
import y.q.Transfer.Tools.MobileDataHelper;
import y.q.Transfer.Tools.WifiApHelper;
import y.q.Transfer.Tools.WifiHelper;
import y.q.wifisend.Entry.ApNameInfo;
import y.q.wifisend.Reciver.RecvingPrepareStateChangReciver;
import y.q.wifisend.Utils.ApNameUtil;
/**
 * Created by Cfun on 2015/4/21.
 */
public class ReceiveService extends Service implements WifiStateBroadcastReciver.OnWIfiStateChangedListener, ApStateBroadcastReciver.OnApStateChangListener
{
	private ReceiveTask receiveTask;
	private ReceiveActionBinder binder = null;
	private WifiApHelper apHelper;
	private WifiHelper wifiHelper;
	private ApStateBroadcastReciver apStateBroadcastReciver;
	private WifiStateBroadcastReciver wifiStateBroadcastReciver;
	private String ssid;
	private WifiConfiguration apConfig = null;
	@Override
	public void onCreate()
	{
		super.onCreate();
		wifiHelper = new WifiHelper();
		apHelper = new WifiApHelper(wifiHelper);
		binder = new ReceiveActionBinder();
		apStateBroadcastReciver = new ApStateBroadcastReciver();
		wifiStateBroadcastReciver = new WifiStateBroadcastReciver();

	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return binder;
	}

	@Override
	public void onWifiStateChanged(int wifiSatate)
	{
		if (wifiSatate == WifiManager.WIFI_STATE_DISABLED)
		{
			wifiStateBroadcastReciver.unRegisterSelf();
			WifiConfiguration config = apHelper.openWifiAp(ssid, apConfig == null);
			if(config != null)
				apConfig = config;
		}
	}

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
		apStateBroadcastReciver.setApStateChangListener(null);
		apStateBroadcastReciver.unRegisterSelf();
//				String ip = getPhoneIp();
		//Ap已打开，则开始监听端口
		if(receiveTask != null)
			receiveTask.stopTran();
		receiveTask = new ReceiveTask(AppConfig.port);
		receiveTask.setName("接收进程");
		receiveTask.start();
	}

	@Override
	public void onApClosed()
	{
		wifiStateBroadcastReciver.unRegisterSelf();
		WifiConfiguration config = apHelper.openWifiAp(ssid, apConfig == null);
		if(config != null)
			apConfig = config;
	}

	public class ReceiveActionBinder extends Binder
	{
		public void prepareRecive(int photoId, String name)
		{
			RecvingPrepareStateChangReciver.sendBroadcast(RecvingPrepareStateChangReciver.STATE_PREPARING);
			 ssid = ApNameUtil.encodeApName(new ApNameInfo(name, photoId));
			apStateBroadcastReciver.setApStateChangListener(ReceiveService.this);
			apStateBroadcastReciver.registerSelf();
			if (MobileDataHelper.getMobileDataState())
			{
				MobileDataHelper.setMobileData(false);
			}
			if ((!apHelper.isApEnabled()) && (!wifiHelper.isWifiEnabled()))
			{
				//如果wifi与AP都没有打开，则需要直接开启热点
				WifiConfiguration config = apHelper.openWifiAp(ssid, apConfig == null);
				if(config != null)
					apConfig = config;
				return;
			}

			wifiStateBroadcastReciver.setOnWIfiStateChangedListener(ReceiveService.this);
			wifiStateBroadcastReciver.registerSelf();
			apHelper.closeWifiAp(apConfig);
			wifiHelper.setWifiEnabled(false);
		}

		public void onlyCloseAP()
		{
			if(receiveTask != null)
					receiveTask.stopTran();
			apHelper.closeWifiAp(apConfig);
		}

		public void stopReceiveService()
		{
			if(receiveTask != null)
				receiveTask.stopTran();
		}
	}
}
