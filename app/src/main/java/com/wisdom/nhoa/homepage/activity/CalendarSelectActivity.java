package com.wisdom.nhoa.homepage.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.AppApplication;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.homepage.adapter.CalendarEventAdapter;
import com.wisdom.nhoa.homepage.model.CalendarEventModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.greendao.CalendarEventModelDao;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;
import com.wisdom.nhoa.widget.CustomDatePicker;
import com.wisdom.nhoa.widget.CustomDayView;
import com.wisdom.nhoa.widget.calendar.Utils;
import com.wisdom.nhoa.widget.calendar.component.CalendarAttr;
import com.wisdom.nhoa.widget.calendar.component.CalendarViewAdapter;
import com.wisdom.nhoa.widget.calendar.interf.OnSelectDateListener;
import com.wisdom.nhoa.widget.calendar.model.CalendarDate;
import com.wisdom.nhoa.widget.calendar.view.Calendar;
import com.wisdom.nhoa.widget.calendar.view.MonthPager;

import org.greenrobot.greendao.query.QueryBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class CalendarSelectActivity extends BaseActivity {
    private static final int CALENDARTOEVENT = 101;
    @BindView(R.id.content)
    CoordinatorLayout content;
    @BindView(R.id.calendar_view)
    MonthPager monthPager;
    @BindView(R.id.list)
    RecyclerView rvToDoList;
    @BindView(R.id.last_month)
    TextView lastMonth;
    @BindView(R.id.next_month)
    TextView nextMonth;
    @BindView(R.id.tv_no_calendar_event)
    TextView tvNoCalendarEvent;
    private CalendarEventAdapter madapter;
    private ArrayList<Calendar> currentCalendars = new ArrayList<>();
    private CalendarViewAdapter calendarAdapter;
    private OnSelectDateListener onSelectDateListener;
    private int mCurrentPage = MonthPager.CURRENT_DAY_INDEX;
    private Context context;
    private CalendarDate currentDate;
    private boolean initiated = false;
    private List<CalendarEventModel> modelList = new ArrayList<>();
    private CustomDatePicker customDatePicker;
    private String stringtime = "";
    private TextView tv_calendar_title;
    @Override
    public void initViews() {
        context = this;
        setRightIcon(R.mipmap.add);
        rightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(new Intent(CalendarSelectActivity.this, CalendarEventActivity.class));
                intent.putExtra("currentTime", stringtime);
                startActivityForResult(intent, CALENDARTOEVENT);
            }
        });
        //此处强行setViewHeight
        monthPager.setViewHeight(Utils.dpi2px(context, 270));
        rvToDoList.setHasFixedSize(true);
        //这里用线性显示 类似于listview
        View titleView = LayoutInflater.from(context).inflate(R.layout.calendar_re_title, null);
        tv_calendar_title=titleView.findViewById(R.id.tv_calendar_title);
        madapter = new CalendarEventAdapter(this, modelList);
        madapter.addHeadView(titleView);
        rvToDoList.setLayoutManager(new LinearLayoutManager(this));
        rvToDoList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvToDoList.setAdapter(madapter);
        initCurrentDate();
        initCalendarView();
        initCalendarPick();
        getCalendarEvent();
        refreshClickDate(currentDate);
        Log.e("ldf", "OnCreated");
    }

    private void initCalendarPick() { //初始化时间选择器
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        customDatePicker = new CustomDatePicker(context, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                refreshMonthPager(time);
                refreshClickDate(currentDate);
            }
        }, "1990-01-01 00:00", "2100-01-01 00:00");
        customDatePicker.showSpecificTime(false); // 不显示时和分
        customDatePicker.setIsLoop(false); // 不允许循环滚动
    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_calendar_select);
    }

    /**
     * onWindowFocusChanged回调时，将当前月的种子日期修改为今天
     *
     * @return void
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !initiated) {
            refreshMonthPager("");
            initiated = true;
        }
    }

    /*
    * 如果你想以周模式启动你的日历，请在onResume是调用
    * Utils.scrollTo(content, rvToDoList, monthPager.getCellHeight(), 200);
    * calendarAdapter.switchToWeek(monthPager.getRowIndex());
    * */
    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * 初始化对应功能的listener
     *
     * @return void
     */
    @OnClick({R.id.comm_head_title, R.id.back_today_button, R.id.scroll_switch, R.id.next_month, R.id.last_month,})
    public void OnViewClick(View view) {
        switch (view.getId()) {
            case R.id.back_today_button:
                refreshMonthPager("");
                break;
            case R.id.scroll_switch:
                if (calendarAdapter.getCalendarType() == CalendarAttr.CalendarType.WEEK) {
                    Utils.scrollTo(content, rvToDoList, monthPager.getViewHeight(), 200);
                    lastMonth.setText("上一月");
                    nextMonth.setText("下一月");
                    calendarAdapter.switchToMonth();
                } else {
                    Utils.scrollTo(content, rvToDoList, monthPager.getCellHeight(), 200);
                    lastMonth.setText("上一周");
                    nextMonth.setText("下一周");
                    calendarAdapter.switchToWeek(monthPager.getRowIndex());
                }
                break;
            case R.id.next_month:
                monthPager.setCurrentItem(monthPager.getCurrentPosition() + 1);
                refreshClickDate(currentDate);
                break;
            case R.id.last_month:
                monthPager.setCurrentItem(monthPager.getCurrentPosition() - 1);
                refreshClickDate(currentDate);
                break;
            case R.id.comm_head_title:
                String currentdate = currentDate.getYear() + "-" + currentDate.getMonth() + "-" + currentDate.getDay();
                customDatePicker.show(currentdate);
                break;
        }

    }

    /**
     * 初始化currentDate
     *
     * @return void
     */
    private void initCurrentDate() {//初始化当年日期
        currentDate = new CalendarDate();
        setTitle(currentDate.getYear() + "年" + currentDate.getMonth() + "月");
        String month = currentDate.getMonth() + "";
        String day = currentDate.getDay() + "";
        if (Integer.valueOf(currentDate.getMonth()) < 10) {
            month = "0" + currentDate.getMonth();
        }
        if (Integer.valueOf(currentDate.getDay()) < 10) {
            day = "0" + currentDate.getDay();
        }
        stringtime = currentDate.year + "-" + month + "-" + day;
    }

    /**
     * 初始化CustomDayView，并作为CalendarViewAdapter的参数传入
     */
    private void initCalendarView() {
        initListener();
        CustomDayView customDayView = new CustomDayView(context, R.layout.custom_day);
        calendarAdapter = new CalendarViewAdapter(
                context,
                onSelectDateListener, CalendarAttr.CalendarType.MONTH, CalendarAttr.WeekArrayType.Monday, customDayView);
        calendarAdapter.setOnCalendarTypeChangedListener(new CalendarViewAdapter.OnCalendarTypeChanged() {
            @Override
            public void onCalendarTypeChanged(CalendarAttr.CalendarType type) {
                rvToDoList.scrollToPosition(0);
            }
        });
//        initMarkData();
        initMonthPager();
    }

    /**
     * 初始化标记数据，HashMap的形式，可自定义
     * 如果存在异步的话，在使用setMarkData之后调用 calendarAdapter.notifyDataChanged();
     */
    private void initMarkData(List<CalendarEventModel> calendarEventModels) {
        HashMap<String, String> markData = new HashMap<>();
        if (calendarEventModels.size() > 0) {
            for (int i = 0; i < calendarEventModels.size(); i++) {
                CalendarEventModel calendarEventModel = calendarEventModels.get(i);
                markData.put(calendarEventModel.getDatetxt().substring(0, 4) + calendarEventModel.getDatetxt().substring(4, 9).replace("0", "") + calendarEventModel.getDatetxt().substring(9, 10), "1");
            }
        }
        Log.v("markmap", markData.toString());
        calendarAdapter.setMarkData(markData);
        calendarAdapter.notifyDataChanged();


    }

    //    public static Date strToDateLong(String strDate) {
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        ParsePosition pos = new ParsePosition(0);
//        Date strtodate = formatter.parse(strDate, pos);
//        return strtodate;
//    }
    private void initListener() {
        onSelectDateListener = new OnSelectDateListener() {
            @Override
            public void onSelectDate(CalendarDate date) {
                refreshClickDate(date);
            }

            @Override
            public void onSelectOtherMonth(int offset) {
                //偏移量 -1表示刷新成上一个月数据 ， 1表示刷新成下一个月数据
                monthPager.selectOtherMonth(offset);
            }
        };
    }


    private void refreshClickDate(CalendarDate date) {//点击日历时候刷新事件并查找是否有日程安排
        currentDate = date;
        String month = date.getMonth() + "";
        String day = date.getDay() + "";
        if (Integer.valueOf(date.getMonth()) < 10) {
            month = "0" + date.getMonth();
        }
        if (Integer.valueOf(date.getDay()) < 10) {
            day = "0" + date.getDay();
        }
        stringtime = date.getYear() + "-" + month + "-" + day;
        setTitle(date.getYear() + "年" + date.getMonth() + "月");
        String datestring = date.getYear() + "-" + month + "-" + day;
//         List<CalendarEventModel> calendarEventModel= AppApplication.getDaosession().queryBuilder(CalendarEventModel.class).list();
        QueryBuilder queryBuilder = AppApplication.getDaosession().getDao(CalendarEventModel.class).queryBuilder();
        queryBuilder.where(CalendarEventModelDao.Properties.Datetxt.eq(datestring));
        List<CalendarEventModel> calendarEventModels = queryBuilder.list();
        refreshAdapter(calendarEventModels);
        Log.v("databasecalendar", calendarEventModels.toString() + datestring);
        tv_calendar_title.setText(currentDate.getYear()+"年"+currentDate.getMonth()+"月"+currentDate.getDay()+"日");
    }

    public void refreshAdapter(List<CalendarEventModel> list) {
        if (modelList.size()!=0) {
            modelList.clear();
        }else {
            modelList.addAll(list);
        }
        if (list.size()!=0){
            tvNoCalendarEvent.setVisibility(View.GONE);
        }else {
            tvNoCalendarEvent.setVisibility(View.VISIBLE);
        }
        madapter.notifyDataSetChanged();
    }

    /**
     * 初始化monthPager，MonthPager继承自ViewPager
     *
     * @return void
     */
    private void initMonthPager() {
        monthPager.setAdapter(calendarAdapter);
        monthPager.setCurrentItem(MonthPager.CURRENT_DAY_INDEX);
        monthPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                position = (float) Math.sqrt(1 - Math.abs(position));
                page.setAlpha(position);
            }
        });
        monthPager.addOnPageChangeListener(new MonthPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPage = position;
                currentCalendars = calendarAdapter.getPagers();
                if (currentCalendars.get(position % currentCalendars.size()) != null) {
                    CalendarDate date = currentCalendars.get(position % currentCalendars.size()).getSeedDate();
                    currentDate = date;
                    tv_calendar_title.setText(currentDate.getYear()+"年"+currentDate.getMonth()+"月"+currentDate.getDay()+"日");
                    setTitle(date.getYear() + "年" + date.getMonth() + "月");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }


    private void refreshMonthPager(String time) {//调到指定日期

        CalendarDate today = new CalendarDate();
        if (!"".equals(time)) {
            String years = time.substring(0, 4);
            String month = time.substring(5, 7);
            String day = time.substring(8, 10);
            today.setYear(Integer.valueOf(years));
            today.setMonth(Integer.valueOf(month));
            today.setDay(Integer.valueOf(day));
            currentDate = today;
        }
        refreshClickDate(today);
        calendarAdapter.notifyDataChanged(today);
        setTitle(today.getYear() + "年" + today.getMonth() + "月");
    }

    public void getCalendarEvent() {

        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(context).getAccess_token());
        HttpUtil.httpGet(ConstantUrl.GETCALENDAREVENT, params, new JsonCallback<BaseModel<List<CalendarEventModel>>>() {
            @Override
            public void onSuccess(BaseModel<List<CalendarEventModel>> listBaseModel, Call call, Response response) {
                Log.v("ssssssssssss", listBaseModel.results.toString());
                initMarkData(listBaseModel.results);
                AppApplication.getDaosession().deleteAll(CalendarEventModel.class);
                for (int i = 0; i < listBaseModel.results.size(); i++) {
                    AppApplication.getDaosession().insertOrReplace(listBaseModel.results.get(i));
                }
                refreshAdapter(listBaseModel.results);
                refreshClickDate(currentDate);
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
        if (requestCode == CALENDARTOEVENT & resultCode == 1) {
            getCalendarEvent();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
