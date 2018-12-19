package com.wisdom.nhoa.homepage.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
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
import com.wisdom.nhoa.homepage.activity.CalendarEventActivity;
import com.wisdom.nhoa.homepage.activity.HomePageActivity;
import com.wisdom.nhoa.homepage.activity.MyScheduleDetailActivity;
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
import com.wisdom.nhoa.widget.floatingactionbutton.FloatingActionsMenu;


import org.greenrobot.greendao.query.QueryBuilder;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;
public class MyScheduleFragment extends Fragment {
    @BindView(R.id.floating_action_menu)
    FloatingActionsMenu floatingActionsMenu;//悬浮按钮菜单
    List<String>  list=new ArrayList<>();//查分出的日期存储
    private static final int MYSCHEDULEEVENT = 101;
    @BindView(R.id.content)
    CoordinatorLayout content;
    @BindView(R.id.calendar_view)
    MonthPager monthPager;
    @BindView(R.id.list)
    RecyclerView rvToDoList;//日程列表
    @BindView(R.id.last_month)
    TextView lastMonth;
    @BindView(R.id.next_month)
    TextView nextMonth;
    @BindView(R.id.tv_no_calendar_event)
    TextView tvNoCalendarEvent;
    private CalendarEventAdapter madapter;//日程适配器
    private ArrayList<Calendar> currentCalendars = new ArrayList<>();
    private CalendarViewAdapter calendarAdapter;//日历适配器
    private OnSelectDateListener onSelectDateListener;
    private int mCurrentPage = MonthPager.CURRENT_DAY_INDEX;
    private CalendarDate currentDate;
    private boolean initiated = false;
    private List<CalendarEventModel> modelList = new ArrayList<>();
    private CustomDatePicker customDatePicker;// 时间选择 picker
    private String stringtime = "";
    private TextView tv_calendar_title;
    private FragmentInteraction listterner;
    private FloatingActionButton bt_floatingAction;
    private Unbinder unbinder;
    private int clickDatatype=0;//点击类型区分根据此信息查询日程分类
    public interface FragmentInteraction {
        void returnTime(String time);
    }
    public int scrollState =0;//monthpager滑动状态监听为了区别滑动页面还是长按事件
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_my_schedule,container,false);
        unbinder= ButterKnife.bind(this,view);
        initView(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteraction){//activity到fragment的一个回调用于数据传输
            listterner= (FragmentInteraction) context;
        } else{
            throw new IllegalArgumentException("activity must implements FragmentInteraction");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(CalendarEventActivity.MSGADDCALENDARRESULTCODE);
        getActivity().registerReceiver(receiver,intentFilter);
    }

    public void initView(View view ) {
        //此处强行setViewHeight
        monthPager.setViewHeight(Utils.dpi2px(getActivity(), 270));
        rvToDoList.setHasFixedSize(true);
        //这里用线性显示 类似于listview
//        View titleView = LayoutInflater.from(getActivity()).inflate(R.layout.calendar_re_title, null);
//        tv_calendar_title=titleView.findViewById(R.id.tv_calendar_title);
//        titleView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String currentdate = currentDate.getYear() + "-" + currentDate.getMonth() + "-" + currentDate.getDay();
//                customDatePicker.show(currentdate);
//            }
//        });
        tv_calendar_title=view.findViewById(R.id.date_title);
        tv_calendar_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String month = currentDate.getMonth() + "";
                if (Integer.valueOf(currentDate.getMonth()) < 10) {
                    month = "0" + currentDate.getMonth();
                }
                String currentdate = currentDate.getYear() + "-" + month + "-" +"01";
                customDatePicker.show(currentdate);
            }
        });
        madapter = new CalendarEventAdapter(getActivity(), modelList);
//        madapter.addHeadView(titleView);
        rvToDoList.setLayoutManager(new LinearLayoutManager(getActivity()));
