package y.q.wifisend.Base;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;


/**
 * Created by CFun on 2015/4/11.
 */
public class BaseApplication extends Application
{
	static Application instance;
	static Handler handler;

	@Override
	public void onCreate()
	{
		super.onCreate();
		instance = this;
		handler = new Handler(Looper.getMainLooper());
	}

	public static Application getInstance()
	{
		return instance;
	}

	public static void showToast(final String msg)
	{
		if (handler != null)
			handler.post(new Runnable()
			{
				@Override
				public void run()
				{
					Toast.makeText(instance, msg, Toast.LENGTH_LONG).show();
				}
			});
	}
}
