package com.wisdom.nhoa.widget.paint;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.wisdom.nhoa.R;


public class WritePadDialog extends Dialog {
    private Context mContext;
    private WriteDilogListener.WriteDialogListener mWriteDialogListener;
    private PaintView mPaintView;
    private FrameLayout mFrameLayout;
    private Button mBtnOK, mBtnClear, mBtnCancel;
    private CancelOnClickListener listener;

    public void setListener(CancelOnClickListener listener) {
        this.listener = listener;
    }


    public WritePadDialog(Context context, WriteDilogListener.WriteDialogListener writeDialogListener) {
        super(context);
        this.mContext = context;
        this.mWriteDialogListener = writeDialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //无标题
        setContentView(R.layout.write_pad);
        mFrameLayout = (FrameLayout) findViewById(R.id.tablet_view);
        // 取屏幕尺寸
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        int screenWidth = mDisplayMetrics.widthPixels;
        int screenHeight = mDisplayMetrics.heightPixels;

        mPaintView = new PaintView(mContext, screenWidth, screenHeight);
        mFrameLayout.addView(mPaintView);
        mPaintView.requestFocus();

        mBtnOK = (Button) findViewById(R.id.write_pad_ok);

        mBtnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mPaintView.getPath().isEmpty()) {
                    Toast.makeText(mContext, "请签名!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mWriteDialogListener.onPaintDone(mPaintView.getPaintBitmap());
                // dismiss();
            }
        });


        mBtnClear = (Button) findViewById(R.id.write_pad_clear);
        mBtnClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mPaintView.clear();
            }
        });
        mBtnCancel = (Button) findViewById(R.id.write_pad_cancel);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("esult.confirm();+---", "dianji");
            }
        });


    }

    public String getBtText() {
        return (String) mBtnOK.getText();
    }

    public void setBtText(String text) {
        mBtnOK.setText(text);
    }

    public interface CancelOnClickListener {
        void onCancle();
    }

    /**
     * 显示对话框
     */
    public void showDialog() {
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
//        window.setWindowAnimations(R.style.mystyle); // 添加动画

        // 两句的顺序不能调换
        this.show();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.FILL_PARENT;
        window.setAttributes(lp);
        window.getDecorView().setPadding(0, 0, 0, 0);

    }
}