/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wisdom.nhoa.widget.qrcode;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.zxing.Result;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.metting.activity.MeetingScanResultActivity;
import com.wisdom.nhoa.metting.model.ScanResultModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.widget.qrcode.camera.CameraManager;
import com.wisdom.nhoa.widget.qrcode.decode.DecodeThread;
import com.wisdom.nhoa.widget.qrcode.utils.BeepManager;
import com.wisdom.nhoa.widget.qrcode.utils.CaptureActivityHandler;
import com.wisdom.nhoa.widget.qrcode.utils.InactivityTimer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;

import okhttp3.Call;
import okhttp3.Response;

/**
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CaptureActivity extends AppCompatActivity implements SurfaceHolder.Callback, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = CaptureActivity.class.getSimpleName();

    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;

    private SurfaceView scanPreview = null;
    private RelativeLayout scanContainer;
    private RelativeLayout scanCropView;
    private ImageView scanLine;
    private ImageView head_back_iv;
    private CheckBox cb_login;
    private CheckBox cb_sign;
    private TextView tvHint;

    private Rect mCropRect = null;
    private boolean isHasSurface = false;
    private Intent nextIntent = new Intent();
    private String scanType = "0";//判断扫码类型  0：扫码登录  1：扫码签到

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.VIBRATE}, ConstantString.QRCODE_SCAN_REQUEST_CODE);
        }
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_capture);

        scanPreview = findViewById(R.id.capture_preview);
        scanContainer = findViewById(R.id.capture_container);
        scanCropView = findViewById(R.id.capture_crop_view);
        scanLine = findViewById(R.id.capture_scan_line);
        head_back_iv = findViewById(R.id.head_back_iv);
        cb_login = findViewById(R.id.cb_login);
        cb_sign = findViewById(R.id.cb_sign);
        tvHint = findViewById(R.id.tv1);
        cb_login.setOnCheckedChangeListener(this);
        cb_sign.setOnCheckedChangeListener(this);
        cb_login.setChecked(true);

        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);

        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation
                .RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.9f);
        animation.setDuration(4500);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        scanLine.startAnimation(animation);
        head_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CaptureActivity.this.finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // CameraManager must be initialized here, not in onCreate(). This is
        // necessary because we don't
        // want to open the camera driver and measure the screen size if we're
        // going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the
        // wrong size and partially
        // off screen.
        cameraManager = new CameraManager(getApplication());

        handler = null;

        if (isHasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(scanPreview.getHolder());
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            scanPreview.getHolder().addCallback(this);
        }

        inactivityTimer.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstantString.QRCODE_SCAN_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    ) {
                surfaceCreated(scanPreview.getHolder());

            } else {
                // Permission Denied
                CaptureActivity.this.finish();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!isHasSurface) {
            scanPreview.getHolder().removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult The contents of the barcode.
     * @param bundle    The extras
     */
    public void handleDecode(Result rawResult, Bundle bundle) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();
        // TODO: 2018/4/10 扫完码之后的操作
        Log.i(TAG, "扫描结果: " + rawResult.getText());
        nextIntent.setClass(this, MeetingScanResultActivity.class);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(rawResult.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject == null) {
            ToastUtil.showToast("请扫描正确的二维码");
            this.finish();
        } else {
            if ("0".equals(scanType)) {
                //扫码登录
                //扫描的是 登录二维码
                final JSONObject finalJsonObject = jsonObject;
                if (jsonObject.has("time")) {
                    new AlertDialog.Builder(this)
                            .setTitle("提示")
                            .setMessage("确定授权网页登录操作？")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        //执行二维码登录操作
                                        QRCodeLogin(finalJsonObject);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .create().show();
                } else {
                    ToastUtil.showToast("请扫描正确的二维码");
                    this.finish();
                }
            } else if ("1".equals(scanType)) {
                //扫描的是 会议签到二维码
                if (jsonObject.optJSONObject("results").has("meetingid")) {
                    ScanResultModel scanResultModel = new Gson().fromJson(
                            jsonObject.optJSONObject("results").toString(), ScanResultModel.class);
                    getMeetingSignStateAndJump(scanResultModel);
                } else {
                    ToastUtil.showToast("请扫描正确的二维码");
                    this.finish();
                }
            } else {
                ToastUtil.showToast("请扫描正确的二维码");
                this.finish();
            }

        }

    }

    private void QRCodeLogin(JSONObject jsonObject) throws JSONException {

        String time = jsonObject.getString("time");
        String pageId = jsonObject.getString("token");
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        params.put("qrTime", time);
        params.put("pageId", pageId);
        params.put("loginName", SharedPreferenceUtil.getUserInfo(this).getUser_name());
        params.put("password", SharedPreferenceUtil.getUserInfo(this).getPasssword());
        HttpUtil.httpGet(ConstantUrl.WEB_SCAN_QR_CODE_LOGIN, params, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject jsonObject1 = new JSONObject(s);
                    switch (jsonObject1.getInt("error_code")) {
                        case 0: {
                            ToastUtil.showToast("登录成功！");
                            CaptureActivity.this.finish();
                        }
                        break;
                        case 10001: {
                            ToastUtil.showToast("二维码已失效！");
                        }
                        break;
                        case 10002: {
                            ToastUtil.showToast("二维码超时，请刷新后重新登录！");
                        }
                        break;
                        case 10003: {
                            ToastUtil.showToast("用户名或密码错误!");
                        }
                        break;
                        default: {
                            ToastUtil.showToast("登录失败，请重试!");
                        }
                        break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 获取指定会议的签到状态，并跳转到下一级界面
     */
    private void getMeetingSignStateAndJump(final ScanResultModel scanResultModel) {
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        params.put("meetingid", scanResultModel.getMeetingid());
        HttpUtil.httpGet(ConstantUrl.CHECK_MEETING_SIGN_STATE, params, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject jsonObject = new JSONObject(s).getJSONObject("results");
                    String signStatus = jsonObject.getString("signStatus");
                    Bundle bundle1 = new Bundle();
                    bundle1.putSerializable("data", scanResultModel);
                    nextIntent.putExtras(bundle1);
                    nextIntent.putExtra("signStatus", signStatus);
                    head_back_iv.setVisibility(View.GONE);
                    startActivityForResult(nextIntent, ConstantString.CODE_ACTIVITY_FINISH);
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.showToast("获取用户签到状态失败，请重试");
                    CaptureActivity.this.finish();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ConstantString.CODE_ACTIVITY_FINISH == requestCode) {
            this.finish();
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
            }

            initCrop();
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        // camera error
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("Camera error");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }

    public Rect getCropRect() {
        return mCropRect;
    }

    /**
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 页面下方的切换按钮，切换事件
     *
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.cb_login: {
                    //                扫码登录
                    scanType = "0";
                    cb_login.setChecked(true);
                    cb_sign.setChecked(false);
                    tvHint.setText(R.string.camera_hint2);
                }
                break;
                case R.id.cb_sign: {
                    //            扫码签到
                    scanType = "1";
                    cb_sign.setChecked(true);
                    cb_login.setChecked(false);
                    tvHint.setText(R.string.camera_hint);
                }
                break;
            }
        }
    }
}