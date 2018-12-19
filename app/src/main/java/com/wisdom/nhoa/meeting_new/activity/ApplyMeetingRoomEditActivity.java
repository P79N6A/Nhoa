package com.wisdom.nhoa.meeting_new.activity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.meeting_new.model.ApplyMeetingRoomDetailModel;
import com.wisdom.nhoa.meeting_new.model.MeetingRoomModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.StrUtils;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;
import com.wisdom.nhoa.widget.CustomDatePicker;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.meeting_new.activity
 * @class describe：申请会议室-编辑会议室信息
 * @time 2018/10/23 15:12
 * @change
 */
public class ApplyMeetingRoomEditActivity extends BaseActivity {
    @BindView(R.id.et_meeting_theme)
    EditText etMeetingTheme;
    @BindView(R.id.sp_meeting_room)
    Spinner spMeetingRoom;
    @BindView(R.id.et_meeting_participate_num)
    EditText etMeetingParticipateNum;
    @BindView(R.id.et_start_time)
    EditText etStartTime;
    @BindView(R.id.et_end_time)
    EditText etEndTime;
    private int currentmark = 0;
    private String currentTime = "";
    private CustomDatePicker customDatePicker;
    private String roomId = "";//会议室Id
    private ApplyMeetingRoomDetailModel applyMeetingRoomDetailModel;
    public static final String TAG = ApplyMeetingRoomEditActivity.class.getSimpleName();

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_meeting_room_edit_or_apply);
    }

    @Override
    public void initViews() {
        setTitle(R.string.meeting_apply_edit_title);
        initDataPick();
        initRoomAddr();
    }


    /**
     * 编辑完成 提交服务器的按钮
     */
    @OnClick(R.id.bt_apply_meeting_room)
    public void onViewClicked() {
        if (verifyInput()) {
            if (applyMeetingRoomDetailModel != null) {
                submitData();
            } else {
                ToastUtil.showToast("获取会议室信息失败，请重试");
            }
        }
    }


    /**
     * 选择会议开始时间
     */
    @OnClick(R.id.et_start_time)
    public void onEtStartTimeClicked() {
        currentmark = 0;
        customDatePicker.show(etStartTime.getText().toString());
    }

    /**
     * 选择会议结束时间
     */
    @OnClick(R.id.et_end_time)
    public void onEtEndTimeClicked() {
        currentmark = 1;
        customDatePicker.show(etEndTime.getText().toString());
    }

    /**
     * 初始化时间控件
     */
    private void initDataPick() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());

        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                currentTime = time;
                if (currentmark == 0) {
                    etStartTime.setText(time);
                } else {
                    etEndTime.setText(time);
                }
            }
        }, "1990-01-01 00:00", "2100-01-01 00:00");
        customDatePicker.showSpecificTime(true); // 显示时和分
        customDatePicker.setIsLoop(true); // 允许循环滚动
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis() + 10 * 60 * 1000);
        Date onehourDate = new Date(System.currentTimeMillis() + 10 * 60 * 60 * 1000);
        etStartTime.setText(formatter.format(curDate));
        etEndTime.setText(formatter.format(onehourDate));
    }

    /**
     * 验证界面输入的信息是否真实有效
     *
     * @return
     */
    private Boolean verifyInput() {
        SimpleDateFormat CurrentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date beginTime = null;
        Date endTime = null;
        try {
            beginTime = CurrentTime.parse(etStartTime.getText().toString());
            endTime = CurrentTime.parse(etEndTime.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (etMeetingTheme.getText().toString().trim().equals("")) {
            ToastUtil.showToast("请输入会议主题");
            return false;
        } else if (spMeetingRoom.getSelectedItem().toString().trim().equals("")) {
            ToastUtil.showToast("请选择会议室");
            return false;
        } else if (etMeetingParticipateNum.getText().toString().trim().equals("")) {
            ToastUtil.showToast("请输入人员数量");
            return false;
        } else if (etStartTime.getText().toString().trim().equals("")) {
            ToastUtil.showToast("请输入开始时间");
            return false;
        } else if (etEndTime.getText().toString().trim().equals("")) {
            ToastUtil.showToast("请输入结束时间");
            return false;
        } else if (beginTime.getTime() > endTime.getTime()) {
            ToastUtil.showToast("结束时间不能小于开始时间");
            return false;
        }
//        else if (beginTime.getTime() < System.currentTimeMillis()) {
//            ToastUtil.showToast("会议开始时间不能早于当前时间");
//            return false;
//        }
        else {
            return true;
        }
    }


    /**
     * 初始化会议室地址信息
     */
    private void initRoomAddr() {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        params.put("appkey", ConstantString.APP_KEY);
        HttpUtil.httpGet(ConstantUrl.GET_MEETING_ROOM_INFO_URL, params, new JsonCallback<BaseModel<List<MeetingRoomModel>>>() {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                ToastUtil.showToast("会议室信息加载失败，请重试");
                U.closeDialog();
            }

            @Override
            public void onSuccess(final BaseModel<List<MeetingRoomModel>> listBaseModel, Call call, Response response) {
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ApplyMeetingRoomEditActivity.this, android.R.layout.simple_spinner_dropdown_item);
                for (int i = 0; i < listBaseModel.results.size(); i++) {
                    dataAdapter.add(listBaseModel.results.get(i).getBdrmName());
                }
                U.closeDialog();
                //设置下拉选的数据集
                spMeetingRoom.setAdapter(dataAdapter);
                spMeetingRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        roomId = listBaseModel.results.get(position).getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                //从上级页面获得相关数据，遍历数据，将选中数据设置在spinner中
                if (getIntent() != null) {
                    applyMeetingRoomDetailModel = (ApplyMeetingRoomDetailModel) getIntent().getExtras().getSerializable("data");
                    roomId = applyMeetingRoomDetailModel.getId();
                    Log.i(TAG, "上级页面返回的id: " + roomId);
                    etEndTime.setText(applyMeetingRoomDetailModel.getEndTime());
                    etStartTime.setText(applyMeetingRoomDetailModel.getStaTime());
                    etMeetingParticipateNum.setText(applyMeetingRoomDetailModel.getCrewSize());
                    etMeetingTheme.setText(applyMeetingRoomDetailModel.getTheme());
                    for (int j = 0; j < listBaseModel.results.size(); j++) {
                        if (applyMeetingRoomDetailModel.getBdrmName().equals(listBaseModel.results.get(j).getBdrmName())) {
                            spMeetingRoom.setSelection(j);
                        }
                    }
                }

            }
        });
    }

    /**
     * 提交更新的数据到接口
     * 会议室信息编辑接口
     */
    private void submitData() {
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        params.put("dataId", applyMeetingRoomDetailModel.getId());
        params.put("theme", StrUtils.getEdtTxtContent(etMeetingTheme));
        params.put("bdrmId", roomId);
        params.put("crewSize", StrUtils.getEdtTxtContent(etMeetingParticipateNum));
        params.put("staTime", StrUtils.getEdtTxtContent(etStartTime));
        params.put("endTime", StrUtils.getEdtTxtContent(etEndTime));
        HttpUtil.httpGet(ConstantUrl.ALTER_MEETING_ROOM_INFO_URL, params, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject josnObject = new JSONObject(s);
                    JSONObject jsonObject1 = josnObject.getJSONObject("results");
                    if (jsonObject1.getBoolean("result")) {
                        ToastUtil.showToast("提交成功");
                        ApplyMeetingRoomEditActivity.this.finish();
                    } else {
                        ToastUtil.showToast("提交失败，请重试");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
