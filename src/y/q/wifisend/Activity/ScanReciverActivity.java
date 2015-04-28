package y.q.wifisend.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import y.q.wifisend.Base.BaseActivity;
import y.q.wifisend.R;

/**
 * Created by CFun on 2015/4/19.
 */

/***
 * 扫描接收者的Activity
 */
public class ScanReciverActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_reciver);
        initView();
    }

    private void initView()
    {
        View view = findViewById(R.id.scanResult);
        int width =  view.getWidth();
        ViewGroup.LayoutParams params =  view.getLayoutParams();
        params.height = (int)(width*1.2);
        view.setLayoutParams(params);
    }
}
