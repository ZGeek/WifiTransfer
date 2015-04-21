package y.q.wifisend.Activity;

import android.os.Bundle;
import y.q.wifisend.Base.BaseActivity;
import y.q.wifisend.R;

/**
 * Created by CFun on 2015/4/21.
 */

/***
 * 显示正在发送文件的文件和进度的Activity
 */
public class SendingActivity  extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_list);
	}
}
