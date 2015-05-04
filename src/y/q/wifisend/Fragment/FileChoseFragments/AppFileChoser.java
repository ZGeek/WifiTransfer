package y.q.wifisend.Fragment.FileChoseFragments;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import y.q.wifisend.Base.BaseApplication;
import y.q.wifisend.Base.BaseFragment;
import y.q.wifisend.Entry.FileType;
import y.q.wifisend.R;
import y.q.wifisend.Reciver.FileChoseChangedReciver;
import y.q.wifisend.Utils.FileSizeFormatUtil;

import java.io.File;
import java.util.*;

/**
 * Created by CFun on 2015/4/21.
 */
public class AppFileChoser extends BaseFragment implements AdapterView.OnItemClickListener
{
	private GridView gridView;
	private Context context;
	private View mView;
	private List<PackageInfo> userApp;
	private HashSet<String> fileChose = new HashSet<>();
	private static final FileType type = FileType.app;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (mView != null)
		{
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null)
			{
				parent.removeView(mView);
			}
			return mView;
		}
		context = inflater.getContext();
		gridView = new GridView(context);
		gridView.setNumColumns(4);
		if (userApp == null) userApp = getAllUserAppInfo();
		gridView.setAdapter(new AppAdapter(inflater, userApp));
		gridView.setOnItemClickListener(this);
		mView = gridView;
		return gridView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	public static List<PackageInfo> getAllUserAppInfo()
	{
		List<PackageInfo> packages = BaseApplication.getInstance().getPackageManager().getInstalledPackages(0);
		Iterator<PackageInfo> iterator = packages.iterator();
		List<PackageInfo> ps = new LinkedList<>();
		while (iterator.hasNext())
		{
			PackageInfo temp = iterator.next();
			if ((temp.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0)
				ps.add(temp);
		}
		return ps;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		ViewHolder holder = ((ViewHolder) view.getTag());
		if (holder.ivChecked.getVisibility() == View.VISIBLE)
		{
			holder.ivChecked.setVisibility(View.GONE);
			FileChoseChangedReciver.sendBroadcast(false, holder.filePath, type);
			fileChose.remove(holder.filePath);
		} else
		{
			holder.ivChecked.setVisibility(View.VISIBLE);
			FileChoseChangedReciver.sendBroadcast(true, holder.filePath, type);
			fileChose.add(holder.filePath);
		}
	}



	class AppAdapter extends BaseAdapter
	{
		private LayoutInflater inflater;
		private List<PackageInfo> appInfos;
		private PackageManager packageManager;

		public AppAdapter(LayoutInflater inflater, List<PackageInfo> appInfos)
		{
			this.inflater = inflater;
			this.appInfos = appInfos;
			packageManager = BaseApplication.getInstance().getPackageManager();
		}

		@Override
		public int getCount()
		{
			return appInfos == null ? 0 : appInfos.size();
		}

		@Override
		public Object getItem(int position)
		{
			return appInfos.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			PackageInfo info = appInfos.get(position);
			if (convertView == null)
			{
				ViewHolder holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.app_choser_item, null);
				holder.appIcon = (ImageView) convertView.findViewById(R.id.appIcon);
				holder.appName = (TextView) convertView.findViewById(R.id.appName);
				holder.appSize = (TextView) convertView.findViewById(R.id.appSize);
				holder.ivChecked = (ImageView) convertView.findViewById(R.id.iv_checked);
				convertView.setTag(holder);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.appIcon.setImageDrawable(info.applicationInfo.loadIcon(packageManager).getCurrent());
			holder.appName.setText(info.applicationInfo.loadLabel(packageManager));
			holder.filePath = info.applicationInfo.sourceDir;
			holder.ivChecked.setVisibility(fileChose.contains(holder.filePath) ? View.VISIBLE : View.GONE);

			long  big = new File(info.applicationInfo.sourceDir).length();
			holder.appSize.setText(FileSizeFormatUtil.format(big));

			return convertView;
		}
	}

	class ViewHolder
	{
		public String filePath;
		public ImageView appIcon;
		public TextView appName;
		public TextView appSize;
		public ImageView ivChecked;
	}
}
