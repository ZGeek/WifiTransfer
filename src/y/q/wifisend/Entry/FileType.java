package y.q.wifisend.Entry;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by CFun on 2015/4/21.
 */
public enum FileType implements Serializable
{
	contact, app, file, image, audio, video;

	private final static String[] appSubfix = {"apk"};
	private final static String[] imageSubfix = {"bmp", "pcx", "tiff", "gif", "jpeg", "jpg", "tga", "exif", "fpx", "svg", "psd", "cdr", "pcd", "dxf", "raw", "png"};
	private final static String[] audioSubfix = {"wav", "mp3", "midi", "wma", "realaudio", "ogg", "cd", "aiff", "au", "amr"};
	private final static String[] videoSubfix = {"mp4", "3gp", "avi", "mov", "asf", "wmv", "navi", "rm", "rmvb", "flv", "f4v", "webm"};

	private final static String[] doc = {"doc", "docx", "wps"};
	private final static String[] excel = {"xls", "xlsx"};
	private final static String[] db = {"db"};
	private final static String[] archive = {"zip", "rar", "7z"};

	public static FileType parse(String subfix)
	{
		if (TextUtils.isEmpty(subfix))
			return null;
		if (isContain(subfix, appSubfix))
			return FileType.app;
		if (isContain(subfix, imageSubfix))
			return FileType.image;
		if (isContain(subfix, audioSubfix))
			return FileType.audio;
		if (isContain(subfix, videoSubfix))
			return FileType.video;

		return null;
	}

	public static String parseSubType(String subfix)
	{
		if (TextUtils.isEmpty(subfix))
			return null;
		if (isContain(subfix, doc))
			return "word";
		if (isContain(subfix, excel))
			return "excel";
		if (isContain(subfix, db))
			return "db";
		if (isContain(subfix, archive))
			return "archive";
		return null;
	}

	public static boolean isContain(String sub, String[] arr)
	{
		if (arr == null || arr.length == 0)
			return false;
		for (int i = 0; i < arr.length; i++)
		{
			if (sub.toLowerCase().equals(arr[i]))
				return true;
		}
		return false;
	}
}
