package com.wisdom.nhoa.mine.activity;

import android.Manifest;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.AppApplication;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.homepage.activity.HomePageActivity;
import com.wisdom.nhoa.mine.model.LoginModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.StrUtils;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.dbHelper.DbHelperCustom;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.mine.activity
 * @class describe：登录页面
 * @time 2018/3/7 17:23
 * @change
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.mine_et_login_account)
    EditText mineEtLoginAccount;
    @BindView(R.id.mine_et_login_psw)
    EditText mineEtLoginPsw;
    public static final String TAG = LoginActivity.class.getSimpleName();


    @Override
    public void initViews() {
        judgeIsLoginOutOfDate();
        //申请权限的操作
        requestPermissions();

        LoginModel loginModel = SharedPreferenceUtil.getUserInfo(LoginActivity.this);
        if (loginModel != null) {
            mineEtLoginAccount.setText(loginModel.getLogin_name() + "");
            mineEtLoginPsw.setText(loginModel.getPasssword() + "");
        }

    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.mine_activity_login);
    }

    @OnClick(R.id.mine_btn_login)
    public void onViewClicked(View view) {
        if (checkWidget()) {//界面控验证通过
            U.showLoadingDialog(LoginActivity.this);
            HttpParams httpParams = new HttpParams();
            httpParams.put("loginname", StrUtils.getEdtTxtContent(mineEtLoginAccount));
            httpParams.put("password", StrUtils.getEdtTxtContent(mineEtLoginPsw));
            HttpUtil.httpGet(ConstantUrl.LOGIN_URL, httpParams, new JsonCallback<BaseModel<LoginModel>>() {
                @Override
                public void onSuccess(BaseModel<LoginModel> loginModelBaseModel, Call call, Response response) {
                    loginModelBaseModel.results.setPasssword(mineEtLoginPsw.getText().toString());
                    loginModelBaseModel.results.setLogin_name(mineEtLoginAccount.getText().toString());
                    ConstantString.LOGIN_STATE = true;
                    //存储用户信息到sp文件
                    SharedPreferenceUtil
                            .getConfig(LoginActivity.this)
                            .putSerializable(ConstantString.USER_INFO, loginModelBaseModel.results);
                    ToastUtil.showToast("登录成功");
                    Log.i(TAG, "onSuccessToken: " + loginModelBaseModel.results.getAccess_token());
                    //注册，绑定百度推送
                    PushManager.startWork(LoginActivity.this
                            , PushConstants.LOGIN_TYPE_API_KEY, ConstantString.BAIDU_API_KEY);
                    U.closeLoadingDialog();
                    DbHelperCustom.creatTable(loginModelBaseModel.results.getUid().replaceAll("-", ""), AppApplication.getInstance().GetSqliteDataBase());
                    //启动主页面
                    Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                    //用来判断是否是推送点击进来启动的App
                    if (getIntent().getBooleanExtra(ConstantString.FORM_NOTICE_OPEN, false)) {
                        intent.putExtra(ConstantString.FORM_NOTICE_OPEN
                                , getIntent().getBooleanExtra(ConstantString.FORM_NOTICE_OPEN, false));
                        intent.putExtra("messTypeCode", getIntent().getStringExtra("messTypeCode"));
                        intent.putExtra("dataId", getIntent().getStringExtra("dataId"));
                        startActivity(intent);
                    } else {
                        intent.putExtra(ConstantString.FORM_NOTICE_OPEN
                                , false);
                        startActivity(intent);
                    }
                    LoginActivity.this.finish();
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    U.closeLoadingDialog();
                }
            });
        }
    }



    /**
     * 检查界面控件是否为空
     *
     * @return
     */
    private boolean checkWidget() {
        Boolean isChecked = true;
        if (StrUtils.isEdtTxtEmpty(mineEtLoginAccount)) {
            isChecked = false;
            ToastUtil.showToast(R.string.mine_login_account_hint);
        } else if (StrUtils.isEdtTxtEmpty(mineEtLoginPsw)) {
            isChecked = false;
            ToastUtil.showToast(R.string.mine_login_psw_hint);
        }
        return isChecked;
    }

    /**
     * 检查并申请权限的方法
     */
    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.READ_PHONE_STATE
                            , Manifest.permission.CAMERA
                            , Manifest.permission.CALL_PHONE
                            , Manifest.permission.READ_EXTERNAL_STORAGE
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE
                            , Manifest.permission.ACCESS_WIFI_STATE},
                    1);
        }
    }

    /**
     * 判断当前登录是否过期
     */
    private void judgeIsLoginOutOfDate() {
        String expires_in = "";
        if (SharedPreferenceUtil.getUserInfo(this) != null) {
            expires_in = SharedPreferenceUtil.getUserInfo(this).getExpires_in();
        }
        Log.i(TAG, "expires_in: " + expires_in);
        if (!"".equals(expires_in) && ConstantString.LOGIN_STATE) {
            //用户之前登陆过，判断是否过期
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                long expiresTime = format.parse(expires_in).getTime();
                long currentTime = System.currentTimeMillis();
                if (expiresTime > currentTime) {
                    //登录没有过期，直接进入主页
                    Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                    //用来判断是否是推送点击进来启动的App
                    if (getIntent().getBooleanExtra(ConstantString.FORM_NOTICE_OPEN, false)) {
                        intent.putExtra(ConstantString.FORM_NOTICE_OPEN
                                , getIntent().getBooleanExtra(ConstantString.FORM_NOTICE_OPEN, false));
                        intent.putExtra("messTypeCode", getIntent().getStringExtra("messTypeCode"));
                        intent.putExtra("dataId", getIntent().getStringExtra("dataId"));
                        startActivity(intent);
                    } else {
                        intent.putExtra(ConstantString.FORM_NOTICE_OPEN
                                , false);
                        startActivity(intent);
                    }
                    //注册，绑定百度推送
                    PushManager.startWork(LoginActivity.this, PushConstants.LOGIN_TYPE_API_KEY, ConstantString.BAIDU_API_KEY);
                    this.finish();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 初始化自定义百度推送样式
     */
//    private void initCustomBuilder(){
//        Resources resource = this.getResources();
//        String pkgName = this.getPackageName();
//        CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder
//                (resource.getIdentifier("notification_custom_builder", "layout", pkgName),
//                resource.getIdentifier("notification_icon", "id", pkgName),
//                resource.getIdentifier("notification_title", "id", pkgName),
//                        resource.getIdentifier("notification_text", "id", pkgName));
//        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
//        cBuilder.setNotificationDefaults(Notification.DEFAULT_VIBRATE);
//        cBuilder.setStatusbarIcon(this.getApplicationInfo().icon);
//        cBuilder.setLayoutDrawable(resource.getIdentifier( "logo", "mipmap", pkgName));
//        cBuilder.setNotificationSound(Uri.withAppendedPath(
//               MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "6").toString());
//        // 推送高级设置，通知栏样式设置为下面的ID
//         PushManager.setNotificationBuilder(this, 1, cBuilder);
//    }

}
