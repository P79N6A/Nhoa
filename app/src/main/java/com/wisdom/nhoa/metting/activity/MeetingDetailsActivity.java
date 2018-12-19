package com.wisdom.nhoa.metting.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.base.model.SuccessSimpleReslutModel;
import com.wisdom.nhoa.metting.model.MeetingDetailsModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;
import com.wisdom.nhoa.widget.MeetingSignDialog;
import com.wisdom.nhoa.widget.qrcode.MeetingOverDialog;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

public class MeetingDetailsActivity extends BaseActivity implements View.OnClickListener, MeetingSignDialog.SignInputListener, MeetingOverDialog.OverMettingListener {

    @BindView(R.id.comm_head_title)
    TextView commHeadTitle;
    @BindView(R.id.tv_meeting_theme)
    TextView tvMeetingTheme;
    @BindView(R.id.tv_convoke_department)
    TextView tvConvokeDepartment;
    @BindView(R.id.tv_compere)
    TextView tvCompere;
    @BindView(R.id.tv_Participant)
    TextView tvParticipant;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.tv_current_state)
    TextView tvCurrentState;
    @BindView(R.id.tv_meetingcontent)
    TextView tvMeetingcontent;
    @BindView(R.id.tv_create_time)
    TextView tvCreateTime;
    @BindView(R.id.tv_meeting_room)
    TextView tv_meeting_room;
    @BindView(R.id.bt_sign_user)
    Button btSignUser;
    @BindView(R.id.bt_over_meeting)
    Button btOverMeeting;
    @BindView(R.id.bt_sign)
    Button btSign;

    private String meetingid = "";//会议id
    private String signstate = "";//签到状态
    private String isCreater = "";//是否是创建者
    private String state = "";//会议开始状态

    private String TAG = MeetingDetailsActivity.class.getSimpleName();

    @Override
    public void initViews() {
        setTitle(R.string.index_meeting_detail_title);
        if (getIntent() != null) {
            Intent intent = getIntent();
            meetingid = intent.getStringExtra("meetingid");
            signstate = intent.getStringExtra("signstate");
            isCreater = intent.getStringExtra("isCreater");
            state = intent.getStringExtra("state");
        }
        btSignUser.setOnClickListener(this);
        btOverMeeting.setOnClickListener(this);
        btSign.setOnClickListener(this);
        if (!"".equals(meetingid) && meetingid != null) {
            getdata();
        }
        if (ConstantString.METTING_UNSIGN.equals(signstate)) {
            btSign.setText("签到");
        } else {
            Drawable drawable = getResources().getDrawable(R.mipmap.meeting_signin_pre);
            // / 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            btSign.setCompoundDrawables(null, drawable, null, null); //设置上图标
            btSign.setText("已签到");
            btSign.setTextColor(getResources().getColor(R.color.meeting_signedin_text));
            btSign.setClickable(false);
        }
        //只有会议创建者才能结束会议；会议已经结束就不显示结束按钮了。
        if (ConstantString.METTING_IS_CREATER_TRUE.equals(isCreater) &&
                !state.equals(ConstantString.MEETING_STATE_OVER)
                ) {
            btOverMeeting.setVisibility(View.VISIBLE);
        } else {
            btOverMeeting.setVisibility(View.GONE);
        }
//        会议结束的话，就隐藏会议签到按钮
        if (state.equals(ConstantString.MEETING_STATE_OVER)) {
            btSign.setVisibility(View.GONE);
        }
    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_meeting_details);
    }


    //获取会议详情
    private void getdata() {
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(MeetingDetailsActivity.this).getAccess_token());
        params.put("meetingid", meetingid);
        HttpUtil.httpGet(ConstantUrl.MEETING_DETAILS, params, new JsonCallback<BaseModel<MeetingDetailsModel>>() {
            @Override
            public void onSuccess(final BaseModel<MeetingDetailsModel> listBaseModel, Call call, Response response) {
                tvMeetingTheme.setText(listBaseModel.results.getMeetingtitle());
                tvConvokeDepartment.setText(listBaseModel.results.getConvokedep());
                tvCompere.setText(listBaseModel.results.getCompere());
                tvParticipant.setText(listBaseModel.results.getMemberid());
                tvStartTime.setText(listBaseModel.results.getStarttime());
                tvEndTime.setText(listBaseModel.results.getEndtime());
                tvCreateTime.setText(listBaseModel.results.getCreatetime());
                tvCurrentState.setText(listBaseModel.results.getState());
                tvMeetingcontent.setText(listBaseModel.results.getMeetingcontent());
                tv_meeting_room.setText(listBaseModel.results.getBdrmName());
                tv_meeting_room.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(MeetingDetailsActivity.this)
                                .setTitle("会议室地址")
                                .setMessage(listBaseModel.results.getBdrmAdd())
                                .setPositiveButton("确定", null)
                                .create().show();
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_sign_user:
                Intent intent = new Intent(this, MeetingSignUserActivity.class);
                intent.putExtra("meetingid", meetingid);
                startActivity(intent);
                break;
            case R.id.bt_sign:
                showDialogFragment(new MeetingSignDialog());
                break;
            case R.id.bt_over_meeting:
                showDialogFragment(new MeetingOverDialog());
                break;
            default:
                break;

        }
    }

    //签到
    private void postSign() {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(MeetingDetailsActivity.this).getAccess_token());
        params.put("meetingid", meetingid);
        params.put("user_id", SharedPreferenceUtil.getUserInfo(MeetingDetailsActivity.this).getUid());
        HttpUtil.httpGet(ConstantUrl.MEETING_SIGNING, params, new JsonCallback<BaseModel<SuccessSimpleReslutModel>>() {
            @Override
            public void onSuccess(BaseModel<SuccessSimpleReslutModel> meetingSignModelBaseModel, Call call, Response response) {
                U.closeLoadingDialog();
                ToastUtil.showToast("签到成功");
                Drawable drawable = getResources().getDrawable(R.mipmap.meeting_signin_pre);
                // / 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                        drawable.getMinimumHeight());
                btSign.setCompoundDrawables(null, drawable, null, null); //设置上图标
                btSign.setText("已签到");
                btSign.setTextColor(getResources().getColor(R.color.meeting_signedin_text));
                btSign.setClickable(false);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
            }
        });
    }

    private void postOverMeeting() {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(MeetingDetailsActivity.this).getAccess_token());
        params.put("meetingid", meetingid);
        HttpUtil.httpGet(ConstantUrl.MEETING_OVER, params, new JsonCallback<BaseModel<SuccessSimpleReslutModel>>() {
            @Override
            public void onSuccess(BaseModel<SuccessSimpleReslutModel> meetingSignModelBaseModel, Call call, Response response) {
                U.closeLoadingDialog();
                ToastUtil.showToast("会议已结束");
                finish();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
            }
        });
    }


    /**
     * 弹出dialogFragment的方法
     *
     * @param dialogFragment
     */
    private void showDialogFragment(DialogFragment dialogFragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        dialogFragment.show(ft, "signin");
    }

    //签到按钮
    @Override
    public void onSignin() {
        postSign();
    }

    //结束会议
    @Override
    public void onOverMetting() {
        postOverMeeting();
    }


}
