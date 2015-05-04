package y.q.Transfer.Services.send;

import y.q.Transfer.Config;
import y.q.Transfer.Services.Tran.Range;
import y.q.Transfer.Services.Tran.Tran;
import y.q.wifisend.Entry.SendFileInfo;
import y.q.wifisend.Entry.SendStatus;
import y.q.wifisend.Reciver.SendStateChangedReciver;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by Cfun on 2015/4/27.
 */
public class SendAction
{

	public static void doSend(BufferedOutputStream oStream, SendFileInfo sendFileInfo) throws IOException
	{
		Range range = sendFileInfo.getTransRange();
		writeFirstLine(range, oStream);
		new Tran(sendFileInfo, range).writeToOStream(oStream);
		oStream.write("\r\n".getBytes("ISO8859-1"));
		writeFile2OStream(sendFileInfo.getUuid(), sendFileInfo.getFilepath(),range, oStream);
	}

	public static void writeFirstLine(Range range, BufferedOutputStream oStream) throws IOException
	{
		String cmd;
		if (range.getBeginByte() == 0)
			cmd = "send";
		else
			cmd = "send part";
		oStream.write((Config.SendFlag + " " + cmd + " " + "v1.0\r\n").getBytes("ISO8859-1"));
	}

	public static void writeFile2OStream(String uuid, String file, Range range, BufferedOutputStream oStream) throws IOException
	{
		RandomAccessFile accessFile = new RandomAccessFile(file, "r");
		accessFile.skipBytes(range.getBeginByte());
		int realFileLength = range.getEndByte() - range.getBeginByte();
		int len = range.getEndByte() - range.getBeginByte();
		byte[] buffer = new byte[Config.bufferSize];
		while (len>0)
		{
			int hasRead = accessFile.read(buffer);
			if(hasRead <= 0)
				throw new IllegalStateException("File End Befor Content-Length Bytes Read");
			oStream.write(buffer, 0, hasRead);
			len -= hasRead;
			SendStateChangedReciver.sendStatuChangedBroadcast(uuid, len==0?SendStatus.Finish : SendStatus.PercentChange, 1-len/realFileLength);
			oStream.flush();
		}
	}
}
