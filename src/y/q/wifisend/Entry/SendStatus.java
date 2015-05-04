package y.q.wifisend.Entry;

import java.io.Serializable;

/**
 * Created by CFun on 2015/4/21.
 */
public enum SendStatus implements Serializable
{
	SenddingBegin,
	PercentChange,
	Finish,
	Cancle,
	AllFinish,
	Error;
	}
