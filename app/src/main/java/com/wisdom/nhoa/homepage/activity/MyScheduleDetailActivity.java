package com.wisdom.nhoa.homepage.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.noober.menu.FloatMenu;
import com.noober.menu.MenuItem;
import com.wisdom.nhoa.AppApplication;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.homepage.model.CalendarEventModel;
import com.wisdom.nhoa.supervision.activity.SupervisionDetailActivity;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.greendao.CalendarEventModelDao;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.widget.CustomDatePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.wisdom.nhoa.homepage.activity.UpdataScheduleEventActivity.RESULTCODE;

public class MyScheduleDetailActivity extends BaseActivity {
    private static final int REQUESTCODE = 1021;
    public static final int DELETERESULTCODE = 1111;
//    @BindView(R.id.et_event_content)
//    EditText event_content;
    @BindView(R.id.tv_event_starttime)
    TextView event_starttime;
    @BindView(R.id.tv_event_endtime)
    TextView event_endtime;
    @BindView(R.id.et_schedule_title)
    EditText event_title;
    @BindView(R.id.edit_schedule)
    Button edit_schedule;
    @BindView(R.id.et_schedule_type)
    TextView tv_schedule_type;
    @BindView(R.id.et_schedule_location)
    EditText et_schedule_location;
    @BindView(R.id.schedule_allday)
    Switch schedule_allday;
    @BindView(R.id.tv_is_all_day)
    TextView tv_is_all_day;
    private String content="";

//    private String datetext="";
    private String starttime="";
    private String endtime="";
    private String Id="";
    private String datetext;
    private String title;
    private String location;
    private int calendarid;
    private boolean isAllDay;
    private CustomDatePicker customDatePicker;
    private int currentmark=0;
    private String currentchosetime="";
    private String currentTime="";
    private List<String>  list=new ArrayList<>();
    private boolean  isCanEdit=false;
    private FloatMenu floatMenu;
    private List<MenuItem> menuItemList=new ArrayList<>();
    private String[] schedule_types={"我的日程","工作","家庭","朋友","旅行","其他","生日","公共假期"};
    @BindView(R.id.calendar_type_color)
    ImageView calendar_type_color;
    private GradientDrawable gradientDrawable;
    @Override
    public void initViews() {
        gradientDrawable= (GradientDrawable) calendar_type_color.getBackground();//gradientDrawable 是drawable的子类

        content= getIntent().getStringExtra("content");
//        datetext= getIntent().getStringExtra("datetext");
        starttime= getIntent().getStringExtra("starttime");
        endtime= getIntent().getStringExtra("endtime");
        Id= getIntent().getStringExtra("Id");
        datetext=getIntent().getStringExtra("datetext");
        title=getIntent().getStringExtra("title");
        location=getIntent().getStringExtra("location");
        calendarid=getIntent().getIntExtra("calendarid",0);
        isAllDay=getIntent().getBooleanExtra("isallday",true);
        setImageColor(calendarid);
        setTitle("日程详细");
        right.setText("删除");
        right.setVisibility(View.VISIBLE);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyScheduleDetailActivity.this,UpdataScheduleEventActivity.class);
//////                intent.putExtra("currentTime", datetext);
////                intent.putExtra("starttime", starttime);
////                intent.putExtra("endtime", endtime);
////                intent.putExtra("content", content);
////                intent.putExtra("Id", Id);
////                startActivityForResult(intent,REQUESTCODE);
                ShowDialog();//删除提示框
            }
        });
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(DELETERESULTCODE);//后退刷新
                finish();
            }
        });
        event_title.setText(""+title);
        event_starttime.setText(""+starttime);
        event_endtime.setText(""+endtime);
//      event_content.setText(""+content);
        et_schedule_location.setText(""+location);
        tv_schedule_type.setText(""+schedule_types[calendarid-1]);
        event_title.setSelection(event_title.getText().toString().length());
        schedule_allday.setChecked(isAllDay);
        if (isAllDay){
            tv_is_all_day.setText("是");
        }else {
            tv_is_all_day.setText("否");
        }
        edit_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//            if (!isCanEdit){//判断点击编辑再次点击提交
//                CanEdit();
//                edit_schedule.setText("确认");
//                isCanEdit=true;
//            }else {
//                changeSchedule();
//
//            }
                changeSchedule();
            }
        });
        initDataPick();
