package y.q.wifisend.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import y.q.PageIndicator.TabView;
import y.q.wifisend.R;

/**
 * Created by CFun on 2015/4/21.
 */
public class FileChoseTab extends TabView {

    private CheckBox checkBox = null;
    private TextView textView = null;
    public FileChoseTab(Context context, int mIndex) {
        super(context, mIndex);

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.tab_item, this);
        checkBox = (CheckBox) v.findViewById(R.id.checkBox);
        textView = (TextView) findViewById(R.id.textView);
    }

    public void setOnCheckedChangedListener(CheckBox.OnCheckedChangeListener listener)
    {
        checkBox.setOnCheckedChangeListener(listener);
    }

    public void setTabText(CharSequence text)
    {
        textView.setText(text);
    }
}