//        rvToDoList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rvToDoList.setAdapter(madapter);
        //进入日程详细
        madapter.SetOnItemClickListener(new CalendarEventAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, List<CalendarEventModel> data,int position) {
                String month = currentDate.getMonth() + "";
                String day = currentDate.getDay() + "";
                if (Integer.valueOf(currentDate.getMonth()) < 10) {
                    month = "0" + currentDate.getMonth();
                }
                if (Integer.valueOf(currentDate.getDay()) < 10) {
                    day = "0" + currentDate.getDay();
                }
                stringtime = currentDate.getYear() + "-" + month + "-" + day;
             Intent intent=new Intent(getActivity(), MyScheduleDetailActivity.class);
             intent.putExtra("title",data.get(position).getTitle());
             intent.putExtra("isallday",data.get(position).getIsallday());
             intent.putExtra("location",data.get(position).getLocation());
             intent.putExtra("datetext",stringtime);
             intent.putExtra("starttime",data.get(position).getStarttime());
             intent.putExtra("endtime",data.get(position).getEndtime());
             intent.putExtra("Id",data.get(position).getId());
              intent.putExtra("calendarid",data.get(position).getCalendarid());
             startActivityForResult(intent,MYSCHEDULEEVENT);
            }
        });
        initCurrentDate();
        initCalendarView();
        initCalendarPick();
        getCalendarEvent();
        refreshClickDate(currentDate);
        floatingActionsMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {

            }
            @Override
            public void onMenuCollapsed() {
             initMarkData("0");
             clickDatatype=0;
             calendarAdapter.notifyDataChanged(currentDate);
            }
        });
        Log.e("ldf", "OnCreated");
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
    /*
    * 悬浮按钮的点击处理
    *
    * */
    @OnClick({R.id.calendarid1, R.id.calendarid2, R.id.calendarid3, R.id.calendarid4, R.id.calendarid5 ,R.id.calendarid6, R.id.calendarid7, R.id.calendarid8})
    public void OnViewClick(View view) {
        switch (view.getId()) {
            case R.id.calendarid1:
               initMarkData("1");
                clickDatatype=1;
                break;
            case R.id.calendarid2:
                initMarkData("2");
                clickDatatype=2;
                break;
            case R.id.calendarid3:
                initMarkData("3");
                clickDatatype=3;
                break;
            case R.id.calendarid4:
                initMarkData("4");
                clickDatatype=4;
                break;
            case R.id.calendarid5:
                initMarkData("5");
                clickDatatype=5;
                break;
            case R.id.calendarid6:
                initMarkData("6");
                clickDatatype=6;
                break;
            case R.id.calendarid7:
                initMarkData("7");
                clickDatatype=7;
                break;
            case R.id.calendarid8:
                initMarkData("8");
                clickDatatype=8;
                break;
                default:
                    break;
        }

    }

    /**
     * 初始化对应功能的listener
     *
     * @return void
     */
