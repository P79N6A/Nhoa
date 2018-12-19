package com.wisdom.nhoa.approval.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.approval.custom.OfficeStuffPopWindowWheel;
import com.wisdom.nhoa.approval.custom.PeopleTreeWindowWheel;
import com.wisdom.nhoa.approval.model.CopyToSelectedModel;
import com.wisdom.nhoa.approval.model.OfficeStuffModel;
import com.wisdom.nhoa.approval.model.OrganizationModel;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.StrUtils;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.approval.activity
 * @class describe：添加办公用品页面
 * @time 2018/7/13 16:55
 * @change
 */
public class AddOfficeStuffActivity extends BaseActivity {
    @BindView(R.id.tv_leave_stuff)
    TextView tvLeaveStuff;
    @BindView(R.id.tv_stuff_subject)
    TextView tv_stuff_subject;
    @BindView(R.id.tv_leave_num)
    EditText tvLeaveNum;
    @BindView(R.id.tvleave_reason)
    EditText tvleaveReason;
    @BindView(R.id.tv_approver)
    TextView tvApprover;
    @BindView(R.id.tv_remark)
    EditText tvRemark;
    @BindView(R.id.approval_ll_top_items)
    LinearLayout approvalLlTopItems;
    @BindView(R.id.title_bar)
    View title_bar;
    @BindView(R.id.rl_parent)
    RelativeLayout rl_parent;
    @BindView(R.id.tv_copy_to)
    TextView tv_copy_to;
    private OfficeStuffModel.ChildBean childBean;
    private OrganizationModel.ChiledBean chiledBean;

    private List<Map<Integer, String>> ischeck;
    private String reminder = "";
    private CopyToSelectedModel copyToSelectedModel;

    @Override
    public void initViews() {
        setTitle(R.string.office_stuff_title);

    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_add_office_stuff);

    }


    @OnClick({R.id.tv_copy_to, R.id.tv_leave_stuff, R.id.tv_approver, R.id.bt_apply_for})
    public void onViewClicked(View view) {
        WindowManager wm1 = this.getWindowManager();
        int height = wm1.getDefaultDisplay().getHeight();
        double statusBarHeight = Math.ceil(20 * this.getResources().getDisplayMetrics().density);
        //计算完毕后剪掉多余高度留下的结果
        int heightLeft = (int) (height - approvalLlTopItems.getMeasuredHeight()
                - title_bar.getMeasuredHeight() - statusBarHeight);
        switch (view.getId()) {
            case R.id.tv_leave_stuff: {
                //选择物品
                OfficeStuffPopWindowWheel popWindowWheel = new OfficeStuffPopWindowWheel(this);
                popWindowWheel.setHeight(heightLeft);
                popWindowWheel.showAtLocation(rl_parent, Gravity.BOTTOM, 0, 0);
                popWindowWheel.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1f);
                    }
                });
                popWindowWheel.setListener(new OfficeStuffPopWindowWheel.onChildItemClikedListener() {
                    @Override
                    public void onChildItemClicked(OfficeStuffModel.ChildBean childBean) {
                        AddOfficeStuffActivity.this.childBean = childBean;
                        tvLeaveStuff.setText(childBean.getName());
                    }
                });
                backgroundAlpha(0.5f);
            }
            break;
            case R.id.tv_copy_to: {
//                选择抄送人
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", copyToSelectedModel);
                Intent intent = new Intent(this, ChooseCopyToActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, ConstantString.COPY_TO_REQUEST_CODE);
            }
            break;
            case R.id.tv_approver: {
                //选择审批人员
                PeopleTreeWindowWheel popWindowWheel = new PeopleTreeWindowWheel(this);
                popWindowWheel.setHeight(heightLeft);
                popWindowWheel.showAtLocation(rl_parent, Gravity.BOTTOM, 0, 0);
                popWindowWheel.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1f);
                    }
                });
                popWindowWheel.setListener(new PeopleTreeWindowWheel.onChildItemClikedListener() {
                    @Override
                    public void onChildItemClicked(OrganizationModel.ChiledBean childBean) {
                        AddOfficeStuffActivity.this.chiledBean = childBean;
                        tvApprover.setText(childBean.getName());
                    }
                });
                backgroundAlpha(0.5f);
            }
            break;
            case R.id.bt_apply_for: {
                //点击申请按钮
                if (checkData()) {
                    comit();
                }
            }
            break;
        }
    }

    /**
     * 检查界面数据是否合法
     */
    private Boolean checkData() {
        Boolean isChecked = false;
        if (childBean == null) {
            ToastUtil.showToast(R.string.office_stuff_stuff_hint);
        } else if (StrUtils.isEdtTxtEmpty(tvLeaveNum)) {
            ToastUtil.showToast(R.string.office_stuff_num_hint);
        } else if (Integer.parseInt(StrUtils.getEdtTxtContent(tvLeaveNum)) >
                Integer.parseInt(childBean.getNumbers())
                ) {
            ToastUtil.showToast(R.string.office_stuff_num_wrong_hint);
            tvLeaveNum.setText("");
        } else if (StrUtils.isEdtTxtEmpty(tvleaveReason)) {
            ToastUtil.showToast(R.string.office_stuff_reason_hint);
        } else if (chiledBean == null) {
            ToastUtil.showToast(R.string.office_stuff_person_hint);
        } else {
            isChecked = true;
        }
        return isChecked;
    }

    /**
     * 点击申请按钮
     * 提交数据到接口
     */
    private void comit() {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("officeid", childBean.getId());
        params.put("numbers", StrUtils.getEdtTxtContent(tvLeaveNum));
        params.put("reason", StrUtils.getEdtTxtContent(tvRemark));
        params.put("auditor", chiledBean.getId());
        params.put("reminder", reminder);
        params.put("remark", StrUtils.getEdtTxtContent(tvRemark));
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token"
                , SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        HttpUtil.httpGet(ConstantUrl.SUBMIT_OFFICE_STUFF_URL, params, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                U.closeLoadingDialog();
                String code = "";
                String msg = "";
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    msg = jsonObject.getString("error_msg");
                    code = jsonObject.getString("error_code");
                    if ("0".equals(code)) {
                        ToastUtil.showToast("申请成功！");
                        AddOfficeStuffActivity.this.finish();
                    } else {
                        if ("notfounddata".equals(msg)) {
                            ToastUtil.showToast("暂无数据！");
                        } else {
                            ToastUtil.showToast(msg);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = f;
        getWindow().setAttributes(lp);
    }

    /**
     * 选择抄送人的回调部分
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case ConstantString.COPY_TO_RESULT_CODE: {
//                选择抄送人的回调
                if (data != null) {
                    copyToSelectedModel = (CopyToSelectedModel) data.getExtras().getSerializable("data");
                    ischeck = copyToSelectedModel.getIscheck();
                    reminder = copyToSelectedModel.getId();
                    if (!"".equals(copyToSelectedModel.getName())) {
                        tv_copy_to.setText(copyToSelectedModel.getName());
                    }
                }
            }
            break;
        }
    }

}
