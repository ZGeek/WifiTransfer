package y.q.wifisend.Reciver;

/**
 * Created by Cfun on 2015/5/3.
 */

import android.content.Context;
import android.content.Intent;
import y.q.Transfer.Services.Tran.Range;
import y.q.wifisend.Base.BaseApplication;
import y.q.wifisend.Base.BaseReciver;
import y.q.wifisend.Entry.FileType;
import y.q.wifisend.Entry.SendStatus;

/***
 * 当发送状态改变时接发出通知
 */
public class SendStateChangedReciver extends BaseReciver
{
	private static final String INTENT_FILTER = SendStateChangedReciver.class.getName();
	private OnSendStateChangedListener onSendStateChangedListener;
	private OnBeginTranListener onBeginTranListener;
	private OnAllTasksStartListener onAllTasksStartListener;

	public SendStateChangedReciver()
	{
		super(INTENT_FILTER);
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		if(INTENT_FILTER.equals(intent.getAction()))
		{
			int flag = intent.getIntExtra("flag", 0);
			if(flag == 1)
			{
				if(onSendStateChangedListener != null)
				{
					String uuid = intent.getStringExtra("uuid");
					SendStatus state = (SendStatus) intent.getSerializableExtra("state");
					float percent = intent.getFloatExtra("percent", 0);
					onSendStateChangedListener.onSendStateChanged(uuid, state, percent);
				}
			}
			else if(flag == 2)
			{
				if(onBeginTranListener != null)
				{
					String uuid = intent.getStringExtra("uuid");
					String filePath = intent.getStringExtra("filePath");
					String fileDesc = intent.getStringExtra("fileDesc");
					Range range = (Range) intent.getSerializableExtra("range");
					FileType type = (FileType) intent.getSerializableExtra("type");
					onBeginTranListener.onBeginTranListener(uuid, filePath, fileDesc, range, type);
				}
			}
			else if(flag == 3 && onAllTasksStartListener != null)
			{
				onAllTasksStartListener.onAllTasksStart();
			}

		}
	}

	public void setOnSendStateChangedListener(OnSendStateChangedListener onSendStateChangedListener)
	{
		this.onSendStateChangedListener = onSendStateChangedListener;
	}

	public void setOnBeginTranListener(OnBeginTranListener onBeginTranListener)
	{
		this.onBeginTranListener = onBeginTranListener;
	}

	public void setOnAllTasksStartListener(OnAllTasksStartListener onAllTasksStartListener)
	{
		this.onAllTasksStartListener = onAllTasksStartListener;
	}

	/**
	 * 所有任务刚要开始的时候响应的事件
	 */
	public interface OnAllTasksStartListener
	{
		void onAllTasksStart();
	}
	public interface OnSendStateChangedListener
	{
		void onSendStateChanged(String uuid, SendStatus state, float percent);
	}
	public interface OnBeginTranListener
	{
		void onBeginTranListener(String uuid, String filePath, String fileDesc, Range range, FileType type);
	}
	public static void sendStatuChangedBroadcast(final String uuid,final SendStatus state,final float percent)
	{
		Intent intent = new Intent(INTENT_FILTER);
		intent.putExtra("flag", 1);

		intent.putExtra("uuid", uuid);
		intent.putExtra("state", state);
		intent.putExtra("percent", percent);
		BaseApplication.getInstance().sendBroadcast(intent);
	}
	public static void sendBeginTranBroadcast(String uuid, String filePath, String fileDesc, Range range, FileType type)
	{
		Intent intent = new Intent(INTENT_FILTER);
		intent.putExtra("flag", 2);

		intent.putExtra("uuid", uuid);
		intent.putExtra("filePath", filePath);
		intent.putExtra("fileDesc", fileDesc);
		intent.putExtra("range", range);
		intent.putExtra("type", type);
		BaseApplication.getInstance().sendBroadcast(intent);
	}
	public static void sendAllTasksStartBroadcast()
	{
		Intent intent = new Intent(INTENT_FILTER);
		intent.putExtra("flag", 3);

		BaseApplication.getInstance().sendBroadcast(intent);
	}
}
