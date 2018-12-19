package com.wisdom.nhoa.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.widget.calendar.Utils;
import com.wisdom.nhoa.widget.calendar.component.State;
import com.wisdom.nhoa.widget.calendar.interf.IDayRenderer;
import com.wisdom.nhoa.widget.calendar.model.CalendarDate;
import com.wisdom.nhoa.widget.calendar.view.DayView;

/**
 * Created by ldf on 17/6/26.
 */

@SuppressLint("ViewConstructor")
public class CustomDayView extends DayView {

    private int layoutId=0;//选择标记
    private TextView dateTv;
    private ImageView marker;
    private View selectedBackground;
    private View todayBackground;
    private final CalendarDate today = new CalendarDate();
    /**
     * 构造器
     *
     * @param context 上下文
     * @param layoutResource 自定义DayView的layout资源
     */
    public CustomDayView(Context context, int layoutResource) {
        super(context, layoutResource);
        layoutId=layoutResource;
        dateTv = (TextView) findViewById(R.id.date);
        marker = (ImageView) findViewById(R.id.maker);
        selectedBackground = findViewById(R.id.selected_background);
        todayBackground = findViewById(R.id.today_background);
    }

    @Override
    public void refreshContent() {
        renderToday(day.getDate());
        renderSelect(day.getState());
        if (layoutId==R.layout.custom_day){
        renderMarker(day.getDate(), day.getState());
        }else if(layoutId==R.layout.custom_day1) {
            renderMarker1(day.getDate(), day.getState());
        }
        super.refreshContent();
    }

    private void renderMarker(CalendarDate date, State state) {//对每个day 进行标记判断

        if (Utils.loadMarkData().containsKey(date.toString())) {
            if (state == State.SELECT || date.toString().equals(today.toString())) {
                marker.setVisibility(GONE);
            } else {
                marker.setVisibility(VISIBLE);
                if (Utils.loadMarkData().get(date.toString()).equals("0")) {
                    marker.setEnabled(true);
                } else {
                    marker.setEnabled(false);
                }
                GradientDrawable gradientDrawable= (GradientDrawable) marker.getBackground();//gradientDrawable 是drawable的子类
                switch (Utils.loadMarkData().get(date.toString())){
                    case "1":
                        gradientDrawable.setColor(getResources().getColor(R.color.calendar_color1));
                        break;
                    case "2":
                        gradientDrawable.setColor(getResources().getColor(R.color.calendar_color2));
                        break;
                    case "3":
                        gradientDrawable.setColor(getResources().getColor(R.color.calendar_color3));
                        break;
                    case "4":
                        gradientDrawable.setColor(getResources().getColor(R.color.calendar_color4));
                        break;
                    case "5":
                        gradientDrawable.setColor(getResources().getColor(R.color.calendar_color5));
                        break;
                    case "6":
                        gradientDrawable.setColor(getResources().getColor(R.color.calendar_color6));
                        break;
                    case "7":
                        gradientDrawable.setColor(getResources().getColor(R.color.calendar_color7));
                        break;
                    case "8":
                        gradientDrawable.setColor(getResources().getColor(R.color.calendar_color8));
                        break;
                       default:
                           break;

                }




            }
        } else {
            marker.setVisibility(GONE);
        }
    }
    private void renderMarker1(CalendarDate date, State state) {
        if (Utils.loadMarkData1().containsKey(date.toString())) {
            if (state == State.SELECT || date.toString().equals(today.toString())) {
                marker.setVisibility(GONE);
            } else {
                marker.setVisibility(VISIBLE);
                if (Utils.loadMarkData1().get(date.toString()).equals("0")) {
                    marker.setEnabled(true);
                } else {
                    marker.setEnabled(false);
                }
                GradientDrawable gradientDrawable= (GradientDrawable) marker.getBackground();//gradientDrawable 是drawable的子类

                switch (Utils.loadMarkData1().get(date.toString())){
                    case "1":
                        gradientDrawable.setColor(getResources().getColor(R.color.calendar_color1));
                        break;
                    case "2":
                        gradientDrawable.setColor(getResources().getColor(R.color.calendar_color2));
                        break;
                    case "3":
                        gradientDrawable.setColor(getResources().getColor(R.color.calendar_color3));
                        break;
                    case "4":
                        gradientDrawable.setColor(getResources().getColor(R.color.calendar_color4));
                        break;
                    case "5":
                        gradientDrawable.setColor(getResources().getColor(R.color.calendar_color5));
                        break;
                    case "6":
                        gradientDrawable.setColor(getResources().getColor(R.color.calendar_color6));
                        break;
                    case "7":
                        gradientDrawable.setColor(getResources().getColor(R.color.calendar_color7));
                        break;
                    case "8":
                        gradientDrawable.setColor(getResources().getColor(R.color.calendar_color8));
                        break;
                    default:
                        break;

                }


            }
        } else {
            marker.setVisibility(GONE);
        }
    }
    private void renderSelect(State state) {
        if (state == State.SELECT) {
            selectedBackground.setVisibility(VISIBLE);
            dateTv.setTextColor(Color.WHITE);
        } else if (state == State.NEXT_MONTH || state == State.PAST_MONTH) {
            selectedBackground.setVisibility(GONE);
            dateTv.setTextColor(Color.parseColor("#d5d5d5"));
        } else {
            selectedBackground.setVisibility(GONE);
            dateTv.setTextColor(Color.parseColor("#111111"));
        }
    }

    private void renderToday(CalendarDate date) {
        if (date != null) {
            if (date.equals(today)) {
                dateTv.setText("今");
                todayBackground.setVisibility(VISIBLE);
            } else {
                dateTv.setText(date.day + "");
                todayBackground.setVisibility(GONE);
            }
        }
    }

    @Override
    public IDayRenderer copy() {
        return new CustomDayView(context, layoutResource);
    }

}
