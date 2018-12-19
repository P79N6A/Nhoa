package com.wisdom.nhoa.homepage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.AppApplication;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.homepage.activity.LeaderScheduleDetaileActivity;
import com.wisdom.nhoa.homepage.adapter.LeaderScheduleAdapter;
import com.wisdom.nhoa.homepage.model.LeaderScheduleModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.greendao.LeaderScheduleModelDao;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

public class LeaderShipScheduleFragment extends Fragment {

    private static final int CALENDARTOEVENT = 101;
    @BindView(R.id.content)
    CoordinatorLayout content;
    @BindView(R.id.calendar_view)
    MonthPager monthPager;
    @BindView(R.id.list)
    RecyclerView rvToDoList;//日程recycleview
    @BindView(R.id.last_month)
    TextView lastMonth;
    @BindView(R.id.next_month)
    TextView nextMonth;
    @BindView(R.id.tv_no_calendar_event)
    TextView tvNoCalendarEvent;
    private LeaderScheduleAdapter madapter;//日程适配器
    private ArrayList<Calendar> currentCalendars = new ArrayList<>();
    private CalendarViewAdapter leaderAdapter;//日历适配器
    private OnSelectDateListener onSelectDateListener;
    private int mCurrentPage = MonthPager.CURRENT_DAY_INDEX;
    private CalendarDate currentDate;
    private boolean initiated = false;
    private List<LeaderScheduleModel> modelList = new ArrayList<>();
    private CustomDatePicker customDatePicker;//时间选择
    private String stringtime = "";
     TextView tv_calendar_title;
    List<String>  list=new ArrayList<>();
    private Unbinder unbinder;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      view =inflater.inflate(R.layout.fragment_leader_ship,container,false);
      unbinder= ButterKnife.bind(this,view);
        initView();
        return view;
    }


    public void initView( ) {
        //此处强行setViewHeight
        monthPager.setViewHeight(Utils.dpi2px(getActivity(), 270));
        rvToDoList.setHasFixedSize(true);
        //这里用线性显示 类似于listview
//        View titleView = LayoutInflater.from(getActivity()).inflate(R.layout.calendar_re_title, null);
        tv_calendar_title=view.findViewById(R.id.date_title);
        tv_calendar_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentdate = currentDate.getYear() + "-" + currentDate.getMonth() + "-" + currentDate.getDay();
                customDatePicker.show(currentdate);
            }
        });
        madapter = new LeaderScheduleAdapter(getActivity(), modelList);
        //进入日程详细
        madapter.SetOnItemClickListener(new LeaderScheduleAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, LeaderScheduleModel data, int position) {
                Intent intent=new Intent(getActivity(), LeaderScheduleDetaileActivity.class);
                intent.putExtra("calendarid",data.getCalendarid());
                intent.putExtra("isallday",data.getIsallday());
                intent.putExtra("location",data.getLocation());
                intent.putExtra("starttime",data.getStarttime());
                intent.putExtra("endtime",data.getEndtime());
                intent.putExtra("Id",data.getId());
                intent.putExtra("title",data.getTitle());
                intent.putExtra("username",data.getUsername());
                intent.putExtra("ownername",data.getOwnername());
                startActivity(intent);
            }
        });
//        madapter.addHeadView(titleView);
        rvToDoList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvToDoList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rvToDoList.setAdapter(madapter);
        initCurrentDate();
        initCalendarView();
        initCalendarPick();
        refreshClickDate(currentDate);
        Log.e("ldf", "OnCreated");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            getLeaderShipSchedule();//实现懒更新为防止数据过大同时加载导致进入卡顿现象
        }
    }

    private void initCalendarPick() { //初始化时间选择器
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        customDatePicker = new CustomDatePicker(getActivity(), new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                refreshMonthPager(time);
                refreshClickDate(currentDate);
            }
        }, "1990-01-01 00:00", "2100-01-01 00:00");
        customDatePicker.showSpecificTime(false); // 不显示时和分
        customDatePicker.setIsLoop(false); // 不允许循环滚动
    }

