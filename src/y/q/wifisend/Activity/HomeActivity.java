package y.q.wifisend.Activity;

import android.os.Bundle;
import android.view.Window;
import y.q.wifisend.Base.BaseActivity;
import y.q.wifisend.R;

/**
 * Created by CFun on 2015/4/11.
 */
public class HomeActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
    }
}
