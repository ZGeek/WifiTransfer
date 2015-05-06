package y.q.wifisend.Reciver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import y.q.wifisend.Base.BaseApplication;
import y.q.wifisend.Base.BaseReciver;
import y.q.wifisend.Entry.ApNameInfo;

import java.util.ArrayList;

/**
 * Created by Cfun on 2015/5/1.
 */

/***
 * 当扫描到可用的用户连接的时候，发送服务会发送此广播以通知UI改变
 */
public class ScanReciverResultReciver extends BaseReciver
{
	private OnScanReciverResultAvilableListener onScanReciverResultAvilableListener;

	public ScanReciverResultReciver()
	{
		super(ScanReciverResultReciver.class.getName());
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (intent.getAction().equals(INTENT_FILTER))
		{
			if (onScanReciverResultAvilableListener != null)
			{
				ArrayList<ApNameInfo> infos = (ArrayList<ApNameInfo>) intent.getExtras().getSerializable("infos");
				onScanReciverResultAvilableListener.onScanReciverResultAviable(infos);
			}

		}
	}

	public interface OnScanReciverResultAvilableListener
	{
		void onScanReciverResultAviable(ArrayList<ApNameInfo> infos);
	}

	public void setOnScanReciverResultAvilableListener(OnScanReciverResultAvilableListener onScanReciverResultAvilableListener)
	{
		this.onScanReciverResultAvilableListener = onScanReciverResultAvilableListener;
	}

	public static void sendBroadcast(ArrayList<ApNameInfo> infos)
	{
		Intent intent = new Intent(ScanReciverResultReciver.class.getName());
		Bundle bundle = new Bundle();
		bundle.putSerializable("infos", infos);
		intent.putExtras(bundle);
		BaseApplication.getInstance().sendBroadcast(intent);
	}
}
