package com.wisdom.nhoa.mine.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.BaseRequest;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.mine.model.VersionCodeModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.VersionUtil;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 关于页面
 */
public class AboutActivity extends BaseActivity {
    @BindView(R.id.tv_app_version)
    TextView app_version;
    @BindView(R.id.ll_check)
    LinearLayout ll_check;
    public static final String TAG = AboutActivity.class.getSimpleName();
    private ProgressDialog progressDialog;

    @Override
    public void initViews() {
        setTitle("关于");
        try {
            String pkName = this.getPackageName();
            String versionName = this.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            app_version.setText("V" + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_about);
    }


    /**
     * 检查版本更新的方法
     */
    @OnClick(R.id.ll_check)
    public void onViewClicked() {
        getVersionInfoFromServer();
    }


    /**
     * 从服务器获取服务器端的apk信息
     */
    private void getVersionInfoFromServer() {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        params.put("deviceType", "android");
        HttpUtil.httpGet(ConstantUrl.GET_VERSION_INFO, params, new JsonCallback<BaseModel<VersionCodeModel>>() {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeDialog();
                ToastUtil.showToast(e.getMessage());
            }

            @Override
            public void onSuccess(final BaseModel<VersionCodeModel> versionCodeModel, Call call, Response response) {
                U.closeDialog();
                //用服务器版本号与当前版本号进行对比
                if (versionCodeModel.results != null) {
                    int code = VersionUtil.compareVersion(versionCodeModel.results.getVersionNum()
                            , VersionUtil.getVersion(AboutActivity.this));
                    Log.i(TAG, "版本号对比code: " + code);
                    //版本号1>版本号2 并且不进行强制更新操作
                    if (code == 1 && ConstantString.FORCE_UPDATE_FALSE.equals(versionCodeModel.results.getForceUpdate())) {
                        new AlertDialog.Builder(AboutActivity.this)
                                .setTitle("提示")
                                .setMessage("发现新版本：\n" + versionCodeModel.results.getContent() + "\n 是否进行更新？")
                                .setNegativeButton("暂不更新", null)
                                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        updateApk(versionCodeModel.results.getFileUrl());
                                    }
                                }).create().show();
                    } else {
                        ToastUtil.showToast("当前已是最新版本");
                    }

                }
            }
        });

    }

    /**
     * 根据下载地址对apk进行更新的操作
     *
     * @param url
     */
    private void updateApk(String url) {
        Log.i(TAG, "********下载URL*******: " + HttpUtil.getAbsolteUrl(url.replaceAll("'\'", "")));
        OkGo.get(HttpUtil.getAbsolteUrl(url.replaceAll("'\'", "")))
                .cacheMode(CacheMode.DEFAULT)
                .execute(new FileCallback(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/", "nhoa.apk") {
                    @Override
                    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        progressDialog.setMax(100);
                        progressDialog.setProgress((int) (progress * 100));
                        super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
                    }

                    @Override
                    public void onBefore(BaseRequest request) {
                        progressDialog.show();
                        super.onBefore(request);
                    }

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(final File file, Call call, Response response) {
                        progressDialog.dismiss();
                        VersionUtil.installApk(AboutActivity.this, file);
                    }
                });

    }
}
