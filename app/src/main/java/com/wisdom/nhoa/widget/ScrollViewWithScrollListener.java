package com.wisdom.nhoa.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.widget
 * @class describe：
 * @time 2018/3/27 8:49
 * @change
 */

public class ScrollViewWithScrollListener extends ScrollView {
    private ScrollViewListener scrollViewListener = null;

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    public ScrollViewWithScrollListener(Context context) {
        super(context);
    }

    public ScrollViewWithScrollListener(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewWithScrollListener(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    public interface ScrollViewListener {
        void onScrollChanged(ScrollViewWithScrollListener scrollView, int x, int y, int oldx, int oldy);

    }
}
