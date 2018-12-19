package com.wisdom.nhoa.widget.calendar.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.wisdom.nhoa.widget.calendar.Const;
import com.wisdom.nhoa.widget.calendar.Utils;
import com.wisdom.nhoa.widget.calendar.component.CalendarAttr;
import com.wisdom.nhoa.widget.calendar.component.CalendarRenderer;
import com.wisdom.nhoa.widget.calendar.interf.IDayRenderer;
import com.wisdom.nhoa.widget.calendar.interf.OnAdapterSelectListener;
import com.wisdom.nhoa.widget.calendar.interf.OnSelectDateListener;
import com.wisdom.nhoa.widget.calendar.model.CalendarDate;

import java.security.PrivilegedAction;

@SuppressLint("ViewConstructor")
public class Calendar extends View {
    /**
     * 日历列数
     */
    private CalendarAttr.CalendarType calendarType;
    private int cellHeight; // 单元格高度
    private int cellWidth; // 单元格宽度

    private OnSelectDateListener onSelectDateListener;    // 单元格点击回调事件
    private Context context;
    private CalendarAttr calendarAttr;
    private CalendarRenderer renderer;

    private OnAdapterSelectListener onAdapterSelectListener;
    private float touchSlop;
    private final static int TOUCHSLOP=8;
    private boolean isMove;//判断是否移动
    private Runnable runnable;
    public Calendar(Context context,
                    OnSelectDateListener onSelectDateListener,
                    CalendarAttr attr) {
        super(context);
        this.onSelectDateListener = onSelectDateListener;
        calendarAttr = attr;
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        touchSlop = Utils.getTouchSlop(context);
        initAttrAndRenderer();
    }

    private void initAttrAndRenderer() {
        renderer = new CalendarRenderer(this, calendarAttr, context);
        renderer.setOnSelectDateListener(onSelectDateListener);
        runnable =new Runnable() {
            @Override
            public void run() {
                CalendarDate calendarDate=renderer.getLongClickDate((int)posX/cellWidth,(int)posY/cellHeight);
               if (onAdapterSelectListener!=null) {
                   onAdapterSelectListener.onCalendarLongClick(calendarDate);
               }

            }
        };
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        renderer.draw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        cellHeight = h / Const.TOTAL_ROW;
        cellWidth = w / Const.TOTAL_COL;
        calendarAttr.setCellHeight(cellHeight);
        calendarAttr.setCellWidth(cellWidth);
        renderer.setAttr(calendarAttr);
    }

    private float posX = 0;
    private float posY = 0;

    /*
     * 触摸事件为了确定点击的位置日期
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                posX = event.getX();
                posY = event.getY();
                isMove=false;
                postDelayed(runnable, ViewConfiguration.getLongPressTimeout());
                break;
            case MotionEvent.ACTION_UP:

                float disX = event.getX() - posX;
                float disY = event.getY() - posY;
                if (Math.abs(disX) < touchSlop && Math.abs(disY) < touchSlop) {
                    int col = (int) (posX / cellWidth);
                    int row = (int) (posY / cellHeight);
                    onAdapterSelectListener.cancelSelectState();
                    renderer.onClickDate(col, row);
                    onAdapterSelectListener.updateSelectState();
                    invalidate();
                }
                removeCallbacks(runnable);
                break;
             case  MotionEvent.ACTION_MOVE:
                 if (isMove){
                     break;
                 }
                 if (Math.abs(posX-event.getX())>TOUCHSLOP||Math.abs(posX-event.getX())>TOUCHSLOP){
                     isMove=true;
                     removeCallbacks(runnable);
                 }
                 break;
        }
        return true;
    }


    public CalendarAttr.CalendarType getCalendarType() {
        return calendarAttr.getCalendarType();
    }

    public void switchCalendarType(CalendarAttr.CalendarType calendarType) {
        calendarAttr.setCalendarType(calendarType);
        renderer.setAttr(calendarAttr);
    }

    public int getCellHeight() {
        return cellHeight;
    }

    public void resetSelectedRowIndex() {
        renderer.resetSelectedRowIndex();
    }

    public int getSelectedRowIndex() {
        return renderer.getSelectedRowIndex();
    }

    public void setSelectedRowIndex(int selectedRowIndex) {
        renderer.setSelectedRowIndex(selectedRowIndex);
    }

    public void setOnAdapterSelectListener(OnAdapterSelectListener onAdapterSelectListener) {
        this.onAdapterSelectListener = onAdapterSelectListener;
    }

    public void showDate(CalendarDate current) {
        renderer.showDate(current);
    }

    public void updateWeek(int rowCount) {
        renderer.updateWeek(rowCount);
        invalidate();
    }

    public void update() {
        renderer.update();
    }

    public void cancelSelectState() {
        renderer.cancelSelectState();
    }

    public CalendarDate getSeedDate() {
        return renderer.getSeedDate();
    }

    public CalendarDate getFirstDate() {
        return renderer.getFirstDate();
    }

    public CalendarDate getLastDate() {
        return renderer.getLastDate();
    }

    public void setDayRenderer(IDayRenderer dayRenderer) {
        renderer.setDayRenderer(dayRenderer);
    }
}