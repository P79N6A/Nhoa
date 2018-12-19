package com.wisdom.nhoa.mine.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class ConfirmChangeActivity extends BaseActivity {
    @BindView(R.id.et_new_parword)
    EditText new_parword;
    @BindView(R.id.et_confirm_parword)
    EditText econfirm_parword;
    Context mContext;
    @Override
    public void initViews() {
     setTitle(R.string.change_password);
        mContext=this;
    }

    private void confirmChange() {

        String getverify=getIntent().getStringExtra("verify");
        String newPassword = new_parword.getText().toString();
        String confirmPassword = new_parword.getText().toString();
        if (TextUtils.isEmpty(newPassword)) {
            ToastUtil.showToast(this, "请填密码");
            return;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            ToastUtil.showToast(this, "请填确认填写密码");
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            ToastUtil.showToast(this, "两次密码不一致");
            return;
        }
        U.showLoadingDialog(mContext);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        params.put("newpassword", newPassword);
        params.put("checkcode", getverify);
        HttpUtil.httpGet(ConstantUrl.CHANGE_PASSWORD, params, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                U.closeLoadingDialog();
                ToastUtil.showToast(ConfirmChangeActivity.this, "修改成功");
             finish();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
            }
        });
    }
    @OnClick(R.id.bt_confirm_fin)
    public void OnViewClick(View view){
      switch (view.getId()){
       case R.id.bt_confirm_fin:
           confirmChange();
           break;
      }
    }
    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_confirm_change);
    }
}
