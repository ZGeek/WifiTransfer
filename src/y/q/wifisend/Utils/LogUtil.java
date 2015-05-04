package y.q.wifisend.Utils;

import android.util.Log;
import y.q.wifisend.BuildConfig;

/**
 * Created by Cfun BuildConfig.DEBUG 2015/4/30.
 */
public class LogUtil
{
	public static void v(Object o, String message)
	{
		if (BuildConfig.DEBUG)
			Log.v(o.getClass().getSimpleName(), message);
	}

	public static void d(Object o, String message)
	{
		if (BuildConfig.DEBUG)
			Log.d(o.getClass().getSimpleName(), message);
	}

	public static void e(Object o, String message)
	{
		if (BuildConfig.DEBUG)
			Log.e(o.getClass().getSimpleName(), message);
	}

	public static void i(Object o, String message)
	{
		if (BuildConfig.DEBUG)
			Log.i(o.getClass().getSimpleName(), message);
	}

	public static void w(Object o, String message)
	{
		if (BuildConfig.DEBUG)
			Log.w(o.getClass().getSimpleName(), message);
	}


}
