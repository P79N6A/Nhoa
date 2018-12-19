package com.wisdom.nhoa.approval.activity;

import android.content.Intent;
import android.view.View;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.util.UserPermissionHelper;

import butterknife.OnClick;

import static com.wisdom.nhoa.ConstantString.PERMISSION_BUSINESS_APPLY;
import static com.wisdom.nhoa.ConstantString.PERMISSION_LEAVE_APPLY;
import static com.wisdom.nhoa.ConstantString.PERMISSION_OFFICE_SUPPLIES_APPLY;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.approval.activity
 * @class describe： 审批-首页
 * @time 2018/3/7 16:45
 * @change
 */

public class ApprovalActivity extends BaseActivity {
    @Override
    public void initViews() {
        setTitle(R.string.approval_title);
    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.approval_activity_approval);
    }


    @OnClick({R.id.head_back_iv, R.id.approval_tv_my_sponsor, R.id.approval_tv_my_approval, R.id.approval_tv_my_copy_to, R.id.approval_ll_on_business_trip, R.id.approval_ll_ask_for_leave, R.id.approval_office_stuff, R.id.approval_become_a_full_member})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_iv: {
                this.finish();
            }
            break;
            case R.id.approval_tv_my_sponsor: {//我发起的
                startActivity(new Intent(this, MySponsorListActivity.class));
            }
            break;
            case R.id.approval_tv_my_approval: {//我审批的
                startActivity(new Intent(this, MyApprovalListActivity.class));
            }
            break;
            case R.id.approval_tv_my_copy_to: {//我抄送的
            }
            startActivity(new Intent(this, MyCopyToListActivity.class));
            break;
            case R.id.approval_ll_on_business_trip: {//出差
                if (UserPermissionHelper.getUserPermissionStatus(this, PERMISSION_BUSINESS_APPLY)) {
                    startActivity(new Intent(this, BusinessTripActivity.class));
                }
            }
            break;
            case R.id.approval_ll_ask_for_leave: {//请假
                if (UserPermissionHelper.getUserPermissionStatus(this, PERMISSION_LEAVE_APPLY)) {
                    startActivity(new Intent(this, ApplyModifyActivity.class));
                }
            }
            break;
            case R.id.approval_office_stuff: {//办公用品
                if (UserPermissionHelper.getUserPermissionStatus(this, PERMISSION_OFFICE_SUPPLIES_APPLY)) {
                    startActivity(new Intent(this, AddOfficeStuffActivity.class));
                }
            }
            break;
            case R.id.approval_become_a_full_member: {//转正
            }
            break;
        }
    }
}
