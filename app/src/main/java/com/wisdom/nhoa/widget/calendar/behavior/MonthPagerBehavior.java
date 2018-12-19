package com.wisdom.nhoa.widget.calendar.behavior;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.wisdom.nhoa.widget.calendar.Utils;
import com.wisdom.nhoa.widget.calendar.component.CalendarViewAdapter;
import com.wisdom.nhoa.widget.calendar.view.MonthPager;

public class MonthPagerBehavior extends CoordinatorLayout.Behavior<MonthPager> {
    private int top = 0;
    private int touchSlop = 1;
    private int offsetY = 0;
    private int dependentViewTop = -1;

    public MonthPagerBehavior() {
    }

    public boolean layoutDependsOn(CoordinatorLayout parent, MonthPager child, View dependency) {
        return dependency instanceof RecyclerView;
    }

    public boolean onLayoutChild(CoordinatorLayout parent, MonthPager child, int layoutDirection) {
        parent.onLayoutChild(child, layoutDirection);
        child.offsetTopAndBottom(this.top);
        return true;
    }

    public boolean onDependentViewChanged(CoordinatorLayout parent, MonthPager child, View dependency) {
      CalendarViewAdapter calendarViewAdapter = (CalendarViewAdapter)child.getAdapter();
        if (this.dependentViewTop != -1) {
            int dy = dependency.getTop() - this.dependentViewTop;
            int top = child.getTop();
            if (dy > this.touchSlop) {
                calendarViewAdapter.switchToMonth();
            } else if (dy < -this.touchSlop) {
                calendarViewAdapter.switchToWeek(child.getRowIndex());
            }

            if (dy > -top) {
                dy = -top;
            }

            if (dy < -top - child.getTopMovableDistance()) {
                dy = -top - child.getTopMovableDistance();
            }

            child.offsetTopAndBottom(dy);
            Log.e("ldf", "onDependentViewChanged = " + dy);
        }

        this.dependentViewTop = dependency.getTop();
        this.top = child.getTop();
        if (this.offsetY > child.getCellHeight()) {
            calendarViewAdapter.switchToMonth();
        }

        if (this.offsetY < -child.getCellHeight()) {
            calendarViewAdapter.switchToWeek(child.getRowIndex());
        }

        if (this.dependentViewTop > child.getCellHeight() - 24 && this.dependentViewTop < child.getCellHeight() + 24 && this.top > -this.touchSlop - child.getTopMovableDistance() && this.top < this.touchSlop - child.getTopMovableDistance()) {
           Utils.setScrollToBottom(true);
            calendarViewAdapter.switchToWeek(child.getRowIndex());
            this.offsetY = 0;
        }

        if (this.dependentViewTop > child.getViewHeight() - 24 && this.dependentViewTop < child.getViewHeight() + 24 && this.top < this.touchSlop && this.top > -this.touchSlop) {
           Utils.setScrollToBottom(false);
            calendarViewAdapter.switchToMonth();
            this.offsetY = 0;
        }

        return true;
    }
}
