package com.wisdom.nhoa.circulated.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.base.BroadCastManager;
import com.wisdom.nhoa.circulated.adapter.GroupMemberAdapter;
import com.wisdom.nhoa.circulated.adapter.ManagementGroupMemberAdapter;
import com.wisdom.nhoa.circulated.model.CirculatedListModel;
import com.wisdom.nhoa.circulated.model.GroupDetailModel;
import com.wisdom.nhoa.circulated.model.GroupManagementModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.StrUtils;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.circulated.activity
 * @class describe： 群管理页面
 * @time 2018/3/27 11:56
 * @change
 */

public class GroupManagementActivity extends BaseActivity {
    @BindView(R.id.circulated_et_group_name)
    EditText circulatedEtGroupName;
    @BindView(R.id.circulated_et_group_notice)
    EditText circulatedEtGroupNotice;
    @BindView(R.id.circulated_create_group_listview)
    ListView circulatedCreateGroupListview;
    @BindView(R.id.circulated_tv_confirm)
    TextView circulatedTvConfirm;

    private CirculatedListModel circulatedListModel;
    private HttpParams params;
    private GroupManagementModel listData = new GroupManagementModel();
    public static final String TAG = GroupManagementActivity.class.getSimpleName();
    private int totalCount;

    @Override
    public void initViews() {
        if (getIntent() != null) {
            circulatedListModel = (CirculatedListModel) getIntent().getExtras().getSerializable("data");
        }
        setTitle(R.string.circulated_group_management_title);
        params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        params.put("groupid", circulatedListModel.getGroup_id());
        //尚未加载完页面数据时候，此按钮不可以点击
        circulatedTvConfirm.setClickable(false);
        getGroupDetail();
    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.circulated_activity_group_management);
    }


    /**
     * 获得群详情信息
     */
    private void getGroupDetail() {
        U.showLoadingDialog(this);
        HttpUtil.httpGet(ConstantUrl.GET_GROUP_DETAIL_URL, params, new JsonCallback<BaseModel<GroupDetailModel>>() {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
            }

            @Override
            public void onSuccess(BaseModel<GroupDetailModel> groupDetailModel, Call call, Response response) {
                U.closeLoadingDialog();
                circulatedEtGroupNotice.setText(groupDetailModel.results.getGroup_notice());
                circulatedEtGroupName.setText(groupDetailModel.results.getGroup_name());
                if (ConstantString.IS_CREATER_FALSE.equals(circulatedListModel.getIscreater())) {
                    //不是管理员的逻辑。页面只能展示不能修改
                    circulatedTvConfirm.setClickable(true);
                    circulatedTvConfirm.setText("退出群");
                    circulatedEtGroupName.setFocusable(false);
                    circulatedEtGroupNotice.setFocusable(false);
                    circulatedCreateGroupListview.setAdapter(new GroupMemberAdapter(
                            GroupManagementActivity.this
                            , groupDetailModel.results.getUserlist()
                    ));
                } else {
                    //是管理员的逻辑。页面能展示也能修改
                    circulatedEtGroupName.setFocusable(true);
                    circulatedEtGroupNotice.setFocusable(true);
                    getManagementData();
                }
            }
        });
    }

    /**
     * 请求管理者数据
     */
    private void getManagementData() {
        HttpUtil.httpGet(ConstantUrl.GET_GROUP_MANAGMENT_URL, params, new JsonCallback<BaseModel<GroupManagementModel>>() {
            @Override
            public void onSuccess(BaseModel<GroupManagementModel> listBaseModel, Call call, Response response) {
                listData = listBaseModel.results;
                circulatedCreateGroupListview.setAdapter(new ManagementGroupMemberAdapter(GroupManagementActivity.this
                        , listData));
                stataicMemberCount();
                circulatedTvConfirm.setClickable(true);
                circulatedCreateGroupListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        ManagementGroupMemberAdapter.AllStaffHolder vHollder = (ManagementGroupMemberAdapter.AllStaffHolder) view.getTag();
                        //在每次获取点击的item时将对于的checkbox状态改变，同时修改map的值。
                        vHollder.checkBox.toggle();
                        ManagementGroupMemberAdapter.isSelected.put(position, vHollder.checkBox.isChecked());
                        stataicMemberCount();
                    }
                });


            }
        });
    }

    /**
     * 统计一共选中了多少个成员，动态改变页面最下方按钮上的数字
     */
    private void stataicMemberCount() {
        totalCount = 0;
        for (int k = 0; k < ManagementGroupMemberAdapter.isSelected.size(); k++) {
            if (ManagementGroupMemberAdapter.isSelected.get(k)) {
                totalCount++;
            }
        }
        circulatedTvConfirm.setText("确定(" + totalCount + ")");
    }

    /**
     * 群管理员/群成员 对群进行管理修改的点击按钮
     */
    @OnClick(R.id.circulated_tv_confirm)
    public void onViewClicked() {
        if (ConstantString.IS_CREATER_FALSE.equals(circulatedListModel.getIscreater())) {
            //不是创建者，执行退出群的逻辑
            final HttpParams httpParams = new HttpParams();
            httpParams.put("appkey", ConstantString.APP_KEY);
            httpParams.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
            httpParams.put("group_id", circulatedListModel.getGroup_id());
            new AlertDialog.Builder(GroupManagementActivity.this)
                    .setTitle("提示")
                    .setMessage("您确定要退出该群吗？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            quitGroup(httpParams);
                        }
                    })
                    .create().show();
        } else {
            //是创建者，执行修改群的信息功能
            alterGroupInfo();
        }
    }

    /**
     * 修改群信息的方法
     */
    private void alterGroupInfo() {
        if (checkData()) {
            //界面控件值检查完毕,拼装参数，提交到接口
            String ids = "";
            for (int k = 0; k < ManagementGroupMemberAdapter.isSelected.size(); k++) {
                if (ManagementGroupMemberAdapter.isSelected.get(k)) {
                    ids += listData.getUserlist().get(k).getUser_id() + ",";
                }
            }
            //拼接后的字符串，把最后一个因为逗号截取掉
            if (!"".equals(ids)) {
                ids = ids.substring(0, ids.length() - 1);
            }
            Log.i(TAG, "ids: " + ids);
            //提交数据到接口
            HttpParams params = new HttpParams();
            params.put("appkey", ConstantString.APP_KEY);
            params.put("access_token", SharedPreferenceUtil.getUserInfo(GroupManagementActivity.this).getAccess_token());
            params.put("groupid", circulatedListModel.getGroup_id());
            params.put("userid", ids);
            params.put("group_name", StrUtils.getEdtTxtContent(circulatedEtGroupName));
            params.put("group_notice", StrUtils.getEdtTxtContent(circulatedEtGroupNotice));
            HttpUtil.httpGet(ConstantUrl.ALTER_GROUP_SUBMIT, params, new StringCallback() {
                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    ToastUtil.showToast("修改失败，请重试");
                }

                @Override
                public void onSuccess(String s, Call call, Response response) {
                    String code = "";
                    try {
                        code = new JSONObject(s).getString("error_code");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //接口返回的是成功码
                    if ("0".equals(code)) {
                        new AlertDialog.Builder(GroupManagementActivity.this)
                                .setTitle("提示")
                                .setMessage("群信息修改成功")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent();
                                        intent.putExtra("data", totalCount + "");
                                        setResult(ConstantString.MANAGMENT_REQUEST_CODE, intent);
                                        GroupManagementActivity.this.finish();
                                    }
                                })
                                .create().show();
                    } else {
                        ToastUtil.showToast("修改失败，请重试");
                    }
                }
            });
        }
    }


    /**
     * 退出群的方法
     *
     * @param params
     */
    private void quitGroup(HttpParams params) {
        HttpUtil.httpGet(ConstantUrl.QUIT_GROUP_URL, params, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                String code;
                try {
                    code = new JSONObject(s).getString("error_code");
                    if ("0".equals(code)) {
                        ToastUtil.showToast("退出成功！");
//                        ActivityManager.getActivityManagerInstance().clearAllActivity();
                        finish();
                        Intent intent = new Intent();
                        intent.setAction(ConstantString.BROADCAST_GROUP_FINISH);
                        BroadCastManager.getInstance().sendBroadCast(GroupManagementActivity.this, intent);
                    } else {
                        ToastUtil.showToast("退出失败！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 检测页面数据是否合法
     *
     * @return
     */
    private boolean checkData() {
        Boolean isChecked = true;
        if (StrUtils.isEdtTxtEmpty(circulatedEtGroupName)) {
            isChecked = false;
            ToastUtil.showToast(R.string.circulated_group_name_hint);
        } else if (StrUtils.isEdtTxtEmpty(circulatedEtGroupNotice)) {
            isChecked = false;
            ToastUtil.showToast(R.string.circulated_group_notice_hint);
        } else {
            if (ManagementGroupMemberAdapter.isSelected.size() > 0) {
                isChecked = false;
                for (int j = 0; j < ManagementGroupMemberAdapter.isSelected.size(); j++) {
                    if (ManagementGroupMemberAdapter.isSelected.get(j)) {
                        isChecked = true;
                    }
                }
                if (!isChecked) {
                    ToastUtil.showToast(R.string.circulated_group_staff_hint2);
                }
            } else {
                isChecked = false;
                ToastUtil.showToast(R.string.circulated_group_staff_hint);
            }
        }
        return isChecked;
    }

}
