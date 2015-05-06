package y.q.wifisend.Activity;

/**
 * Created by CFun on 2015/4/21.
 */

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.litepal.crud.DataSupport;
import y.q.Transfer.Config;
import y.q.wifisend.Base.BaseActivity;
import y.q.wifisend.Base.BaseApplication;
import y.q.wifisend.Entry.FileType;
import y.q.wifisend.Entry.ReciveDbEntry;
import y.q.wifisend.R;
import y.q.wifisend.Utils.FileSizeFormatUtil;
import y.q.wifisend.Utils.IcoUtil;
import y.q.wifisend.Utils.OpenFiles;

import java.io.File;
import java.util.List;

/**
 * 显示接收历史的Activity
 */
public class ReciveHistoryActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener
{
	private ImageView back;
	private TextView title;
	private ListView listView;
	private TextView cleanAll;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recive_history);

		back = (ImageView) findViewById(R.id.iv_back);
		title = (TextView) findViewById(R.id.tv_title);
		listView = (ListView) findViewById(R.id.listView);
		cleanAll = (TextView) findViewById(R.id.cleanAll);

		cleanAll.setOnClickListener(this);
		back.setOnClickListener(this);

		title.setText(R.string.history);
		HistoryAdapter adapter = new HistoryAdapter(DataSupport.findAll(ReciveDbEntry.class));
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		ViewHolder holder = (ViewHolder) view.getTag();
		if (holder.data.isExists)
			OpenFiles.openFile(this, new File(holder.data.getPath()));
		else
			BaseApplication.showToast(getString(R.string.fileNotFound));
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.cleanAll:
				DataSupport.deleteAll(ReciveDbEntry.class);
				BaseApplication.showToast(getString(R.string.reciveHistoryCleanAll));
				finish();
				break;
			case R.id.iv_back:
				finish();
				break;
		}
	}

	class HistoryAdapter extends BaseAdapter
	{
		private List<ReciveDbEntry> entries = null;
		private LayoutInflater inflater;

		public HistoryAdapter(List<ReciveDbEntry> entries)
		{
			this.entries = entries;
			inflater = LayoutInflater.from(ReciveHistoryActivity.this);
		}

		@Override
		public int getCount()
		{
			return entries == null ? 0 : entries.size();
		}

		@Override
		public Object getItem(int position)
		{
			return entries.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
			{
				ViewHolder holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.recive_history_list_item, null);
				holder.ico = (ImageView) convertView.findViewById(R.id.ico);
				holder.desc = (TextView) convertView.findViewById(R.id.desc);
				holder.path = (TextView) convertView.findViewById(R.id.path);

				convertView.setTag(holder);
			}


			ViewHolder holder = (ViewHolder) convertView.getTag();

			holder.data = entries.get(position);//数据绑定
			holder.size = (TextView) convertView.findViewById(R.id.size);

			//利用数据设置控件
			holder.ico.setImageDrawable(IcoUtil.getIco(FileType.valueOf(holder.data.getFileType()), holder.data.getPath()));
			holder.path.setText(holder.data.getPath().substring((Config.baseDir).length() + 1));
			holder.desc.setText(holder.data.getDesc());
			holder.size.setText(FileSizeFormatUtil.format(holder.data.getSize()));
			holder.data.isExists = new File(holder.data.getPath()).exists();
			if (holder.data.isExists)
			{
				holder.desc.setTextColor(holder.size.getCurrentTextColor());
				holder.desc.getPaint().setFlags(Paint.DEV_KERN_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
			} else
			{
				holder.desc.setTextColor(Color.RED);
				holder.desc.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
			}

			return convertView;
		}
	}

	class ViewHolder
	{
		public ReciveDbEntry data;
		public ImageView ico;
		public TextView desc;
		public TextView path;
		public TextView size;
	}
}
