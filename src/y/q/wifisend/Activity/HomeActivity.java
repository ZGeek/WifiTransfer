package y.q.wifisend.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import y.q.AppConfig;
import y.q.wifisend.Activity.Recive.ReciveActivity;
import y.q.wifisend.Activity.Send.FileChoseActivity;
import y.q.wifisend.Base.BaseActivity;
import y.q.wifisend.R;
import y.q.wifisend.Utils.BlueToothShareUtil;
import y.q.wifisend.widget.CircleImageView;

/**
 * Created by CFun on 2015/4/11.
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener
{

	private TextView sendBtn = null;
	private TextView reviveBtn = null;
	private ImageView history;

	private TextView name = null;
	private CircleImageView photo =  null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		initView();
	}

	private void initView()
	{
		history = (ImageView) findViewById(R.id.iv_history);
		reviveBtn = (TextView) findViewById(R.id.tv_recive);
		sendBtn = (TextView) findViewById(R.id.tv_send);
		findViewById(R.id.tv_inviteFriends).setOnClickListener(this);
		photo = (CircleImageView) findViewById(R.id.iv_photo);
		name = (TextView) findViewById(R.id.iv_name);

		history.setOnClickListener(this);
		reviveBtn.setOnClickListener(this);
		sendBtn.setOnClickListener(this);
		photo.setOnClickListener(this);
		photo.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if(event.getAction() == MotionEvent.ACTION_DOWN)
					photo.setBorderColor(Color.parseColor("#888888"));
				else if(event.getAction() == MotionEvent.ACTION_UP)
					photo.setBorderColor(Color.parseColor("#FFFFFF"));
				return false;
			}
		});
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		photo.setImageResource(AppConfig.getPhotoResorce());
		name.setText(AppConfig.userName);
	}

	@Override
	public void onClick(View v)
	{
		Intent intent;
		switch (v.getId())
		{
			case R.id.tv_recive:
				intent = new Intent(this, ReciveActivity.class);
				startActivity(intent);
				break;
			case R.id.tv_send:
				intent = new Intent(this, FileChoseActivity.class);
				startActivity(intent);
				break;
			case R.id.tv_inviteFriends:
				BlueToothShareUtil.DoShareFileByBt(this, getApplicationInfo().sourceDir);
				break;
			case R.id.iv_photo:
				startActivity(new Intent(this, SettingActivity.class));
				break;
			case R.id.iv_history:
				startActivity(new Intent(this, ReciveHistoryActivity.class));
		}
	}
}
