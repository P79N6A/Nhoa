package com.wisdom.nhoa.widget.fileview;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.util.U;


/**
 * @author HanXueFeng
 * @ProjectName project： hrbzwt
 * @class package：com.wisdom.hrbzwt.homepage.activity
 * @class describe：预览图片专用页面
 * @time 2018/1/12 10:36
 * @change
 */

public class PreviwPicActivity extends Activity {
    private ImageView iv_pic;
    private ImageView iv_back;
    private TextView tv_title;
    private TextView tv_preview_hint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_pic);
        iv_pic = (ImageView) findViewById(R.id.iv_preview);
        iv_back = (ImageView) findViewById(R.id.head_back_iv);
        tv_title = (TextView) findViewById(R.id.comm_head_title);
        tv_preview_hint = (TextView) findViewById(R.id.tv_preview_hint);
        String title = "";
        String picUrl = "";
        if (getIntent() != null) {
//            title = getIntent().getStringExtra("title");
            picUrl = getIntent().getStringExtra("url");
        }
        tv_title.setText("文件预览");
        U.showLoadingDialog(this);
        Glide.with(this)
                .load(picUrl)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        U.closeLoadingDialog();
                        iv_pic.setVisibility(View.GONE);
                        tv_preview_hint.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        U.closeLoadingDialog();
                        iv_pic.setVisibility(View.VISIBLE);
                        tv_preview_hint.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(iv_pic);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreviwPicActivity.this.finish();
            }
        });
    }


}
