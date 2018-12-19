package com.wisdom.nhoa.homepage.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;

import butterknife.BindView;

public class LeaderScheduleDetaileActivity extends BaseActivity {
    @BindView(R.id.tv_schdule_title)
    TextView tv_schdule_title;
    @BindView(R.id.tv_add_name)
    TextView tv_add_name;
    @BindView(R.id.tv_leader_name)
    TextView tv_leader_name;
    @BindView(R.id.tv_event_starttime)
    TextView tv_event_starttime;
    @BindView(R.id.tv_event_endtime)
    TextView tv_event_endtime;
    @BindView(R.id.tv_is_all_day)
    TextView tv_is_all_day;
    @BindView(R.id.tv_schedule_type)
    TextView tv_schedule_type;
    @BindView(R.id.tv_schedule_location)
    TextView tv_schedule_location;
    private int calendarid;
    private String starttime="";
    private String endtime="";
    private String Id="";
    private String title="";
    private String username="";
    private String ownername="";
    private boolean isallday;
    private String location;
    private String[] schedule_types={"我的日程","工作","家庭","朋友","旅行","其他","生日","公共假期"};
    @Override
    public void initViews() {
        setTitle("日程详细");
        calendarid= getIntent().getIntExtra("calendarid",0);
        starttime= getIntent().getStringExtra("starttime");
        endtime= getIntent().getStringExtra("endtime");
        Id= getIntent().getStringExtra("Id");
        title= getIntent().getStringExtra("title");
        username= getIntent().getStringExtra("username");
        ownername= getIntent().getStringExtra("ownername");
        isallday= getIntent().getBooleanExtra("isallday",true);
        location= getIntent().getStringExtra("location");
        tv_schdule_title.setText(""+title);
        if (username.equals("")){
            tv_add_name.setText(""+ownername);
        }else {
            tv_add_name.setText(""+username);
        }
        if (Integer.valueOf(calendarid)<=8) {
            tv_schedule_type.setText("" + schedule_types[Integer.valueOf(calendarid) - 1]);
        }
        tv_leader_name.setText(""+ownername);
        tv_event_starttime.setText(""+starttime);
        tv_event_endtime.setText(""+endtime);
        tv_schedule_location.setText(""+location);

      if (isallday){
          tv_is_all_day.setText("是");
      }else {
          tv_is_all_day.setText("否");
      }


    }

    @Override
    public void setlayoutIds() {
    setContentView(R.layout.activity_leader_schedule_detaile);
    }
}
