package y.q.wifisend.Utils;

/**
 * Created by Cfun on 2015/5/5.
 */

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import y.q.wifisend.Base.BaseApplication;

import java.io.File;

/**
 * 用于生成缩略图
 */
public class MyThumbnailUtil
{
	/**
	 * 根据指定的图像路径和大小来获取缩略图 (裁剪方式)
	 *
	 * @param imagePath 图像的路径
	 * @param kind      指定输出图像的类型 MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
	 * @return 生成的缩略图
	 */
	public static Bitmap getImageThumbnail(String imagePath, int kind)
	{
		int width = 96;
		int height = 96;
		if (kind == MediaStore.Images.Thumbnails.MINI_KIND)
		{
			width = 512;
			height = 384;
		}
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight)
		{
			be = beWidth;
		} else
		{
			be = beHeight;
		}
		if (be <= 0)
		{
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}


	/**
	 * 获取视频的缩略图
	 * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
	 * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
	 *
	 * @param videoPath 视频的路径
	 * @param width     指定输出视频缩略图的宽度
	 * @param height    指定输出视频缩略图的高度度
	 * @param kind      参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
	 *                  其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
	 * @return 指定大小的视频缩略图
	 */
	public static Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind)
	{
		Bitmap bitmap = null;
		// 获取视频的缩略图
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	public static Bitmap getImageThumbnailBySystem(long origId, int kind, @Nullable BitmapFactory.Options options)
	{
		return MediaStore.Images.Thumbnails.getThumbnail(BaseApplication.getInstance().getContentResolver(), origId, kind, options);
	}

	public static Bitmap getVideoThumbnailBySystem(long origId, int kind, @Nullable BitmapFactory.Options options)
	{
		return MediaStore.Video.Thumbnails.getThumbnail(BaseApplication.getInstance().getContentResolver(), origId, kind, options);
	}

	/**
	 * @param videoID video的id
	 * @param kind    参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
	 *                其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
	 * @return
	 */
	public static Bitmap getVideoThumbnailByID(long videoID, int kind)
	{
		if (kind != MediaStore.Images.Thumbnails.MINI_KIND)
			kind = MediaStore.Images.Thumbnails.MICRO_KIND;


		String[] projection = {MediaStore.Video.Thumbnails._ID, MediaStore.Video.Thumbnails.DATA};
		String selection = MediaStore.Video.Thumbnails.VIDEO_ID + "=? and " + MediaStore.Video.Thumbnails.KIND + " = ?";
		String[] selectionArgs = {"" + videoID, "" + kind};
		Cursor cursor = BaseApplication.getInstance().getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);
		int count = cursor.getCount();
		if (count == 1) //如果找到就返回bitmap
		{
			cursor.moveToNext();
			String path = cursor.getString(1);
			if (new File(path).exists())
			{
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.RGB_565;
				cursor.close();
				return BitmapFactory.decodeFile(path, options);
			}
		}
		else if(kind == MediaStore.Images.Thumbnails.MICRO_KIND)
		{
			//如果是查找小图片未找到，则进行大图查找，看是否能找到
			Bitmap bitmap = getVideoThumbnailByID(videoID, MediaStore.Images.Thumbnails.MINI_KIND);
			if(bitmap != null)
			{
				//找到大图，则对大图进行压缩成小图进行返回
				cursor.close();
				return ThumbnailUtils.extractThumbnail(bitmap, 96, 96, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
			}
		}
		cursor.close();
		return null;
	}
	/**
	 * @param imageID vimage的id
	 * @param kind    参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
	 *                其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
	 * @return
	 */
	public static Bitmap getImageThumbnailByID(long imageID, int kind)
	{
		if (kind != MediaStore.Images.Thumbnails.MINI_KIND)
			kind = MediaStore.Images.Thumbnails.MICRO_KIND;


		String[] projection = {MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.DATA};
		String selection = MediaStore.Images.Thumbnails.IMAGE_ID + "=? and " + MediaStore.Images.Thumbnails.KIND + " = ?";
		String[] selectionArgs = {"" + imageID, "" + kind};
		Cursor cursor = BaseApplication.getInstance().getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);
		int count = cursor.getCount();
		if (count == 1) //如果找到就返回bitmap
		{
			cursor.moveToNext();
			String path = cursor.getString(1);
			if (new File(path).exists())
			{
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.RGB_565;
				cursor.close();
				return BitmapFactory.decodeFile(path, options);
			}
		}
		else if(kind == MediaStore.Images.Thumbnails.MICRO_KIND)
		{
			//如果是查找小图片未找到，则进行大图查找，看是否能找到
			Bitmap bitmap = getVideoThumbnailByID(imageID, MediaStore.Images.Thumbnails.MINI_KIND);
			if(bitmap != null)
			{
				//找到大图，则对大图进行压缩成小图进行返回
				cursor.close();
				return ThumbnailUtils.extractThumbnail(bitmap, 96, 96, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
			}
		}
		cursor.close();
		return null;
	}
}
