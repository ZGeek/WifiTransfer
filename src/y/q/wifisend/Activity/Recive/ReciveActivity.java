package y.q.wifisend.Activity.Recive;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import y.q.AppConfig;
import y.q.Transfer.Services.Tran.Range;
import y.q.Transfer.Services.receive.ReceiveService;
import y.q.wifisend.Base.BaseActivity;
import y.q.wifisend.Base.BaseApplication;
import y.q.wifisend.Entry.FileType;
import y.q.wifisend.Entry.SendFileInfo;
import y.q.wifisend.Entry.SendStatus;
import y.q.wifisend.Fragment.RecevingFragment;
import y.q.wifisend.Fragment.WaittingReciveFragment;
import y.q.wifisend.R;
import y.q.wifisend.Reciver.RecvingPrepareStateChangReciver;
import y.q.wifisend.Reciver.SendStateChangedReciver;

import java.util.ArrayList;

/**
 * Created by Cfun on 2015/5/4.
 */
public class ReciveActivity extends BaseActivity implements RecvingPrepareStateChangReciver.OnRecivePrepareStateChangedListener, SendStateChangedReciver.OnAllTasksStartListener, SendStateChangedReciver.OnSendStateChangedListener, SendStateChangedReciver.OnBeginTranListener, RecevingFragment.StopTran
{
	private boolean isBind = false;
	private ReceiveService.ReceiveActionBinder binder;
	private ServiceConnection serviceConnection;
	private RecvingPrepareStateChangReciver recvingPrepareStateChangReciver = new RecvingPrepareStateChangReciver();
	private SendStateChangedReciver sendStateChangedReciver = new SendStateChangedReciver();
	private ArrayList<SendFileInfo> sendFileInfos = new ArrayList<>();
	private FragmentManager fragmentManager;

	private RecevingFragment recevingFragment = null;
	private WaittingReciveFragment waittingReciveFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		serviceConnection = new ServiceConnection()
		{
			@Override
			public void onServiceConnected(ComponentName componentName, IBinder binder)
			{
				//调用bindService方法启动服务时候，如果服务需要与activity交互，
				//则通过onBind方法返回IBinder并返回当前本地服务
				ReciveActivity.this.binder = ((ReceiveService.ReceiveActionBinder) binder);
				recvingPrepareStateChangReciver.registerSelf();
				ReciveActivity.this.binder.prepareRecive(AppConfig.photoId, AppConfig.userName);
			}

			@Override
			public void onServiceDisconnected(ComponentName componentName)
			{
				binder.onlyCloseAP();
			}
		};
		isBind = BaseApplication.getInstance().bindService(new Intent(BaseApplication.getInstance(), ReceiveService.class), serviceConnection, Context.BIND_AUTO_CREATE);

		recvingPrepareStateChangReciver.setOnRecivePrepareStateChangedListener(this);
		sendStateChangedReciver.setOnBeginTranListener(this);
		sendStateChangedReciver.setOnSendStateChangedListener(this);
		sendStateChangedReciver.setOnAllTasksStartListener(this);
		fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction =fragmentManager.beginTransaction();
		waittingReciveFragment = new WaittingReciveFragment();
		transaction.replace(android.R.id.content, waittingReciveFragment, RecevingFragment.class.getSimpleName());
		transaction.commit();
	}

	@Override
	protected void onDestroy()
	{//当要退出的时候，发送端还没有连接或者Socket已经绑定，则说明本次的热点打开无效，在退出的时候关闭热点
			binder.onlyCloseAP();
		sendStateChangedReciver.unRegisterSelf();
		recvingPrepareStateChangReciver.unRegisterSelf();
		if (isBind)
		{
			BaseApplication.getInstance().unbindService(serviceConnection);
			isBind = false;
		}
		super.onDestroy();
	}

	@Override
	public void finish()
	{
		super.finish();
	}

	@Override
	public void onRecivePrepareStateChanged(int state)
	{
		if(waittingReciveFragment != null)
			waittingReciveFragment.onRecivePrepareStateChanged(state);
		switch (state)
		{
			case RecvingPrepareStateChangReciver.STATE_ERROR:
				recvingPrepareStateChangReciver.unRegisterSelf();
				break;
			case RecvingPrepareStateChangReciver.STATE_FINISH:
				recvingPrepareStateChangReciver.unRegisterSelf();
				sendStateChangedReciver.registerSelf();
				break;
			case RecvingPrepareStateChangReciver.STATE_PREPARING:
				break;
		}
	}

	/**
	 * 当发送状态发生改变时响应此事件，此事件的发生说明发送者已经连接上来了
	 */
	@Override
	public void onAllTasksStart()
	{
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		waittingReciveFragment = null;
		recevingFragment = new RecevingFragment();
		recevingFragment.activitySendFileInfo = sendFileInfos;
		transaction.replace(android.R.id.content, recevingFragment, RecevingFragment.class.getSimpleName());
		transaction.commit();
	}

	@Override
	public void onSendStateChanged(String uuid, SendStatus state, float percent)
	{
		if(recevingFragment != null && recevingFragment.isReady)
			recevingFragment.onSendStateChanged(uuid, state, percent);
		else
		{
			if(state == SendStatus.AllFinish)
				return;
			SendFileInfo sendFileInfo = null;
			for(int i = 0; i<sendFileInfos.size(); i++)
			{
				if(uuid.equals(sendFileInfos.get(i).getUuid()))
				{
					sendFileInfo =sendFileInfos.get(i);
					break;
				}
			}
			if(sendFileInfo != null)
			{
				sendFileInfo.setSendStatu(state);
				sendFileInfo.setSendPercent(percent);
			}
		}
	}

	@Override
	public void onBeginTranListener(String uuid, String filePath, String fileDesc, Range range, FileType type)
	{
		if(recevingFragment != null && recevingFragment.isReady)
			recevingFragment.onBeginTranListener(uuid, filePath, fileDesc, range, type);
		else
		{//如果recevingFragment==null 说明recevingFragment还没准备好，此时由Activity代理记录onBeginTranListener 和 onSendStateChanged事件
			SendFileInfo sendFileInfo = new SendFileInfo(uuid);
			sendFileInfo.setFilepath(filePath);
			sendFileInfo.setFileDesc(fileDesc);
			sendFileInfo.setTransRange(range);
			sendFileInfo.setFileType(type);
			sendFileInfos.add(sendFileInfo);
		}
	}

	@Override
	public void stopTran()
	{
		binder.stopReceiveService();
	}
}
