package com.wisdom.nhoa.meeting_new.activity;

import android.content.Intent;
import android.view.View;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;

import butterknife.OnClick;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.metting.activity
 * @class describe： 新版本的会议主页
 * @time 2018/10/22 9:23
 * @change
 */
public class MeetingMainActivity extends BaseActivity {
    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_meeting_main);
    }

    @Override
    public void initViews() {
        setTitle(R.string.index_meeting_title);
    }


    @OnClick({R.id.tv_meeting_my_sponsor, R.id.tv_meeting_my_check, R.id.tv_meeting_my_participation, R.id.ll_apply_meeting_room, R.id.ll_publish_meeting, R.id.ll_meeting_sign_meeting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_meeting_my_sponsor: {
                //我发起的
                startActivity(new Intent(this, MeetingMySponsorListActivity.class));
            }
            break;
            case R.id.tv_meeting_my_check: {
                //我审核的
                 startActivity(new Intent(this, MyCheckedApplyMeetingRoomActivity.class));
            }
            break;
            case R.id.tv_meeting_my_participation: {
                //我参与的
                startActivity(new Intent(this, MeetingMyParticipationListActivity.class));
            }
            break;
            case R.id.ll_apply_meeting_room: {
                //申请会议室
                startActivity(new Intent(this, ApplyMeetingRoomActivity.class));
            }
            break;
            case R.id.ll_publish_meeting: {
                //发布会议
                startActivity(new Intent(this, ReleaseMeetingMainActivity.class));
            }
            break;
            case R.id.ll_meeting_sign_meeting: {
                //会议签到
                startActivity(new Intent(this, RegistrationActivity.class));

            }
            break;
        }
    }
}
