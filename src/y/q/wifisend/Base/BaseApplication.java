package y.q.wifisend.Base;

import android.app.Application;

/**
 * Created by CFun on 2015/4/11.
 */
public class BaseApplication extends Application {
    static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Application getInstance() {
        return instance;
    }
}
