package y.q.wifisend.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import y.q.Transfer.Config;
import y.q.Transfer.Services.Tran.Range;
import y.q.Transfer.Services.Tran.Tran;
import y.q.Transfer.Services.Tran.TranTool;
import y.q.wifisend.Base.BaseFragment;
import y.q.wifisend.Entry.FileType;
import y.q.wifisend.Entry.ReciveDbEntry;
import y.q.wifisend.Entry.SendFileInfo;
import y.q.wifisend.Entry.SendStatus;
import y.q.wifisend.R;
import y.q.wifisend.Reciver.SendStateChangedReciver;
import y.q.wifisend.Utils.FileSizeFormatUtil;
import y.q.wifisend.Utils.IcoUtil;
import y.q.wifisend.Utils.OpenFiles;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Cfun on 2015/5/4.
 */
public class RecevingFragment extends BaseFragment implements SendStateChangedReciver.OnSendStateChangedListener, SendStateChangedReciver.OnBeginTranListener, View.OnClickListener, AdapterView.OnItemClickListener
{
	private ArrayList<SendFileInfo> sendFileInfos = null;
	public ArrayList<SendFileInfo> activitySendFileInfo = null;
	private ListView listView;
	private TranListAdapter adapter;

	public boolean isReady = false;
	private TextView bottomButton;


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.send_recive_list, null);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		bottomButton = (TextView) view.findViewById(R.id.bottomButton);
		bottomButton.setText("取消");
		bottomButton.setOnClickListener(this);
		view.findViewById(R.id.iv_back).setOnClickListener(this);
		((TextView) view.findViewById(R.id.tv_title)).setText(R.string.reciving);
		listView = (ListView) view.findViewById(R.id.sendList);
		sendFileInfos = new ArrayList<>();
		if (activitySendFileInfo != null)
			sendFileInfos.addAll(activitySendFileInfo);
		activitySendFileInfo.clear();
		activitySendFileInfo = null;
		adapter = new TranListAdapter(LayoutInflater.from(getActivity()), sendFileInfos);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		isReady = true;
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.iv_back:
				/** 应该先ShowPopWindow提示用户是否真的要退出*/
				getActivity().finish();
				break;
			case R.id.bottomButton:
				if (bottomButton.getText().equals("取消"))
				{
					if (getActivity() instanceof StopTran)
					{
						((StopTran) getActivity()).stopTran();
						bottomButton.setText("完成");
					}
				} else if (bottomButton.getText().equals("完成"))
				{
					getActivity().finish();
				}
				break;
		}
	}

	/**
	 * 当发送状态发生改变的时候响应此函数，用以更新UI
	 *
	 * @param uuid
	 * @param state
	 * @param percent
	 */
	@Override
	public void onSendStateChanged(String uuid, SendStatus state, float percent)
	{
		if (state == SendStatus.AllFinish) //对于AllFinish做特殊处理
		{
			bottomButton.setText("已完成");
			return;
		}

		ViewHolder holder = getViewHolderByUUID(uuid);
		if (holder == null)
		{
			int index = getIndexByUuid(uuid);
			if (index >= 0)
				listView.setSelection(index);
			holder = getViewHolderByUUID(uuid);
		}
		if (holder == null)
			return;
		if (state == SendStatus.Error) //对于Error的处理
		{
			holder.sendPercent.setTextColor(Color.RED);
		}
		holder.data.setSendStatu(state);
		if (state != SendStatus.Error)
		{
			holder.progressBar.setProgress((int) (100 * percent));
			holder.sendPercent.setText(((int) (percent * 100)) + "%");
			holder.data.setSendPercent(percent);
		}
		if (state == SendStatus.Finish)
		{
			holder.img.setImageDrawable(IcoUtil.getIco(holder.data.getFileType(), holder.data.getFilepath()));
			ReciveDbEntry entry = new ReciveDbEntry();
			entry.setDesc(holder.data.getFileDesc());
			entry.setFileType(holder.data.getFileType().toString());
			entry.setInsertDate(new Date(System.currentTimeMillis()));
			entry.setPath(holder.data.getFilepath());
			entry.setSize(holder.data.getFileLength());
			entry.save();
		}

	}

	/**
	 * 判定给定UUID的view是否在用户可见的屏幕区域
	 */
	public boolean isUUidViewVisiable(String uuid)
	{
		final int firstListItemPosition = listView.getFirstVisiblePosition();
		final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

		int index = getIndexByUuid(uuid);
		return index >= firstListItemPosition && index <= lastListItemPosition;
	}

	public int getIndexByUuid(String uuid)
	{
		if (sendFileInfos == null || sendFileInfos.size() == 0)
			return -1;
		for (int i = 0; i < sendFileInfos.size(); i++)
		{
			if (sendFileInfos.get(i).getUuid().equals(uuid))
				return i;
		}
		return -1;
	}

	public ViewHolder getViewHolderByUUID(String uuid)
	{
		final int firstListItemPosition = listView.getFirstVisiblePosition();
		final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

		int length = lastListItemPosition - firstListItemPosition + 1;
		for (int i = 0; i < length; i++)
		{
			View view = listView.getChildAt(i);
			if (uuid.equals(((ViewHolder) view.getTag()).data.getUuid()))
			{
				return (ViewHolder) view.getTag();
			}
		}
		return null;
	}

	/**
	 * 每当一个传送任务被新建时就调用此函数
	 *
	 * @param uuid     传送任务的任务id
	 * @param filePath
	 * @param fileDesc
	 * @param range
	 * @param type
	 */
	@Override
	public void onBeginTranListener(String uuid, String filePath, String fileDesc, Range range, FileType type)
	{
		SendFileInfo sendFileInfo = new SendFileInfo(uuid);
		sendFileInfo.setFilepath(filePath);
		sendFileInfo.setFileDesc(fileDesc);
		sendFileInfo.setTransRange(range);
		sendFileInfo.setFileType(type);
		sendFileInfo.setSendStatu(SendStatus.SenddingBegin);
		sendFileInfos.add(sendFileInfo);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		ViewHolder holder = (ViewHolder) view.getTag();
		String path =Config.baseDir + "/" + holder.data.getFileType() + "/" + TranTool.getFileNameByPath(holder.data.getFilepath());
		OpenFiles.openFile(getActivity(), new File(path));

	}

	class TranListAdapter extends BaseAdapter
	{
		private LayoutInflater inflater;
		private ArrayList<SendFileInfo> sendFileInfos;

		public TranListAdapter(LayoutInflater inflater, ArrayList<SendFileInfo> sendFileInfos)
		{
			this.inflater = inflater;
			this.sendFileInfos = sendFileInfos;
		}

		@Override
		public int getCount()
		{
			return sendFileInfos == null ? 0 : sendFileInfos.size();
		}

		@Override
		public Object getItem(int position)
		{
			return sendFileInfos.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			SendFileInfo data = sendFileInfos.get(position);
			if (convertView == null)
			{
				convertView = inflater.inflate(R.layout.send_recive_list_item, null);
				ViewHolder holder = new ViewHolder();
				convertView.setTag(holder);
				holder.img = (ImageView) convertView.findViewById(R.id.iv_sendImg);
				holder.fileSize = (TextView) convertView.findViewById(R.id.tv_sendSize);
				holder.sendPercent = (TextView) convertView.findViewById(R.id.tv_sendPercent);
				holder.fileName = (TextView) convertView.findViewById(R.id.tv_sendFileName);
				holder.progressBar = (ProgressBar) convertView.findViewById(R.id.pb_sendProgress);
				holder.progressBar.setMax(100);

			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.data = data;
			if (data.getSendPercent() == 1)
				holder.img.setImageDrawable(IcoUtil.getIco(data.getFileType(), data.getFilepath()));
			else
				holder.img.setImageDrawable(IcoUtil.getSimpleIco(data.getFileType(), data.getFilepath()));
			holder.fileSize.setText(FileSizeFormatUtil.format(data.getTransRange().getEndByte() - data.getTransRange().getBeginByte()));
			holder.sendPercent.setText(((int) data.getSendPercent() * 100) + "%");
			holder.fileName.setText(data.getFileDesc());
			holder.progressBar.setProgress((int) (data.getSendPercent() * 100));

			return convertView;
		}

	}

	class ViewHolder
	{
		public SendFileInfo data;
		public ImageView img;
		public TextView fileSize;
		public TextView sendPercent;
		public TextView fileName;
		public ProgressBar progressBar;
	}

	public interface StopTran
	{
		void stopTran();
	}
}
