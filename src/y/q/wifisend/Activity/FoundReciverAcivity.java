package y.q.wifisend.Activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import y.q.wifisend.Base.BaseActivity;
import y.q.wifisend.R;

/**
 * Created by CFun on 2015/4/19.
 */
public class FoundReciverAcivity extends BaseActivity{


    private ImageView animView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chose_reciver);
        animView = (ImageView)findViewById(R.id.iv_anim);
        ((AnimationDrawable)animView.getDrawable()).start();
    }
}
