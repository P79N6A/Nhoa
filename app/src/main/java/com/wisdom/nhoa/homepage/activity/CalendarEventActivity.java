package com.wisdom.nhoa.homepage.activity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.noober.menu.FloatMenu;
import com.noober.menu.MenuItem;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.widget.CustomDatePicker;
import com.wisdom.nhoa.widget.calendar.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class CalendarEventActivity extends BaseActivity{
    public static final String MSGADDCALENDARRESULTCODE = "com.wisdom.nhoa.addcalendarmsg";
//    @BindView(R.id.et_event_content)
//    EditText event_content;
    @BindView(R.id.tv_event_starttime)
    TextView event_starttime;
    @BindView(R.id.tv_event_endtime)
    TextView event_endtime;
    @BindView(R.id.et_schedule_title)
    EditText event_title;
    @BindView(R.id.et_schedule_type)
    TextView tv_schedule_type;
    @BindView(R.id.et_schedule_location)
    EditText et_schedule_location;
    @BindView(R.id.schedule_allday)
    Switch  schedule_allday;
    @BindView(R.id.tv_is_all_day)
    TextView  tv_is_all_day;
    @BindView(R.id.calendar_type_color)
    ImageView calendar_type_color;
    private String currentTime="";
    private int currentmark=0;
    private String currentchosetime="";
    private CustomDatePicker customDatePicker;
    private boolean allday=true;
    private FloatMenu floatMenu;
    private List<MenuItem> menuItemList=new ArrayList<>();
    private String[] schedule_type={"我的日程","工作","家庭","朋友","旅行","其他","生日","公共假期"};
    private int scheduleType=1;
    private  GradientDrawable gradientDrawable;
    @Override
    public void initViews() {
        gradientDrawable= (GradientDrawable) calendar_type_color.getBackground();//gradientDrawable 是drawable的子类
        setImageColor(scheduleType);
        setTitle("添加日程");
        currentchosetime=getIntent().getStringExtra("currentTime");
       initDataPick();
       schedule_allday.setChecked(true);
        schedule_allday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              if (isChecked){
                  allday=true;
                  tv_is_all_day.setText("是");
              }else {
                 allday=false;
                  tv_is_all_day.setText("否");
              }
            }
        });
        for (int i=0;i<schedule_type.length;i++){
            MenuItem menuItem=new MenuItem();
            menuItem.setItem(schedule_type[i]);
            menuItemList.add(menuItem);
        }

        floatMenu=new FloatMenu(this,tv_schedule_type);
        floatMenu.items(menuItemList);
        floatMenu.setOnItemClickListener(new FloatMenu.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
             scheduleType=position+1;
                setImageColor(scheduleType);
                tv_schedule_type.setText(""+schedule_type[position]);
             floatMenu.dismiss();
            }
        });

    }

    private void setImageColor(int scheduleType) {//设置日程类型颜色

        switch (scheduleType){
            case  1:
                gradientDrawable.setColor(getResources().getColor(R.color.calendar_color1));
                break;
            case 2:
                gradientDrawable.setColor(getResources().getColor(R.color.calendar_color2));
                break;
            case 3:
                gradientDrawable.setColor(getResources().getColor(R.color.calendar_color3));
                break;
            case 4:
                gradientDrawable.setColor(getResources().getColor(R.color.calendar_color4));
                break;
            case 5:
                gradientDrawable.setColor(getResources().getColor(R.color.calendar_color5));
                break;
            case 6:
                gradientDrawable.setColor(getResources().getColor(R.color.calendar_color6));
                break;
            case 7:
                gradientDrawable.setColor(getResources().getColor(R.color.calendar_color7));
                break;
            case 8:
                gradientDrawable.setColor(getResources().getColor(R.color.calendar_color8));
                break;
            default:
                break;
        }
    }

    private void initDataPick() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
//        String now = sdf.format(new Date());
        customDatePicker=new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                currentTime=time;
          if (currentmark==0){
              event_starttime.setText(time);
          }else {
              event_endtime.setText(time);
          }
            }
        },"1990-01-01 00:00", "2100-01-01 00:00");
        customDatePicker.showSpecificTime(true); // 显示时和分
        customDatePicker.setIsLoop(true); // 允许循环滚动
        SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("HH:mm");
        Date curDate =  new Date(System.currentTimeMillis());
        Date onehourDate =  new Date(System.currentTimeMillis()+60*60*1000);
        event_starttime.setText(currentchosetime+" "+formatter.format(curDate));
        event_endtime.setText(currentchosetime+" "+formatter.format(onehourDate));
    }
    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_calendar_event);
    }
    @OnClick({R.id.tv_event_starttime,R.id.tv_event_endtime,R.id.bt_submit_event,R.id.et_schedule_type})
    public void OnClickView(View view){
        switch (view.getId()){
            case R.id.bt_submit_event:
                submitEvent();//提交
                break;
            case R.id.tv_event_starttime:
                currentmark=0;
                customDatePicker.show(event_starttime.getText().toString());
                break;
            case R.id.tv_event_endtime:
                currentmark=1;
                customDatePicker.show(event_endtime.getText().toString());
                break;
            case R.id.et_schedule_type:
                floatMenu.show();

                break;
                default:
                    break;
        }



    }

    private void submitEvent() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        //将字符串形式的时间转化为Date类型的时间
        long starttimestamp = 0;
        long endtimestamp = 0;
        try {
            starttimestamp = sdf.parse(event_starttime.getText().toString()).getTime();
            endtimestamp = sdf.parse(event_endtime.getText().toString()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(event_title.getText().toString())){
            ToastUtil.showToast(this,"请填写标题");
            return;
        } else if (TextUtils.isEmpty(et_schedule_location.getText().toString())){
                ToastUtil.showToast(this,"请填写位置");
                return;
            }
//        else if (TextUtils.isEmpty(event_content.getText().toString())){
//                ToastUtil.showToast(this,"请填写内容");
//                return;
//            }
            else if (starttimestamp > endtimestamp) {
             ToastUtil.showToast("起始时间不能大于结束时间");
                return;
         }
        U.showLoadingDialog(this);
        String datetxt=event_starttime.getText().toString().substring(0,10);
        String starttime=event_starttime.getText().toString();
        String endtime=event_endtime.getText().toString();
//        String contents=event_content.getText().toString();
        String location=et_schedule_location.getText().toString();

        String title=event_title.getText().toString();
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
//        params.put("datetxt",datetxt);
        params.put("location", location);
        params.put("starttime", starttime);
        params.put("endtime", endtime);//做了字符串拆分，本来是一整体
//        params.put("contents",contents);
        params.put("title",title);
        params.put("isallday",allday);
        params.put("calendarid",scheduleType);
        HttpUtil.httpGet(ConstantUrl.SUBMITCALENDAREVENT, params, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                U.closeLoadingDialog();
                ToastUtil.showToast(CalendarEventActivity.this, "添加成功");
                Intent  intent=new Intent();
                intent.setAction(MSGADDCALENDARRESULTCODE);
                sendBroadcast(intent);
                finish();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
            }
        });
    }




}