//    @OnClick({R.id.comm_head_title, R.id.back_today_button, R.id.scroll_switch, R.id.next_month, R.id.last_month,})
//    public void OnViewClick(View view) {
//        switch (view.getId()) {
//            case R.id.back_today_button:
//                refreshMonthPager("");
//                break;
//            case R.id.scroll_switch:
//                if (calendarAdapter.getCalendarType() == CalendarAttr.CalendarType.WEEK) {
//                    Utils.scrollTo(content, rvToDoList, monthPager.getViewHeight(), 200);
//                    lastMonth.setText("上一月");
//                    nextMonth.setText("下一月");
//                    calendarAdapter.switchToMonth();
//                } else {
//                    Utils.scrollTo(content, rvToDoList, monthPager.getCellHeight(), 200);
//                    lastMonth.setText("上一周");
//                    nextMonth.setText("下一周");
//                    calendarAdapter.switchToWeek(monthPager.getRowIndex());
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
        listterner.returnTime(stringtime);
    }

    /**
     * 初始化CustomDayView，并作为CalendarViewAdapter的参数传入
     */
    private void initCalendarView() {
        initListener();
        CustomDayView customDayView = new CustomDayView(getActivity(), R.layout.custom_day);

        calendarAdapter = new CalendarViewAdapter(
                getActivity(),
                onSelectDateListener, CalendarAttr.CalendarType.MONTH, CalendarAttr.WeekArrayType.Monday, customDayView);
        calendarAdapter.setOnCalendarTypeChangedListener(new CalendarViewAdapter.OnCalendarTypeChanged() {
            @Override
            public void onCalendarTypeChanged(CalendarAttr.CalendarType type) {
                rvToDoList.scrollToPosition(0);
            }
        });
        calendarAdapter.setOnOnLongClickListener(new CalendarViewAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(CalendarDate calendarDate) {
                if (calendarDate==null){
                    calendarDate=currentDate;
                }
                if (scrollState==0) {
                    String month = calendarDate.getMonth() + "";
                    String day = calendarDate.getDay() + "";
                    if (Integer.valueOf(calendarDate.getMonth()) < 10) {
                        month = "0" + calendarDate.getMonth();
                    }
                    if (Integer.valueOf(calendarDate.getDay()) < 10) {
                        day = "0" + calendarDate.getDay();
                    }
                    String nowsdata = calendarDate.getYear() + "-" + month + "-" + day;
                    Intent intent = new Intent(new Intent(getActivity(), CalendarEventActivity.class));
                    intent.putExtra("currentTime", nowsdata);
                    startActivity(intent);
                }
            }
        });
//        initMarkData();
        initMonthPager();
    }

    /**
     * 初始化标记数据，HashMap的形式，可自定义
     * 如果存在异步的话，在使用setMarkData之后调用 calendarAdapter.notifyDataChanged();
     *  此次在数据库里查询标记列表
     */
    private void initMarkData(String markType) {//为了区分标志分类所以添加类型筛选
        QueryBuilder queryBuilder = AppApplication.getDaosession().getCalendarEventModelDao().queryBuilder();
        List<CalendarEventModel> calendarEventModels;
        if (markType.equals("0")) {
            calendarEventModels= queryBuilder.orderAsc().listLazy();
        }else {
            calendarEventModels = queryBuilder.orderAsc().where(CalendarEventModelDao.Properties.Calendarid.eq(markType)).listLazy();
        }

        HashMap<String, String> markData = new HashMap<>();
        if (calendarEventModels.size() > 0) {
            for (int i = 0; i < calendarEventModels.size(); i++) {
                CalendarEventModel calendarEventModel = calendarEventModels.get(i);
                if (calendarEventModel.getStarttime().length()>=10) {
                    markData.put(calendarEventModel.getDatetxt().substring(0, 4) +
                            calendarEventModel.getDatetxt().substring(4, 6).replace("0", "")
                            +calendarEventModel.getDatetxt().substring(6,7 )
                            +calendarEventModel.getDatetxt().substring(7,9 ).replace("0", "")
                            + calendarEventModel.getDatetxt().substring(9, 10), ""+calendarEventModel.getCalendarid());

                }
            }
        }
        Log.v("markmap", markData.toString());
        calendarAdapter.setMarkData(markData);
        if (!markType.equals("0")) {
        calendarAdapter.notifyDataChanged(currentDate);
        }
    }

    //    public static Date strToDateLong(String strDate) {
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        ParsePosition pos = new ParsePosition(0);
//        Date strtodate = formatter.parse(strDate, pos);
//        return strtodate;
//    }
    //初始化日期选择监听器
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
        listterner.returnTime(stringtime);
//        setTitle(date.getYear() + "年" + date.getMonth() + "月");
        String datestring = date.getYear() + "-" + month + "-" + day;
