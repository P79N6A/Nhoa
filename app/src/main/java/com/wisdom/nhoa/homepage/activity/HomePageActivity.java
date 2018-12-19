package com.wisdom.nhoa.homepage.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.android.pushservice.PushManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.BaseRequest;
import com.wisdom.nhoa.AppApplication;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.ActivityManager;
import com.wisdom.nhoa.homepage.fragment.ContactFragment;
import com.wisdom.nhoa.homepage.fragment.HomePageFragment;
import com.wisdom.nhoa.homepage.fragment.MineFragment;
import com.wisdom.nhoa.homepage.fragment.MsgFragment;
import com.wisdom.nhoa.homepage.model.MsgListModel;
import com.wisdom.nhoa.mine.model.VersionCodeModel;
import com.wisdom.nhoa.push.activity.PushDetailActivity;
import com.wisdom.nhoa.util.DoubleClickExitHelper;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.UserPermissionHelper;
import com.wisdom.nhoa.util.VersionUtil;
import com.wisdom.nhoa.util.dbHelper.DbHelperCustom;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;
import com.wisdom.nhoa.widget.qrcode.CaptureActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.homepage.activity
 * @class describe：首页…………………………
 * @time 2018/1/30 14:22
 * @change
 */

public class HomePageActivity extends FragmentActivity {
    public static final String UPDATAMSGCOUNTS = "com.wisdom.nhoa.homepage.receiver";
    @BindView(R.id.vp_home)
    ViewPager vpHome;
    @BindView(R.id.tab_home)
    TabLayout tabHome;
    public static final String TAG = HomePageActivity.class.getSimpleName();
    //Tab 文字
    private final int[] TAB_TITLES = new int[]{R.string.tab_homepage, R.string.tab_msg, R.string.tab_addressbook, R.string.tab_mine};
    //Tab 图片
    private final int[] TAB_IMGS = new int[]{R.drawable.ic_tab_homepage_selector, R.drawable.ic_tab_msg_selector, R.drawable.ic_tab_addressbook_selecter, R.drawable.ic_tab_mine_selector};
    //Fragment 数组
    private Fragment[] TAB_FRAGMENTS;
    //Tab 数目
    private final int COUNT = TAB_TITLES.length;
    private MyViewPagerAdapter mAdapter;
    private DoubleClickExitHelper doubleClickExitHelper;
    private int vpposition = 0;//存position
    private TextView messageCounts;
    private ProgressDialog progressDialog;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setlayoutIds();
        mContext = this;
        ButterKnife.bind(this);
        ActivityManager.getActivityManagerInstance().addActivity(this);
        TAB_FRAGMENTS = new Fragment[]{new HomePageFragment(), new MsgFragment(), new ContactFragment(), new MineFragment()};
        initViews();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UPDATAMSGCOUNTS);
        registerReceiver(receiver, intentFilter);
        //判断是不是点击通知栏唤醒的主页面
        if (getIntent().getBooleanExtra(ConstantString.FORM_NOTICE_OPEN, false)) {
            //通过点击通知栏唤醒的App，直接跳转到推送详情页面。
            Intent intent = new Intent(this, PushDetailActivity.class);
            intent.putExtra("messTypeCode", getIntent().getStringExtra("messTypeCode"));
            intent.putExtra("dataId", getIntent().getStringExtra("dataId"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        //获得接口返回的用户权限，并进行本地化操作
        UserPermissionHelper.getUserPermission(this);
    }

    public void initViews() {
        doubleClickExitHelper = new DoubleClickExitHelper(this);//连续点击两次退出的提示
        // tabHome = (TabLayout) findViewById(R.id.tab_home);
        setTabs(tabHome, getLayoutInflater(), TAB_TITLES, TAB_IMGS);
        mAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        //vpHome = (ViewPager) findViewById(R.id.vp_home);
        vpHome.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabHome));


        vpHome.setAdapter(mAdapter);
        tabHome.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                if (tab.getPosition() == 1) {
//                    if (!U.isLogin()) {
//                        //ToastUtil.showToast("请先登录！");
//                       // startActivityForResult(new Intent(HomePageActivity.this, LoginActivity.class), 1);
//                    } else {
//                        mViewPager.setCurrentItem(tab.getPosition());
//                    }
//                } else {
                vpHome.setCurrentItem(tab.getPosition(), false);
                // }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        progressDialog = new ProgressDialog(HomePageActivity.this);
        progressDialog.setTitle("正在下载……");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCanceledOnTouchOutside(false);
        //检查服务器端的版本号进项对比，判断是否进行强制更新
        getVersionInfoFromServer();
    }


    public void setlayoutIds() {
        setContentView(R.layout.homepage_activity_homepage);
    }

    public void scanTest(View view) {
        Intent openCameraIntent = new Intent(this, CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);
    }

    /**
     * @description: 设置添加Tab
     */
    private void setTabs(TabLayout tabLayout, LayoutInflater inflater, int[] tabTitlees, int[] tabImgs) {
        for (int i = 0; i < tabImgs.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            View view = null;
            view = inflater.inflate(R.layout.tab_item, null);
            tab.setCustomView(view);
            TextView tvTitle = (TextView) view.findViewById(R.id.tab_tv);
            tvTitle.setText(tabTitlees[i]);
            TextView counts = view.findViewById(R.id.tab_tv_logo);
            if (i == 1) {//设置只有第二个现实消息
                messageCounts = counts;
                counts.setVisibility(View.VISIBLE);
                UpDataMsgCounts();
            }
            ImageView imgTab = (ImageView) view.findViewById(R.id.tab_img);
            imgTab.setImageResource(tabImgs[i]);
            tabLayout.addTab(tab);
        }
    }

    /**
     * 扫码回调，暂时不需要
     *
     * @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     * super.onActivityResult(requestCode, resultCode, data);
     * if (resultCode == Activity.RESULT_OK) {
     * Bundle bundle = data.getExtras();
     * String scanResult = bundle.getString("result");
     * new AlertDialog.Builder(this)
     * .setTitle("扫描结果")
     * .setMessage(scanResult).create().show();
     * }
     * }
     */
//更新消息数量
    public void UpDataMsgCounts() {
//       QueryBuilder queryBuilder=AppApplication.getDaosession().getDao(MsgListModel.class).queryBuilder();
//        List<MsgListModel>  listModels =queryBuilder.where(MsgListModelDao.Properties.Isread.eq("0")).listLazy();
        List<MsgListModel> listModels = DbHelperCustom.queryListWhere(AppApplication.getInstance().GetSqliteDataBase(), this);
        if (listModels.size() == 0) {
            messageCounts.setVisibility(View.INVISIBLE);
        } else {
            messageCounts.setVisibility(View.VISIBLE);
        }
        messageCounts.setText("" + listModels.size());
        Log.v("conts", "" + listModels.toString());
    }

    //接受消息广播
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(UPDATAMSGCOUNTS)) {
                UpDataMsgCounts();
            }
        }
    };

    /**
     * 连续点击两次退出的方法
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return doubleClickExitHelper.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * @description: ViewPager 适配器
     */
    private class MyViewPagerAdapter extends FragmentPagerAdapter {
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return TAB_FRAGMENTS[position];
        }

        @Override
        public int getCount() {
            return COUNT;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PushManager.stopWork(this);
        if (SharedPreferenceUtil.getUserInfo(AppApplication.getInstance()) != null) {
            List<String> tags = new ArrayList<>();
            tags.add(SharedPreferenceUtil.getUserInfo(AppApplication.getInstance()).getUid());
            PushManager.delTags(AppApplication.getInstance(), tags);
        }
        unregisterReceiver(receiver);
    }


    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(
            10);

    public interface MyOnTouchListener {
        public boolean onTouch(MotionEvent ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            if (listener != null) {
                listener.onTouch(ev);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }

    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener);
    }

    /**
     * 从服务器获取服务器端的apk信息
     */
    private void getVersionInfoFromServer() {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        params.put("deviceType", "android");
        HttpUtil.httpGet(ConstantUrl.GET_VERSION_INFO, params, new JsonCallback<BaseModel<VersionCodeModel>>() {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeDialog();
                ToastUtil.showToast(e.getMessage());
            }

            @Override
            public void onSuccess(final BaseModel<VersionCodeModel> versionCodeModel, Call call, Response response) {
                U.closeDialog();
                //用服务器版本号与当前版本号进行对比
                if (versionCodeModel.results != null) {
                    int code = VersionUtil.compareVersion(versionCodeModel.results.getVersionNum()
                            , VersionUtil.getVersion(HomePageActivity.this));
                    Log.i(TAG, "版本号对比code: " + code);
                    if (code == 1 && ConstantString.FORCE_UPDATE_TRUE.equals(versionCodeModel.results.getForceUpdate())) {
                        //版本号1>版本号2 并且要求强制更新操作
                        updateApk(versionCodeModel.results.getFileUrl());
                    } else if (code == 1 && ConstantString.FORCE_UPDATE_FALSE.equals(versionCodeModel.results.getForceUpdate())) {
                        //版本号1>版本号2 并且不进行强制更新操作
                        new AlertDialog.Builder(HomePageActivity.this)
                                .setTitle("提示")
                                .setMessage("发现新版本：\n" + versionCodeModel.results.getContent() + "\n 是否进行更新？")
                                .setNegativeButton("暂不更新", null)
                                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        updateApk(versionCodeModel.results.getFileUrl());
                                    }
                                }).create().show();
                    }
                }
            }
        });
    }


    /**
     * 根据下载地址对apk进行更新的操作
     *
     * @param url
     */
    private void updateApk(String url) {
        Log.i(TAG, "********下载URL*******: " + HttpUtil.getAbsolteUrl(url.replaceAll("'\'", "")));
        OkGo.get(HttpUtil.getAbsolteUrl(url.replaceAll("'\'", "")))
                .cacheMode(CacheMode.DEFAULT)
                .execute(new FileCallback(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/", "nhoa.apk") {
                    @Override
                    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        progressDialog.setMax(100);
                        progressDialog.setProgress((int) (progress * 100));
                        super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
                    }

                    @Override
                    public void onBefore(BaseRequest request) {
                        progressDialog.show();
                        super.onBefore(request);
                    }

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(final File file, Call call, Response response) {
                        progressDialog.dismiss();
                        VersionUtil.installApk(HomePageActivity.this, file);
                    }
                });
    }


}
