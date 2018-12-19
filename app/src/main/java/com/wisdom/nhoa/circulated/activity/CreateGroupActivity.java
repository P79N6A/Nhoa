package com.wisdom.nhoa.circulated.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.wisdom.nhoa.circulated.adapter.SearchAllStaffAdapter;
import com.wisdom.nhoa.circulated.model.GroupAllStaffModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.StrUtils;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.circulated.activity
 * @class describe：创建群页面
 * @time 2018/3/26 9:16
 * @change
 */

public class CreateGroupActivity extends BaseActivity {
    @BindView(R.id.circulated_create_group_listview)
    ListView circulatedCreateGroupListview;
    @BindView(R.id.circulated_tv_confirm)
    TextView circulatedTvConfirm;
    @BindView(R.id.circulated_et_group_name)
    EditText circulatedEtGroupName;
    @BindView(R.id.circulated_et_group_notice)
    EditText circulatedEtGroupNotice;

    public static final String TAG = CreateGroupActivity.class.getSimpleName();
    private List<GroupAllStaffModel> listData = new ArrayList<>();

    @Override
    public void initViews() {
        setTitle(R.string.circulated_create_group);
        circulatedTvConfirm.setText(R.string.approval_refuse_issuse_confirm);
        getAllSttaf();
    }


    @Override
    public void setlayoutIds() {
        setContentView(R.layout.circulated_activity_create_group);
    }


    /**
     * 确定创建群的按钮点击事件
     */
    @OnClick(R.id.circulated_tv_confirm)
    public void onViewClicked() {
        if (checkData()) {
            U.showLoadingDialog(this);
            //界面控件值检查完毕,拼装参数，提交到接口
            String ids = "";
            for (int k = 0; k < SearchAllStaffAdapter.isSelected.size(); k++) {
                if (SearchAllStaffAdapter.isSelected.get(k)) {
                    ids += listData.get(k).getUser_id() + ",";
                }
            }
            //拼接后的字符串，把最后一个因为逗号截取掉
            ids = ids.substring(0, ids.length() - 1);
            Log.i(TAG, "ids: " + ids);
            //提交数据到接口
            HttpParams params = new HttpParams();
            params.put("appkey", ConstantString.APP_KEY);
            params.put("access_token", SharedPreferenceUtil.getUserInfo(CreateGroupActivity.this).getAccess_token());
            params.put("group_name", StrUtils.getEdtTxtContent(circulatedEtGroupName));
            params.put("group_notice", StrUtils.getEdtTxtContent(circulatedEtGroupNotice));
            params.put("userid", ids);
            HttpUtil.httpGet(ConstantUrl.CIRCLATED_CREATE_GROUP, params, new StringCallback() {
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
                        U.closeLoadingDialog();
                        new AlertDialog.Builder(CreateGroupActivity.this)
                                .setTitle("提示")
                                .setMessage("创建成功")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        setResult(0x13);
                                        CreateGroupActivity.this.finish();
                                    }
                                })
                                .create().show();
                    } else {
                        U.closeLoadingDialog();
                        ToastUtil.showToast("创建失败，请重试");
                    }
                }
            });
        }
    }


    /**
     * 请求接口，获得全部可选人员信息列表
     */
    private void getAllSttaf() {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        HttpUtil.httpGet(ConstantUrl.CIRCLATED_SEARCH_ALL_STAFF, params, new JsonCallback<BaseModel<List<GroupAllStaffModel>>>() {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
            }

            @Override
            public void onSuccess(BaseModel<List<GroupAllStaffModel>> listBaseModel, Call call, Response response) {
                listData = listBaseModel.results;
                circulatedCreateGroupListview.setAdapter(new SearchAllStaffAdapter(CreateGroupActivity.this
                        , listData));
                U.closeLoadingDialog();
                circulatedCreateGroupListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        SearchAllStaffAdapter.AllStaffHolder vHollder = (SearchAllStaffAdapter.AllStaffHolder) view.getTag();
                        //在每次获取点击的item时将对于的checkbox状态改变，同时修改map的值。
                        vHollder.checkBox.toggle();
                        SearchAllStaffAdapter.isSelected.put(position, vHollder.checkBox.isChecked());
                        int totalCount = 0;
                        //统计一共选中了多少个成员，动态改变页面最下方按钮上的数字
                        for (int k = 0; k < SearchAllStaffAdapter.isSelected.size(); k++) {
                            if (SearchAllStaffAdapter.isSelected.get(k)) {
                                totalCount++;
                            }
                        }
                        circulatedTvConfirm.setText("确定(" + totalCount + ")");
                    }
                });
            }
        });
    }

    /**
     * 检测页面数据是否合法
     *
     * @return
     */
    private boolean checkData() {
        Boolean isChecked = false;
        if (StrUtils.isEdtTxtEmpty(circulatedEtGroupName)) {
            isChecked = false;
            ToastUtil.showToast(R.string.circulated_group_name_hint);
        } else if (StrUtils.isEdtTxtEmpty(circulatedEtGroupNotice)) {
            isChecked = false;
            ToastUtil.showToast(R.string.circulated_group_notice_hint);
        } else {
            if (SearchAllStaffAdapter.isSelected.size() > 0) {
                for (int j = 0; j < SearchAllStaffAdapter.isSelected.size(); j++) {
                    if (SearchAllStaffAdapter.isSelected.get(j)) {
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
