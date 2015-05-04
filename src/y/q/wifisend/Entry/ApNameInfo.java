package y.q.wifisend.Entry;

import java.io.Serializable;

/**
 * Created by Cfun on 2015/5/1.
 */
public class ApNameInfo implements Serializable
{
	String name;
	int photoId;

	public ApNameInfo()
	{
	}

	public ApNameInfo(String name, int photoId)
	{
		this.name = name;
		this.photoId = photoId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getPhotoId()
	{
		return photoId;
	}

	public void setPhotoId(int photoId)
	{
		this.photoId = photoId;
	}

	@Override
	public boolean equals(Object o)
	{
		if(o instanceof ApNameInfo)
		{
			ApNameInfo info = (ApNameInfo)o;
			if(this.name.equals(info.getName()) && this.photoId == info.getPhotoId())
				return true;
		}
		return false;
	}
}
