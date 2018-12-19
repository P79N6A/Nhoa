package com.wisdom.nhoa.homepage.activity;


import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.AppApplication;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.homepage.model.CalendarEventModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.greendao.CalendarEventModelDao;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.widget.CustomDatePicker;

import org.greenrobot.greendao.query.QueryBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class UpdataScheduleEventActivity extends BaseActivity {
    public static final int RESULTCODE =1100 ;
    @BindView(R.id.et_event_content)
    EditText event_content;
    @BindView(R.id.tv_event_starttime)
    TextView event_starttime;
    @BindView(R.id.tv_event_endtime)
    TextView event_endtime;
    private String currentTime="";
    private int currentmark=0;
    private String currentchosetime="";
    private CustomDatePicker customDatePicker;
    private String starttime="";
    private String endtime="";
    private String Id="";
    private String content="";
    List<String>  list=new ArrayList<>();//查分出的日期存储
    @Override
    public void initViews() {
        setTitle("修改日程");
        currentchosetime=getIntent().getStringExtra("currentTime");
        starttime=getIntent().getStringExtra("starttime");
        endtime=getIntent().getStringExtra("endtime");
        Id=getIntent().getStringExtra("Id");
        content=getIntent().getStringExtra("content");
        initDataPick();
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
        SimpleDateFormat formatter   =   new   SimpleDateFormat   ("HH:mm");
//        Date curDate =  new Date(System.currentTimeMillis());
//        Date onehourDate =  new Date(System.currentTimeMillis()+60*60*1000);
        event_content.setText(content+"");
        event_starttime.setText(starttime);
        event_endtime.setText(endtime);
    }
    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_updata_schedule_event);
    }
    @OnClick({R.id.rl_start_layout,R.id.rl_end_layout,R.id.bt_submit_event})
    public void OnClickView(View view){
        switch (view.getId()){
            case R.id.bt_submit_event:
                submitEvent();//提交
                break;
            case R.id.rl_start_layout:
                currentmark=0;
                customDatePicker.show(event_starttime.getText().toString());
                break;
            case R.id.rl_end_layout:
                currentmark=1;
                customDatePicker.show(event_endtime.getText().toString());
                break;
        }



    }

    private void submitEvent() {


        if (TextUtils.isEmpty(event_content.getText().toString())){
            ToastUtil.showToast(this,"请填写内容");
            return;
        }
        U.showLoadingDialog(this);
        String datetxt=event_starttime.getText().toString().substring(0,10);
        final String starttime=event_starttime.getText().toString();
        final String endtime=event_endtime.getText().toString();
        final String contents=event_content.getText().toString();
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(context).getAccess_token());
        params.put("starttime", starttime);
        params.put("endtime", endtime);
        params.put("contents",contents);
        params.put("id",Id);
        HttpUtil.httpGet(ConstantUrl.UPDATACALENDAREVENT, params, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                U.closeLoadingDialog();
                ToastUtil.showToast(UpdataScheduleEventActivity.this, "修改成功");
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
//                            model.setContent(contents);
                            Log.v("insert",model.toString());
                            AppApplication.getDaosession().insertOrReplace(model);
                        }

                    }
                }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent();
                intent.putExtra("content",""+event_content.getText().toString());
                setResult(RESULTCODE,intent);
                finish();
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