//        NotCanEdit();
        schedule_allday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    isAllDay=true;
                    tv_is_all_day.setText("是");
                }else {
                    isAllDay=false;
                    tv_is_all_day.setText("否");
                }
            }
        });
        for (int i=0;i<schedule_types.length;i++){
            MenuItem menuItem=new MenuItem();
            menuItem.setItem(schedule_types[i]);
            menuItemList.add(menuItem);
        }

        floatMenu=new FloatMenu(this,tv_schedule_type);
        floatMenu.items(menuItemList);
        floatMenu.setOnItemClickListener(new FloatMenu.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                calendarid=position+1;
                setImageColor(calendarid);
                tv_schedule_type.setText(""+schedule_types[position]);
                floatMenu.dismiss();
            }
        });
    }
    private void NotCanEdit(){//布局不可编辑
        event_title.setEnabled(false);
        event_title.setCursorVisible(false);
        event_starttime.setClickable(false);
        event_endtime.setClickable(false);
//        event_content.setEnabled(false);
//        event_content.setCursorVisible(false);

    }
    private void CanEdit(){//布局可编辑
        event_title.setEnabled(true);;
        event_title.setCursorVisible(true);
        event_starttime.setClickable(true);
        event_endtime.setClickable(true);
//        event_content.setEnabled(true);
//        event_content.setCursorVisible(true);

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
        },"2000-01-01 00:00", "2050-01-01 00:00");
        customDatePicker.showSpecificTime(true); // 显示时和分
        customDatePicker.setIsLoop(true); // 允许循环滚动
    }
    @OnClick({R.id.tv_event_starttime,R.id.tv_event_endtime,R.id.et_schedule_type})
    public void OnClickView(View view){
        switch (view.getId()){
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
    private void ShowDialog() {

        AlertDialog.Builder builder=new AlertDialog.Builder(MyScheduleDetailActivity.this);
        builder.setTitle("确认删除此日程？").setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteSchedule();
                dialog.dismiss();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_my_schedule_detail);
    }
    private void deleteSchedule() {
        U.showLoadingDialog(context);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(MyScheduleDetailActivity.this).getAccess_token());
        params.put("id",Id);
        HttpUtil.httpGet(ConstantUrl.DELETECALENDAREVENT, params, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                U.closeLoadingDialog();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getInt("error_code") == 0) {
                        List<CalendarEventModel>  list=AppApplication.getDaosession().getCalendarEventModelDao().queryBuilder().where(CalendarEventModelDao.Properties.Id.eq(Id)).listLazy();
                        if (list.size()>0){
                            for (int i=0;i<list.size();i++){
                                AppApplication.getDaosession().getCalendarEventModelDao().delete(list.get(i));
                            }
                        }
                        ToastUtil.showToast(MyScheduleDetailActivity.this, "删除成功");
                        setResult(DELETERESULTCODE);
                        finish();
                    }else {
                        ToastUtil.showToast(MyScheduleDetailActivity.this, ""+jsonObject.optString("error_msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //从数据查询要删除的一条并删除
//                CalendarEventModel calendarEventModel= AppApplication.getDaosession().getCalendarEventModelDao().queryBuilder().where(CalendarEventModelDao.Properties.Id.eq(Id),CalendarEventModelDao.Properties.Datetxt.eq(datetext)).unique();
//                AppApplication.getDaosession().getCalendarEventModelDao().delete(calendarEventModel);

            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUESTCODE){
         if(resultCode== RESULTCODE){
             String contents=data.getStringExtra("content");
             content=contents;

         }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(DELETERESULTCODE);//后退刷新
    }

    private void changeSchedule() {//修改日程内容
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
        }
        else if (TextUtils.isEmpty(et_schedule_location.getText().toString())){
            ToastUtil.showToast(this,"请填写位置");
            return;
        }
          else if (starttimestamp > endtimestamp) {
            ToastUtil.showToast("起始时间不能大于结束时间");
            return;
        }
//        else if (TextUtils.isEmpty(event_content.getText().toString())){
//            ToastUtil.showToast(this,"请填写内容");
//            return;
//        }
        U.showLoadingDialog(this);
        final String starttime=event_starttime.getText().toString();
        final String endtime=event_endtime.getText().toString();
//        final String contents=event_content.getText().toString();
        final String title=event_title.getText().toString();
        location=et_schedule_location.getText().toString();
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(context).getAccess_token());
        params.put("starttime", starttime);
        params.put("endtime", endtime);
//        params.put("contents",contents);
        params.put("id",Id);
        params.put("title",title);
        params.put("calendarid",calendarid);
        params.put("location",location);
        params.put("isallday",isAllDay);
        HttpUtil.httpGet(ConstantUrl.UPDATACALENDAREVENT, params, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                U.closeLoadingDialog();
                ToastUtil.showToast(MyScheduleDetailActivity.this, "修改成功");
//                edit_schedule.setText("修改");
//                isCanEdit=false;
//                NotCanEdit();
                List<String> dateList = null;
                try {

                    dateList = getDate(starttime.substring(0, 10), endtime.substring(0, 10));
                    if (dateList!=null) {
                        for (int j = 0; j < dateList.size(); j++) {

                            CalendarEventModel model =AppApplication.getDaosession().getCalendarEventModelDao().queryBuilder().where(CalendarEventModelDao.Properties.Id.eq(Id),
                                    CalendarEventModelDao.Properties.Datetxt.eq(dateList.get(j))).unique();
                            if (model!=null){
                                model.setDatetxt(dateList.get(j));
                                model.setStarttime(starttime);
                                model.setEndtime(endtime);
                                model.setLocation(location);
                                model.setCalendarid(calendarid);
                                model.setIsallday(isAllDay);
//                              地点和是否全当天  model.setIsallday(listBaseModel.results.get(i).getIsallday());
//                                model.setLocation(listBaseModel.results.get(i).getLocation());
                                model.setTitle(title);
                                Log.v("insert",model.toString());
                                AppApplication.getDaosession().insertOrReplace(model);
                            }

                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                Intent intent = new Intent();
//                intent.putExtra("content",""+event_content.getText().toString());
//                setResult(RESULTCODE,intent);
//                finish();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
            }
        });
    }
    //遍历周期间隔并且拆分出日期
    private List<String> getDate(String beginDate, String endDate) throws ParseException {
        if (list!=null){
            list.clear();
        }
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        java.util.Calendar  calendar= java.util.Calendar.getInstance();
        calendar.setTime(sdf.parse(beginDate));
        for (long d =calendar.getTimeInMillis();d<=sdf.parse(endDate).getTime();d=get_add_1(calendar)){
            list.add(sdf.format(d));
        }
        return list;
    }

    private long get_add_1(java.util.Calendar c) {
        c.set(java.util.Calendar.DAY_OF_MONTH, c.get(java.util.Calendar.DAY_OF_MONTH)+1);
        return c.getTimeInMillis();
    }


}