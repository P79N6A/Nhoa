package com.wisdom.nhoa.meeting_new.activity;

import android.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.meeting_new.model.ApplyMeetingRoomDetailModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.meeting_new.activity
 * @class describe：我审核的 会议室列表详情界面
 * @time 2018/10/23 14:27
 * @change
 */
public class MyCheckedMeetingRoomDetailActivity extends BaseActivity {
    @BindView(R.id.et_meeting_theme)
    EditText etMeetingTheme;
    @BindView(R.id.et_meeting_room)
    EditText etMeetingRoom;
    @BindView(R.id.et_meeting_participate_num)
    EditText etMeetingParticipateNum;
    @BindView(R.id.et_start_time)
    EditText etStartTime;
    @BindView(R.id.et_end_time)
    EditText etEndTime;
    @BindView(R.id.et_meeting_apply_time)
    EditText etMeetingApplyTime;
    @BindView(R.id.et_current_state)
    EditText etCurrentState;
    private String dataId = "";

    private ApplyMeetingRoomDetailModel applyMeetingRoomDetailModel;

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_my_checked_meeting_room_details);
    }

    @Override
    public void initViews() {
        setTitle(R.string.meeting_apply_detail_title);
        if (getIntent() != null) {
            dataId = getIntent().getStringExtra("data");
        }
        getData();
    }


    /**
     * 获得接口数据
     */
    private void getData() {
        if (!"".equals(dataId)) {
            HttpParams params = new HttpParams();
            params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
            params.put("appkey", ConstantString.APP_KEY);
            params.put("dataId", dataId);
            HttpUtil.httpGet(ConstantUrl.APPLY_MEETING_ROOM_DETAIL_URL, params, new JsonCallback<BaseModel<ApplyMeetingRoomDetailModel>>() {
                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    //接口访问出错

                }

                @Override
                public void onSuccess(final BaseModel<ApplyMeetingRoomDetailModel> model, Call call, Response response) {
                    //访问成功，设置数据到界面
                    etMeetingTheme.setText(model.results.getTheme());
                    etEndTime.setText(model.results.getEndTime());
                    etMeetingApplyTime.setText(model.results.getAddTime());
                    etStartTime.setText(model.results.getStaTime());
                    etMeetingParticipateNum.setText(model.results.getCrewSize());
                    etMeetingRoom.setText(model.results.getBdrmName());
                    etCurrentState.setText(model.results.getStatus());
                    applyMeetingRoomDetailModel = model.results;
                    //点击会议室名称，对话框展示会议室地址
                    etMeetingRoom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showMeetingRoomAddr(model.results.getBdrmAdd());
                        }
                    });
                }
            });
        } else {
            ToastUtil.showToast("获取信息失败，请重试");
            this.finish();
        }

    }

    /**
     * 点击会议室后弹出会议室地址的方法
     *
     * @param bdrmAdd
     */
    private void showMeetingRoomAddr(String bdrmAdd) {
        new AlertDialog.Builder(MyCheckedMeetingRoomDetailActivity.this)
                .setTitle("会议室地址：")
                .setMessage(bdrmAdd)
                .setPositiveButton("确定", null)
                .create().show();
    }


}
