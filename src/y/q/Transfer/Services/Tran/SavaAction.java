package y.q.Transfer.Services.Tran;

import android.text.TextUtils;
import y.q.Transfer.Config;
import y.q.Transfer.Expection.FirstLineFormatExpection;
import y.q.Transfer.Expection.UnExpectedEndOfStream;
import y.q.wifisend.Utils.InputStreamUtil;

import java.io.*;

/**
 * Created by Cfun on 2015/4/26.
 */
public class SavaAction
{
	BufferedOutputStream out;
	BufferedInputStream in;

	public SavaAction(InputStream in, OutputStream out)
	{
		this.out = new BufferedOutputStream(out);
		this.in = new BufferedInputStream(in);
	}

	public void doSave() throws IOException
	{
		String line = InputStreamUtil.readLineWithoutEnd(in);
		String cmd =analysisFirstLine(line);
		Tran tran = analysisHead(in);
		if (cmd.equals("send"))
		{
			int length = tran.getContentLength();
			saveFile(makePath(tran), 0, length, in);
		} else if (cmd.equals("send-part"))
		{
			Range range = tran.getRange();
			int length = range.getEndByte() - range.getBeginByte() + 1;
			saveFile(makePath(tran), range.getBeginByte(), length, in);
		}
	}

	protected void saveFile(String filePath, int beginByte, int contentLength, BufferedInputStream iStream) throws IOException
	{
		File file = new File(filePath);
		if (!file.exists())
			file.createNewFile();
		RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
		accessFile.skipBytes(beginByte);

		byte[] buffer = new byte[1024 * 8];
		int len = 0;
		while (contentLength > 0)
		{
			len = iStream.read(buffer, 0, contentLength);
			if (len <= 0)
			{
				throw new UnExpectedEndOfStream();
			}
			contentLength -= len;
			accessFile.write(buffer, 0, len);
		}
	}

	protected String makePath(Tran tran)
	{
		String path = Config.baseDir;
		path = path + "/" + tran.getContentType() + "/" + tran.getParameter("File-Name");
		return path;
	}

	protected String analysisFirstLine(String line)
	{
		String[] strs = TextUtils.split(line, " ");
		if (strs.length != 2 || !strs[0].equals(Config.SendFlag))
			throw new FirstLineFormatExpection(line);
		return strs[1];
	}

	protected Tran analysisHead(BufferedInputStream inputStream) throws IOException
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
