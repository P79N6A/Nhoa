package com.wisdom.nhoa.approval.activity;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.approval.model.ApprovalListModel;
import com.wisdom.nhoa.approval.model.MessageEvent;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.circulated.activity.FilePreviewActivity;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.widget.LeaveApproveDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class LeaveApproveActivity extends BaseActivity {
    @BindView(R.id.ll_parent)
    LinearLayout llParent;
    private ApprovalListModel model;
    private String insid = "";
    @BindView(R.id.bt_approve)
    Button bt_approve;

    @Override
    public void initViews() {
        if (getIntent() != null) {
            model = (ApprovalListModel) getIntent().getExtras().getSerializable("data");
            initData(model);
        }
        setTitle("待审详细");
        if (backIv != null)
            backIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    setResult(0);
                }
            });
        if (!model.getStatus().equals("0")) {
            bt_approve.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(0);

    }

    /**
     * 拼装数据设置到界面
     *
     * @param model
     */
    private void initData(ApprovalListModel model) {
        switch (model.getType()) {
            case ConstantString.APPROVAL_TYPE_XJ: {
                //休假
                String key[] = {"申请人", "开始时间", "结束时间", "休假类型", "休假天数"
                        , "休假理由", "备注", "审批人", "审批时间", "抄送人", "备注"};
                String value[] = {model.getUsername(), model.getStarttime(), model.getEndtime(), model.getTypename()
                        , model.getDays(), model.getReason(), model.getRemark(), model.getAuditorname(), model.getAuditortime()
                        , model.getRemindername(), model.getExplain()};
                setShowData(key, value);
            }
            break;
            case ConstantString.APPROVAL_TYPE_CC: {
                //出差
                String key[] = {"申请人", "开始时间", "结束时间", "交通工具", "出差地", "出差天数"
                        , "出差理由", "备注", "审批人", "审批时间", "抄送人", "备注"};
                String value[] = {model.getUsername(), model.getStarttime(), model.getEndtime(), model.getVehicle(), model.getAddr()
                        , model.getDays(), model.getReason(), model.getRemark(), model.getAuditorname(), model.getAuditortime()
                        , model.getRemindername(), model.getExplain()};
                setShowData(key, value);
            }
            break;
            case ConstantString.APPROVAL_TYPE_YP: {
                //办公用品
                String key[] = {"申请人", "物品名", "规格", "申请数", "申请理由"
                        , "备注", "审批人", "审批时间", "抄送人", "备注"};
                String value[] = {model.getUsername(), model.getTypename(), model.getSpecifications()
                        , model.getNumbers(), model.getReason(), model.getRemark(), model.getAuditorname(), model.getAuditortime()
                        , model.getRemindername(), model.getExplain()};
                setShowData(key, value);
            }
            break;

        }
    }

    /**
     * 循环布局，将数据源动态展现在界面
     *
     * @param key
     * @param value
     */
    private void setShowData(String[] key, String[] value) {
        if (key.length == value.length) {
            for (int i = 0; i < key.length; i++) {
                View itemView = LayoutInflater.from(this).inflate(R.layout.item_my_approval_detail, null, false);
                TextView tv_key = itemView.findViewById(R.id.tv_key);
                TextView tv_value = itemView.findViewById(R.id.tv_value);
                tv_key.setText(key[i]);
                if (!"".equals(value[i])) {
                    tv_value.setText(value[i]);
                } else {
                    tv_value.setText("无");
                }
                llParent.addView(itemView);
            }
            //单独处理文件预览的布局
            View fileView = LayoutInflater.from(this).inflate(R.layout.item_my_approval_detail, null, false);
            TextView tv_key_file = fileView.findViewById(R.id.tv_key);
            TextView tv_value_file = fileView.findViewById(R.id.tv_value);
            tv_key_file.setText("附件");
            if (!"".equals(model.getFileurl()) || model.getFileurl() == null) {
                tv_value_file.setText(model.getFilename());
                tv_value_file.setTextColor(getResources().getColor(R.color.light_blue_new));
                tv_value_file.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FilePreviewActivity.show(context
                                , ConstantUrl.BASE_URL + model.getFileurl().replaceAll(" ", "")
                                , "");
                    }
                });
            } else {
                tv_value_file.setText("无");
            }
            llParent.addView(fileView);
        } else {
            ToastUtil.showToast("数据加载失败请重试");

            this.finish();
        }
    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_leave_approve);
    }


    @OnClick({R.id.bt_approve})
    public void OnClickView(View view) {
        switch (view.getId()) {
            case R.id.bt_approve:
                submitEvent();//提交
                break;
            default:
                break;
        }
    }


    private void submitEvent() {
        LeaveApproveDialog leaveApproveDialog = new LeaveApproveDialog();
        Bundle bundle = new Bundle();
        bundle.putString("id", model.getId());
        bundle.putString("type", model.getType());
        leaveApproveDialog.setArguments(bundle);
        showDialogFragment(leaveApproveDialog);

    }

    private void showDialogFragment(DialogFragment dialogFragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        dialogFragment.show(ft, "df");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("1")) {
            bt_approve.setVisibility(View.GONE);
            this.finish();
        }
    }

}
