package com.wisdom.nhoa.circulated.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.BaseRequest;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.widget.fileview.FileDisplayActivity;
import com.wisdom.nhoa.widget.fileview.PreviwPicActivity;
import com.wisdom.nhoa.widget.fileview.SuperFileView2;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;


/**
 * 多功能文件预览类
 * 此方法可以在预览文件前访问服务器接口，备案此文件已经被查看下载
 * 调用 静态方法show 可实现预览doc和图片等
 */
public class FilePreviewActivity extends Activity {


    private String TAG = FilePreviewActivity.class.getSimpleName();
    private SuperFileView2 mSuperFileView;
    private TextView tv_preview_hint;
    private ProgressDialog progressDialog;
    private String filePath;
    private String fileId;
    private TextView tv_title;
    private String pathIn;
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private ImageView head_back_iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_preview);
        init();
    }


    public void init() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mSuperFileView = findViewById(R.id.mSuperFileView);
        head_back_iv = findViewById(R.id.head_back_iv);
        tv_preview_hint = findViewById(R.id.tv_preview_hint);
        tv_title = findViewById(R.id.comm_head_title);
        tv_title.setText("文件预览");
        head_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = this.getIntent();
        pathIn = (String) intent.getSerializableExtra("path");
        filePath = pathIn;
        fileId = (String) intent.getSerializableExtra("fileId");
        //判断手机是否有内存读写权限，没有的话就先申请权限，再操作
        PackageManager pm = getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getPackageName()));
        if (permission) {//有权限
            previewByFilePath(pathIn);
        } else {//木有权限
            ActivityCompat.requestPermissions(this, permissions, 321);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //        根据文件名字进行判断是否能够进行预览操作
        previewByFilePath(pathIn);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("123", "FileDisplayActivity-->onDestroy");
        if (mSuperFileView != null) {
            mSuperFileView.onStopDisplay();
        }
    }


    public static void show(Context context, String url, String fileId) {
        Intent intent = new Intent(context, FilePreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("path", url);
        bundle.putSerializable("fileId", fileId);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, ConstantString.REQUEST_CODE_REFRESH_DATA);
    }

    /**
     * 根据文件路径进行判断文件的预览形式
     *
     * @param path
     */
    private void previewByFilePath(String path) {
        Log.i(TAG, "fileId: " + fileId);
        if (fileId != null && !"".equals(fileId)) {
            //向服务器记录一次该用户已经预览一次改文件了。
            recordDownload(fileId);
        }
        String fileNameArray[] = path.split("\\.");
        String fileName = "";
        if (fileNameArray.length > 2) {
            fileName = fileNameArray[fileNameArray.length - 2];
        }
        Log.i(TAG, "onClick: 截取地址-----" + fileNameArray.length);
        if (fileNameArray.length > 1) {
            String fileLastName = fileNameArray[fileNameArray.length - 1];
            if (fileLastName.endsWith("jpg") ||
                    fileLastName.endsWith("png") ||
                    fileLastName.endsWith("jpeg") ||
                    fileLastName.endsWith("bmp") ||
                    fileLastName.endsWith("gif")

                    ) {//等待预览的文件是图片资源
                mSuperFileView.setVisibility(View.VISIBLE);
                tv_preview_hint.setVisibility(View.GONE);
                Intent intent = new Intent(this,
                        PreviwPicActivity.class);
                intent.putExtra("title", fileName);
                intent.putExtra("url", path);
                this.startActivity(intent);
                this.finish();
            } else if (fileLastName.endsWith("txt")) {
                mSuperFileView.setVisibility(View.GONE);
                tv_preview_hint.setVisibility(View.VISIBLE);
                okGoDownload(path);
            } else {
                FileDisplayActivity.showForResult(this, filePath);
            }
        } else {//没有拿到文件的后缀名称
            mSuperFileView.setVisibility(View.GONE);
            tv_preview_hint.setVisibility(View.VISIBLE);
            okGoDownload(path);
        }
    }

    /**
     * 告诉服务器，我下载了这个附件，访问下接口，保存下载记录
     */
    private void recordDownload(String fileId) {
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        params.put("file_id", fileId);
        HttpUtil.httpGet(ConstantUrl.RECORD_DOWNLOAD_URL, params, new StringCallback() {
            @Override
            public void onError(okhttp3.Call call, okhttp3.Response response, Exception e) {
                super.onError(call, response, e);
                Log.i(TAG, "onSuccess: 下载记录保存失败");
            }

            @Override
            public void onSuccess(String s, okhttp3.Call call, okhttp3.Response response) {
                Log.i(TAG, "onSuccess: 下载记录保存成功");
            }
        });
    }

    /**
     * 下载文件的方法
     */
    private void okGoDownload(String url) {
        OkGo.get(url).execute(new FileCallback() {

            @Override
            public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                //这里回调下载进度(该回调在主线程,可以直接更新ui)
                progressDialog.setProgress((int) currentSize);
            }

            @Override
            public void onBefore(BaseRequest request) {
                super.onBefore(request);
                progressDialog.show();
            }

            @Override
            public void onError(okhttp3.Call call, okhttp3.Response response, Exception e) {
                super.onError(call, response, e);
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(File file, okhttp3.Call call, okhttp3.Response response) {
                Log.i(TAG, "onSuccess: 下载成功");
                progressDialog.dismiss();
                try {
                    tv_preview_hint.setVisibility(View.VISIBLE);
                    tv_preview_hint.setText("暂不支持该文件类型的预览，文件已为您下载成功，保存路径为：" + file.getCanonicalPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantString.CODE_ACTIVITY_FINISH) {
            this.finish();
        }
    }
}
