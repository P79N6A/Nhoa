package com.wisdom.nhoa.metting.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.base.model.SuccessSimpleReslutModel;
import com.wisdom.nhoa.metting.model.ScanResultModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.homepage.activity
 * @class describe：扫完码之后，展示给用户的小弹层……
 * @time 2018/4/10 14:56
 * @change
 */

public class MeetingScanResultActivity extends BaseActivity {
    @BindView(R.id.tv_subject)
    TextView tvSubject;
    @BindView(R.id.tv_value_subject)
    TextView tvValueSubject;
    @BindView(R.id.tv_value_department)
    TextView tvValueDepartment;
    @BindView(R.id.tv_value_begin)
    TextView tvValueBegin;
    @BindView(R.id.tv_value_content)
    TextView tvValueContent;
    @BindView(R.id.btn_sign)
    Button btnSign;
    @BindView(R.id.ll_parent)
    LinearLayout ll_parent;


    private ScanResultModel scanResultModel;
    private String signStatus="";


    @Override
    public void initViews() {
        if (getIntent() != null) {
            scanResultModel = (ScanResultModel) getIntent().getExtras().getSerializable("data");
            signStatus=getIntent().getStringExtra("signStatus");
        }
        if (scanResultModel != null) {
            tvSubject.setText(scanResultModel.getMeetingtitle());
            tvValueDepartment.setText(scanResultModel.getConvokedep());
            tvValueBegin.setText(scanResultModel.getStarttime());
            tvValueContent.setText(scanResultModel.getMeetingContent());
            if (ConstantString.METTING_SIGN.equals(signStatus)) {
                //已签到
                btnSign.setText("已签到");
                btnSign.setBackground(getResources().getDrawable(R.drawable.shape_circle_cornrer_bg_grey));
                btnSign.setClickable(false);
            } else {
                //未签到
                btnSign.setText("签到");
                btnSign.setBackground(getResources().getDrawable(R.drawable.shape_circle_cornrer_bg_orange));
                btnSign.setClickable(true);
            }
        }
        ll_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeetingScanResultActivity.this.finish();
            }
        });
    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_meeting_scan_result);
    }


    /**
     * 签到按钮点击事件
     */
    @OnClick(R.id.btn_sign)
    public void onViewClicked() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("您确定进行签到吗？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        postSign();//进行签到操作
                    }
                })
                .create().show();
    }

    /**
     * 签到
     */
    private void postSign() {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(MeetingScanResultActivity.this).getAccess_token());
        params.put("meetingid", scanResultModel.getMeetingid());
        params.put("user_id", SharedPreferenceUtil.getUserInfo(MeetingScanResultActivity.this).getUid());
        HttpUtil.httpGet(ConstantUrl.MEETING_SIGNING, params, new JsonCallback<BaseModel<SuccessSimpleReslutModel>>() {
            @Override
            public void onSuccess(BaseModel<SuccessSimpleReslutModel> meetingSignModelBaseModel, Call call, Response response) {
                U.closeLoadingDialog();
                ToastUtil.showToast("签到成功");
                MeetingScanResultActivity.this.finish();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
            }
        });
    }

}
