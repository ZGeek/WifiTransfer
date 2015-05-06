package y.q;

import y.q.wifisend.R;

/**
 * Created by Cfun on 2015/5/1.
 */
public class AppConfig
{
	public static int photos[] = {
			R.mipmap.head_portrait_1,
			R.mipmap.head_portrait_2,
			R.mipmap.head_portrait_3,
			R.mipmap.head_portrait_4,
			R.mipmap.head_portrait_5,
			R.mipmap.head_portrait_6,
			R.mipmap.head_portrait_7,
			R.mipmap.head_portrait_8
	};
	public static int photoId = 0;
	public static String userName = "BayMax";
	public static final int port = 60666;

	public static final String settingName = "setting";

	public static int getPhotoResorce()
	{
		return photos[photoId%photos.length];
	}
	public static int getPhotoResorce(int pos)
	{
		return photos[pos%photos.length];
	}
}
