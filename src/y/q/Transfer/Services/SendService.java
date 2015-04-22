package y.q.Transfer.Services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import y.q.Transfer.Broadcast.ApStateBroadcastReciver;
import y.q.Transfer.Tools.WifiApHelper;
import y.q.Transfer.Tools.WifiHelper;
import y.q.wifisend.Base.BaseApplication;


import java.util.List;

/**
 * Created by Cfun on 2015/4/21.
 */
public class SendService extends Service
{
    private SendAction bind = null;
    private WifiApHelper apHelper;
    private WifiHelper wifiHelper;
    private ApStateBroadcastReciver apStateBroadcastReciver;
    private List<Uri> tranTaskList;

    @Override
    public void onCreate()
    {
        super.onCreate();

        wifiHelper = new WifiHelper();
        apHelper = new WifiApHelper(wifiHelper);
        bind = new SendAction();
        apStateBroadcastReciver = new ApStateBroadcastReciver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        apStateBroadcastReciver.setApStateChangListener(new ApStateBroadcastReciver.OnApStateChangListener()
        {
            @Override
            public void onApClosing()
            {

            }

            @Override
            public void onApOpening()
            {

            }

            @Override
            public void onApOpened()
            {

            }

            @Override
            public void onApClosed()
            {
                openWifiAndScanAP(); //当AP关闭时，打开WIFI并启动扫描
                BaseApplication.getInstance().unregisterReceiver(apStateBroadcastReciver);
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        BaseApplication.getInstance().unregisterReceiver(apStateBroadcastReciver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return bind;
    }

    public class SendAction extends Binder
    {
        /***
         *
         * Activity<--->Service
         * 1:ACtivity通知Service为传书做为准备 --->
         * 2:Service通知ActivityWIFI是否已经就绪（失败，或正在打开）<---
         * 3:Service通知Activitywifi扫描已经就绪，通知Activity通过Service获取扫描列表<---
         * 4：ACtivity得到列表后选择合适的热点通知Service进行连接--->
         * 5：Service通知Activity连接客户端的状态<---
         * 6：若成功，则Service持续报告传输状态<---
         * 7：在传输期间ACtivity可以通知Service取消本次发送--->
         */

        public void preparedTranslate()
        {
            /***
             * 准备wifi传输阶段，需要1:关闭AP 2：打开WIFI 3：扫描热点
             */
            if(apHelper.isApEnabled())
            {
                //注册Ap事件的Receiver并监听
                BaseApplication.getInstance().registerReceiver(apStateBroadcastReciver, new IntentFilter(ApStateBroadcastReciver.ACTION));
                apHelper.closeWifiAp();
            }
            else
                openWifiAndScanAP();
        }
        public void connectionSSID(String ssid, List<Uri> tranList)
        {
            wifiHelper.addNetwork(ssid, "", WifiHelper.TYPE_NO_PASSWD);
            tranTaskList = tranList;
        }
        public void cancleTran()
        {}

    }

    public void openWifiAndScanAP()
    {
        wifiHelper.setWifiEnabled(true);
        wifiHelper.scanApList();
    }
}
