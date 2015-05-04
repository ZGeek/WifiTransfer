package y.q.Transfer.Reciver;


import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import y.q.wifisend.Base.BaseReciver;

/**
 * Created by CFun on 2015/4/22.
 */

/***
 * 当启动wifi扫描后，wifi扫描结果有效时系统发出此广播
 */
public class ScanResultAviableReciver extends BaseReciver
{
	private OnScanResultAviableListener onScanResultAviableListener;

	public ScanResultAviableReciver()
	{
		super(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
	}

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
