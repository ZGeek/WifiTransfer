package y.q.wifisend.Activity.Send;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import y.q.AppConfig;
import y.q.Transfer.Services.send.SendService;
import y.q.wifisend.Base.BaseActivity;
import y.q.wifisend.Entry.ApNameInfo;
import y.q.wifisend.Entry.SendFileInfo;
import y.q.wifisend.R;
import y.q.wifisend.Reciver.ConnectToTargetWifiReciver;
import y.q.wifisend.Reciver.ScanReciverResultReciver;
import y.q.wifisend.Utils.ApNameUtil;
import y.q.wifisend.widget.CircleImageView;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by CFun on 2015/4/19.
 */

/**
 * 扫描接收者的Activity
 */
public class ScanReciverActivity extends BaseActivity implements View.OnClickListener, ScanReciverResultReciver.OnScanReciverResultAvilableListener, ConnectToTargetWifiReciver.OnConnectToTargetWifiListener
{

	private LinearLayout[] layouts = new LinearLayout[10];
	private TextView infoText = null;
	private View scanResultView;
	private int hasAddCount = 0;
	private ScanReciverResultReciver scanReciverResultReciver = new ScanReciverResultReciver();
	private ConnectToTargetWifiReciver connectToTargetWifiReciver = new ConnectToTargetWifiReciver();


	private SendService.SendActionBinder binder;
	private ServiceConnection serviceConnection;
	private ApNameInfo info;
	private boolean hasReSize = false;
//	private CircleImageView iv_photo;

