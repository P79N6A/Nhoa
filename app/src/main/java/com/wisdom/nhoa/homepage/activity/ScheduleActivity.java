package com.wisdom.nhoa.homepage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.homepage.fragment.LeaderShipScheduleFragment;
import com.wisdom.nhoa.homepage.fragment.MyScheduleFragment;
import com.wisdom.nhoa.util.UserPermissionHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.wisdom.nhoa.ConstantString.PERMISSION_LEADER_SCHEDULE;
import static com.wisdom.nhoa.ConstantString.PERMISSION_MINE_SCHEDULE;

public class ScheduleActivity extends AppCompatActivity implements View.OnClickListener, MyScheduleFragment.FragmentInteraction {
    public static final int ADDCALENDARREQUESTCODE = 12345;
    @BindView(R.id.head_back_iv)
    protected ImageView backIv;
    @BindView(R.id.head_right_iv)
    protected ImageView rightIv;
    @BindView(R.id.rb_my_shedule)
    protected RadioButton rb_my_shedule;
    @BindView(R.id.rb_leader_shedule)
    protected RadioButton rb_leader_shedule;
    @BindView(R.id.rg_titlegroup)
    protected RadioGroup radioGroup;
    @BindView(R.id.vp_schedule)
    protected ViewPager viewPager;
    private String stringtime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ButterKnife.bind(this);
        Init();
    }

    private void Init() {
        rightIv.setOnClickListener(this);
        backIv.setOnClickListener(this);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new MyScheduleFragment());
        fragmentList.add(new LeaderShipScheduleFragment());
        viewPager.setAdapter(new ScheduleFragmentAdapter(getSupportFragmentManager(), fragmentList));
        viewPager.setCurrentItem(0);
        rb_my_shedule.setBackgroundColor(getResources().getColor(R.color.radiobt_press));
        rb_leader_shedule.setBackgroundColor(getResources().getColor(R.color.radiobt_normal));
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_my_shedule:
                        rb_my_shedule.setBackgroundColor(getResources().getColor(R.color.radiobt_press));
                        rb_leader_shedule.setBackgroundColor(getResources().getColor(R.color.radiobt_normal));
                        viewPager.setCurrentItem(0);
                        rightIv.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_leader_shedule:
                        viewPager.setCurrentItem(1);
                        rb_my_shedule.setBackgroundColor(getResources().getColor(R.color.radiobt_normal));
                        rb_leader_shedule.setBackgroundColor(getResources().getColor(R.color.radiobt_press));
                        rightIv.setVisibility(View.GONE);
                        break;

                }
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    radioGroup.check(position);
                    rb_my_shedule.setBackgroundColor(getResources().getColor(R.color.radiobt_press));
                    rb_leader_shedule.setBackgroundColor(getResources().getColor(R.color.radiobt_normal));
                    rightIv.setVisibility(View.VISIBLE);
                } else if (position == 1) {
                    if (UserPermissionHelper.getUserPermissionStatus(ScheduleActivity.this, PERMISSION_LEADER_SCHEDULE)) {
                        radioGroup.check(position);
                        rb_my_shedule.setBackgroundColor(getResources().getColor(R.color.radiobt_normal));
                        rb_leader_shedule.setBackgroundColor(getResources().getColor(R.color.radiobt_press));
                        rightIv.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_right_iv:
                if (UserPermissionHelper.getUserPermissionStatus(this, PERMISSION_MINE_SCHEDULE)) {
                    Intent intent = new Intent(new Intent(this, CalendarEventActivity.class));
                    intent.putExtra("currentTime", stringtime);
                    startActivityForResult(intent, ADDCALENDARREQUESTCODE);
                }
                break;
            case R.id.head_back_iv:
                this.finish();
            default:
                break;


        }
    }

    @Override
    public void returnTime(String time) {
        stringtime = time;
    }

    class ScheduleFragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;

        public ScheduleFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            fragmentList = fragments;
            fm.beginTransaction().commitAllowingStateLoss();
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return this.fragmentList.size();
        }
    }
}
