package y.q.wifisend.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Cfun on 2015/5/4.
 */
public class BlueToothShareUtil
{
	public static void DoShareFileByBt(Context context, String aFileName)
	{
		ArrayList<Uri> vUriArray = new ArrayList<>();
		vUriArray.add(Uri.fromFile(new File(aFileName)));
		DoShareFilesByBt(context, vUriArray);
	}

	/*-------------------------实现源码-----------------------------*/
	public static void DoShareFilesByBt(Context aContext, ArrayList<Uri> aUriArray)
	{
		if(aUriArray!=null && aUriArray.size()>0)
		{
			Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
			intent.setClassName("com.android.bluetooth", "com.android.bluetooth.opp.BluetoothOppLauncherActivity");
			intent.putExtra(Intent.EXTRA_STREAM, aUriArray);
			intent.setType("*/*"); //must set this flag
			aContext.startActivity(intent);
		}
	}

	// 获取当前目录下所有文件的uri
	public static ArrayList<Uri> GetTotalUris(String aFilePath, ArrayList<Uri> aUriArray)
	{
		ArrayList<Uri> vRetArray = aUriArray;
		if(vRetArray == null)
		{
			vRetArray = new ArrayList<Uri>();
		}
		// 获取源文件夹当前下的文件或目录
		File vCurFile = new File(aFilePath);
		if(vCurFile.isFile())
		{
			vRetArray.add(Uri.fromFile(vCurFile));
		}
		else
		{
			File[] files = vCurFile.listFiles();
			for (File vFile: files)
			{
				if(vFile.isFile())
				{
					vRetArray.add(Uri.fromFile(vFile));
				}
				else if(vFile.isDirectory())
				{
					GetTotalUris(vFile.getAbsolutePath(),vRetArray);
				}
			}
		}
		return vRetArray;
	}
}
