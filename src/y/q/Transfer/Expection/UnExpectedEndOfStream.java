package y.q.Transfer.Expection;

import java.io.IOException;

/**
 * Created by Cfun on 2015/4/27.
 */
public class UnExpectedEndOfStream extends IOException
{
	public UnExpectedEndOfStream()
	{
	}

	public UnExpectedEndOfStream(String detailMessage)
	{
		super(detailMessage);
	}
}