	private static final int MSG_SCAN_AP = 1;
	private static final int MSG_ENABLE_VIEW = 2;
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			if (msg.what == MSG_SCAN_AP)
			{
				binder.scanAP();
			} else if (msg.what == MSG_ENABLE_VIEW)
			{
				if (scanResultView != null)
					scanResultView.setEnabled(true);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan_reciver);
		initView();
		scanReciverResultReciver.setOnScanReciverResultAvilableListener(this);
		scanReciverResultReciver.registerSelf();
		connectToTargetWifiReciver.setOnConnectToTargetWifiListener(this);
		connectToTargetWifiReciver.registerSelf();
		serviceConnection = new ServiceConnection()
		{
			@Override
			public void onServiceConnected(ComponentName componentName, IBinder binder)
			{
				//调用bindService方法启动服务时候，如果服务需要与activity交互，
				//则通过onBind方法返回IBinder并返回当前本地服务
				ScanReciverActivity.this.binder = ((SendService.SendActionBinder) binder);
				//这里可以提示用户,或者调用服务的某些方法
				ScanReciverActivity.this.binder.preparedTranSend();
			}

			@Override
			public void onServiceDisconnected(ComponentName componentName)
			{
			}
		};
		binderService();
	}

	private void initView()
	{
		scanResultView = findViewById(R.id.scanResult);


		layouts[0] = null;
		layouts[1] = (LinearLayout) scanResultView.findViewById(R.id.v1);
		layouts[2] = (LinearLayout) scanResultView.findViewById(R.id.v2);
		layouts[3] = (LinearLayout) scanResultView.findViewById(R.id.v3);
		layouts[4] = (LinearLayout) scanResultView.findViewById(R.id.v4);
		layouts[5] = null;
		layouts[6] = (LinearLayout) scanResultView.findViewById(R.id.v6);
		layouts[7] = (LinearLayout) scanResultView.findViewById(R.id.v7);
		layouts[8] = (LinearLayout) scanResultView.findViewById(R.id.v8);
		layouts[9] = (LinearLayout) scanResultView.findViewById(R.id.v9);

//		((TextView) findViewById(R.id.tv_name)).setText(AppConfig.userName);
		infoText = (TextView) findViewById(R.id.tv_info);
		infoText.setText(R.string.initing);

		findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		((TextView) findViewById(R.id.tv_title)).setText(R.string.choseRevicer);

		((CircleImageView)findViewById(R.id.iv_photo)).setImageResource(AppConfig.getPhotoResorce());
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && !hasReSize)
		{
			hasReSize = true;
			View view = findViewById(R.id.scanResult);
			int width = view.getWidth();
			ViewGroup.LayoutParams params = view.getLayoutParams();
			params.height = (int) (width * 1.1);
			view.setLayoutParams(params);
		}
	}

	@Override
	protected void onDestroy()
	{

		connectToTargetWifiReciver.unRegisterSelf();
		scanReciverResultReciver.unRegisterSelf();
		unbindService(serviceConnection);
		super.onDestroy();
	}

	@Override
	protected void onStart()
	{
		super.onStart();
	}

	@Override
	protected void onStop()
	{
		scanReciverResultReciver.unRegisterSelf();
		super.onStop();
	}

	private void clearItem(ArrayList<ApNameInfo> scanInfo)
	{
		for (int i = 1; i < 10; i++)
		{
			if (layouts[i] == null)
				continue;
			int count = layouts[i].getChildCount();
			if (count <= 0)
				continue;
			ApNameInfo info = (ApNameInfo) layouts[i].getChildAt(0).getTag();
			if (scanInfo.contains(info))
			{
				scanInfo.remove(info);
			} else
			{
				layouts[i].removeAllViews();
				hasAddCount--;
			}
		}
	}

	private void addItem(ApNameInfo info)
	{
		if (hasAddCount >= 8)
			return;
		int max = 9;
		int min = 1;
		Random random = new Random();
		int i = random.nextInt(max) % (max - min + 1) + min;
		while (layouts[i] == null || layouts[i].getChildCount() > 0)
		{
			i = random.nextInt(max) % (max - min + 1) + min;
		}
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.scan_result_item, null);
		layouts[i].addView(view);
		((ImageView) view.findViewById(R.id.ivPhoto)).setImageResource(AppConfig.getPhotoResorce(info.getPhotoId()));
		((TextView) view.findViewById(R.id.tvName)).setText(info.getName());
		view.setOnClickListener(this);
		view.setTag(info);
		hasAddCount++;
	}

	@Override
	public void onClick(View v)
	{
		info = (ApNameInfo) v.getTag();
		binder.connectionSSID(ApNameUtil.encodeApName(info));
		infoText.setText(String.format(getString(R.string.linking), info.getName()));
		scanResultView.setEnabled(false);
		handler.sendEmptyMessageDelayed(MSG_ENABLE_VIEW, 8000);
	}


	@Override
	public void onScanReciverResultAviable(ArrayList<ApNameInfo> infos)
	{
		if (scanResultView.isEnabled())
			infoText.setText(R.string.scaning);//只有enable的时候才可以改变
		if (hasAddCount > 0)
			clearItem(infos);

		if (infos.size() > 0)
		{
			for (int i = 0; i < infos.size(); i++)
			{
				ApNameInfo info = infos.get(i);
				addItem(info);
			}
		}
		handler.removeMessages(MSG_SCAN_AP);
		handler.sendEmptyMessageDelayed(MSG_SCAN_AP, 7000);//5秒新数据未到达就进行一次扫描
	}

	private void binderService()
	{
		Intent intent = new Intent(this, SendService.class);
		bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	public void onConnectToTargetWifi(String ssid)
	{
		Bundle bundle = getIntent().getExtras();
		startService(new Intent(this, SendService.class));
		InetAddress address = null;

		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
		int ip = dhcpInfo.gateway;

//		System.err.println("gateway:"+intToIp(dhcpInfo.gateway));
//		System.err.println("dhcp ip:"+intToIp(dhcpInfo.ipAddress));
//		System.err.println("wifiinfo ip:"+intToIp(wifiInfo.getIpAddress()));
		byte[] byteAdd = new byte[4];
		byteAdd[0] = (byte) (0xff & ip);
		byteAdd[1] = (byte) ((0xff00 & ip) >> 8);
		byteAdd[2] = (byte) ((0xff0000 & ip) >> 16);
		byteAdd[3] = (byte) ((0xff000000 & ip) >> 24);

		try
		{
			address = Inet4Address.getByAddress(byteAdd);
		} catch (UnknownHostException e)
		{
			e.printStackTrace();
		}

		binder.tranFiles(address, AppConfig.port, (ArrayList<SendFileInfo>) bundle.getSerializable("tranList"));

		Intent intent = new Intent(this, SendingActivity.class);
		intent.putExtra("isSender", true);
		intent.putExtras(bundle);
		startActivity(intent);
		scanReciverResultReciver.unRegisterSelf();
		handler.removeMessages(MSG_SCAN_AP);
		this.finish();
	}

	private String intToIp(int i)
	{
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
	}

}
