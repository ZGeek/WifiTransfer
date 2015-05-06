package y.q.Transfer.Services.Tran;

import android.text.TextUtils;
import y.q.wifisend.Entry.SendFileInfo;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Cfun on 2015/4/26.
 */
public class Tran
{
	private Map<String, String> heads;

	public Tran()
	{
		heads = new HashMap<>();
	}

	public Tran(SendFileInfo info, Range range)
	{
		heads = new HashMap<>();

		setParameter("Content-Length", String.valueOf(range.endByte - range.beginByte));
		setParameter("Range", range.beginByte + "-" + range.getEndByte());
		setParameter("Content-Type", "" + info.getFileType());
		setParameter("File-Name", TranTool.getFileNameByPath(info.getFilepath()));
		setParameter("File-Desc", info.getFileDesc());
	}

	public void setParameter(String key, String value)
	{
		try
		{
			if (key.equals("File-Name") || key.equals("File-Desc"))
				value = URLEncoder.encode(value, "UTF-8");
			heads.put(key, value);
		} catch (UnsupportedEncodingException e)
		{
		}

	}

	public String getParameter(String key)
	{
		String str = heads.get(key);
		if (key.equals("File-Name") || key.equals("File-Desc"))
		{
			try
			{
				return   URLDecoder.decode(str, "UTF-8");
			} catch (UnsupportedEncodingException e)
			{
			}
		}
		return str;
	}


	public Range getRange()
	{
		String range = heads.get("Range");
		if (range != null && !TextUtils.isEmpty(range))
			return new Range(range);
		return null;
	}

	public int getContentLength()
	{
		String len = heads.get("Content-Length");
		if (TextUtils.isEmpty(len))
			return 0;
		return Integer.parseInt(len);
	}

	public String getFileDesc()
	{
		return getParameter("File-Desc");
	}

	public String getContentType()
	{
		return getParameter("Content-Type");
	}

	public void paseLine(String line)
	{
		int index = line.indexOf(": ");
		if (index < 0)
			throw new IllegalArgumentException(line);
		String key = line.substring(0, index);
		String value = line.substring(index + 2);
//		this.setParameter(key, value);
		heads.put(key, value);
	}

	public void writeToOStream(OutputStream outputStream) throws IOException
	{
		Iterator<Map.Entry<String, String>> iterator = heads.entrySet().iterator();
		if (iterator != null)
			while (iterator.hasNext())
			{
				Map.Entry<String, String> item = iterator.next();
				outputStream.write((item.getKey() + ": " + item.getValue() + "\r\n").getBytes());
			}
	}

}
