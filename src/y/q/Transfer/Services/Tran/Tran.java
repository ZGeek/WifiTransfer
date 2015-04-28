package y.q.Transfer.Services.Tran;

import android.text.TextUtils;
import y.q.wifisend.Entry.SendFileInfo;

import java.io.BufferedOutputStream;
import java.io.IOException;
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
		heads.put("Content-Length", String.valueOf(range.endByte - range.beginByte +1));
		heads.put("Range", range.beginByte+"-"+range.getEndByte());
		heads.put("File-Name", TranTool.getFileNameByPath(info.getFile()));
		heads.put("Content-Type", TranTool.getTypeBySubfix(TranTool.getSubFix(info.getFile())));
	}
	public void setParameter(String key, String value)
	{
		heads.put(key, value);
	}

	public String getParameter(String key)
	{
		return heads.get(key);
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

	public String getContentType()
	{
		return heads.get("Content-Type");
	}
	public void paseLine(String line)
	{
		int index = line.indexOf(": ");
		if(index <0 )
			throw new IllegalArgumentException(line);
		String key = line.substring(0, index);
		String value = line.substring(index+2);
		this.setParameter(key, value);
	}

	public void writeToOStream(BufferedOutputStream outputStream) throws IOException
	{
		Iterator<Map.Entry<String, String>> iterator = heads.entrySet().iterator();
		if(iterator != null)
			while (iterator.hasNext())
			{
				Map.Entry<String, String> item = iterator.next();
				outputStream.write((item.getKey()+": "+item.getValue()+"\r\n").getBytes());
			}
	}

}
