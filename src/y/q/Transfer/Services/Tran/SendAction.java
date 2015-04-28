package y.q.Transfer.Services.Tran;

import y.q.Transfer.Config;
import y.q.wifisend.Entry.SendFileInfo;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by Cfun on 2015/4/27.
 */
public class SendAction
{
	private Range range;
	private SendFileInfo sendFileInfo;

	public SendAction(SendFileInfo sendFileInfo, Range range)
	{
		this.sendFileInfo = sendFileInfo;
		this.range = range;
	}

	public void doSend(BufferedOutputStream oStream) throws IOException
	{
		writeFirstLine(range, oStream);
		new Tran(sendFileInfo, range).writeToOStream(oStream);
		oStream.write("\r\n".getBytes());
		writeFile2OStream(sendFileInfo.getFile(),range, oStream);
	}

	public static void writeFirstLine(Range range, BufferedOutputStream oStream) throws IOException
	{
		String cmd;
		if (range.getBeginByte() == 0)
			cmd = "send";
		else
			cmd = "send part";
		oStream.write((Config.SendFlag + " " + cmd + " " + "v1.0\r\n").getBytes());
	}

	public static void writeFile2OStream(String file, Range range, BufferedOutputStream oStream) throws IOException
	{
		RandomAccessFile accessFile = new RandomAccessFile(file, "r");
		accessFile.skipBytes(range.getBeginByte());
		int len = range.getEndByte() - range.getBeginByte() + 1;
		byte[] buffer = new byte[1024*8];
		while (len>0)
		{
			int hasRead = accessFile.read(buffer);
			if(hasRead <= 0)
				throw new IllegalStateException("File End Befor Content-Length Bytes Read");
			oStream.write(buffer, 0, hasRead);
			len -= hasRead;
		}
	}


}