//
//    /**
//     * 初始化对应功能的listener
//     *
//     * @return void
//     */
//    @OnClick({R.id.comm_head_title, R.id.back_today_button, R.id.scroll_switch, R.id.next_month, R.id.last_month,})
//    public void OnViewClick(View view) {
//        switch (view.getId()) {
//            case R.id.back_today_button:
//                refreshMonthPager("");
//                break;
//            case R.id.scroll_switch:
//                if (leaderAdapter.getCalendarType() == CalendarAttr.CalendarType.WEEK) {
//                    Utils.scrollTo(content, rvToDoList, monthPager.getViewHeight(), 200);
//                    lastMonth.setText("上一月");
//                    nextMonth.setText("下一月");
//                    leaderAdapter.switchToMonth();
//                } else {
//                    Utils.scrollTo(content, rvToDoList, monthPager.getCellHeight(), 200);
//                    lastMonth.setText("上一周");
//                    nextMonth.setText("下一周");
//                    leaderAdapter.switchToWeek(monthPager.getRowIndex());
//                }
//                break;
//            case R.id.next_month:
//                monthPager.setCurrentItem(monthPager.getCurrentPosition() + 1);
//                refreshClickDate(currentDate);
//                break;
//            case R.id.last_month:
//                monthPager.setCurrentItem(monthPager.getCurrentPosition() - 1);
//                refreshClickDate(currentDate);
//                break;
//            case R.id.comm_head_title:
//                String currentdate = currentDate.getYear() + "-" + currentDate.getMonth() + "-" + currentDate.getDay();
//                customDatePicker.show(currentdate);
//                break;
//        }
//
//    }

    /**
     * 初始化currentDate
     *
     * @return void
     */
    private void initCurrentDate() {//初始化当年日期
        currentDate = new CalendarDate();
//        setTitle(currentDate.getYear() + "年" + currentDate.getMonth() + "月");
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
        CustomDayView customDayView = new CustomDayView(getActivity(), R.layout.custom_day1);
        leaderAdapter = new CalendarViewAdapter(
                getActivity(),
                onSelectDateListener, CalendarAttr.CalendarType.MONTH, CalendarAttr.WeekArrayType.Monday, customDayView);
        leaderAdapter.setOnCalendarTypeChangedListener(new CalendarViewAdapter.OnCalendarTypeChanged() {
            @Override
            public void onCalendarTypeChanged(CalendarAttr.CalendarType type) {
                rvToDoList.scrollToPosition(0);
            }
        });

//        leaderAdapter.notifyDataChanged();
//        initMarkData();
        initMonthPager();
    }

    /**
     * 初始化标记数据，HashMap的形式，可自定义
     * 如果存在异步的话，在使用setMarkData之后调用 leaderAdapter.notifyDataChanged();
     * 此次在数据库里查询标记列表
     */
    private void initMarkData() {
        QueryBuilder queryBuilder = AppApplication.getDaosession().getDao(LeaderScheduleModel.class).queryBuilder();
        List<LeaderScheduleModel> leaderScheduleModels = queryBuilder.listLazy();
        HashMap<String, String> markData = new HashMap<>();
        if (leaderScheduleModels.size() > 0) {
            for (int i = 0; i < leaderScheduleModels.size(); i++) {
                LeaderScheduleModel scheduleModel = leaderScheduleModels.get(i);
                if (scheduleModel.getStarttime().length()>=10) {
                    markData.put(scheduleModel.getDatetxt().substring(0, 4) +
                            scheduleModel.getDatetxt().substring(4, 6).replace("0", "")
                            +scheduleModel.getDatetxt().substring(6,7 )
                            +scheduleModel.getDatetxt().substring(7,9 ).replace("0", "")
                            + scheduleModel.getDatetxt().substring(9, 10), ""+scheduleModel.getCalendarid());
                }

            }
        }
        Log.v("dabase", leaderScheduleModels.toString());
        Log.v("markmap", markData.toString());
        leaderAdapter.setMarkData1(markData);
//        leaderAdapter.notifyDataChanged();

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
//        setTitle(date.getYear() + "年" + date.getMonth() + "月");
        String datestring = date.getYear() + "-" + month + "-" + day;
//         List<CalendarEventModel> calendarEventModel= AppApplication.getDaosession().queryBuilder(CalendarEventModel.class).list();
        //从数据库查询对应日期的日程列表
        QueryBuilder queryBuilder = AppApplication.getDaosession().getDao(LeaderScheduleModel.class).queryBuilder();
        queryBuilder.where(LeaderScheduleModelDao.Properties.Datetxt.eq(datestring));
        List<LeaderScheduleModel> leaderScheduleModels = queryBuilder.listLazy();
        refreshAdapter(leaderScheduleModels);
        Log.v("databasecalendar", leaderScheduleModels.toString() + datestring);
        tv_calendar_title.setText(currentDate.getYear()+"."+currentDate.getMonth());
    }

    public void refreshAdapter(List<LeaderScheduleModel> list) {
            modelList.clear();
            modelList.addAll(list);
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
        monthPager.setAdapter(leaderAdapter);
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
                currentCalendars = leaderAdapter.getPagers();
                if (currentCalendars.get(position % currentCalendars.size()) != null) {
                    CalendarDate date = currentCalendars.get(position % currentCalendars.size()).getSeedDate();
                    currentDate = date;
                    tv_calendar_title.setText(currentDate.getYear()+"."+currentDate.getMonth());
                     refreshClickDate(date);
//                    setTitle(date.getYear() + "年" + date.getMonth() + "月");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        initMarkData();
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
        leaderAdapter.notifyDataChanged(today);
//        setTitle(today.getYear() + "年" + today.getMonth() + "月");
    }
    //每次进入获取领导列表并且更新数据库
    public void getLeaderShipSchedule() {

        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(getActivity()).getAccess_token());
        HttpUtil.httpGet(ConstantUrl.GETLEADERSCHEDULE, params, new JsonCallback<BaseModel<List<LeaderScheduleModel>>>() {
            @Override
            public void onSuccess(BaseModel<List<LeaderScheduleModel>> listBaseModel, Call call, Response response) {
                Log.v("ssssssssssss", listBaseModel.results.toString());
//                initMarkData(listBaseModel.results);
                AppApplication.getDaosession().deleteAll(LeaderScheduleModel.class);
                //把开始时间拆分成时期并赋给 datetxt并且逐条将每天的数据插入数据库
                for (int i = 0; i < listBaseModel.results.size(); i++) {
                    if (listBaseModel.results.get(i).getStarttime().length()>=10) {
                        try {
                            List<String>  dateList=getDate(listBaseModel.results.get(i).getStarttime().substring(0, 10),listBaseModel.results.get(i).getEndtime().substring(0, 10));
                            for (int j=0;j<dateList.size();j++){
                                LeaderScheduleModel  model=new LeaderScheduleModel();
                                model.setDatetxt(dateList.get(j));
                                model.setStarttime(listBaseModel.results.get(i).getStarttime());
                                model.setEndtime(listBaseModel.results.get(i).getEndtime());
                                model.setCalendarid(listBaseModel.results.get(i).getCalendarid());
                                model.setIsallday(listBaseModel.results.get(i).getIsallday());
                                model.setLocation(listBaseModel.results.get(i).getLocation());
                                model.setId(listBaseModel.results.get(i).getId());
                                model.setTitle(listBaseModel.results.get(i).getTitle());
                                model.setOwnername(listBaseModel.results.get(i).getOwnername());
                                model.setUsername(listBaseModel.results.get(i).getUsername());
                                AppApplication.getDaosession().insertOrReplace(model);
//                                listBaseModel.results.get(i).setDatetxt(dateList.get(j));
//                                AppApplication.getDaosession().insertOrReplace(listBaseModel.results.get(i));
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                }
                initMarkData();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
