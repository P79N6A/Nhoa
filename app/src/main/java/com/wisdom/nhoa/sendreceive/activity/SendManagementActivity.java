package com.wisdom.nhoa.sendreceive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.approval.activity
 * @class describe：
 * @time 2018/3/8 9:45
 * @change
 */

public class SendManagementActivity extends BaseActivity {

    public static final String TAG = SendManagementActivity.class.getSimpleName();
    @BindView(R.id.approval_ll_issue_todo)
    LinearLayout approvalLlIssueTodo;
    @BindView(R.id.approval_ll_issue_check)
    LinearLayout approvalLlIssueCheck;

    @Override
    public void initViews() {
        setTitle(R.string.approval_issue_management_title);
    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.approval_activity_issued_management);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
//        int w = View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED);
//        int h = View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED);
//        approvalLlIssueTodo.measure(w, h);
//        int width = approvalLlIssueTodo.getMeasuredWidth();
//        Log.i(TAG, "setlayoutIds: "+width);
//
//        approvalLlIssueTodo.setLayoutParams(new LinearLayout.LayoutParams(width, width));
//        approvalLlIssueCheck.setLayoutParams(new LinearLayout.LayoutParams(width, width));
    }

    @OnClick({R.id.head_back_iv, R.id.approval_tv_my_sponsor, R.id.approval_tv_my_approval, R.id.approval_ll_issue_todo, R.id.approval_ll_issue_check})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_iv: {//返回
                this.finish();
            }
            break;
            case R.id.approval_tv_my_sponsor: {//我发起的
                startActivity(new Intent(this, SendIssueSponsorListActivity.class));
            }
            break;
            case R.id.approval_tv_my_approval: {//我审批的
                startActivity(new Intent(this, SendIssueApprovalListActivity.class));
            }
            break;
            case R.id.approval_ll_issue_todo: {//发文代办
                startActivity(new Intent(this, SendIssueTodoListActivity.class));
            }
            break;
            case R.id.approval_ll_issue_check: {//发文审阅
                startActivity(new Intent(this, SendIssueCheckListActivity.class));
            }
            break;
        }
    }


}
