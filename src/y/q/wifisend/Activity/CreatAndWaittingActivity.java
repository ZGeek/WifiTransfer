package y.q.wifisend.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import y.q.wifisend.Base.BaseActivity;
import y.q.wifisend.R;

/**
 * Created by CFun on 2015/4/21.
 */

/***
 * 接收数据放建立热点并等待连接的ACtivity
 */
public class CreatAndWaittingActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ap_waitting);

	}

	private void initView()
	{
		View view = findViewById(R.id.scanAnim);
		int width =  view.getWidth();
		ViewGroup.LayoutParams params =  view.getLayoutParams();
		params.height = width;
		view.setLayoutParams(params);
	}
}
