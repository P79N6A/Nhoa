package com.wisdom.nhoa.metting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.base.model.SuccessSimpleReslutModel;
import com.wisdom.nhoa.meeting_new.activity.ParticipantActivity;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;
import com.wisdom.nhoa.widget.CustomDatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author lxd
 * @ProjectName project： 创建会议
 * @class package：
 * @class describe：CreateMeetingActivity
 * @time 17:34
 * @change
 */
public class CreateMeetingActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.et_meeting_theme)
    EditText etMeetingTheme;//会议主题
    @BindView(R.id.et_convoke_department)
    EditText etConvokeDepartment;//召开部门
    @BindView(R.id.tv_compere)
    TextView tvCompere;//主持人
    @BindView(R.id.tv_Participant)
    TextView tvParticipant;//参会人员
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;//开始时间
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;//结束时间
    public String meeting_time = "";
    @BindView(R.id.bt_create_meeting)
    Button btCreateMeeting;
    @BindView(R.id.et_meetingContent)
    EditText etMeetingContent;

    private Calendar c = Calendar.getInstance();
    private String memberid = "";
    private String username = "";
    private SimpleDateFormat formatter;
    private CustomDatePicker customDatePicker;
    private int currentmark = 0;
    private String currentTime = "";
    private String TAG = CreateMeetingActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initViews() {
        setTitle(R.string.index_meeting_createMeeting);
        initDataPick();
        tvCompere.setOnClickListener(this);
        tvParticipant.setOnClickListener(this);
        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        btCreateMeeting.setOnClickListener(this);
        etMeetingContent.setOnClickListener(this);
        tvCompere.setText(SharedPreferenceUtil.getUserInfo(this).getUser_name());
    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_create_meeting);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_compere:

                break;
            case R.id.tv_Participant:
                startActivityForResult(new Intent(this, ParticipantActivity.class), 777);
                break;
            case R.id.tv_start_time:
//                showDatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, tvStartTime, c);
                currentmark = 0;
                customDatePicker.show(tvStartTime.getText().toString());
                break;
            case R.id.tv_end_time:
//                showDatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, tvEndTime, c);
                currentmark = 1;
                customDatePicker.show(tvEndTime.getText().toString());
                break;
            case R.id.bt_create_meeting://创建会议
                if (verifyInput()) {
                    getdata();
                }
                break;
            default:
                break;

        }
    }

    private Boolean verifyInput() {
        SimpleDateFormat CurrentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date beginTime = null;
        Date endTime = null;
        try {
            beginTime = CurrentTime.parse(tvStartTime.getText().toString());
            endTime = CurrentTime.parse(tvEndTime.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (etMeetingTheme.getText().toString().trim().equals("")) {
            ToastUtil.showToast("请输入会议主题");
            return false;
        } else if (etConvokeDepartment.getText().toString().trim().equals("")) {
            ToastUtil.showToast("请输入召开部门");
            return false;
        } else if (tvCompere.getText().toString().trim().equals("")) {
            ToastUtil.showToast("请输入主持人");
            return false;
        } else if (tvParticipant.getText().toString().trim().equals("")) {
            ToastUtil.showToast("请输入参会人员");
            return false;
        } else if (tvStartTime.getText().toString().trim().equals("")) {
            ToastUtil.showToast("请输入开始时间");
            return false;
        } else if (tvEndTime.getText().toString().trim().equals("")) {
            ToastUtil.showToast("请输入结束时间");
            return false;
        } else if (etMeetingContent.getText().toString().trim().equals("")) {
            ToastUtil.showToast("请输入会议内容");
            return false;
        } else if (beginTime.getTime() > endTime.getTime()) {
            ToastUtil.showToast("结束时间不能小于开始时间");
            return false;
        } else if (beginTime.getTime() < System.currentTimeMillis()) {
            ToastUtil.showToast("会议开始时间不能早于当前时间");
            return false;
        } else {
            return true;
        }
    }


    private void getdata() {
        U.showLoadingDialog(this);
        formatter = new SimpleDateFormat("yyyy-MM-dd   HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String createtime = formatter.format(curDate);
        Log.i("createtime", "createtime: " + createtime);
        Log.i(TAG, "memberid: " + memberid);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(CreateMeetingActivity.this).getAccess_token());
        params.put("meetingtitle", etMeetingTheme.getText().toString().trim());
        params.put("convokedep", etConvokeDepartment.getText().toString().trim());
        params.put("compere", tvCompere.getText().toString().trim());
        params.put("memberid", memberid);
        params.put("starttime", tvStartTime.getText().toString().trim());
        params.put("endtime", tvEndTime.getText().toString().trim());
        params.put("createtime", createtime);
        params.put("meetingcontent", etMeetingContent.getText().toString().trim());
        HttpUtil.httpGet(ConstantUrl.MEETING_SEND, params, new JsonCallback<BaseModel<SuccessSimpleReslutModel>>() {
            @Override
            public void onSuccess(BaseModel<SuccessSimpleReslutModel> listBaseModel, Call call, Response response) {
                U.closeLoadingDialog();
                ToastUtil.showToast("创建会议完成");
                finish();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 777) {
            if (data != null) {
                memberid = data.getStringExtra("memberid");
                username = data.getStringExtra("username");
                tvParticipant.setText(username);
            }
        }

    }

    private void initDataPick() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());

        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                currentTime = time;
                if (currentmark == 0) {
                    tvStartTime.setText(time);
                } else {
                    tvEndTime.setText(time);
                }
            }
        }, "1990-01-01 00:00", "2100-01-01 00:00");
        customDatePicker.showSpecificTime(true); // 显示时和分
        customDatePicker.setIsLoop(true); // 允许循环滚动
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis() + 10 * 60 * 1000);
        Date onehourDate = new Date(System.currentTimeMillis() + 10*60 * 60 * 1000);
        tvStartTime.setText(formatter.format(curDate));
        tvEndTime.setText(formatter.format(onehourDate));
    }


}
