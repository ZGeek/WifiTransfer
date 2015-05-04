package y.q.wifisend.Fragment.FileChoseFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import y.q.wifisend.Base.BaseFragment;

import java.util.Collection;
import java.util.List;

/**
 * Created by CFun on 2015/4/21.
 */
public class ContactChoser  extends BaseFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		TextView textView = new TextView(inflater.getContext());
		textView.setText(this.getClass().getSimpleName());
		textView.setTextSize(30);
		return textView;
	}
}
