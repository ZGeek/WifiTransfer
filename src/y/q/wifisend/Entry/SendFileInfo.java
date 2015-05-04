package y.q.wifisend.Entry;

import android.text.TextUtils;
import y.q.Transfer.Services.Tran.Range;

import java.io.File;
import java.io.Serializable;
import java.util.UUID;

/**
 * Created by CFun on 2015/4/21.
 */
public class SendFileInfo implements Serializable
{
	String uuid;
	String filepath;
	String fileDesc;
	float sendPercent; //(percent为0-1之间的小数)
	SendStatus sendStatu = SendStatus.SenddingBegin;
	Range transRange;
	FileType fileType = FileType.file;

	public SendFileInfo()
	{
		uuid = UUID.randomUUID().toString();
	}
	public SendFileInfo(String uuid)
	{
		this.uuid = uuid;
	}

//	public SendFileInfo(String parser)
//	{
//		int index = parser.indexOf("://");
//		if (index < 0)
//			throw new IllegalArgumentException(parser);
//		String scama = parser.substring(0, index);
//		filepath = parser.substring(index + 3);
//		if (scama.toLowerCase().equals("contact"))
//		{
//			fileType = FileType.contact;
//		} else if ((scama.toLowerCase().equals("filepath")))
//		{
//			int dot =  parser.lastIndexOf('.');
//			String subfix = parser.substring(dot+1);
//			fileType = FileType.parse(subfix);
//		}
//		transRange = new Range();
//	}


	public String getUuid()
	{
		return uuid;
	}

	public String getFilepath()
	{
		return filepath;
	}

	public void setFilepath(String filepath)
	{
		this.filepath = filepath;
	}

	public float getSendPercent()
	{
		return sendPercent;
	}

	public void setSendPercent(float sendPercent)
	{
		this.sendPercent = sendPercent;
	}

	public SendStatus getSendStatu()
	{
		return sendStatu;
	}

	public void setSendStatu(SendStatus sendStatu)
	{
		this.sendStatu = sendStatu;
	}

	public FileType getFileType()
	{
		return fileType;
	}

	public void setFileType(FileType fileType)
	{
		this.fileType = fileType;
	}

	public Range getTransRange()
	{
		return transRange;
	}

	public void setTransRange(Range transRange)
	{
		this.transRange = transRange;
	}

	public String getFileDesc()
	{
		return fileDesc;
	}

	public void setFileDesc(String fileDesc)
	{
		this.fileDesc = fileDesc;
	}

	public int getFileLength()
	{
		if(TextUtils.isEmpty(filepath))
			return 0;
		File file = new File(filepath);
		return (int) file.length();
	}
}
