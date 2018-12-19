package com.wisdom.nhoa.widget.calendar.interf;

import com.wisdom.nhoa.widget.calendar.model.CalendarDate;

/**
 * Created by ldf on 17/6/15.
 */

public interface OnAdapterSelectListener {
    void cancelSelectState();
    void updateSelectState();
    void onCalendarLongClick(CalendarDate calendarDate);
}
