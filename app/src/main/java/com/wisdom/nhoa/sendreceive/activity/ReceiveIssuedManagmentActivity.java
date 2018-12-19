package com.wisdom.nhoa.sendreceive.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.sendreceive
 * @class describe：收文管理
 * @time 2018/3/27 18:05
 * @change
 */

public class ReceiveIssuedManagmentActivity extends BaseActivity {

    @BindView(R.id.approval_tv_my_sponsor)
    TextView approvalTvMySponsor;
    @BindView(R.id.approval_tv_my_approval)
    TextView approvalTvMyApproval;
    @BindView(R.id.approval_ll_issue_todo)
    LinearLayout approvalLlIssueTodo;
    @BindView(R.id.approval_ll_issue_check)
    LinearLayout approvalLlIssueCheck;

    @Override
    public void initViews() {
        setTitle(R.string.sendreceive_receive_management_title);
    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.sendreceivel_activity_receive_issued_management);
    }


    @OnClick({R.id.approval_tv_my_sponsor, R.id.approval_tv_my_approval, R.id.approval_ll_issue_todo, R.id.approval_ll_issue_check})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.approval_tv_my_sponsor: {
                //我发起的
                startActivity(new Intent(this, ReceiveIssueSponsorListActivity.class));
            }
            break;
            case R.id.approval_tv_my_approval: {
                //我审批的
                startActivity(new Intent(this, ReceiveIssueApprovalListActivity.class));
            }
            break;
            case R.id.approval_ll_issue_todo: {
                //收文待办
                startActivity(new Intent(this, ReceiveIssueTodoListActivity.class));
            }
            break;
            case R.id.approval_ll_issue_check: {
                //收文查阅
                startActivity(new Intent(this, ReceiveIssueCheckListActivity.class));
            }
            break;
        }
    }
}
