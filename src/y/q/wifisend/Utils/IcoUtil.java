package y.q.wifisend.Utils;


import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import y.q.wifisend.Base.BaseApplication;
import y.q.wifisend.Entry.FileType;
import y.q.wifisend.R;

import java.io.File;

/**
 * Created by Cfun on 2015/5/3.
 */
public class IcoUtil
{
	private static final float MaxHeightOrWidth = 80;

	/**
	 * 根据输入的文件类型或后缀返回程序对应的图标, 统一返回80*80等比缩放的图片
	 *
	 * @param fileType 文件类型
	 * @param filePath 文件路径
	 * @return
	 */
	public static Drawable getIco(FileType fileType, String filePath)
	{
		FileType type = fileType;
		String subfix = getFileSubFix(filePath);
		if (type == null || type == FileType.file)
		{
			type = FileType.parse(subfix);
		}
		Resources resources = BaseApplication.getInstance().getResources();
		if (type != null)
			switch (type)
			{
				case app:
					return getAPPDrawable(filePath);
				case contact:
					return resources.getDrawable(R.mipmap.contact);
				case audio:
					return resources.getDrawable(R.mipmap.audio);
				case video:
					return getVideoDrawable(filePath);
				case image:
					return getImageDrawable(filePath);
			}

		if (subfix != null)
		{
			Drawable drawable = getSubTypeIco(resources, subfix);
			if (drawable != null)
				return drawable;
		}

		return resources.getDrawable(R.mipmap.file);

	}

	/***
	 * 得到一个简单的图标，用于接收端在接收中显示的图标
	 * @param fileType
	 * @param filePath
	 * @return
	 */
	public static Drawable getSimpleIco(FileType fileType, String filePath)
	{
		FileType type = fileType;
		String subfix = getFileSubFix(filePath);
		if (type == null || type == FileType.file)
		{
			type = FileType.parse(subfix);
		}
		Resources resources = BaseApplication.getInstance().getResources();
		if (type != null)
			switch (type)
			{
				case app:
					return resources.getDrawable(R.mipmap.apk);
				case contact:
					return resources.getDrawable(R.mipmap.contact);
				case audio:
					return resources.getDrawable(R.mipmap.audio);
				case video:
					return resources.getDrawable(R.mipmap.video);
				case image:
					return resources.getDrawable(R.mipmap.image);
			}

		if (subfix != null)
		{
			Drawable drawable = getSubTypeIco(resources, subfix);
			if (drawable != null)
				return drawable;
		}

		return resources.getDrawable(R.mipmap.file);
	}

	protected static Drawable getAPPDrawable(String path)
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

	protected static Drawable getSubTypeIco(Resources resources, String subfix)
	{
		String subtype = FileType.parseSubType(subfix);
		if (subtype.equals("word"))
			return resources.getDrawable(R.mipmap.doc);
		if (subtype.equals("excel"))
			return resources.getDrawable(R.mipmap.excel);
		if (subtype.equals("db"))
			return resources.getDrawable(R.mipmap.db);
		if (subtype.equals("archive"))
			return resources.getDrawable(R.mipmap.archive);
		return null;
	}

	protected static Drawable getImageDrawable(String path)
	{
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// 设置为ture只获取图片大小
		opts.inJustDecodeBounds = true;
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		// 返回为空
		Bitmap bitmap = BitmapFactory.decodeFile(path, opts);
		if (opts.outHeight > MaxHeightOrWidth || opts.outWidth > MaxHeightOrWidth)
		{
			int oHeight = (int) MaxHeightOrWidth;
			int oWidth = (int) MaxHeightOrWidth;
			if (opts.outHeight > opts.outWidth)
			{
				oWidth = (int) (opts.outHeight / MaxHeightOrWidth * opts.outWidth);
			} else
			{
				oHeight = (int) (opts.outWidth / MaxHeightOrWidth * opts.outHeight);
			}
			bitmap = ThumbnailUtils.extractThumbnail(bitmap, oWidth, oHeight);
			if (bitmap != null)
				return new BitmapDrawable(BaseApplication.getInstance().getResources(), bitmap);
		}
		//尝试输出原文件
		bitmap = BitmapFactory.decodeFile(path);
		if (bitmap != null)
			return new BitmapDrawable(BaseApplication.getInstance().getResources(), bitmap);

		return BaseApplication.getInstance().getResources().getDrawable(R.mipmap.image);
	}

	protected static Drawable getVideoDrawable(String path)
	{
		Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MICRO_KIND);
		if (bitmap != null)
			return new BitmapDrawable(BaseApplication.getInstance().getResources(), bitmap);
		return BaseApplication.getInstance().getResources().getDrawable(R.mipmap.video);
	}

	public static String getFileSubFix(String fileName)
	{
		int index1 = fileName.lastIndexOf(File.separatorChar);
		int index2 = fileName.lastIndexOf(".");
		if (index2 <= index1)
			return "";
		return fileName.substring(index2 + 1);
	}
}
