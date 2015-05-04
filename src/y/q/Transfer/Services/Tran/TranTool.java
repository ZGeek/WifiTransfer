package y.q.Transfer.Services.Tran;

import android.text.TextUtils;
import y.q.Transfer.Config;
import y.q.Transfer.Expection.FirstLineFormatExpection;

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

	public static String analysisFirstLine(String line)
	{
		String[] strs = TextUtils.split(line, " ");
		if (strs.length != 3 || !strs[0].equals(Config.SendFlag))
			throw new FirstLineFormatExpection(line);
		return strs[1];
	}
}
