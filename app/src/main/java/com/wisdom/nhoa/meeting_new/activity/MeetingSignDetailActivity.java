package com.wisdom.nhoa.meeting_new.activity;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.base.model.SuccessSimpleReslutModel;
import com.wisdom.nhoa.meeting_new.model.MeetingDetailNewModel;
import com.wisdom.nhoa.meeting_new.model.RegistrationModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.StrUtils;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;
import com.wisdom.nhoa.widget.MeetingSignDialog;
import com.wisdom.nhoa.widget.fileview.FileDisplayActivity;
import com.wisdom.nhoa.widget.fileview.PreviwPicActivity;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.meeting_new.activity
 * @class describe：会议签到点进去的详情页面
 * @time 2018/11/7 14:33
 * @change
 */
public class MeetingSignDetailActivity extends BaseActivity implements MeetingSignDialog.SignInputListener {
    @BindView(R.id.tv_meeting_theme)
    TextView tvMeetingTheme;
    @BindView(R.id.tv_convoke_department)
    TextView tvConvokeDepartment;
    @BindView(R.id.tv_meeting_room)
    TextView tvMeetingRoom;
    @BindView(R.id.tv_compere)
    TextView tvCompere;
    @BindView(R.id.tv_Participant)
    TextView tvParticipant;
    @BindView(R.id.tv_meeting_contect_person)
    TextView tvMeetingContectPerson;
    @BindView(R.id.tv_meeting_contect_person_phone)
    TextView tvMeetingContectPersonPhone;
    @BindView(R.id.tv_meeting_record_person)
    TextView tvMeetingRecordPerson;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.tv_current_state)
    TextView tvCurrentState;
    @BindView(R.id.tv_meeting_readed_person)
    TextView tvMeetingReadedPerson;
    @BindView(R.id.tv_meeting_unreaded_person)
    TextView tvMeetingUnreadedPerson;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.ll_add_subject)
    LinearLayout llAddSubject;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.bt_sign_meeting)
    Button btSignMeeting;
    private RegistrationModel registrationModel;
    public static final String TAG = MeetingSignDetailActivity.class.getSimpleName();

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_meeting_sign_detail_new);
    }

    @Override
    public void initViews() {
        setTitle(R.string.meeting_sign_meeting);
        btSignMeeting.setVisibility(View.GONE);
        if (getIntent() != null) {
            registrationModel = (RegistrationModel) getIntent().getExtras().getSerializable("data");
            getData(registrationModel.getMeetingid());
        }
    }


    /**
     * 签到按钮点击事件
     */
    @OnClick(R.id.bt_sign_meeting)
    public void onViewClicked() {
        showDialogFragment(new MeetingSignDialog());
    }

    /**
     * 访问接口获得相关数据
     *
     * @param meetingId
     */
    private void getData(String meetingId) {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(MeetingSignDetailActivity.this).getAccess_token());
        params.put("meetingid", meetingId);
        HttpUtil.httpGet(ConstantUrl.MEETING_DETAILS, params, new JsonCallback<BaseModel<MeetingDetailNewModel>>() {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
                ToastUtil.showToast(e.getMessage());
            }

            @Override
            public void onSuccess(BaseModel<MeetingDetailNewModel> meetingDetailNewModelBaseModel, Call call, Response response) {
                U.closeLoadingDialog();
                //得到会议详情信息，并设置到界面
                setDataOnToInterface(meetingDetailNewModelBaseModel.results);
                //调整会议签到按钮状态
                setBtnSignState();
            }
        });
    }

    /**
     * 设置签到按钮的状态
     */
    private void setBtnSignState() {
        if (ConstantString.MEETING_STATE_DOING.equals(registrationModel.getState())) {
            btSignMeeting.setVisibility(View.VISIBLE);
            if (ConstantString.METTING_UNSIGN.equals(registrationModel.getSignstate())) {
                btSignMeeting.setText("签到");
                btSignMeeting.setBackgroundColor(getResources().getColor(R.color.color_647aea));
                btSignMeeting.setClickable(true);
            } else {
                btSignMeeting.setText("已签到");
                btSignMeeting.setBackgroundColor(getResources().getColor(R.color.color_cccccc));
                btSignMeeting.setClickable(false);
            }
        } else {
            btSignMeeting.setVisibility(View.GONE);
        }
    }

    /**
     * 将接口返回的详情信息设置在界面
     *
     * @param model
     */
    private void setDataOnToInterface(MeetingDetailNewModel model) {
        checkValidateAndSetValue(tvMeetingTheme, model.getMeetingtitle());
        checkValidateAndSetValue(tvConvokeDepartment, model.getConvokedep());
        checkValidateAndSetValue(tvMeetingRoom, model.getBdrmName());
        checkValidateAndSetValue(tvCompere, model.getMaster());
        checkValidateAndSetValue(tvParticipant, model.getMemberid());
        checkValidateAndSetValue(tvMeetingContectPerson, model.getLinkman());
        checkValidateAndSetValue(tvMeetingContectPersonPhone, model.getLinkTel());
        checkValidateAndSetValue(tvMeetingRecordPerson, model.getLogMan());
        checkValidateAndSetValue(tvStartTime, model.getStarttime());
        checkValidateAndSetValue(tvEndTime, model.getEndtime());
        checkValidateAndSetValue(tvCurrentState, model.getState());
        checkValidateAndSetValue(tvMeetingReadedPerson, model.getAccMan());
        checkValidateAndSetValue(tvMeetingUnreadedPerson, model.getNotAccMan());
        checkValidateAndSetValue(tvRemark, model.getNote());
        //遍历数据，设置议题相关的布局
        if (model.getIssues().size() > 0) {
            for (int j = 0; j < model.getIssues().size(); j++) {
                View view = LayoutInflater.from(this).inflate(R.layout.item_meeting_publish_add_subjet, null, false);
                TextView textView = view.findViewById(R.id.tv_layout_title);
                EditText et_meeting_theme = view.findViewById(R.id.et_meeting_theme);
                EditText et_convoke_department = view.findViewById(R.id.et_convoke_department);
                TextView tv_file = view.findViewById(R.id.tv_file);
                TextView tv_add_attach = view.findViewById(R.id.tv_add_attach);
                tv_add_attach.setVisibility(View.GONE);
                tv_file.setText(model.getIssues().get(j).getSsuesFileName());
                et_meeting_theme.setText(model.getIssues().get(j).getSsuesName());
                et_convoke_department.setText(model.getIssues().get(j).getYtry());
                et_meeting_theme.setFocusable(false);
                et_convoke_department.setFocusable(false);
                llContent.addView(view, llContent.getChildCount() - 1);
                textView.setText("议题" + StrUtils.toChinese((j + 1) + ""));
                //设置点击预览
                final String fileName = model.getIssues().get(j).getSsuesFileName();
                final String fileUrl = ConstantUrl.BASE_URL
                        + model.getIssues().get(j).getSsuesFileUrl()
                        + fileName;
                tv_file.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFile(fileUrl, fileName);
                    }
                });

            }
        }
    }

    /**
     * 执行预览文件的方法
     *
     * @param fileUrl
     */
    private void showFile(String fileUrl, String fileNameStr) {
        Log.i(TAG, "fileUrl: " + fileUrl);
        Log.i(TAG, "fileNameStr: " + fileNameStr);
        //预览
        String fileName = fileNameStr;
        String fileNameArray[] = fileName.split("\\.");
        Log.i(TAG, "onClick: 截取地址-----" + fileNameArray.length);
        if (fileNameArray.length > 1) {
            String fileLastName = fileNameArray[fileNameArray.length - 1];
            Log.i(TAG, "fileLastName: " + fileLastName);
            if (fileLastName.endsWith("jpg") ||
                    fileLastName.endsWith("png") ||
                    fileLastName.endsWith("jpeg") ||
                    fileLastName.endsWith("bmp") ||
                    fileLastName.endsWith("gif")
                    ) {//等待预览的文件是图片资源
                Intent intent = new Intent(context,
                        PreviwPicActivity.class);
                intent.putExtra("title", fileName);
                intent.putExtra("url", fileUrl);
                Log.i(TAG, "onClick: 截取地址-----" + fileUrl);
                context.startActivity(intent);
            } else if (fileLastName.endsWith("txt")
                    || fileLastName.endsWith("rar")
                    || fileLastName.endsWith("zip")) {
                ToastUtil.showToast("暂不支持预览此格式的文件");
            } else {
                FileDisplayActivity.show(context, fileUrl);
            }
        } else {//没有拿到文件的后缀名称
            ToastUtil.showToast("暂不支持预览此格式的文件");
        }
    }

    /**
     * 判断如果值不为空，那么将值设置在指定TextView控件
     *
     * @param textView
     * @param value
     */
    public void checkValidateAndSetValue(TextView textView, String value) {
        if (!StrUtils.isEmpty(value)) {
            textView.setText(value);
        }
    }

    //签到
    private void postSign() {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(MeetingSignDetailActivity.this).getAccess_token());
        params.put("meetingid", registrationModel.getMeetingid());
        params.put("user_id", SharedPreferenceUtil.getUserInfo(MeetingSignDetailActivity.this).getUid());
        HttpUtil.httpGet(ConstantUrl.MEETING_SIGNING, params, new JsonCallback<BaseModel<SuccessSimpleReslutModel>>() {
            @Override
            public void onSuccess(BaseModel<SuccessSimpleReslutModel> meetingSignModelBaseModel, Call call, Response response) {
                U.closeLoadingDialog();
                ToastUtil.showToast("签到成功");
                btSignMeeting.setText("已签到");
                btSignMeeting.setBackgroundColor(getResources().getColor(R.color.color_cccccc));
                btSignMeeting.setClickable(false);
                setResult(ConstantString.REQUEST_CODE_REFRESH_DATA);
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

    /**
     * 签到功能的监听回调
     */
    @Override
    public void onSignin() {
        postSign();
    }
}
