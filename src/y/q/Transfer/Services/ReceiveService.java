package y.q.Transfer.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by Cfun on 2015/4/21.
 */
public class ReceiveService extends Service
{
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class ReceiveAction extends Binder
    {
        public void openReceiveService()
        {}

        public void stopReceiveService()
        {}
    }
}
