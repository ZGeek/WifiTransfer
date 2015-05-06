package y.q.wifisend.Activity.Send;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import y.q.PageIndicator.TabPageIndicator;
import y.q.PageIndicator.TabTitleAdapter;
import y.q.PageIndicator.TabView;
import y.q.wifisend.Base.BaseActivity;
import y.q.wifisend.Entry.SendFileInfo;
import y.q.wifisend.Fragment.FileChoseFragments.*;
import y.q.wifisend.R;
import y.q.wifisend.Reciver.FileChoseChangedReciver;
import y.q.wifisend.Utils.LogUtil;
import y.q.wifisend.widget.FileChoseTab;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by CFun on 2015/4/21.
 */
public class FileChoseActivity extends BaseActivity implements FileChoseChangedReciver.OnFileChoseChangedListener, View.OnClickListener
{

	private TabPageIndicator indicator;
	private ViewPager viewPager;
	private TextView btn1;
	private TextView btn2;
	private int choseFile = 0;
	private ImageView ivBack;
	private TextView tvTitle;
	FileChoseChangedReciver fileChoseChanged;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_chose);
		fileChoseChanged = new FileChoseChangedReciver();
		fileChoseChanged.setOnFileChoseChangedListener(this);
		fileChoseChanged.registerSelf();
		initView();
	}

	@Override
	protected void onDestroy()
	{
		fileChoseChanged.unRegisterSelf();
		super.onDestroy();
	}

	private void initView()
	{
		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		btn1 = (TextView) findViewById(R.id.btn1);
		btn2 = (TextView) findViewById(R.id.btn2);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		tvTitle = (TextView) findViewById(R.id.tv_title);


		tvTitle.setText(R.string.pleaseChoseFile);
		ivBack.setOnClickListener(this);
		btn1.setEnabled(choseFile > 0);
		btn2.setEnabled(choseFile > 0);
		btn1.setText(String.format(getString(R.string.hasChosed), choseFile));
		btn2.setText("下一步");
		btn2.setOnClickListener(this);
		viewPager.setAdapter(new FileChoseAdapter(getSupportFragmentManager()));
		indicator.setViewPager(viewPager);
	}

	@Override
	public void onFileChoseChang(boolean increse)
	{
		if (increse) choseFile++;
		else choseFile--;
		btn1.setEnabled(choseFile > 0);
		btn2.setEnabled(choseFile > 0);

		btn1.setText(String.format(getString(R.string.hasChosed), choseFile));
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn2:
				Intent intent = new Intent(this, ScanReciverActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("tranList", getAllSendFileInfo());
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.iv_back:
				this.finish();
		}
	}

	private ArrayList<SendFileInfo> getAllSendFileInfo()
	{
		ArrayList<SendFileInfo> sendFileInfos = new ArrayList<>(30);
		Fragment[] fragments = ((FileChoseAdapter) viewPager.getAdapter()).getFragments();
		for (int i = 0; i < fragments.length; i++)
		{
			Collection<SendFileInfo> info = ((GetChoseFile) fragments[i]).getChosedFile();
			if (info != null)
				sendFileInfos.addAll(info);
		}
		return sendFileInfos;
	}

	class FileChoseAdapter extends FragmentPagerAdapter implements TabTitleAdapter
	{
		private String[] titles = new String[]{"文件", "应用", "联系人", "图片", "音乐", "视频"};

		Fragment[] getFragments()
		{
			return fragments;
		}

		Fragment[] fragments = new Fragment[6];

		public FileChoseAdapter(FragmentManager fm)
		{
			super(fm);
			fragments[0] = new CommonFileChoser();
			fragments[1] = new AppFileChoser();
			fragments[2] = new ContactChoser();
			fragments[3] = new ImageFileChoser();
			fragments[4] = new MusicFileChoser();
			fragments[5] = new VideoFileChoser();
		}

		@Override
		public Fragment getItem(int index)
		{
			LogUtil.d(this, index + "");
			return fragments[index];
		}

		@Override
		public TabView getTabView(int index)
		{
			FileChoseTab tab = new FileChoseTab(FileChoseActivity.this, index);
			tab.setTabText(titles[index]);
			return tab;
		}

		@Override
		public int getCount()
		{
			return titles.length;
		}
	}
}
