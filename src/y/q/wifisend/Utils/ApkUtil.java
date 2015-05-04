package y.q.wifisend.Utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import y.q.wifisend.Base.BaseApplication;
import y.q.wifisend.R;

/**
 * Created by Cfun on 2015/5/4.
 */
public class ApkUtil
{
	public static Drawable getAPPDrawable(String path)
	{
		PackageManager pm = BaseApplication.getInstance().getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
		if (info != null)
		{
			ApplicationInfo appInfo = info.applicationInfo;
			appInfo.publicSourceDir = path;
			Drawable icon = pm.getApplicationIcon(appInfo);
			if(icon != null)
				return icon;
		}

		return BaseApplication.getInstance().getResources().getDrawable(R.mipmap.apk);
	}

	public static String getAPPName(String path)
	{
		PackageManager pm = BaseApplication.getInstance().getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
		if (info != null)
		{
			ApplicationInfo appInfo = info.applicationInfo;
			appInfo.publicSourceDir = path;
			String name = pm.getApplicationLabel(appInfo).toString();
			if(!TextUtils.isEmpty(name))
				return name;
		}

		return null;
	}

}
