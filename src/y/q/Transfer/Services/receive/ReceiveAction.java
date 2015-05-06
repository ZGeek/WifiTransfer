package y.q.Transfer.Services.receive;

import y.q.Transfer.Config;
import y.q.Transfer.Expection.UnExpectedEndOfStream;
import y.q.Transfer.Services.Tran.Range;
import y.q.Transfer.Services.Tran.Tran;
import y.q.wifisend.Entry.FileType;
import y.q.wifisend.Entry.SendStatus;
import y.q.wifisend.Reciver.SendStateChangedReciver;
import y.q.wifisend.Utils.InputStreamUtil;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

/**
 * Created by Cfun on 2015/4/26.
 */
public class ReceiveAction
{

	public static void doSave(boolean asyn, final String cmd, final BufferedInputStream in, final Socket clientSocket) throws IOException
	{
		Runnable runnable = new Runnable()
		{
			private String uuid;

			@Override
			public void run()
			{
				try
				{
					Tran tran = analysisHead(in);
					String filePath = makePath(tran);
					uuid = UUID.randomUUID().toString();
					Range range = tran.getRange();
					SendStateChangedReciver.sendBeginTranBroadcast(uuid, filePath, tran.getFileDesc(), range, FileType.valueOf(tran.getContentType()));
					saveFile(uuid, filePath, range.getBeginByte(), tran.getContentLength(), in);
					SendStateChangedReciver.sendStatuChangedBroadcast(uuid, SendStatus.Finish, 1);
				} catch (IOException e)
				{
					e.printStackTrace();
					SendStateChangedReciver.sendStatuChangedBroadcast(uuid, SendStatus.Error, 0);
				} finally
				{
					try
					{
						clientSocket.close();
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		};

		if (asyn)
			new Thread(runnable).start();
		else
			runnable.run();


	}

	protected static void saveFile(String uuid, String filePath, int beginByte, int contentLength, BufferedInputStream iStream) throws IOException
	{
		File file = new File(filePath);
		if (!file.exists())
			file.createNewFile();
		RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
		if (beginByte != 0) accessFile.skipBytes(beginByte);

		float oldPercent = 0;
		byte[] buffer = new byte[Config.bufferSize];
		int len = 0;
		float realFileLength = contentLength;
		while (contentLength > 0)
		{
			len = iStream.read(buffer, 0, Math.min(contentLength, buffer.length));
			if (len <= 0)
			{
				throw new UnExpectedEndOfStream("contentLength:" + contentLength + "   len:" + len);
			}
			contentLength -= len;
			accessFile.write(buffer, 0, len);
			float currentPercent = (1 - contentLength / realFileLength);
			if (currentPercent - oldPercent > 0.01)
			{
				SendStateChangedReciver.sendStatuChangedBroadcast(uuid, SendStatus.PercentChange, currentPercent);
				oldPercent = currentPercent;
			}
		}
	}

	public static String makePath(Tran tran)
	{
		String path = Config.baseDir;
		path = path + "/" + tran.getContentType() + "/" + tran.getParameter("File-Name");
		File file = new File(path);
		if (!file.exists())
			file.getParentFile().mkdirs();
		return path;
	}


	protected static Tran analysisHead(BufferedInputStream inputStream) throws IOException
	{
		Tran tran = new Tran();
		while (true)
		{
			String line = InputStreamUtil.readLineWithoutEnd(inputStream);
			if (line.length() == 0)
				break;
			tran.paseLine(line);
		}
		return tran;
	}

}
