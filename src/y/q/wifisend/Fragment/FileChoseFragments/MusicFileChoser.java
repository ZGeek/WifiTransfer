package y.q.wifisend.Fragment.FileChoseFragments;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import y.q.Transfer.Services.Tran.Range;
import y.q.Transfer.Services.Tran.TranTool;
import y.q.wifisend.Base.BaseFragment;
import y.q.wifisend.Entry.FileType;
import y.q.wifisend.Entry.SendFileInfo;
import y.q.wifisend.Entry.SendStatus;
import y.q.wifisend.R;
import y.q.wifisend.Reciver.FileChoseChangedReciver;
import y.q.wifisend.Utils.FileSizeFormatUtil;
import y.q.wifisend.Utils.MusicUtils;
import y.q.wifisend.Utils.MyThumbnailUtil;

import java.util.*;

/**
 * Created by CFun on 2015/4/21.
 */
public class MusicFileChoser extends BaseFragment implements GetChoseFile, AdapterView.OnItemClickListener
{
	private ListView listView;
	private Context context;
	private View mView;
	private HashMap<String, String> selectPathTitle = new HashMap<>();

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
		listView = new ListView(context);
		listView.setAdapter(new ImageAdapter(context, getImageCursor(context), false));
		listView.setOnItemClickListener(this);
		mView = listView;
		return listView;
	}


	private Cursor getImageCursor(Context context)
	{
		// 指定要查询的uri资源
		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		// 获取ContentResolver
		ContentResolver contentResolver = context.getContentResolver();
		// 查询的字段
		String[] projection = {
				MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.ALBUM_ID,
				MediaStore.Audio.Media.DATA,//路径
				MediaStore.Audio.Media.TITLE,//歌曲名称
				MediaStore.Audio.Media.ARTIST,//歌手
				MediaStore.Audio.Media.SIZE
		};
		// 条件
//		String selection = MediaStore.Images.Media.MIME_TYPE + "=?";
		// 条件值(這裡的参数不是图片的格式，而是标准，所有不要改动)
//		String[] selectionArgs = {"image/jpeg"};
		// 排序
		String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " desc";
		// 查询sd卡上的图片
		Cursor cursor = contentResolver.query(uri, projection, null, null, sortOrder);
		return cursor;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		ViewHolder holder = (ViewHolder) view.getTag();
		if (holder.check.getVisibility() == View.VISIBLE)
		{
			holder.check.setVisibility(View.GONE);
			selectPathTitle.remove(holder.data.path);
			FileChoseChangedReciver.sendBroadcast(false);
		} else
		{
			holder.check.setVisibility(View.VISIBLE);
			selectPathTitle.put(holder.data.path, holder.data.title);
			FileChoseChangedReciver.sendBroadcast(true);
		}
	}

	@Override
	public Collection<SendFileInfo> getChosedFile()
	{
		LinkedList<SendFileInfo> sendFileInfos = new LinkedList<>();
		Iterator<Map.Entry<String, String>> iterator = selectPathTitle.entrySet().iterator();
		while (iterator.hasNext())
		{
			Map.Entry<String, String> var = iterator.next();
			String path = var.getKey();
			String des = var.getKey();
			SendFileInfo info = new SendFileInfo();
			info.setFileType(FileType.audio);
			info.setFilepath(path);
			info.setTransRange(Range.getByPath(path));
			info.setSendStatu(SendStatus.SenddingBegin);
			info.setFileDesc(des);
			info.setSendPercent(0);
			sendFileInfos.add(info);
		}
		return sendFileInfos;
	}

	class ImageAdapter extends CursorAdapter
	{
		LayoutInflater inflater;

		public ImageAdapter(Context context, Cursor c, boolean autoRequery)
		{
			super(context, c, autoRequery);
			inflater = LayoutInflater.from(context);
		}

		public ImageAdapter(Context context, Cursor c, int flags)
		{
			super(context, c, flags);
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent)
		{
			ViewHolder holder = new ViewHolder();

			holder.data.id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
			holder.data.path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
			holder.data.title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
			holder.data.artis = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
			holder.data.size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
			holder.data.albumid = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
			View view = inflater.inflate(R.layout.audio_choser_item, null);

			holder.img = (ImageView) view.findViewById(R.id.img);
			holder.check = (ImageView) view.findViewById(R.id.check);
			holder.title = (TextView) view.findViewById(R.id.audioName);
			holder.artil = (TextView) view.findViewById(R.id.artile);
			holder.size = (TextView) view.findViewById(R.id.size);


			holder.check.setVisibility(selectPathTitle.keySet().contains(holder.data.path) ? View.VISIBLE : View.GONE);
			holder.title.setText(holder.data.title);
			holder.size.setText(FileSizeFormatUtil.format(holder.data.size));
			holder.artil.setText(holder.data.artis);

			view.setTag(holder);


			holder.img.setImageDrawable(new BitmapDrawable(context.getResources(), MusicUtils.getArtwork(context, holder.data.id, holder.data.albumid, true)));
			return view;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor)
		{
			ViewHolder holder = (ViewHolder) view.getTag();

			holder.data.id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
			holder.data.path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
			holder.data.title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
			holder.data.artis = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
			holder.data.size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
			holder.data.albumid = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

			holder.check.setVisibility(selectPathTitle.keySet().contains(holder.data.path) ? View.VISIBLE : View.GONE);
			holder.title.setText(holder.data.title);
			holder.size.setText(FileSizeFormatUtil.format(holder.data.size));
			holder.artil.setText(holder.data.artis);


			holder.img.setImageDrawable(new BitmapDrawable(context.getResources(), MusicUtils.getArtwork(context, holder.data.id, holder.data.albumid, true)));
		}


	}

	class ViewDate
	{
		/**
		 * MediaStore.Audio.Media._ID,
		 * MediaStore.Audio.Media.DATA,//路径
		 * MediaStore.Audio.Media.TITLE,//歌曲名称
		 * MediaStore.Audio.Media.ARTIST//歌手
		 */
		public long id;
		public long albumid;
		public String path;
		public String artis;
		public String title;
		public long size;
	}

	class ViewHolder
	{
		public ViewDate data;
		public ImageView img;
		public ImageView check;
		public TextView title;
		public TextView artil;
		public TextView size;

		public ViewHolder()
		{
			data = new ViewDate();
		}
	}
}
