package com.wisdom.nhoa.widget;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
/**
 * @author lxd
 * @ProjectName project：
 * @class package：
 * @class describe：日期选择对话框
 * @time 15:39
 * @change
 */
public class DateDialog extends DatePickerDialog {

    public DateDialog(@NonNull Context context, int themeResId, @Nullable OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth) {
        super(context, themeResId, listener, year, monthOfYear, dayOfMonth);

    }

    @Override
    protected void onStop() {
//        super.onStop();
    }
}
