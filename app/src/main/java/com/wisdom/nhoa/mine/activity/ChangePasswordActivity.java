package com.wisdom.nhoa.mine.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.wisdom.nhoa.util.http_util.HttpUtil;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class ChangePasswordActivity extends BaseActivity {
    @BindView(R.id.et_mine_phone)
    TextView mine_phone;
    @BindView(R.id.et_write_m_verify)
    EditText write_m_verify;
    @BindView(R.id.bt_get_m_verify)
    Button getVerify;
    @BindView(R.id.bt_next_step)
    Button  next_step;
    @BindView(R.id.ll_all)
    LinearLayout ll_all;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initViews() {
        setTitle(R.string.change_password);
        String phone=SharedPreferenceUtil.getUserInfo(this).getPhone();
        if (phone.equals("")){
            ll_all.setVisibility(View.INVISIBLE);
            Dialog  dialog=new AlertDialog.Builder(this).setTitle("请先绑定手机号").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                 finish();
                }
            }).create();
            dialog.show();
        }else {
            mine_phone.setText(phone);
        }
    }
    @OnClick({R.id.bt_get_m_verify,R.id.bt_next_step})
    public void OnViewClick(View view){
        switch (view.getId()){
            case R.id.bt_get_m_verify:
                getVerifyCode();
                break;
            case R.id.bt_next_step:
                 next_step();
                break;
                default:
                    break;
        }

    }

    private void next_step() {
        String phoneNumber=mine_phone.getText().toString();
        String VerifyCode=write_m_verify.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)){
            ToastUtil.showToast(this,"请填写手机号码");
            return;
        }
        if (TextUtils.isEmpty(VerifyCode)){
            ToastUtil.showToast(this,"请填写验证码");
            return;
        }
        Intent intent=new Intent(ChangePasswordActivity.this,ConfirmChangeActivity.class);
        intent.putExtra("verify",VerifyCode);
        startActivity(intent);
        finish();
    }

    private void getVerifyCode() {
        new MyCountDownTimer(60000,1000,getVerify).start();
        String phoneNumber=mine_phone.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)){
            ToastUtil.showToast(this,"请填写手机号码");
            return;
        }
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        params.put("newphone", phoneNumber);
        HttpUtil.httpGet(ConstantUrl.CHANGE_TELEPHONE, params, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {

            }
        });

    }


    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_change_password);
    }
}
