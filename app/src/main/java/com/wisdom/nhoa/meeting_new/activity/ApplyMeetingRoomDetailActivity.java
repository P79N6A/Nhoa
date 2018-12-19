package com.wisdom.nhoa.meeting_new.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.meeting_new.model.ApplyMeetingRoomDetailModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.StrUtils;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.UserPermissionHelper;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.wisdom.nhoa.ConstantString.PERMISSION_MEETING_PUBLISH;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.meeting_new.activity
 * @class describe：申请会议室列表详情界面
 * @time 2018/10/23 14:27
 * @change
 */
public class ApplyMeetingRoomDetailActivity extends BaseActivity {
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
    @BindView(R.id.bt_meeting_apply_delete)
    Button btMeetingApplyDelete;
    @BindView(R.id.bt_meeting_apply_edit)
    Button btMeetingApplyEdit;
    @BindView(R.id.tv_hint_return)
    TextView tvHintReturn;
    @BindView(R.id.tv_hint_pass)
    TextView tvHintPass;
    private String dataId = "";

    private ApplyMeetingRoomDetailModel applyMeetingRoomDetailModel;

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_apply_meeting_room_details);
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
     * 删除按钮点击事件
     */
    @OnClick(R.id.bt_meeting_apply_delete)
    public void onBtMeetingApplyDeleteClicked() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("您确定删除关于" + applyMeetingRoomDetailModel.getTheme() + "的全部信息？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMeetingRoomByDataId(applyMeetingRoomDetailModel.getId());
                    }
                }).create().show();
    }

    /**
     * 编辑按钮点击事件
     */
    @OnClick(R.id.bt_meeting_apply_edit)
    public void onBtMeetingApplyEditClicked() {
        setResult(ConstantString.REQUEST_CODE_REFRESH_DATA);
        if (applyMeetingRoomDetailModel != null) {
            Intent intent = new Intent(this, ApplyMeetingRoomEditActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", applyMeetingRoomDetailModel);
            intent.putExtras(bundle);
            startActivityForResult(intent, ConstantString.REQUEST_CODE_REFRESH_DATA);
        } else {
            ToastUtil.showToast("暂无会议室信息");
        }
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
                    setOptionsByDataStatus(model.results);
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
        new AlertDialog.Builder(ApplyMeetingRoomDetailActivity.this)
                .setTitle("会议室地址：")
                .setMessage(bdrmAdd)
                .setPositiveButton("确定", null)
                .create().show();
    }

    /**
     * 删除会议室
     *
     * @param dataId
     */
    private void deleteMeetingRoomByDataId(String dataId) {
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        params.put("dataId", dataId);
        HttpUtil.httpGet(ConstantUrl.DELETE_MEETING_ROOM_INFO_BT_DATA_ID, params, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject josnObject = new JSONObject(s);
                    JSONObject jsonObject1 = josnObject.getJSONObject("results");
                    if (jsonObject1.getBoolean("result")) {
                        ToastUtil.showToast("删除成功");
                        ApplyMeetingRoomDetailActivity.this.finish();
                    } else {
                        ToastUtil.showToast("删除失败，请重试");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        setResult(ConstantString.REQUEST_CODE_REFRESH_DATA);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getData();
    }

    /**
     * 根据数据状态，设置相关操作权限
     *
     * @param meetingRoomDetailModel
     */
    private void setOptionsByDataStatus(final ApplyMeetingRoomDetailModel meetingRoomDetailModel) {
        if (meetingRoomDetailModel.getStatusCode() != null) {
            //非待审状态
            switch (meetingRoomDetailModel.getStatusCode()) {
                case "": {
                    //待审状态 不能删除 能编辑
                    btMeetingApplyEdit.setVisibility(View.VISIBLE);
                    btMeetingApplyDelete.setVisibility(View.GONE);
                    tvHintPass.setVisibility(View.GONE);
                    tvHintReturn.setVisibility(View.GONE);
                }
                break;
                case ConstantString.APPLY_MEETING_ROOM_STATUS_PUBLISH: {//已发布
                    //已发布 能删除 不能编辑
                    btMeetingApplyEdit.setVisibility(View.GONE);
                    btMeetingApplyDelete.setVisibility(View.VISIBLE);
                    btMeetingApplyDelete.setBackgroundColor(Color.parseColor("#d14738"));
                    tvHintPass.setVisibility(View.GONE);
                    tvHintReturn.setVisibility(View.GONE);
                }
                break;
                case ConstantString.APPLY_MEETING_ROOM_STATUS_PASS: {//审批通过
                    //审批通过 能删除 不能编辑 提示可以发布设置跳转链接
                    btMeetingApplyEdit.setVisibility(View.GONE);
                    btMeetingApplyDelete.setVisibility(View.VISIBLE);
                    btMeetingApplyDelete.setBackgroundColor(Color.parseColor("#d14738"));
                    tvHintPass.setVisibility(View.VISIBLE);
                    tvHintReturn.setVisibility(View.GONE);
                    //关键字变色
                    StrUtils.changeKeywordsColor(tvHintPass
                            , getResources().getString(R.string.meeting_room_detail_hint_pass)
                            , "这里");
                    //关键字的点击事件
                    tvHintPass.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (UserPermissionHelper.getUserPermissionStatus(ApplyMeetingRoomDetailActivity.this,PERMISSION_MEETING_PUBLISH)) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("data", meetingRoomDetailModel);
                                Intent intent = new Intent(ApplyMeetingRoomDetailActivity.this
                                        , MeetingPublishDoPublishActivity.class);
                                intent.putExtra("tag", ConstantString.MEETING_PUBLISH_TAG_MEETING_ROOM_LIST);
                                intent.putExtras(bundle);
                                startActivityForResult(intent,ConstantString.REQUEST_CODE_REFRESH_DATA);
                            }
                        }
                    });

                }
                break;
                case ConstantString.APPLY_MEETING_ROOM_STATUS_RETURN: {//审批退回
                    //审批退回 能编辑能删除 提示可重新编辑重新审核
                    btMeetingApplyEdit.setVisibility(View.VISIBLE);
                    btMeetingApplyDelete.setVisibility(View.VISIBLE);
                    tvHintPass.setVisibility(View.GONE);
                    tvHintReturn.setVisibility(View.VISIBLE);
                }
                break;

            }
        } else {
            //待审状态
            btMeetingApplyEdit.setVisibility(View.VISIBLE);
            btMeetingApplyDelete.setVisibility(View.GONE);
            tvHintPass.setVisibility(View.GONE);
            tvHintReturn.setVisibility(View.GONE);
        }
    }

}
