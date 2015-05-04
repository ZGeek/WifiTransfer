package y.q.Transfer.Services.send;

import y.q.Transfer.Config;
import y.q.wifisend.Entry.SendStatus;
import y.q.wifisend.Reciver.SendStateChangedReciver;

import java.io.BufferedOutputStream;
import java.io.IOException;

/**
 * Created by Cfun on 2015/5/4.
 */
public class AllTaskFinishAction
{
	public static void doSend(BufferedOutputStream oStream)
	{
		try
		{
			oStream.write((Config.SendFlag + " "+Config.AllFinishFlag + " V1.0\r\n\r\n").getBytes("ISO8859-1"));
			oStream.flush();
			oStream.close();
		} catch (IOException e)
		{

		}
		SendStateChangedReciver.sendStatuChangedBroadcast(null, SendStatus.AllFinish, 1);
	}
}
