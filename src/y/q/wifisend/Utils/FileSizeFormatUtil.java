package y.q.wifisend.Utils;

/**
 * Created by Cfun on 2015/5/3.
 */
public class FileSizeFormatUtil
{
	public static final int kb = 1024;
	public static final int mb = kb*1024;
	public static final int gb = mb*1024;

	public static String format(float fileSize)
	{

		if(fileSize > gb)
			return ((int)(fileSize/gb*100))/100.0 +"G";
		if(fileSize > mb)
			return ((int)(fileSize/mb*100))/100.0 +"M";
		if(fileSize > kb)
			return ((int)(fileSize/kb*100))/100.0 +"K";
		return ((int)fileSize)+"B";
	}
}
