package com.wisdom.nhoa.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.wisdom.nhoa.R;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.widget
 * @class describe：
 * @time 2018/3/19 16:58
 * @change
 */

public class SavePicDialog extends Dialog implements View.OnClickListener {
    private Button btn_save_local;
    private LinearLayout ll_bg;
    private OnDialogDismissListener onDialogDismissListener;

    public SavePicDialog(@NonNull Context context) {
        super(context);
    }

    public SavePicDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }


    public void setOnDialogDismissListener(OnDialogDismissListener onDialogDismissListener) {
        this.onDialogDismissListener = onDialogDismissListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_save_pic);
        initViews();
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.dialog_save_pic, container, false);
//        initViews(view);
//        return view;
//    }

    /**
     * 初始化页面内相关控件
     */
    private void initViews() {
        btn_save_local = findViewById(R.id.btn_save_local);
        ll_bg = findViewById(R.id.ll_bg);
        btn_save_local.setOnClickListener(this);
//        ll_bg.setBackgroundColor(getResources().getColor(R.color.translucent));
    }

    /**
     * 页面内点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save_local: {
                this.dismiss();
                onDialogDismissListener.onDialogDismiss();
            }
            break;
        }
    }


    public interface OnDialogDismissListener {
        void onDialogDismiss();
    }
}
