package y.q.wifisend.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import y.q.PageIndicator.TabPageIndicator;
import y.q.PageIndicator.TabTitleAdapter;
import y.q.PageIndicator.TabView;
import y.q.wifisend.Base.BaseActivity;
import y.q.wifisend.Fragment.FileChoseFragments.*;
import y.q.wifisend.R;
import y.q.wifisend.widget.FileChoseTab;

/**
 * Created by CFun on 2015/4/21.
 */
public class FileChoseActivity extends BaseActivity {

    private TabPageIndicator indicator;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_chose);
        indicator = (TabPageIndicator) findViewById(R.id.indicator);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new FileChoseAdapter(getSupportFragmentManager()));
        indicator.setViewPager(viewPager);
    }

    class FileChoseAdapter extends FragmentPagerAdapter implements TabTitleAdapter {
        private String[] titles = new String[]{"文件", "应用", "联系人", "图片", "音乐", "视频"};

        public FileChoseAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {
            switch (index) {
                case 0:
                    return new CommonFileChoser();
                case 1:
                    return new AppFileChoser();
                case 2:
                    return new ContactChoser();
                case 3:
                    return new ImageFileChoser();
                case 4:
                    return new MusicFileChoser();
                case 5:
                    return new VideoFileChoser();
                default:
                    return null;
            }
        }

        @Override
        public TabView getTabView(int index) {
            FileChoseTab tab = new FileChoseTab(FileChoseActivity.this, index);
            tab.setTabText(titles[index]);
            return tab;
        }

        @Override
        public int getCount() {
            return 6;
        }
    }
}
