package y.q.PageIndicator;

import android.content.Context;
import android.view.ViewParent;
import android.widget.FrameLayout;
import y.q.wifisend.R;

/**
 * Created by CFun on 2015/4/19.
 */
public class TabView extends FrameLayout {
    private int mIndex;
//    private int mMaxTabWidth = 50;

    public TabView(Context context, int mIndex) {
        super(context, null, R.attr.vpiTabPageIndicatorStyle);
//        this.mMaxTabWidth = mMaxTabWidth;
        setBackgroundResource(R.drawable.vpi__tab_indicator);
        this.mIndex = mIndex;
    }
        @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            ViewParent parent = this.getParent();

            while (parent != null)
            {
                if(parent instanceof TabPageIndicator)
                {
                    //Re-measure if we went beyond our maximum size.
                    int mMaxTabWidth = ((TabPageIndicator)parent).getMaxTabWidth();
                    if ( mMaxTabWidth> 0 && getMeasuredWidth() > mMaxTabWidth)
                    {
                        super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth, MeasureSpec.EXACTLY),
                                heightMeasureSpec);
                    }
                    break;
                }
                parent = parent.getParent();
            }

    }
//
//    public void setmMaxTabWidth(int mMaxTabWidth) {
//        this.mMaxTabWidth = mMaxTabWidth;
//    }

    public int getIndex() {
        return mIndex;
    }
}