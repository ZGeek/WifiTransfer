package y.q.wifisend.Activity.Send;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import y.q.Transfer.Services.send.SendService;
import y.q.wifisend.Base.BaseActivity;
import y.q.wifisend.Entry.SendFileInfo;
import y.q.wifisend.Entry.SendStatus;
import y.q.wifisend.R;
import y.q.wifisend.Reciver.SendStateChangedReciver;
import y.q.wifisend.Utils.FileSizeFormatUtil;
import y.q.wifisend.Utils.IcoUtil;

import java.util.ArrayList;

/**
 * Created by CFun on 2015/4/21.
 */

/**
 * 显示正在发送文件的文件和进度的Activity
 */
public class SendingActivity extends BaseActivity implements View.OnClickListener, SendStateChangedReciver.OnSendStateChangedListener
{
	private SendStateChangedReciver reciver = new SendStateChangedReciver();
	private ArrayList<SendFileInfo> sendFileInfos = null;
	private ListView listView;
	private TranListAdapter adapter;
	private TextView bottomButton = null;

	private SendService.SendActionBinder binder;
	private ServiceConnection serviceConnection;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_recive_list);
		initDateAndView();
		reciver.setOnSendStateChangedListener(this);
		reciver.registerSelf();
		serviceConnection = new ServiceConnection()
		{
			@Override
			public void onServiceConnected(ComponentName componentName, IBinder binder)
			{
				//调用bindService方法启动服务时候，如果服务需要与activity交互，
				//则通过onBind方法返回IBinder并返回当前本地服务
				SendingActivity.this.binder = ((SendService.SendActionBinder) binder);
				//这里可以提示用户,或者调用服务的某些方法
				SendingActivity.this.binder.preparedTranSend();
			}

			@Override
			public void onServiceDisconnected(ComponentName componentName)
			{
			}
		};
		binderService();
	}

	private void binderService()
	{
		Intent intent = new Intent(this, SendService.class);
		bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
	}

	private void initDateAndView()
	{
		bottomButton = (TextView) findViewById(R.id.bottomButton);
		bottomButton.setText("取消");
		bottomButton.setOnClickListener(this);
		findViewById(R.id.iv_back).setOnClickListener(this);
		((TextView) findViewById(R.id.tv_title)).setText(R.string.sendding);
		listView = (ListView) findViewById(R.id.sendList);
		sendFileInfos = (ArrayList<SendFileInfo>) getIntent().getExtras().getSerializable("tranList");
		adapter = new TranListAdapter(LayoutInflater.from(this), sendFileInfos);
		listView.setAdapter(adapter);
	}

	@Override
	protected void onDestroy()
	{
		unbindService(serviceConnection);
		reciver.unRegisterSelf();
		super.onDestroy();
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.iv_back:
				/** 应该先ShowPopWindow提示用户是否真的要退出*/
				this.finish();
				break;
			case R.id.bottomButton:
				if(bottomButton.getText().equals("取消"))
				{
					binder.cancleTran();
					bottomButton.setText("完成");
				}
				else if(bottomButton.getText().equals("完成"))
				{
					this.finish();
				}
				break;
		}
	}

	/**
	 * 当发送状态发生改变的时候响应此函数，用以更新UI
	 *
	 * @param uuid
	 * @param state
	 * @param percent
	 */
	@Override
	public void onSendStateChanged(String uuid, SendStatus state, float percent)
	{
		if(state == SendStatus.AllFinish)
		{
			bottomButton.setText("完成");
			return;
		}
		ViewHolder holder = getViewHolderByUUID(uuid);
		if(holder == null)
		{
			int index = getIndexByUuid(uuid);
			if(index>=0)
				listView.setSelection(index);
			holder = getViewHolderByUUID(uuid);
		}
		if(holder == null)
			return;
		holder.data.setSendStatu(state);
		holder.data.setSendPercent(percent);
		holder.progressBar.setProgress((int) (100 * percent));
		holder.sendPercent.setText(((int) (percent * 100)) + "%");


	}

	/***
	 * 判定给定UUID的view是否在用户可见的屏幕区域
	 */
	public boolean isUUidViewVisiable(String uuid)
	{
		final int firstListItemPosition = listView.getFirstVisiblePosition();
		final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

		int index = getIndexByUuid(uuid);
		return index>=firstListItemPosition && index<=lastListItemPosition;
	}
	public int getIndexByUuid(String uuid)
	{
		if(sendFileInfos == null || sendFileInfos.size() == 0)
			return -1;
		for(int i = 0; i <sendFileInfos.size(); i++)
		{
			if(sendFileInfos.get(i).getUuid().equals(uuid))
				return i;
		}
		return -1;
	}

	public ViewHolder getViewHolderByUUID(String uuid)
	{
		final int firstListItemPosition = listView.getFirstVisiblePosition();
		final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

		int length = lastListItemPosition - firstListItemPosition +1;
		for(int i = 0; i< length; i++)
		{
			View view = listView.getChildAt(i);
			if(uuid.equals(((ViewHolder)view.getTag()).data.getUuid()))
			{
				return (ViewHolder) view.getTag();
			}
		}
		return null;
	}

	class TranListAdapter extends BaseAdapter
	{
		private LayoutInflater inflater;
		private ArrayList<SendFileInfo> sendFileInfos;

		public TranListAdapter(LayoutInflater inflater, ArrayList<SendFileInfo> sendFileInfos)
		{
			this.inflater = inflater;
			this.sendFileInfos = sendFileInfos;
		}

		@Override
		public int getCount()
		{
			return sendFileInfos == null ? 0 : sendFileInfos.size();
		}

		@Override
		public Object getItem(int position)
		{
			return sendFileInfos.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
			{
				convertView = inflater.inflate(R.layout.send_recive_list_item, null);
				ViewHolder holder = new ViewHolder();
				convertView.setTag(holder);
				holder.img = (ImageView) convertView.findViewById(R.id.iv_sendImg);
				holder.fileSize = (TextView) convertView.findViewById(R.id.tv_sendSize);
				holder.sendPercent = (TextView) convertView.findViewById(R.id.tv_sendPercent);
				holder.fileName = (TextView) convertView.findViewById(R.id.tv_sendFileName);
				holder.progressBar = (ProgressBar) convertView.findViewById(R.id.pb_sendProgress);
				holder.progressBar.setMax(100);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.data =  sendFileInfos.get(position);
			holder.img.setImageDrawable(IcoUtil.getIco(holder.data.getFileType(), holder.data.getFilepath()));
			holder.fileSize.setText(FileSizeFormatUtil.format(holder.data.getTransRange().getEndByte() - holder.data.getTransRange().getBeginByte()));
			holder.sendPercent.setText(holder.data.getSendPercent() * 100 + "%");
			holder.fileName.setText(holder.data.getFileDesc());
			holder.progressBar.setProgress((int) (holder.data.getSendPercent() * 100));
			return convertView;
		}

	}

	class ViewHolder
	{
		public SendFileInfo data;
		public ImageView img;
		public TextView fileSize;
		public TextView sendPercent;
		public TextView fileName;
		public ProgressBar progressBar;
	}
}