//         List<CalendarEventModel> calendarEventModel= AppApplication.getDaosession().queryBuilder(CalendarEventModel.class).list();
        //从数据库查询对应日期的日程列表
        QueryBuilder queryBuilder = AppApplication.getDaosession().getDao(CalendarEventModel.class).queryBuilder();
        if (clickDatatype==0){
            queryBuilder.where(CalendarEventModelDao.Properties.Datetxt.eq(datestring));
        }else {
            queryBuilder.where(CalendarEventModelDao.Properties.Datetxt.eq(datestring), CalendarEventModelDao.Properties.Calendarid.eq( clickDatatype));
        }

        List<CalendarEventModel> calendarEventModels = queryBuilder.listLazy();
        refreshAdapter(calendarEventModels);
        Log.v("databasecalendar", calendarEventModels.size()+calendarEventModels.toString() + datestring);
        tv_calendar_title.setText(currentDate.getYear()+"."+currentDate.getMonth());
    }
    //刷新日程列表数据
    public void refreshAdapter(List<CalendarEventModel> list) {
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
//                    tv_calendar_title.setText(currentDate.getYear()+"年"+currentDate.getMonth()+"月"+currentDate.getDay()+"日");
                    tv_calendar_title.setText(currentDate.getYear()+"."+currentDate.getMonth());
                    refreshClickDate(date);
//                    setTitle(date.getYear() + "年" + date.getMonth() + "月");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                scrollState=state;
            }
        });


        initMarkData("0");
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
//        setTitle(today.getYear() + "年" + today.getMonth() + "月");
    }
    //每次进入获取我的日程列表并且更新数据库
    public void getCalendarEvent() {

        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(getActivity()).getAccess_token());
        HttpUtil.httpGet(ConstantUrl.GETCALENDAREVENT, params, new JsonCallback<BaseModel<List<CalendarEventModel>>>() {
            @Override
            public void onSuccess(BaseModel<List<CalendarEventModel>> listBaseModel, Call call, Response response) {
                Log.v("ssssssssssss", listBaseModel.results.toString());

                AppApplication.getDaosession().deleteAll(CalendarEventModel.class);
                //把开始时间拆分成时期并赋给 datetxt并且逐条将每天的数据插入数据库
                for (int i = 0; i < listBaseModel.results.size(); i++) {
                    if (listBaseModel.results.get(i).getStarttime().length()>=10) {
                        try {
                            if (listBaseModel.results.get(i).getStarttime().length()>=10) {
                                List<String> dateList = getDate(listBaseModel.results.get(i).getStarttime().substring(0, 10), listBaseModel.results.get(i).getEndtime().substring(0, 10));
                                for (int j = 0; j < dateList.size(); j++) {
                                    CalendarEventModel  model=new CalendarEventModel();
                                        model.setDatetxt(dateList.get(j));
                                        model.setStarttime(listBaseModel.results.get(i).getStarttime());
                                        model.setEndtime(listBaseModel.results.get(i).getEndtime());
                                        model.setCalendarid(listBaseModel.results.get(i).getCalendarid());
                                        model.setIsallday(listBaseModel.results.get(i).getIsallday());
                                        model.setLocation(listBaseModel.results.get(i).getLocation());
                                        model.setId(listBaseModel.results.get(i).getId());
                                        model.setTitle(listBaseModel.results.get(i).getTitle());
                                        AppApplication.getDaosession().insertOrReplace(model);
//                                      listBaseModel.results.get(i).setDatetxt(dateList.get(j));
//                                    AppApplication.getDaosession().insertOrReplace(listBaseModel.results.get(i));
                                    Log.v("inserone",listBaseModel.results.get(i).toString());
                                }
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                }
                initMarkData("0");
               calendarAdapter.notifyDataChanged(currentDate);
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

    //接受消息广播
    BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(CalendarEventActivity.MSGADDCALENDARRESULTCODE)){
                getCalendarEvent();
            }
        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //删除日程返回界面
        if (requestCode==MYSCHEDULEEVENT){
                initMarkData(""+clickDatatype);//重置标记
                refreshClickDate(currentDate);//刷新数据


        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }
}
