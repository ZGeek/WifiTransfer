package y.q.wifisend.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import y.q.wifisend.Base.BaseApplication;
import y.q.wifisend.R;

import java.io.File;

/**
 * Created by Cfun on 2015/5/6.
 */
public class OpenFiles
{
	//android获取一个用于打开HTML文件的intent
	public static Intent getHtmlFileIntent(File file)
	{
		Uri uri = Uri.parse(file.toString()).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(file.toString()).build();
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.setDataAndType(uri, "text/html");
		return intent;
	}

	//android获取一个用于打开图片文件的intent
	public static Intent getImageFileIntent(File file)
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "image/*");
		return intent;
	}

	//android获取一个用于打开PDF文件的intent
	public static Intent getPdfFileIntent(File file)
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "application/pdf");
		return intent;
	}

	//android获取一个用于打开文本文件的intent
	public static Intent getTextFileIntent(File file)
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "text/plain");
		return intent;
	}

	//android获取一个用于打开音频文件的intent
	public static Intent getAudioFileIntent(File file)
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "audio/*");
		return intent;
	}

	//android获取一个用于打开视频文件的intent
	public static Intent getVideoFileIntent(File file)
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "video/*");
		return intent;
	}


	//android获取一个用于打开CHM文件的intent
	public static Intent getChmFileIntent(File file)
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "application/x-chm");
		return intent;
	}


	//android获取一个用于打开Word文件的intent
	public static Intent getWordFileIntent(File file)
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "application/msword");
		return intent;
	}

	//android获取一个用于打开Excel文件的intent
	public static Intent getExcelFileIntent(File file)
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "application/vnd.ms-excel");
		return intent;
	}

	//android获取一个用于打开PPT文件的intent
	public static Intent getPPTFileIntent(File file)
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
		return intent;
	}

	//android获取一个用于打开apk文件的intent
	public static Intent getApkFileIntent(File file)
	{
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		return intent;
	}

	private static boolean checkEndsWithInStringArray(String checkItsEnd, String[] fileEndings)
	{
		for (String aEnd : fileEndings)
		{
			if (checkItsEnd.endsWith(aEnd))
				return true;
		}
		return false;
	}

	public static void openFile(Context context, File currentPath)
	{
		if (currentPath != null && currentPath.isFile())
		{
			String fileName = currentPath.toString();
			Intent intent;
			if (checkEndsWithInStringArray(fileName, context.getResources().
					getStringArray(R.array.fileEndingImage)))
			{
				intent = OpenFiles.getImageFileIntent(currentPath);
				context.startActivity(intent);
			} else if (checkEndsWithInStringArray(fileName, context.getResources().
					getStringArray(R.array.fileEndingWebText)))
			{
				intent = OpenFiles.getHtmlFileIntent(currentPath);
				context.startActivity(intent);
			} else if (checkEndsWithInStringArray(fileName, context.getResources().
					getStringArray(R.array.fileEndingPackage)))
			{
				intent = OpenFiles.getApkFileIntent(currentPath);
				context.startActivity(intent);

			} else if (checkEndsWithInStringArray(fileName, context.getResources().
					getStringArray(R.array.fileEndingAudio)))
			{
				intent = OpenFiles.getAudioFileIntent(currentPath);
				context.startActivity(intent);
			} else if (checkEndsWithInStringArray(fileName, context.getResources().
					getStringArray(R.array.fileEndingVideo)))
			{
				intent = OpenFiles.getVideoFileIntent(currentPath);
				context.startActivity(intent);
			} else if (checkEndsWithInStringArray(fileName, context.getResources().
					getStringArray(R.array.fileEndingText)))
			{
				intent = OpenFiles.getTextFileIntent(currentPath);
				context.startActivity(intent);
			} else if (checkEndsWithInStringArray(fileName, context.getResources().
					getStringArray(R.array.fileEndingPdf)))
			{
				intent = OpenFiles.getPdfFileIntent(currentPath);
				context.startActivity(intent);
			} else if (checkEndsWithInStringArray(fileName, context.getResources().
					getStringArray(R.array.fileEndingWord)))
			{
				intent = OpenFiles.getWordFileIntent(currentPath);
				context.startActivity(intent);
			} else if (checkEndsWithInStringArray(fileName, context.getResources().
					getStringArray(R.array.fileEndingExcel)))
			{
				intent = OpenFiles.getExcelFileIntent(currentPath);
				context.startActivity(intent);
			} else if (checkEndsWithInStringArray(fileName, context.getResources().
					getStringArray(R.array.fileEndingPPT)))
			{
				intent = OpenFiles.getPPTFileIntent(currentPath);
				context.startActivity(intent);
			} else
			{
				BaseApplication.showToast("无法打开，请安装相应的软件！");
			}
		} else
		{
			BaseApplication.showToast("对不起，这不是文件！");
		}
	}

}
