package com.wisdom.nhoa.widget.qrcode;

import android.app.TimePickerDialog;
import android.content.Context;

/**
 * @author lxd
 * @ProjectName project：
 * @class package：
 * @class describe：时间对话框
 * @time 15:38
 * @change
 */

public class TimeDialog extends TimePickerDialog {
    public TimeDialog(Context context, int themeResId, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
        super(context, themeResId, listener, hourOfDay, minute, is24HourView);
    }
    @Override
    protected void onStop() {
//        super.onStop();
    }
}
