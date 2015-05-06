package y.q.wifisend.Base;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import org.litepal.LitePalApplication;
import y.q.AppConfig;
import y.q.wifisend.R;


/**
 * Created by CFun on 2015/4/11.
 */
public class BaseApplication extends LitePalApplication
{
	static Application instance;
	static Handler handler;

	@Override
	public void onCreate()
	{
		super.onCreate();
		instance = this;
		handler = new Handler(Looper.getMainLooper());
		ImageLoader.getInstance().init(getImageLoaderConfig());
//		sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,Uri.parse("file://" + Environment.getExternalStorageDirectory())));
		SharedPreferences preferences = getSharedPreferences(AppConfig.settingName, MODE_PRIVATE);
		AppConfig.userName = preferences.getString("name", "BayMax");
		AppConfig.photoId = preferences.getInt("photoId", 0);
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

	private ImageLoaderConfiguration getImageLoaderConfig()
	{
		return new ImageLoaderConfiguration.Builder(this)
				.memoryCacheExtraOptions(384, 512) // max width, max height，即保存的每个缓存文件的最大长宽
				.threadPoolSize(4)//线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 1)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
				.memoryCacheSize(2 * 1024 * 1024)
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.defaultDisplayImageOptions(getDefaultDisplayImageOptions())
				.imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
				.writeDebugLogs() // Remove for release app
				.build();//开始构建
	}
	private static DisplayImageOptions getDefaultDisplayImageOptions()
	{
			return   new DisplayImageOptions.Builder()
				.showImageOnLoading(R.mipmap.image) //设置图片在下载期间显示的图片
				.showImageForEmptyUri(R.mipmap.image)//设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.mipmap.image)  //设置图片加载/解码过程中错误时候显示的图片
				.cacheInMemory(true)//设置下载的图片是否缓存在内存中
				.considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
//				.decodingOptions(android.graphics.BitmapFactory.Options decodingOptions)//设置图片的解码配置
//.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
//设置图片加入缓存前，对bitmap进行设置
//.preProcessor(BitmapProcessor preProcessor)
				.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
				.displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
				.displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
				.build();
	}
}
