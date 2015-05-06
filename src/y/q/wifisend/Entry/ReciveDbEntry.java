package y.q.wifisend.Entry;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by Cfun on 2015/5/6.
 */
public class ReciveDbEntry extends DataSupport
{
	private int id;
	private Date insertDate;
	private String path;
	private String desc;
	private String fileType;
	private long size;
	public boolean isExists;


	public String getFileType()
	{
		return fileType;
	}

	public void setFileType(String fileType)
	{
		this.fileType = fileType;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public Date getInsertDate()
	{
		return insertDate;
	}

	public void setInsertDate(Date insertDate)
	{
		this.insertDate = insertDate;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public long getSize()
	{
		return size;
	}

	public void setSize(long size)
	{
		this.size = size;
	}
}
