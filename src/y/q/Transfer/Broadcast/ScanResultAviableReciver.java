package y.q.Transfer.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

/**
 * Created by CFun on 2015/4/22.
 */
public class ScanResultAviableReciver extends BroadcastReceiver
{
	public final static String ACTION = WifiManager.SCAN_RESULTS_AVAILABLE_ACTION;

	private OnScanResultAviableListener onScanResultAviableListener;

	@Override
	public void onReceive(Context context, Intent intent)
	{
		if(onScanResultAviableListener != null) onScanResultAviableListener.onScanResultAviable();
	}

	public void setOnScanResultAviableListener(OnScanResultAviableListener onScanResultAviableListener)
	{
		this.onScanResultAviableListener = onScanResultAviableListener;
	}

	public interface OnScanResultAviableListener
	{
		void onScanResultAviable();
	}
}
