package com.wisdom.nhoa.supervision.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BroadCastManager;
import com.wisdom.nhoa.supervision.adapter.SupervisionFragmentPagerAdapter;
import com.wisdom.nhoa.supervision.fragment.SupervisionFragment;
import com.wisdom.nhoa.supervision.fragment.SupervisionSearchFragment;
import com.wisdom.nhoa.util.UserPermissionHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.wisdom.nhoa.ConstantString.PERMISSION_SPONSOR_SUPERVISE;

public class SupervisionActivity extends AppCompatActivity implements View.OnClickListener, SupervisionFragment.OnListFragmentInteractionListener {

    @BindView(R.id.head_back_iv)
    protected ImageView backIv;
    @BindView(R.id.head_right_iv)
    protected ImageView rightIv;
    @BindView(R.id.rb_supervision)
    protected RadioButton rb_supervision;
    @BindView(R.id.rb_supervision_search)
    protected RadioButton rb_supervision_search;
    @BindView(R.id.rg_titlegroup)
    protected RadioGroup radioGroup;
    @BindView(R.id.vp_supervision)
    protected ViewPager viewPager;
    private String stringtime = "";
    int pos = 0;//判断当前选中的是左侧还是右侧

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervision);
        ButterKnife.bind(this);
        Init();
    }

    private void Init() {
        rightIv.setOnClickListener(this);
        backIv.setOnClickListener(this);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(SupervisionFragment.newInstance());
        fragmentList.add(SupervisionSearchFragment.newInstance());
        viewPager.setAdapter(new SupervisionFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        viewPager.setCurrentItem(0);
        rb_supervision.setBackgroundColor(getResources().getColor(R.color.radiobt_press));
        rb_supervision_search.setBackgroundColor(getResources().getColor(R.color.radiobt_normal));
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_supervision:
                        rb_supervision.setBackgroundColor(getResources().getColor(R.color.radiobt_press));
                        rb_supervision_search.setBackgroundColor(getResources().getColor(R.color.radiobt_normal));
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.rb_supervision_search:
                        viewPager.setCurrentItem(1);
                        rb_supervision.setBackgroundColor(getResources().getColor(R.color.radiobt_normal));
                        rb_supervision_search.setBackgroundColor(getResources().getColor(R.color.radiobt_press));
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
                    pos = 0;
                    radioGroup.check(position);
                    rb_supervision.setBackgroundColor(getResources().getColor(R.color.radiobt_press));
                    rb_supervision_search.setBackgroundColor(getResources().getColor(R.color.radiobt_normal));
                } else if (position == 1) {
                    pos = 1;
                    radioGroup.check(position);
                    rb_supervision.setBackgroundColor(getResources().getColor(R.color.radiobt_normal));
                    rb_supervision_search.setBackgroundColor(getResources().getColor(R.color.radiobt_press));

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
                if (UserPermissionHelper.getUserPermissionStatus(this, PERMISSION_SPONSOR_SUPERVISE)) {
                    Intent intent = new Intent(new Intent(this, AddSupervisionActivity.class));
                    intent.putExtra("currentTime", stringtime);
                    startActivityForResult(intent, ConstantString.REQUEST_CODE_REFRESH_DATA);
                }
                break;
            case R.id.head_back_iv:
                this.finish();
            default:
                break;
        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onListFragmentInteraction(String item) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantString.REQUEST_CODE_REFRESH_DATA) {
            //发送广播 刷新界面数据
            Intent intent = new Intent();
            if (pos == 0) {
                intent.setAction(ConstantString.BROADCAST_REFRESH_SUPERVISION_LEFT);
            } else {
                intent.setAction(ConstantString.BROADCAST_REFRESH_SUPERVISION_RIGHT);
            }
            BroadCastManager.getInstance().sendBroadCast(SupervisionActivity.this, intent);
        }
    }
}
