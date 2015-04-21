package y.q.PageIndicator;


/**
 * Created by CFun on 2015/4/19.
 */
public interface TabTitleAdapter {
    /**
     * Get icon representing the page at {@code index} in the adapter.
     */
    TabView getTabView(int index);

    // From PagerAdapter
    int getCount();
}
