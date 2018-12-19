package com.wisdom.nhoa.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.widget.calendar.Utils;

/**
 * @authorzhanglichao
 * @date2018/7/25 15:47 绘制圆形进度条 后期可改为动态
 * @package_name com.wisdom.nhoa.widget
 */
public class CirclePrecentView extends View {
    //圆的半径
    private float mRadius;
    //圆的颜色
     private int mCirclecolor;
     //当前百分比
    private  float mCurPrecent;
   //圆心位置坐标
    private float x;
    private float y;
    //要画的角度
    private  int mEndAngel;
    //总体大小
    private int mHeight;
    private int mWidth;
    //圆的画笔
    private Paint mPaint;
    private  Paint  mBackgroundPaint;
    private  Paint  mCircle;
    private int backgroundcolor;
    public CirclePrecentView(Context context) {
     this(context,null);
    }

    public CirclePrecentView(Context context, @Nullable AttributeSet attrs) {
    this(context,attrs,0);
    }

    public CirclePrecentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray  typedArray=context.obtainStyledAttributes(attrs, R.styleable.CirclePrecentView, defStyleAttr, 0);
        mCirclecolor=typedArray.getColor(R.styleable.CirclePrecentView_color,0xff6950a1);
        mCurPrecent=typedArray.getFloat(R.styleable.CirclePrecentView_precent,0);
        mRadius=typedArray.getDimensionPixelSize( R.styleable.CirclePrecentView_radius, Utils.dpi2px(context,100));
        backgroundcolor=typedArray.getColor(R.styleable.CirclePrecentView_backgroundcolor,0xffffffff);
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mCirclecolor);
        mBackgroundPaint=new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setColor(backgroundcolor);
        mCircle=new Paint();
        mCircle.setAntiAlias(true);
        mCircle.setStyle(Paint.Style.STROKE);
        mCircle.setColor(mCirclecolor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mEndAngel= (int) ((mCurPrecent*360)/100);
        RectF rect = new RectF(0, 0, mWidth, mHeight);
        canvas.drawArc(rect,0,360,true,mCircle);
        canvas.drawArc(rect,0,360,true,mBackgroundPaint);
        canvas.drawArc(rect,0,mEndAngel,true,mPaint);

    }

    public void setmCurPrecent(float mCurPrecent) {
        this.mCurPrecent = mCurPrecent;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取测量模式
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        //获取测量大小
        int widthsize=MeasureSpec.getSize(widthMeasureSpec);
        int heightsize=MeasureSpec.getSize(heightMeasureSpec);
        //精准模式
     if (widthMode==MeasureSpec.EXACTLY&&heightMode==MeasureSpec.EXACTLY){
         x=widthsize/2;
         y=heightsize/2;
         mWidth=widthsize;
         mHeight=heightsize;
     //如果是wrapcotent
     }else if(widthMode==MeasureSpec.AT_MOST&&heightMode==MeasureSpec.AT_MOST){
         mWidth= (int) (mRadius*2);
         mHeight= (int) (mRadius*2);
         x=mRadius;
         y=mRadius;
     }
     setMeasuredDimension(mWidth,mHeight);
    }
}
