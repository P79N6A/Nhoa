package com.wisdom.nhoa.sendreceive.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.venusic.handwrite.view.HandWriteView;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.base.BroadCastManager;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.circulated.activity
 * @class describe：
 * @time 2018/4/2 9:33
 * @change
 */

public class WritePadActivity extends BaseActivity {
    @BindView(R.id.handWriteView)
    HandWriteView handWriteView;
    @BindView(R.id.write_pad_pen)
    ImageView writePadPen;
    @BindView(R.id.write_pad_black)
    ImageView writePadBlack;
    @BindView(R.id.write_pad_red)
    ImageView writePadRed;

    public static final String TAG = WritePadActivity.class.getSimpleName();
    private String path = null;
    private File file = null;
    private String insid = "";

    @Override
    public void initViews() {
        path = Environment.getExternalStorageDirectory() + "/oaSign/" + System.currentTimeMillis() + ".png";
        Log.i(TAG, "图片路径：" + path);
        file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        if (getIntent() != null) {
            insid = getIntent().getStringExtra("data");
        }
    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.circulate_write_pad_dialog);
    }


    /**
     * 页面内的点击事件……
     *
     * @param view
     */
    @OnClick({R.id.iv_close, R.id.tv_close,R.id.write_pad_black, R.id.write_pad_red, R.id.btn_confirm, R.id.btn_cancle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.write_pad_black: {
                //黑色按钮
                handWriteView.setPaintColor(Color.BLACK);
                writePadPen.setImageResource(R.mipmap.pen_black);
            }
            break;
            case R.id.write_pad_red: {
                //红色按钮
                handWriteView.setPaintColor(Color.RED);
                writePadPen.setImageResource(R.mipmap.pen_red);
            }
            break;
            case R.id.btn_confirm: {
                //确定按钮
                if (handWriteView.isSign()) {
                    try {
                        //路径
                        //是否清除边缘空白
                        //边缘保留多少像素空白
                        //是否加密存储 如果加密存储会自动在路径后面追加后缀.sign
                        handWriteView.save(path, true, 10, false);
                        //上传文件到服务器
                        postPicToServer();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUtil.showToast("您没有签名~");
                }

            }
            break;
            case R.id.btn_cancle: {
                //清除按钮
                handWriteView.clear();
            }
            break;
            case R.id.iv_close:
            case R.id.tv_close:{
//                点击关闭按钮
                this.finish();
            }
                break;
                default:{}break;
        }
    }

    /**
     * 上传本地图片文件到服务器端
     */
    private void postPicToServer() {
        U.showUploadDialog(this);
        if (file != null && !"".equals(insid)) {
            HttpParams params1 = new HttpParams();
            params1.put("appkey", ConstantString.APP_KEY);
            params1.put("insid", insid);
            params1.put("form_id", ConstantString.FORM_ID);
            params1.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
            params1.put("sign_pic", file);

//            Log.i(TAG, "sign_pic: " + file.getAbsolutePath());
//            Log.i(TAG, "appkey: " + ConstantString.APP_KEY);
//            Log.i(TAG, "insid: " + insid);
//            Log.i(TAG, "form_id: " + ConstantString.FORM_ID);
//            Log.i(TAG, "access_token: " + SharedPreferenceUtil.getUserInfo(this).getAccess_token());
//            上传文件到服务器的逻辑
            HttpUtil.uploadFiles(ConstantUrl.FILEHANDINGSHEET_SIGN, params1, new StringCallback() {
                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    U.closeLoadingDialog();
                    handWriteView.clear();
                    Toast.makeText(WritePadActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onError: " + e);
                }

                @Override
                public void onSuccess(String s, Call call, Response response) {
                    Log.i(TAG, "onSuccess: " + s);
                    String code = "";
                    U.closeLoadingDialog();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        code = jsonObject.getString("error_code");
                        if ("0".equals(code)) {
                            Toast.makeText(WritePadActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                            // 签名成功后的处理
                            Intent intentRefresh = new Intent();
                            intentRefresh.setAction(ConstantString.BROADCAST_REFRESH_TAG);
                            BroadCastManager.getInstance().sendBroadCast(WritePadActivity.this, intentRefresh);
                        } else {
                            Toast.makeText(WritePadActivity.this, jsonObject.getString("error_msg"), Toast.LENGTH_SHORT).show();
                        }
                        WritePadActivity.this.finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Log.i(TAG, "insid: " + insid);
            ToastUtil.showToast("签名文件生成失败，请重试");
        }
    }



}
