package y.q.Transfer.Services.Tran;

import java.io.File;

/**
 * Created by Cfun on 2015/4/27.
 */
public class TranTool
{
	public static String getFileNameByPath(String path)
	{
		int index = path.lastIndexOf(File.separatorChar);
		return path.substring(index+1);
	}
	public static String getTypeBySubfix(String subFix)
	{
		if("apk".equals(subFix))
			return "app";
		if("mp3".equals(subFix) || "wma".equals(subFix) || "ogg".equals(subFix))
			return "audio";
		if("mp4".equals(subFix) || "rm".equals(subFix) || "rmvb".equals(subFix) || "avi".equals(subFix) || "3gp".equals(subFix))
			return "video";
		if("png".equals(subFix) || "bmp".equals(subFix) || "jpg".equals(subFix) || "gif".equals(subFix))
			return "picture";
		return "file";
	}
	public static String getSubFix(String fileName)
	{
		int index1 = fileName.lastIndexOf(File.separatorChar);
		int index2 = fileName.lastIndexOf(".");
		if(index2 <= index1)
			return "";
		return fileName.substring(index2+1);
	}
}
