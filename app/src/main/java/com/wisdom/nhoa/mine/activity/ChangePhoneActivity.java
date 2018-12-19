package com.wisdom.nhoa.mine.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.util.MyCountDownTimer;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class ChangePhoneActivity extends BaseActivity {
    @BindView(R.id.et_write_verify)
    TextView write_verify;
    @BindView(R.id.et_new_phone_number)
    TextView new_phone_number;
    @BindView(R.id.bt_get_verify)
    Button get_verify;
    @BindView(R.id.ll_old_phone_layout)
    LinearLayout old_phone_layout;
    @BindView(R.id.bt_change_tel_finish)
    Button change_tel_finish;
    @BindView(R.id.tv_old_phone)
    TextView tv_old_phone;
    @Override
    public void initViews() {
     setTitle(R.string.change_phone_title);
      String oldPhone=  SharedPreferenceUtil.getUserInfo(this).getPhone();
      if (oldPhone.equals("")){
          old_phone_layout.setVisibility(View.GONE);
          Dialog dialog=new AlertDialog.Builder(this).setTitle("请绑定手机号").setPositiveButton("确定", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
              }
          }).create();
          dialog.show();
      }else {
          tv_old_phone.setText(oldPhone);
      }
    }

    private void getRequest() {

       String newPhoneNumber=new_phone_number.getText().toString();
       String verifyCode=write_verify.getText().toString();
        if (TextUtils.isEmpty(newPhoneNumber)){
            ToastUtil.showToast(this,"请填写新的手机号码");
            return;
        }
        if (TextUtils.isEmpty(verifyCode)){
            ToastUtil.showToast(this,"请填写验证码");
            return;
        }
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        params.put("newphone", newPhoneNumber);
        params.put("checkcode", verifyCode);
        HttpUtil.httpGet(ConstantUrl.CHANGE_TELEPHONE, params, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                U.closeLoadingDialog();
                ToastUtil.showToast(ChangePhoneActivity.this, "修改成功");
               finish();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
            }
        });
    }
    @OnClick({R.id.bt_get_verify,R.id.bt_change_tel_finish})
    public void OnViewClick(View view){
        switch (view.getId()){
            case R.id.bt_get_verify:
                getVerifyRequest();
                break;
            case R.id.bt_change_tel_finish:
                getRequest();
                break;
        default:
                break;
        }

    }

    private void getVerifyRequest() {
        new MyCountDownTimer(60000,1000,get_verify).start();
//        HttpParams params = new HttpParams();
//        params.put("appkey", ConstantString.APP_KEY);
//        params.put("access_token", SharedPreferenceUtil.getUserInfo(context).getAccess_token());
//        params.put("newphone", newPhoneNumber);
//        params.put("checkcode", verifyCode);
//        HttpUtil.httpGet(ConstantUrl.CHANGE_TELEPHONE, params, new StringCallback() {
//            @Override
//            public void onSuccess(String s, Call call, Response response) {
//            }
//        });
    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_change_phone);
    }
}
