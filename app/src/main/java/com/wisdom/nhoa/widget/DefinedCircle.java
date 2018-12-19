package com.wisdom.nhoa.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.wisdom.nhoa.R;

/**
 * @authorzhanglichao
 * @date2018/10/9 11:13
 * @package_name com.wisdom.nhoa.widget
 */
public class DefinedCircle extends View {
    private int circleradius;//每个小圆的半径
    private int circleColor;//每个小圆的默认颜色
    private int defaultWidths=1000;
    private int defaultHeight=300;
    public DefinedCircle(Context context) {
        this(context,null);
}

    public DefinedCircle(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DefinedCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
         TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.DefinedCircle);
         circleradius=typedArray.getDimensionPixelSize(R.styleable.DefinedCircle_circleradius,dpToPx(15));
         circleColor=typedArray.getColor(R.styleable.DefinedCircle_circrlcolor, Color.BLACK);
        typedArray.recycle();
         init();

    }

    private void init() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasuerWidth(widthMeasureSpec),MeasureHeight(heightMeasureSpec));
    }
    private int MeasuerWidth(int widthMeasureSpec){
        int result;
        int specMode=MeasureSpec.getMode(widthMeasureSpec);
        int specSize=MeasureSpec.getSize(widthMeasureSpec);
        if (specMode==MeasureSpec.EXACTLY){//精确模式
            result=specSize;
            return result;
        }else {
            result=defaultWidths;
            if (specMode==MeasureSpec.AT_MOST){//最大父布局
             result=Math.max(result,specSize);
            }
        }
       return result;
    }
    private int MeasureHeight(int heightMeasureSpec){
        int result;
        int specMode=MeasureSpec.getMode(heightMeasureSpec);
        int specSize=MeasureSpec.getSize(heightMeasureSpec);
        if (specMode==MeasureSpec.EXACTLY){
            result=specSize;
            return result;
        }else {
            result=defaultHeight;
            if (specMode==MeasureSpec.AT_MOST){
                result=Math.max(result,specSize);
            }
        }
        return result;

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        defaultHeight=h;
        defaultWidths=w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);




    }


    private int dpToPx(float dps){
     return Math.round(getResources().getDisplayMetrics().density*dps);

    }
}
