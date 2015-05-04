package y.q.wifisend.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import y.q.AppConfig;
import y.q.wifisend.Base.BaseFragment;
import y.q.wifisend.R;
import y.q.wifisend.Reciver.RecvingPrepareStateChangReciver;

/**
 * Created by Cfun on 2015/5/4.
 */
public class WaittingReciveFragment extends BaseFragment implements View.OnClickListener, RecvingPrepareStateChangReciver.OnRecivePrepareStateChangedListener
{
	private ImageView photo;
	private TextView name;
	private TextView stateInfo;


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return  inflater.inflate(R.layout.ap_waitting, null);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		photo = (ImageView) view.findViewById(R.id.iv_photo);
		name = (TextView) view.findViewById(R.id.tv_name);
		stateInfo = (TextView) view.findViewById(R.id.tv_info);
		view.findViewById(R.id.iv_back).setOnClickListener(this);
		((TextView) view.findViewById(R.id.tv_title)).setText(R.string.waiting);

		name.setText(AppConfig.userName);
		stateInfo.setText(getString(R.string.apIniting));
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.iv_back:
				getActivity().finish();
				break;
		}
	}
	@Override
	public void onRecivePrepareStateChanged(int state)
	{
		switch (state)
		{
			case RecvingPrepareStateChangReciver.STATE_ERROR:
				stateInfo.setText(getString(R.string.apInitFail));
				break;
			case RecvingPrepareStateChangReciver.STATE_FINISH:
				stateInfo.setText(getString(R.string.apInitFinish));
				break;
			case RecvingPrepareStateChangReciver.STATE_PREPARING:
				stateInfo.setText(R.string.apIniting);
				break;
		}
	}


}
