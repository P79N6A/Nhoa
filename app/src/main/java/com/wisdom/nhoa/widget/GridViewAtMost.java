package com.wisdom.nhoa.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.widget
 * @class describe：不滚动的GridView
 * @time 2018/7/24 10:58
 * @change
 */
public class GridViewAtMost extends GridView {
    public GridViewAtMost(Context context) {
        super(context);
    }

    public GridViewAtMost(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewAtMost(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int space = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, space);

    }
}
